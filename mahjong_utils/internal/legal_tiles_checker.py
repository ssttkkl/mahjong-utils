from typing import Sequence, Tuple

from mahjong_utils.models.tile import Tile, tile


def ensure_legal_tiles(tiles: Sequence[Tile], allow_any_k: bool = True) -> Tuple[Tile, ...]:
    if not allow_any_k and len(tiles) // 3 != 4:
        raise ValueError(f"invalid length of hand: {len(tiles)}")
    if len(tiles) < 1 or len(tiles) > 14 or len(tiles) % 3 == 0:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        cnt[t] = cnt.get(t, 0) + 1
        if cnt[t] > 4:
            raise ValueError(f"invalid num of tile {t}")

    ret_tiles = tuple(tile(t.tile_type, 5) if t.num == 0 else t for t in tiles)
    return ret_tiles
