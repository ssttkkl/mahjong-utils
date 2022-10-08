from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List

from .tatsu import Tatsu
from .tile import Tile


class Furo(ABC):
    @abstractmethod
    @property
    def tiles(self) -> List[Tile]:
        raise NotImplementedError()


@dataclass(frozen=True)
class Chi(Furo):
    tile: Tile
    material: Tatsu

    def __post_init__(self):
        assert self.tile in self.material.waiting

    @property
    def tiles(self) -> List[Tile]:
        return [self.material.first, self.material.second, self.tile]


class Pon(Furo):
    tile: Tile


class Kan(Furo):
    pass


class AnKan(Kan):
    tile: Tile


class MinKan(Kan):
    tile: Tile


class KaKan(Kan):
    tile: Tile
