from typing import List, Callable

from mahjong_utils.internal.utils.bit import generate_k_bit_number
from mahjong_utils.models.hand import StdHand
from mahjong_utils.models.mentsu import Kotsu, Shuntsu
from mahjong_utils.models.tatsu import Tatsu, Toitsu, Kanchan, Ryanmen, Penchan
from mahjong_utils.models.tile import Tile, tile
from mahjong_utils.models.tile_type import tile_type_index_mapping, tile_type_reversed_index_mapping, TileType


def _encode(t: Tile) -> int:
    return tile_type_index_mapping[t.tile_type] * 9 + t.real_num - 1


def _decode(code: int) -> Tile:
    tile_type = tile_type_reversed_index_mapping[code // 9]
    num = code % 9 + 1
    return tile(tile_type, num)


class StdHandSearcher:
    def __init__(self, k: int, hand: List[Tile],
                 callback: Callable[[StdHand], None]):
        self.hand = hand
        self.callback = callback

        self._count = [0] * (3 * 9 + 7)
        self._n = len(hand)
        self._k = k

        self._mentsu = []
        self._tatsu = []
        self._stop = False

    def run(self):
        if self._stop:
            raise RuntimeError("already stopped")

        for t in self.hand:
            self._count[_encode(t)] += 1

        self._dfs_kotsu()

    def stop(self):
        self._stop = True

    def _dfs_kotsu(self, begin=0):
        # begin用于限制从哪张牌开始枚举（下同）
        # 其目的是避免搜索时按不同顺序取了相同的刻字，优化性能

        if self._stop:
            return

        if self._n >= 3:
            for i in range(begin, 3 * 9 + 7):
                if self._count[i] >= 3:
                    self._n -= 3
                    self._count[i] -= 3
                    self._mentsu.append(Kotsu(_decode(i)))
                    self._dfs_kotsu(i)
                    self._n += 3
                    self._count[i] += 3
                    self._mentsu.pop()

        self._dfs_shuntsu()

    def _dfs_shuntsu(self, begin=0):
        if self._stop:
            return

        if self._n >= 3:
            for i in range(begin // 9, 3):  # m/p/s
                for j in range(7):  # 1~7
                    x = i * 9 + j
                    y = x + 1
                    z = x + 2

                    if x < begin:
                        continue

                    if self._count[x] > 0 and self._count[y] > 0 and self._count[z] > 0:
                        self._n -= 3
                        self._count[x] -= 1
                        self._count[y] -= 1
                        self._count[z] -= 1
                        self._mentsu.append(Shuntsu(_decode(x)))
                        self._dfs_shuntsu(x)
                        self._n += 3
                        self._count[x] += 1
                        self._count[y] += 1
                        self._count[z] += 1
                        self._mentsu.pop()

        self._dfs_tatsu()

    def _dfs_tatsu(self, begin=0, tatsu_type_limitation=0):
        # tatsu_type_limitation用于限制能够取什么样的以begin为第一张牌的搭子（0可以取所有类型，1不可以取对子、2不可以取对子和坎张、3不可以取对子坎张两面）
        # 其目的是避免搜索时按不同顺序取了相同的搭子，优化性能
        # 故当for循环执行了一趟以后就将tatsu_type_limitation置0

        if self._stop:
            return

        taken = False

        if self._n >= 2:
            for i in range(begin, 3 * 9 + 7):
                t = _decode(i)

                # toitsu
                if tatsu_type_limitation == 0 and self._count[i] >= 2:
                    taken = True
                    self._n -= 2
                    self._count[i] -= 2
                    self._tatsu.append(Toitsu(_decode(i)))
                    self._dfs_tatsu(i, 0)
                    self._n += 2
                    self._count[i] += 2
                    self._tatsu.pop()

                # kanchan
                if tatsu_type_limitation <= 1 and t.tile_type != TileType.Z and t.num != 9:
                    j = i + 2
                    if self._count[i] > 0 and self._count[j] > 0:
                        taken = True
                        self._n -= 2
                        self._count[i] -= 1
                        self._count[j] -= 1
                        self._tatsu.append(Kanchan(t))
                        self._dfs_tatsu(i, 1)
                        self._n += 2
                        self._count[i] += 1
                        self._count[j] += 1
                        self._tatsu.pop()

                # ryanmen
                if tatsu_type_limitation <= 2 and t.tile_type != TileType.Z and 2 <= t.num <= 7:
                    j = i + 1
                    if self._count[i] > 0 and self._count[j] > 0:
                        taken = True
                        self._n -= 2
                        self._count[i] -= 1
                        self._count[j] -= 1
                        self._tatsu.append(Ryanmen(t))
                        self._dfs_tatsu(i, 2)
                        self._n += 2
                        self._count[i] += 1
                        self._count[j] += 1
                        self._tatsu.pop()

                # penchan
                if tatsu_type_limitation <= 3 and t.tile_type != TileType.Z and (t.num == 1 or t.num == 8):
                    j = i + 1
                    if self._count[i] > 0 and self._count[j] > 0:
                        taken = True
                        self._n -= 2
                        self._count[i] -= 1
                        self._count[j] -= 1
                        self._tatsu.append(Penchan(t))
                        self._dfs_tatsu(i, 3)
                        self._n += 2
                        self._count[i] += 1
                        self._count[j] += 1
                        self._tatsu.pop()

                tatsu_type_limitation = 0

        if not taken:
            self._on_result()

    def _on_result(self):
        for jyantou, mentsu, tatsu, remaining in self._normalize():
            self.callback(StdHand(jyantou, mentsu.copy(), tatsu.copy(), remaining.copy()))

    def _normalize(self):
        # 将搜索结果处理为（雀头，面子，搭子，浮牌）的形式，且面子数+搭子数不超过k
        remaining = []
        for i in range(3 * 9 + 7):
            if self._count[i] > 0:
                t = _decode(i)
                for j in range(self._count[i]):
                    remaining.append(t)

        has_toitsu = False

        # enumerate jyantou
        for i, tt in enumerate(self._tatsu):
            if isinstance(tt, Toitsu):
                has_toitsu = True

                remaining_tatsu = self._tatsu[0:i] + self._tatsu[i + 1:]

                for tatsu_chosen, tatsu_not_chosen_as_tiles in self._choose_tatsu(self._k - len(self._mentsu),
                                                                                  remaining_tatsu):
                    yield tt.first, self._mentsu, tatsu_chosen, remaining + tatsu_not_chosen_as_tiles

        if not has_toitsu:
            for tatsu_chosen, tatsu_not_chosen_as_tiles in self._choose_tatsu(self._k - len(self._mentsu), self._tatsu):
                yield None, self._mentsu, tatsu_chosen, remaining + tatsu_not_chosen_as_tiles

    @staticmethod
    def _choose_tatsu(k: int, tatsu: List[Tatsu]):
        # 选择k个搭子
        if k >= len(tatsu):
            yield tatsu, []
        else:
            maximum = 1 << len(tatsu)
            for mask in generate_k_bit_number(k):
                if mask >= maximum:
                    break

                tatsu_chosen = []
                tatsu_not_chosen_as_tiles = []

                for i in range(len(tatsu)):
                    if mask & (1 << i):
                        tatsu_chosen.append(tatsu[i])
                    else:
                        tatsu_not_chosen_as_tiles.append(tatsu[i].first)
                        tatsu_not_chosen_as_tiles.append(tatsu[i].second)

                yield tatsu_chosen, tatsu_not_chosen_as_tiles


__all__ = ("StdHandSearcher",)
