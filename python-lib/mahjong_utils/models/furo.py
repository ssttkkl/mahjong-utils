from abc import ABC
from typing import List, Sequence, Union

from pydantic.dataclasses import dataclass

from .mentsu import Shuntsu, Kotsu, Mentsu
from .tile import Tile, parse_tiles
from .tile_type import TileType


class Furo(Mentsu, ABC):
    """
    副露
    """

    def __encode__(self) -> dict:
        return dict(type=type(self).__name__, tile=self.tile.__encode__())

    @classmethod
    def __decode__(cls, data: dict) -> "Furo":
        t = Tile.__decode__(data['tile'])
        if data['type'] == 'Chi':
            return Chi(t)
        elif data['type'] == 'Pon':
            return Pon(t)
        elif data['type'] == 'Kan':
            return Kan(t, data['ankan'])
        else:
            raise ValueError("invalid type: " + data['type'])

    @staticmethod
    def parse(t: Union[Sequence[Tile], str], ankan: bool = False) -> "Furo":
        if isinstance(t, str):
            if len(t) == 5 and t[0] == t[3] == '0' and t[1] == t[2]:
                t = t[1] + t[1] + t[1] + t[1] + t[4]
                ankan = True
            t = parse_tiles(t)

        if len(t) == 3:
            if t[0] == t[1] == t[2]:
                return Pon(t[0])
            else:
                if t[0].tile_type == TileType.Z or t[1].tile_type == TileType.Z or t[2].tile_type == TileType.Z:
                    raise ValueError(f"invalid tiles: {t}")
                t = sorted(t)
                if t[1] - t[0] == 1 and t[2] - t[1] == 1:
                    return Chi(t[0])
                else:
                    raise ValueError(f"invalid tiles: {t}")
        elif len(t) == 4:
            if t[0] == t[1] == t[2] == t[3]:
                return Kan(t[0], ankan)

        raise ValueError(f"invalid tiles: {t}")


@dataclass(frozen=True)
class Chi(Shuntsu, Furo):
    """
    吃
    """

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num + 1}{self.tile.num + 2}{self.tile.tile_type.lower()}"


@dataclass(frozen=True)
class Pon(Kotsu, Furo):
    """
    碰
    """

    def __repr__(self):
        return f"{self.tile.num}{self.tile.num}{self.tile.num}{self.tile.tile_type.lower()}"


@dataclass(frozen=True)
class Kan(Kotsu, Furo):
    """
    杠
    """

    ankan: bool

    def __encode__(self) -> dict:
        x = Furo.__encode__(self)
        x["ankan"] = self.ankan
        return x

    def __repr__(self):
        if self.ankan:
            return f"0{self.tile.num}{self.tile.num}0{self.tile.tile_type.lower()}"
        else:
            return f"{self.tile.num}{self.tile.num}{self.tile.num}{self.tile.num}{self.tile.tile_type.lower()}"

    @property
    def tiles(self) -> List[Tile]:
        return [self.tile] * 4
