from typing import Dict, Set

from mahjong_utils.internal.tile_type_mapping import tile_type_reversed_index_mapping
from mahjong_utils.models.tile import Tile, tile
from mahjong_utils.models.tile_type import TileType

tile_cling: Dict[Tile, Set[Tile]] = {}

for i in range(3):
    tile_type = tile_type_reversed_index_mapping[i]

    for j in range(1, 10):
        t = tile(tile_type, j)
        tile_cling[t] = set()

        for k in {-2, -1, 0, 1, 2}:
            if 1 <= j + k <= 9:
                tile_cling[t].add(tile(tile_type, j + k))

    tile_cling[tile(tile_type, 0)] = tile_cling[tile(tile_type, 5)]

for j in range(1, 8):
    t = tile(TileType.Z, j)
    tile_cling[t] = {t}

__all__ = ("tile_cling",)
