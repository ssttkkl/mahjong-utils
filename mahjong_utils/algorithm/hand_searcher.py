from typing import List, Callable

from mahjong_utils.models.mentsu import Mentsu, Kotsu, Shuntsu
from mahjong_utils.models.tatsu import Tatsu, Toitsu, Kanchan, Ryanmen, Penchan
from mahjong_utils.models.tile import Tile, tile_type_index_mapping, tile_type_reversed_index_mapping, tile


def _encode(t: Tile) -> int:
    return tile_type_index_mapping[t.tile_type] * 9 + t.real_num - 1


def _decode(code: int) -> Tile:
    tile_type = tile_type_reversed_index_mapping[code // 9]
    num = code % 9 + 1
    return tile(tile_type, num)


class HandSearcher:
    def __init__(self, hand: List[Tile], callback: Callable[[Tile, List[Mentsu], List[Tatsu], List[Tile]], None]):
        self.hand = hand
        self.callback = callback

        self._count = [0] * (3 * 9 + 7)
        self._n = len(hand)

        if len(hand) < 3 or len(hand) > 14 or (len(hand) - 2) % 3 == 1:
            raise ValueError(f"invalid length of hand: {len(hand)}")

        if (len(hand) - 2) % 3 == 0:
            self._k = (len(hand) - 2) // 3
        else:
            self._k = (len(hand) - 1) // 3

        self._jyantou = None
        self._mentsu = []
        self._tatsu = []
        self._stop = False

    def run(self):
        if self._stop:
            raise RuntimeError("already stopped")

        for t in self.hand:
            self._count[_encode(t)] += 1

        self._dfs_kotsu()

    def _do_callback(self):
        remaining = []
        for i in range(3 * 9 + 7):
            if self._count[i] > 0:
                t = _decode(i)
                for j in range(self._count[i]):
                    remaining.append(t)
        self.callback(self._jyantou, self._mentsu, self._tatsu, remaining)

    def _dfs_kotsu(self):
        if self._stop:
            return

        self._dfs_shuntsu()

        if self._n >= 3:
            for i in range(3 * 9 + 7):
                if self._count[i] >= 3:
                    self._n -= 3
                    self._count[i] -= 3
                    self._mentsu.append(Kotsu(_decode(i)))
                    self._dfs_kotsu()
                    self._n += 3
                    self._count[i] += 3
                    self._mentsu.pop()

    def _dfs_shuntsu(self):
        if self._stop:
            return

        taken = False

        if self._n >= 3:
            for i in range(3):  # m/p/s
                for j in range(7):  # 1~7
                    x = i * 9 + j
                    y = x + 1
                    z = x + 2

                    if self._count[x] > 0 and self._count[y] > 0 and self._count[z] > 0:
                        taken = True
                        self._n -= 3
                        self._count[x] -= 1
                        self._count[y] -= 1
                        self._count[z] -= 1
                        self._mentsu.append(Shuntsu(_decode(x)))
                        self._dfs_kotsu()
                        self._n += 3
                        self._count[x] += 1
                        self._count[y] += 1
                        self._count[z] += 1
                        self._mentsu.pop()

        if not taken:
            self._dfs_jyantou()

    def _dfs_jyantou(self):
        if self._stop:
            return

        if self._n < 2:
            self._do_callback()
            return

        self._dfs_tatsu()

        for i in range(3 * 9 + 7):
            if self._count[i] >= 2:
                self._n -= 2
                self._count[i] -= 2
                self._jyantou = _decode(i)
                self._dfs_tatsu()
                self._n += 2
                self._count[i] += 2
                self._jyantou = None

    def _dfs_tatsu(self):
        if self._stop:
            return

        if self._n < 2 or len(self._mentsu) + len(self._tatsu) >= self._k:
            self._do_callback()
            return

        taken = False

        for i in range(3 * 9 + 7):
            if self._count[i] >= 2:
                taken = True
                self._n -= 2
                self._count[i] -= 2
                self._tatsu.append(Toitsu(_decode(i)))
                self._dfs_tatsu()
                self._n += 2
                self._count[i] += 2
                self._tatsu.pop()

        for i in range(3):  # m / p / s
            for j in range(7):  # 1 ~ 7
                x = i * 9 + j
                z = x + 2

                if self._count[x] > 0 and self._count[z] > 0:
                    taken = True
                    self._n -= 2
                    self._count[x] -= 1
                    self._count[z] -= 1
                    self._tatsu.append(Kanchan(_decode(x)))
                    self._dfs_tatsu()
                    self._n += 2
                    self._count[x] += 1
                    self._count[z] += 1
                    self._tatsu.pop()

            for j in range(1, 7):  # 2 ~ 7
                x = i * 9 + j
                y = x + 1

                if self._count[x] > 0 and self._count[y] > 0:
                    taken = True
                    self._n -= 2
                    self._count[x] -= 1
                    self._count[y] -= 1
                    self._tatsu.append(Ryanmen(_decode(x)))
                    self._dfs_tatsu()
                    self._n += 2
                    self._count[x] += 1
                    self._count[y] += 1
                    self._tatsu.pop()

            x = i * 9  # 1
            y = x + 1

            if self._count[x] > 0 and self._count[y] > 0:
                taken = True
                self._n -= 2
                self._count[x] -= 1
                self._count[y] -= 1
                self._tatsu.append(Penchan(_decode(x)))
                self._dfs_tatsu()
                self._n += 2
                self._count[x] += 1
                self._count[y] += 1
                self._tatsu.pop()

            x = i * 9 + 6  # 7
            y = x + 1

            if self._count[x] > 0 and self._count[y] > 0:
                taken = True
                self._n -= 2
                self._count[x] -= 1
                self._count[y] -= 1
                self._tatsu.append(Penchan(_decode(x)))
                self._dfs_tatsu()
                self._n += 2
                self._count[x] += 1
                self._count[y] += 1
                self._tatsu.pop()

        if not taken:
            self._do_callback()
