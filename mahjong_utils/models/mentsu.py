from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List, overload, Sequence

from .tile import Tile, parse_tiles, tile_text
from .tile_type import TileType


@dataclass(frozen=True)
class Mentsu(ABC):
    """
    面子
    """
    tile: Tile

    def __str__(self):
        return tile_text(self.tiles)

    @property
    @abstractmethod
    def tiles(self) -> List[Tile]:
        raise NotImplementedError()


class Kotsu(Mentsu):
    """
    刻子
    """

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile] * 3


class Shuntsu(Mentsu):
    """
    顺子
    """

    def __post_init__(self):
        assert self.tile.tile_type != TileType.Z and self.tile.num < 8

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile, self.tile + 1, self.tile + 2]


@overload
def mentsu(t: str) -> Mentsu:
    ...


@overload
def mentsu(t: Sequence[Tile]) -> Mentsu:
    ...


def mentsu(t) -> Mentsu:
    if isinstance(t, str):
        t = parse_tiles(t)

    if len(t) != 3:
        raise ValueError("tiles must has length of 3")

    if t[0] == t[1] == t[2]:
        return Kotsu(t[0])
    else:
        if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z or t[2].tile_type == TileType.Z:
            raise ValueError(f"invalid tiles: {t}")
        t.sort()
        if t[1] - t[0] == 1 and t[2] - t[1] == 1:
            return Shuntsu(t[0])
        else:
            raise ValueError(f"invalid tiles: {t}")


__all__ = ("Mentsu", "Kotsu", "Shuntsu", "mentsu")
