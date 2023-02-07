from io import StringIO
from typing import Union, List, Iterable, Optional, NamedTuple

from mahjong_utils.models.tile_type import TileType, tile_type_index_mapping, tile_type_reversed_index_mapping


class Tile(NamedTuple):
    """
    牌
    """

    tile_type: TileType
    num: int

    def __repr__(self):
        return f"{self.num}{self.tile_type.lower()}"

    @property
    def real_num(self) -> int:
        if self.tile_type != TileType.Z and self.num == 0:
            return 5
        else:
            return self.num

    @property
    def code(self):
        return tile_type_index_mapping[self.tile_type] * 10 + self.num

    def __hash__(self):
        return self.code

    def __cmp__(self, other):
        if not isinstance(other, Tile):
            raise TypeError(other)

        if self.tile_type != other.tile_type:
            return ord(self.tile_type) - ord(other.tile_type)
        elif self.num != 0 and other.num != 0:
            # 两张都不是红dora
            return self.num - other.num
        elif self.num == 0 and other.num == 0:
            # 两张都是红dora
            return 0
        elif self.num == 0:
            # 这张是红dora
            if other.num > 5:
                return -1
            else:
                return 1
        else:
            # 另一张是红dora
            if self.num <= 5:
                return -1
            else:
                return 1

    def __eq__(self, other):
        return other is not None and self.__cmp__(other) == 0

    def __ne__(self, other):
        return self.__cmp__(other) != 0

    def __gt__(self, other):
        return self.__cmp__(other) > 0

    def __lt__(self, other):
        return self.__cmp__(other) < 0

    def __ge__(self, other):
        return self.__cmp__(other) >= 0

    def __le__(self, other):
        return self.__cmp__(other) <= 0

    def __add__(self, other):
        pending_num = self.num + other
        if pending_num in self.tile_type.num_range:
            return self.by_type_and_num(self.tile_type, pending_num)
        else:
            raise ValueError("num out of range")

    def __sub__(self, other):
        if isinstance(other, int):
            return self + (-other)
        elif isinstance(other, Tile):
            if self.tile_type != other.tile_type:
                raise ValueError(f"tile_type of {self} and {other} are not the same")
            return self.num - other.num

    @classmethod
    def by_type_and_num(cls, tile_type: TileType, num: int) -> "Tile":
        tile_type_index = tile_type_index_mapping[tile_type]

        if 0 <= tile_type_index * 10 + num < len(tile_pool):
            t = tile_pool[tile_type_index * 10 + num]
            if t is not None:
                return t

        raise ValueError(f"invalid arguments: tile_type={tile_type}, num={num}")

    @classmethod
    def by_text(cls, text: str) -> "Tile":
        if len(text) != 2:
            raise ValueError(f"invalid arguments: text={text}")

        if text[1].lower() == 'm':
            tile_type = TileType.M
        elif text[1].lower() == 's':
            tile_type = TileType.S
        elif text[1].lower() == 'p':
            tile_type = TileType.P
        elif text[1].lower() == 'z':
            tile_type = TileType.Z
        else:
            raise ValueError(f"invalid arguments: text={text}")

        try:
            num = int(text[0])
        except ValueError as e:
            raise ValueError(f"invalid arguments: text={text}") from e

        return cls.by_type_and_num(tile_type, num)

    @classmethod
    def by_code(cls, code: int) -> "Tile":
        tile_type = tile_type_reversed_index_mapping[code // 10]
        num = code % 10
        return cls.by_type_and_num(tile_type, num)

    def __encode__(self) -> str:
        return str(self)

    @classmethod
    def __decode__(cls, data: str) -> "Tile":
        return cls.by_text(data)


tile_pool: List[Optional[Tile]] = []

for i in range(0, 10):
    tile_pool.append(Tile(TileType.M, i))
for i in range(0, 10):
    tile_pool.append(Tile(TileType.P, i))
for i in range(0, 10):
    tile_pool.append(Tile(TileType.S, i))
tile_pool.append(None)
for i in range(1, 8):
    tile_pool.append(Tile(TileType.Z, i))

Tile._tile_pool = tile_pool


def parse_tiles(text: str) -> List[Tile]:
    ans: List[Tile] = []
    pending: List[int] = []
    for c in text:
        if c.upper() in ("M", "P", "S", "Z"):
            tile_type = TileType[c.upper()]
            for num in pending:
                ans.append(Tile.by_type_and_num(tile_type, num))
            pending.clear()
        elif c.isdigit():
            pending.append(ord(c) - ord('0'))
        else:
            raise ValueError(f"invalid character: {c}")

    if len(pending) > 0:
        raise ValueError("missing tile type at the end of your given text")

    return ans


def tiles_text(tiles: Union[Tile, Iterable[Tile]]) -> str:
    if isinstance(tiles, Tile):
        return str(tiles)

    prev = None
    with StringIO() as sio:
        for t in tiles:
            if prev and prev.tile_type != t.tile_type:
                sio.write(prev.tile_type.lower())
            sio.write(str(t.num))
            prev = t

        if prev:
            sio.write(prev.tile_type.lower())

        return sio.getvalue()


all_yaochu = frozenset(parse_tiles("19m19s19p1234567z"))


def is_m(t: Tile) -> bool:
    """
    判断该牌是否为万子
    """
    return t.tile_type == TileType.M


def is_p(t: Tile) -> bool:
    """
    判断该牌是否为筒子
    """
    return t.tile_type == TileType.P


def is_s(t: Tile) -> bool:
    """
    判断该牌是否为索子
    """
    return t.tile_type == TileType.S


def is_z(t: Tile) -> bool:
    """
    判断该牌是否为字牌
    """
    return t.tile_type == TileType.Z


def is_yaochu(t: Tile) -> bool:
    """
    判断该牌是否为幺九牌
    """
    return t in all_yaochu


def is_wind(t: Tile) -> bool:
    """
    判断该牌是否为风牌
    """
    return t.tile_type == TileType.Z and 1 <= t.num <= 4


def is_sangen(t: Tile) -> bool:
    """
    判断该牌是否为三元牌
    """
    return t.tile_type == TileType.Z and 5 <= t.num <= 7


__all__ = ("Tile", "parse_tiles", "tiles_text", "all_yaochu",
           "is_m", "is_p", "is_s", "is_z", "is_wind", "is_sangen", "is_yaochu")
