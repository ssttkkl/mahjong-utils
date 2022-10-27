from typing import List

from mahjong_utils.models.tile import Tile


def ensure_legal_tiles(tiles: List[Tile], allow_any_k: bool = True):
    if not allow_any_k and len(tiles) // 3 != 4:
        raise ValueError(f"invalid length of hand: {len(tiles)}")
    if len(tiles) < 1 or len(tiles) > 14 or len(tiles) % 3 == 0:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        cnt[t] = cnt.get(t, 0) + 1
        if cnt[t] > 4:
            raise ValueError(f"invalid num of tile {t}")
