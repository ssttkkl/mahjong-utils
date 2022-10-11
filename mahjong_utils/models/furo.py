from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List, overload, Sequence

from .mentsu import Shuntsu, Kotsu, Mentsu
from .tile import Tile, tiles
from .tile_type import TileType


class Furo(Mentsu, ABC):
    """
    副露
    """

    @property
    @abstractmethod
    def tiles(self) -> List[Tile]:
        raise NotImplementedError()


@dataclass(frozen=True)
class Chi(Shuntsu, Furo):
    """
    吃
    """
    pass


@dataclass(frozen=True)
class Pon(Kotsu, Furo):
    """
    碰
    """
    pass


@dataclass(frozen=True)
class Kan(Kotsu, Furo):
    """
    杠
    """

    ankan: bool

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile] * 4


@overload
def furo(t: str) -> Furo:
    ...


@overload
def furo(t: Sequence[Tile]) -> Furo:
    ...


def furo(t) -> Furo:
    if isinstance(t, str):
        t = tiles(t)

    if len(t) == 3:
        if t[0] == t[1] == t[2]:
            return Pon(t[0])
        else:
            if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z or t[2].tile_type == TileType.Z:
                raise ValueError(f"invalid tiles: {t}")
            t.sort()
            if t[1] - t[0] == 1 and t[2] - t[1] == 1:
                return Chi(t[0])
            else:
                raise ValueError(f"invalid tiles: {t}")
    elif len(t) == 4:
        if t[0] == t[1] == t[2] == t[3]:
            return Kan(t[0], False)
        elif t[0] == t[3] and t[1] == t[2] and t[0].tile_type == t[1].tile_type and t[0].num == 0:
            #  0880p 这样的文本表示暗杠
            return Kan(t[1], True)
    else:
        raise ValueError(f"invalid tiles: {t}")
