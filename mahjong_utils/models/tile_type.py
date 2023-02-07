from enum import Enum


class TileType(str, Enum):
    """
    牌的花色
    """

    M = "M"
    P = "P"
    S = "S"
    Z = "Z"

    @property
    def num_range(self):
        if self == TileType.Z:
            return range(1, 8)
        else:
            return range(0, 10)

    def __str__(self):
        return self.name


tile_type_index_mapping = {TileType.M: 0, TileType.P: 1, TileType.S: 2, TileType.Z: 3}
tile_type_reversed_index_mapping = [TileType.M, TileType.P, TileType.S, TileType.Z]

__all__ = ("TileType", "tile_type_index_mapping", "tile_type_reversed_index_mapping")
