from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Set, Sequence, Union

from .mentsu import Mentsu, Shuntsu, Kotsu
from .tile import Tile, parse_tiles, tile_text
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

    def __str__(self):
        return tile_text([self.first, self.second])

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

    if t[0] > t[1]:
        t[0], t[1] = t[1], t[0]

    if t[0] == t[1]:
        return Toitsu(t[0])
    else:
        if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z:
            raise ValueError(f"invalid tiles: {t}")

        if t[1] - t[0] == 1:
            if t[0].num == 1 or t[0].num == 7:
                return Penchan(t[0])
            else:
                return Ryanmen(t[0])
        elif t[1] - t[0] == 2:
            return Kanchan(t[0])
        else:
            raise ValueError(f"invalid tiles: {t}")


__all__ = ("Tatsu", "Toitsu", "Kanchan", "Ryanmen", "Penchan", "parse_tatsu")
