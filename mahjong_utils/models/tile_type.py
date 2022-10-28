from enum import Enum


class TileType(str, Enum):
    """
    牌的花色
    """

    M = "m"
    P = "p"
    S = "s"
    Z = "z"

    @property
    def num_range(self):
        if self == TileType.Z:
            return range(1, 8)
        else:
            return range(0, 10)


__all__ = ("TileType",)
