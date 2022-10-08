from typing import List, Tuple, Set, Dict

from mahjong_utils.internal.hand_searcher import HandSearcher
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.tile import Tile


def std_search(k: int, hand: List[Tile]) -> Tuple[int, List[HandSearcher.Result]]:
    syanten = 10000
    search_result = []

    def callback(result: HandSearcher.Result):
        nonlocal syanten, search_result

        pending_syanten = 2 * (k - len(result.mentsu)) - len(result.tatsu)
        if result.jyantou is not None:
            pending_syanten -= 1

        if pending_syanten < syanten:
            syanten = pending_syanten
            search_result = [result]
        elif pending_syanten == syanten:
            search_result.append(result)

    searcher = HandSearcher(k, hand, callback)
    searcher.run()

    return syanten, search_result


def get_std_advance(k: int, result: HandSearcher.Result):
    advance = set()

    # 搭子的进张
    for tt in result.tatsu:
        advance |= tt.waiting

    # 浮张的靠张
    if len(result.mentsu) + len(result.tatsu) < k:
        for t in result.remaining:
            advance |= tile_cling[t]

    # 无雀头
    if result.jyantou is None:
        for t in result.remaining:
            advance.add(t)

    return advance


def std_syanten(hand: List[Tile]) -> Tuple[int, Set[Tile]]:
    if len(hand) < 1 or len(hand) > 13 or len(hand) % 3 != 1:
        raise ValueError(f"invalid length of hand: {len(hand)}")

    k = (len(hand) - 1) // 3
    syanten, search_result = std_search(k, hand)

    advance = set()
    for result in search_result:
        advance |= get_std_advance(k, result)

    return syanten, advance


def chitoi_syanten(hand: List[Tile]) -> Tuple[int, Set[Tile]]:
    if len(hand) != 13:
        raise ValueError(f"invalid length of hand: {len(hand)}")

    cnt = {}
    for t in hand:
        cnt[t] = cnt.get(t, 0) + 1

    tot_pair = 0
    advance = set()
    for t in cnt:
        if cnt[t] >= 2:
            tot_pair += 1
        elif cnt[t] == 1:
            advance.add(t)

    return 7 - tot_pair, advance


def std_syanten_with_got_tile(hand: List[Tile]) -> Tuple[int, Dict[Tile, Set[Tile]]]:
    if len(hand) < 2 or len(hand) > 14 or len(hand) % 3 != 2:
        raise ValueError(f"invalid length of hand: {len(hand)}")

    k = (len(hand) - 2) // 3
    syanten, search_result = std_search(k, hand)

    discard_advance_mapping: Dict[Tile, Set[Tile]] = {}

    for result in search_result:
        for i, discard in enumerate(result.remaining):
            advance = get_std_advance(k, HandSearcher.Result(result.jyantou, result.mentsu, result.tatsu,
                                                             result.remaining[0:i] + result.remaining[i + 1:]))

            if discard not in discard_advance_mapping:
                discard_advance_mapping[discard] = advance
            else:
                discard_advance_mapping[discard] |= advance

    return syanten, discard_advance_mapping


__all__ = ("std_syanten", "std_syanten_with_got_tile",)
