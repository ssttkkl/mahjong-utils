from enum import Enum

from mahjong_utils.models.tile import Tile
from mahjong_utils.models.tile_type import TileType


class Wind(int, Enum):
    """
    风
    """

    east = 0
    south = 1
    west = 2
    north = 3

    @property
    def tile(self) -> Tile:
        return Tile.by_type_and_num(TileType.Z, self + 1)


__all__ = ("Wind",)
