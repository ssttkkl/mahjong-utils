from typing import List, Tuple, Set, Dict

from mahjong_utils.internal.hand_searcher import HandSearcher
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.tile import Tile, is_yaochu, yaochu


# ======== 标准形 ========
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


# ======== 七对子 ========
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

    return 6 - tot_pair, advance


def chitoi_syanten_with_got_tile(hand: List[Tile]) -> Tuple[int, Dict[Tile, Set[Tile]]]:
    if len(hand) != 14:
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

    discard_advance_mapping = {}
    for t in advance:
        advance_after_discard = advance.copy()
        advance_after_discard.remove(t)
        discard_advance_mapping[t] = advance_after_discard

    return 6 - tot_pair, discard_advance_mapping


# ======== 国士无双 ========
def kokushi_syanten(hand: List[Tile]) -> Tuple[int, Set[Tile]]:
    if len(hand) != 13:
        raise ValueError(f"invalid length of hand: {len(hand)}")

    cnt = {}
    for t in hand:
        if is_yaochu(t):
            cnt[t] = cnt.get(t, 0) + 1

    pair = None
    for t in cnt:
        if cnt[t] >= 2:
            pair = t
            break

    if pair is not None:
        advance = yaochu - cnt.keys()
        return 12 - len(cnt), advance
    else:
        return 13 - len(cnt), yaochu.copy()


def kokushi_syanten_with_got_tile(hand: List[Tile]) -> Tuple[int, Dict[Tile, Set[Tile]]]:
    if len(hand) != 14:
        raise ValueError(f"invalid length of hand: {len(hand)}")

    cnt = {}
    discard = set()  # 非幺九牌可以打掉
    for t in hand:
        if is_yaochu(t):
            cnt[t] = cnt.get(t, 0) + 1
        else:
            discard.add(t)

    pairs = set()
    for t in cnt:
        if cnt[t] >= 2:
            pairs.add(t)

    # 幺九对子多于1对时可以打掉
    if len(pairs) > 1:
        discard |= pairs

    syanten = 10000
    discard_advance_mapping = {}

    hand = hand.copy()
    for t in discard:
        hand.remove(t)
        pending_syanten, pending_advance = kokushi_syanten(hand)

        if pending_syanten < syanten:
            syanten = pending_syanten
            discard_advance_mapping = {t: pending_advance}
        elif pending_syanten == syanten:
            discard_advance_mapping[t] = pending_advance
        hand.append(t)

    return syanten, discard_advance_mapping


# ======== union ========
def syanten(hand: List[Tile]) -> Tuple[int, Set[Tile]]:
    std, std_advance = std_syanten(hand)
    chitoi, chitoi_advance = chitoi_syanten(hand)
    kokushi, kokushi_advance = kokushi_syanten(hand)

    syanten = min(std, chitoi, kokushi)
    advance = set()

    if std == syanten:
        advance |= std_advance
    if chitoi == syanten:
        advance |= chitoi_advance
    if kokushi == syanten:
        advance |= kokushi_advance

    return syanten, advance


def syanten_with_got_tile(hand: List[Tile]) -> Tuple[int, Dict[Tile, Set[Tile]]]:
    std, std_mapping = std_syanten_with_got_tile(hand)
    chitoi, chitoi_mapping = chitoi_syanten_with_got_tile(hand)
    kokushi, kokushi_mapping = kokushi_syanten_with_got_tile(hand)

    syanten = min(std, chitoi, kokushi)
    mapping = dict()

    if std == syanten:
        for discard, advance in std_mapping.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance
    if chitoi == syanten:
        for discard, advance in chitoi_mapping.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance
    if kokushi == syanten:
        for discard, advance in kokushi_mapping.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance

    return syanten, mapping


__all__ = ("std_syanten", "std_syanten_with_got_tile",
           "chitoi_syanten", "chitoi_syanten_with_got_tile",
           "kokushi_syanten", "kokushi_syanten_with_got_tile",
           "syanten", "syanten_with_got_tile")
