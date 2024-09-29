from abc import ABC, abstractmethod
from typing import List, Sequence, Union, Iterable, TYPE_CHECKING

from pydantic.dataclasses import dataclass

from .tile import Tile, parse_tiles
from .tile_type import TileType

if TYPE_CHECKING:
    from .tatsu import Penchan, Kanchan, Ryanmen, Tatsu, Toitsu


@dataclass(frozen=True)
class Mentsu(ABC):
    """
    面子
    """
    tile: Tile

    @property
    @abstractmethod
    def tiles(self) -> Iterable[Tile]:
        raise NotImplementedError()

    @abstractmethod
    def after_discard(self, discard: Tile) -> "Tatsu":
        raise NotImplementedError()

    def __encode__(self) -> str:
        return str(self)

    @classmethod
    def __decode__(cls, data: str) -> "Mentsu":
        return Mentsu.parse(data)

    @staticmethod
    def parse(t: Union[Sequence[Tile], str]) -> "Mentsu":
        if isinstance(t, str):
            t = parse_tiles(t)

        if len(t) != 3:
            raise ValueError("_tiles must has length of 3")

        if t[0] == t[1] == t[2]:
            return Kotsu(t[0])
        else:
            if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z or t[2].tile_type == TileType.Z:
                raise ValueError(f"invalid _tiles: {t}")
            t = sorted(t)
            if t[1] - t[0] == 1 and t[2] - t[1] == 1:
                return Shuntsu(t[0])
            else:
                raise ValueError(f"invalid _tiles: {t}")


@dataclass(frozen=True)
class Kotsu(Mentsu):
    """
    刻子
    """

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num}{self.tile.num}{self.tile.tile_type.lower()}"

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile] * 3

    def after_discard(self, discard: Tile) -> "Toitsu":
        from .tatsu import Toitsu
        if discard == self.tile:
            return Toitsu(discard)
        raise ValueError()


@dataclass(frozen=True)
class Shuntsu(Mentsu):
    """
    顺子
    """

    def __post_init__(self):
        assert self.tile.tile_type != TileType.Z
        assert 1 <= self.tile.num <= 7

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num + 1}{self.tile.num + 2}{self.tile.tile_type.lower()}"

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile, self.tile + 1, self.tile + 2]

    def after_discard(self, discard: Tile) -> "Union[Penchan, Ryanmen, Kanchan]":
        from .tatsu import Penchan, Kanchan, Ryanmen
        if discard == self.tile:
            if discard.num == 7:
                return Penchan(discard + 1)
            elif discard.num == 3:
                return Penchan(discard - 2)
            else:
                return Ryanmen(discard + 1)
        elif discard == self.tile + 1:
            return Kanchan(self.tile)
        elif discard == self.tile + 2:
            if self.tile.num == 1:
                return Penchan(self.tile)
            else:
                return Ryanmen(self.tile)
        raise ValueError()


__all__ = ("Mentsu", "Kotsu", "Shuntsu",)
