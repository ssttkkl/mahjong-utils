from typing import List, Callable, Tuple, Generator, Sequence

from mahjong_utils.internal.tile_type_mapping import tile_type_index_mapping, tile_type_reversed_index_mapping
from mahjong_utils.internal.utils.bit import generate_k_bit_number
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand_pattern import RegularHandPattern
from mahjong_utils.models.mentsu import Kotsu, Shuntsu, Mentsu
from mahjong_utils.models.tatsu import Tatsu, Toitsu, Kanchan, Ryanmen, Penchan
from mahjong_utils.models.tile import Tile, tile
from mahjong_utils.models.tile_type import TileType


def _encode(t: Tile) -> int:
    return tile_type_index_mapping[t.tile_type] * 9 + t.real_num - 1


def _decode(code: int) -> Tile:
    tile_type = tile_type_reversed_index_mapping[code // 9]
    num = code % 9 + 1
    return tile(tile_type, num)


class RegularHandPatternSearcher:
    def __init__(self, tiles: Sequence[Tile], furo: Sequence[Furo],
                 callback: Callable[[RegularHandPattern], None]):
        self._callback = callback

        if not isinstance(furo, tuple):
            furo = tuple(furo)
        self._furo = furo

        self._count = [0] * (3 * 9 + 7)
        for t in tiles:
            self._count[_encode(t)] += 1

        self._n = len(tiles)
        self._k = self._n // 3

        self._mentsu: List[Mentsu] = []
        self._tatsu: List[Tatsu] = []

    def run(self):
        self._dfs_kotsu()

    def _dfs_kotsu(self, begin=0):
        # begin用于限制从哪张牌开始枚举（下同）
        # 其目的是避免搜索时按不同顺序取了相同的刻字，优化性能

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
                if tatsu_type_limitation <= 1 and t.tile_type != TileType.Z and t.num <= 7:
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
                if tatsu_type_limitation <= 2 and 2 <= t.num <= 7 and t.tile_type != TileType.Z:
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
        for hand in self._normalize():
            self._callback(hand)

    def _normalize(self):
        # 将搜索结果处理为（雀头，面子，搭子，浮牌）的形式，且面子数+搭子数不超过k
        remaining = []
        for i in range(3 * 9 + 7):
            if self._count[i] > 0:
                t = _decode(i)
                for j in range(self._count[i]):
                    remaining.append(t)
        remaining = tuple(remaining)

        has_toitsu = False

        # enumerate jyantou
        for i, tt in enumerate(self._tatsu):
            if isinstance(tt, Toitsu):
                has_toitsu = True

                remaining_tatsu = self._tatsu[0:i] + self._tatsu[i + 1:]

                for tatsu_chosen, tatsu_not_chosen_as_tiles in self._choose_tatsu(self._k - len(self._mentsu),
                                                                                  remaining_tatsu):
                    yield RegularHandPattern(k=self._k + len(self._furo),
                                             jyantou=tt.first,
                                             menzen_mentsu=tuple(self._mentsu),
                                             furo=self._furo,
                                             tatsu=tatsu_chosen,
                                             remaining=remaining + tatsu_not_chosen_as_tiles)

        if not has_toitsu:
            for tatsu_chosen, tatsu_not_chosen_as_tiles in self._choose_tatsu(self._k - len(self._mentsu), self._tatsu):
                yield RegularHandPattern(k=self._k + len(self._furo),
                                         jyantou=None,
                                         menzen_mentsu=tuple(self._mentsu),
                                         furo=self._furo,
                                         tatsu=tatsu_chosen,
                                         remaining=remaining + tatsu_not_chosen_as_tiles)

    @staticmethod
    def _choose_tatsu(k: int, tatsu: List[Tatsu]) -> Generator[Tuple[Tuple[Tatsu], Tuple[Tile]], None, None]:
        # 选择k个搭子
        if k >= len(tatsu):
            yield tuple(tatsu), tuple()
        elif k == 0:
            tiles = []
            for tt in tatsu:
                tiles.append(tt.first)
                tiles.append(tt.second)
            yield tuple(), tuple(tiles)
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

                yield tuple(tatsu_chosen), tuple(tatsu_not_chosen_as_tiles)


def regular_hand_pattern_search(tiles: Sequence[Tile], furo: Sequence[Furo]) -> List[RegularHandPattern]:
    patterns = []

    def callback(pattern: RegularHandPattern):
        patterns.append(pattern)

    searcher = RegularHandPatternSearcher(tiles, furo, callback)
    searcher.run()

    return patterns


__all__ = ("regular_hand_pattern_search",)
