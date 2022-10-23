from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Set, Sequence, Union

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

    def __repr__(self):
        return tiles_text([self.first, self.second])

    @property
    @abstractmethod
    def waiting(self) -> Set[Tile]:
        raise NotImplementedError()

    def with_waiting(self, tile: Tile) -> Mentsu:
        raise NotImplementedError()


class Ryanmen(Tatsu):
    """
    两面
    """

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


class Penchan(Tatsu):
    """
    边张
    """

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
        if (self.first.num == 1 and tile == self.first + 2) or (self.first.num == 7 and tile == self.first - 1):
            return Shuntsu(self.first)
        else:
            raise ValueError(f"tile {tile} is not waiting")


class Kanchan(Tatsu):
    """
    坎张
    """

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


def parse_tatsu(t: Union[Sequence[Tile], str]) -> Tatsu:
    if isinstance(t, str):
        t = parse_tiles(t)

    if len(t) != 2:
        raise ValueError("tiles must has length of 2")

    first, second = t[0], t[1]

    if first > second:
        first, second = second, first

    if first == second:
        return Toitsu(first)
    else:
        if first.tile_type == TileType.Z or second.tile_type == TileType.Z:
            raise ValueError(f"invalid tiles: {t}")

        if second - first == 1:
            if first.num == 1 or first.num == 7:
                return Penchan(first)
            else:
                return Ryanmen(first)
        elif second - first == 2:
            return Kanchan(first)
        else:
            raise ValueError(f"invalid tiles: {first}, {second}")


__all__ = ("Tatsu", "Toitsu", "Kanchan", "Ryanmen", "Penchan", "parse_tatsu")
