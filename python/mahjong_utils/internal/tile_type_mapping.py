from mahjong_utils.models.tile_type import TileType

tile_type_index_mapping = {TileType.M: 0, TileType.P: 1, TileType.S: 2, TileType.Z: 3}
tile_type_reversed_index_mapping = [TileType.M, TileType.P, TileType.S, TileType.Z]
__all__ = ("tile_type_index_mapping", "tile_type_reversed_index_mapping")
