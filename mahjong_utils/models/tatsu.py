from abc import ABC, abstractmethod
from typing import Set, Sequence, Union

from pydantic.dataclasses import dataclass

from .mentsu import Mentsu, Shuntsu, Kotsu
from .tile import Tile, parse_tiles, tiles_text
from .tile_type import TileType


@dataclass(frozen=True)
class Tatsu(ABC):
    """
    搭子
    """
    first: Tile

    @property
    @abstractmethod
    def second(self) -> Tile:
        raise NotImplementedError()

    @property
    @abstractmethod
    def waiting(self) -> Set[Tile]:
        raise NotImplementedError()

    def with_waiting(self, tile: Tile) -> Mentsu:
        raise NotImplementedError()

    def __encode__(self) -> str:
        return str(self)

    @classmethod
    def __decode__(cls, data: str) -> "Tatsu":
        return Tatsu.parse(data)

    @staticmethod
    def parse(t: Union[Sequence[Tile], str]) -> "Tatsu":
        if isinstance(t, str):
            t = parse_tiles(t)

        if len(t) != 2:
            raise ValueError("_tiles must has length of 2")

        first, second = t[0], t[1]

        if first > second:
            first, second = second, first

        if first == second:
            return Toitsu(first)
        else:
            if first.tile_type == TileType.Z or second.tile_type == TileType.Z:
                raise ValueError(f"invalid tiles: {t}")

            if second - first == 1:
                if first.num == 1 or first.num == 8:
                    return Penchan(first)
                else:
                    return Ryanmen(first)
            elif second - first == 2:
                return Kanchan(first)
            else:
                raise ValueError(f"invalid tiles: {first}, {second}")


@dataclass(frozen=True)
class Ryanmen(Tatsu):
    """
    两面
    """

    def __post_init__(self):
        assert self.first.num != 1
        assert self.first.num != 8

    @property
    def second(self) -> Tile:
        return self.first + 1

    @property
    def waiting(self) -> Set[Tile]:
        return {self.first - 1, self.second + 1}

    def with_waiting(self, tile: Tile) -> Mentsu:
        if tile == self.first - 1:
            return Shuntsu(tile)
        elif tile == self.second + 1:
            return Shuntsu(self.first)
        else:
            raise ValueError(f"tile {tile} is not waiting")

    def __repr__(self):
        return f"{self.first.num}{self.second.num}{self.first.tile_type.lower()}"


@dataclass(frozen=True)
class Penchan(Tatsu):
    """
    边张
    """

    def __post_init__(self):
        assert self.first.tile_type != TileType.Z
        assert self.first.num == 1 or self.first.num == 8

    @property
    def second(self) -> Tile:
        return self.first + 1

    @property
    def waiting(self) -> Set[Tile]:
        if self.first.num == 1:
            return {self.first + 2}
        else:
            return {self.first - 1}

    def with_waiting(self, tile: Tile) -> Mentsu:
        if self.first.num == 1 and tile == self.first + 2:
            return Shuntsu(self.first)
        elif self.first.num == 8 and tile == self.first - 1:
            return Shuntsu(tile)
        else:
            raise ValueError(f"tile {tile} is not waiting")

    def __repr__(self):
        return f"{self.first.num}{self.second.num}{self.first.tile_type.lower()}"


@dataclass(frozen=True)
class Kanchan(Tatsu):
    """
    坎张
    """

    def __post_init__(self):
        assert self.first.tile_type != TileType.Z
        assert 1 <= self.first.num <= 7

    @property
    def second(self) -> Tile:
        return self.first + 2

    @property
    def waiting(self) -> Set[Tile]:
        return {self.first + 1}

    def with_waiting(self, tile: Tile) -> Mentsu:
        if tile == self.first + 1:
            return Shuntsu(self.first)
        else:
            raise ValueError(f"tile {tile} is not waiting")

    def __repr__(self):
        return f"{self.first.num}{self.second.num}{self.first.tile_type.lower()}"


@dataclass(frozen=True)
class Toitsu(Tatsu):
    """
    对子
    """

    @property
    def second(self) -> Tile:
        return self.first

    @property
    def waiting(self) -> Set[Tile]:
        return {self.first}

    def with_waiting(self, tile: Tile) -> Mentsu:
        if tile == self.first:
            return Kotsu(tile)
        else:
            raise ValueError(f"tile {tile} is not waiting")

    def __repr__(self):
        return f"{self.first.num}{self.second.num}{self.first.tile_type.lower()}"


__all__ = ("Tatsu", "Toitsu", "Kanchan", "Ryanmen", "Penchan",)
