from abc import ABC, abstractmethod
from typing import List, Sequence, Union

from pydantic.dataclasses import dataclass

from .tile import Tile, parse_tiles
from .tile_type import TileType


@dataclass(frozen=True)
class Mentsu(ABC):
    """
    面子
    """
    tile: Tile

    @property
    @abstractmethod
    def tiles(self) -> List[Tile]:
        raise NotImplementedError()


@dataclass(frozen=True)
class Kotsu(Mentsu):
    """
    刻子
    """

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num}{self.tile.num}{self.tile.tile_type}"

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile] * 3


@dataclass(frozen=True)
class Shuntsu(Mentsu):
    """
    顺子
    """

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num + 1}{self.tile.num + 2}{self.tile.tile_type}"

    def __post_init__(self):
        assert self.tile.tile_type != TileType.Z and self.tile.num < 8

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile, self.tile + 1, self.tile + 2]


def parse_mentsu(t: Union[Sequence[Tile], str]) -> Mentsu:
    if isinstance(t, str):
        t = parse_tiles(t)

    if len(t) != 3:
        raise ValueError("tiles must has length of 3")

    if t[0] == t[1] == t[2]:
        return Kotsu(t[0])
    else:
        if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z or t[2].tile_type == TileType.Z:
            raise ValueError(f"invalid tiles: {t}")
        t = sorted(t)
        if t[1] - t[0] == 1 and t[2] - t[1] == 1:
            return Shuntsu(t[0])
        else:
            raise ValueError(f"invalid tiles: {t}")


__all__ = ("Mentsu", "Kotsu", "Shuntsu", "parse_mentsu")
