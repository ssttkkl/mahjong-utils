from dataclasses import dataclass
from enum import Enum
from io import StringIO
from typing import Union, overload, List, Iterable


class TileType(str, Enum):
    M = "M"
    P = "P"
    S = "S"
    Z = "Z"

    @property
    def num_range(self):
        if self == TileType.Z:
            return range(1, 8)
        else:
            return range(0, 10)


tile_type_index_mapping = {TileType.M: 0, TileType.P: 1, TileType.S: 2, TileType.Z: 3}
tile_type_reversed_index_mapping = [TileType.M, TileType.P, TileType.S, TileType.Z]


@dataclass(frozen=True)
class Tile:
    tile_type: TileType
    num: int

    def __str__(self):
        return f"{self.num}{self.tile_type}"

    @property
    def real_num(self) -> int:
        if self.tile_type != TileType.Z and self.num == 0:
            return 5
        else:
            return self.num

    # def code(self) -> int:
    #     return tile_type_index_mapping[self.tile_type] * 10 + self.num

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
        return self.__cmp__(other) == 0

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
            return tile(self.tile_type, pending_num)
        else:
            raise ValueError("num is out of range")

    def __sub__(self, other):
        if isinstance(other, int):
            return self + (-other)
        elif isinstance(other, Tile):
            if self.tile_type != other.tile_type:
                raise ValueError(f"tile_type of {self} and {other} are not the same")
            return self.num - other.num


tile_pool: List[Tile] = []

for i in range(0, 10):
    tile_pool.append(Tile(TileType.M, i))
for i in range(0, 10):
    tile_pool.append(Tile(TileType.P, i))
for i in range(0, 10):
    tile_pool.append(Tile(TileType.S, i))
tile_pool.append(None)
for i in range(1, 8):
    tile_pool.append(Tile(TileType.Z, i))


def _get_tile_by_text(text: str) -> Tile:
    if len(text) != 2:
        raise ValueError(f"invalid arguments: text={text}")

    if text[0].lower() == 'm':
        tile_type = TileType.M
    elif text[0].lower() == 's':
        tile_type = TileType.S
    elif text[0].lower() == 'p':
        tile_type = TileType.P
    elif text[0].lower() == 'z':
        tile_type = TileType.Z
    else:
        raise ValueError(f"invalid arguments: text={text}")

    try:
        num = int(text[1])
    except ValueError as e:
        raise ValueError(f"invalid arguments: text={text}") from e

    return _get_tile_by_type_and_num(tile_type, num)


def _get_tile_by_type_and_num(tile_type: Union[TileType, str], num: int) -> Tile:
    if isinstance(tile_type, str):
        tile_type = TileType[tile_type]

    tile_type_index = tile_type_index_mapping[tile_type]

    if 0 <= tile_type_index * 10 + num < len(tile_pool):
        t = tile_pool[tile_type_index * 10 + num]
        if t is not None:
            return t

    raise ValueError(f"invalid arguments: tile_type={tile_type}, num={num}")


def _get_tile_by_code(code: int) -> Tile:
    tile_type = tile_type_reversed_index_mapping[code // 10]
    num = code % 10
    return _get_tile_by_type_and_num(tile_type, num)


@overload
def tile(tile_type: Union[TileType, str], num: int) -> Tile:
    ...


@overload
def tile(text: str) -> Tile:
    ...


@overload
def tile(code: int) -> Tile:
    ...


def tile(*args) -> Tile:
    if len(args) == 1:
        if isinstance(args[0], str):
            return _get_tile_by_text(args[0])
        elif isinstance(args[0], int):
            return _get_tile_by_code(args[0])
        else:
            raise ValueError("invalid arguments")
    elif len(args) == 2:
        return _get_tile_by_type_and_num(args[0], args[1])
    else:
        raise ValueError("invalid arguments")


def tiles(text: str) -> List[Tile]:
    ans: List[Tile] = []
    pending = []
    for c in text:
        if c.upper() in ("M", "P", "S", "Z"):
            tile_type = TileType[c.upper()]
            for num in pending:
                ans.append(tile(tile_type, num))
            pending.clear()
        elif c.isdigit():
            pending.append(ord(c) - ord('0'))
        else:
            raise ValueError(f"invalid character: {c}")

    if len(pending) > 0:
        raise ValueError("missing tile type at the end of your given text")

    return ans


def tile_text(tile: Union[Tile, Iterable[Tile]]) -> str:
    if isinstance(tile, Tile):
        return str(tile)

    prev = None
    with StringIO() as sio:
        for t in tile:
            if prev and prev.tile_type != t.tile_type:
                sio.write(prev.tile_type)
            sio.write(str(t.num))
            prev = t

        if prev:
            sio.write(prev.tile_type)

        return sio.getvalue()


__all__ = ("TileType", "Tile",
           "tile", "tiles", "tile_text",
           "tile_type_index_mapping", "tile_type_reversed_index_mapping")
