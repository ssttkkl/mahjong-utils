from abc import ABC
from dataclasses import dataclass
from typing import List, Tuple, Set, Dict, Literal, Optional

from mahjong_utils.internal.regular_hand_searcher import RegularHandSearcher
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import RegularHand, Hand, ChitoiHand, KokushiHand
from mahjong_utils.models.tile import Tile, is_yaochu, all_yaochu, tile


@dataclass(frozen=True)
class ShantenResult(ABC):
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    shanten: int
    advance: Set[Tile]
    advance_of_each_hand: List[Tuple[Hand, Set[Tile]]]


@dataclass(frozen=True)
class UnionShantenResult(ShantenResult):
    regular: ShantenResult
    chitoi: Optional[ShantenResult]
    kokushi: Optional[ShantenResult]


@dataclass(frozen=True)
class ShantenWithGotTileResult:
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    shanten: int
    discard_to_advance: Dict[Tile, Set[Tile]]
    discard_to_advance_of_each_hand: List[Tuple[Hand, Dict[Tile, Set[Tile]]]]


@dataclass(frozen=True)
class UnionShantenWithGotTileResult(ShantenWithGotTileResult):
    regular: ShantenWithGotTileResult
    chitoi: Optional[ShantenWithGotTileResult]
    kokushi: Optional[ShantenWithGotTileResult]


# ======== 标准形 ========
def _regular_search(k: int, tiles: List[Tile]) -> Tuple[int, List[RegularHand]]:
    shanten = 10000
    hands = []

    def callback(hand: RegularHand):
        nonlocal shanten, hands

        pending_shanten = 2 * (k - len(hand.menzen_mentsu)) - len(hand.tatsu)
        if hand.jyantou is not None:
            pending_shanten -= 1

        if pending_shanten < shanten:
            shanten = pending_shanten
            hands = [hand]
        elif pending_shanten == shanten:
            hands.append(hand)

    searcher = RegularHandSearcher(k, tiles, callback)
    searcher.run()

    return shanten, hands


def _get_regular_advance(k: int, result: RegularHand) -> Set[Tile]:
    advance = set()

    # 搭子的进张
    for tt in result.tatsu:
        advance |= tt.waiting

    # 浮张的靠张
    if len(result.menzen_mentsu) + len(result.tatsu) < k:
        for t in result.remaining:
            advance |= tile_cling[t]

    # 无雀头
    if result.jyantou is None:
        for t in result.remaining:
            advance.add(t)

    return advance


def calc_regular_shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> ShantenResult:
    if len(tiles) < 1 or len(tiles) > 13 or len(tiles) % 3 != 1:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    if furo is None:
        furo = []

    k = (len(tiles) - 1) // 3
    shanten, hands = _regular_search(k, tiles)

    advance_aggregated = set()
    advance_of_each_hand = list()
    for hand in hands:
        hand.furo = furo

        advance = _get_regular_advance(k, hand)
        advance_aggregated |= advance
        advance_of_each_hand.append((hand, advance))

    return ShantenResult("regular", shanten, advance_aggregated, advance_of_each_hand)


def calc_regular_shanten_with_got_tile(tiles: List[Tile],
                                       furo: Optional[List[Furo]] = None) -> ShantenWithGotTileResult:
    if len(tiles) < 2 or len(tiles) > 14 or len(tiles) % 3 != 2:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    if furo is None:
        furo = []

    k = (len(tiles) - 2) // 3
    shanten, hands = _regular_search(k, tiles)

    discard_to_advance_aggregated = dict()
    discard_to_advance_of_each_hand = list()

    for hand in hands:
        hand.furo = furo

        discard_to_advance = {}
        for i, discard in enumerate(hand.remaining):
            hand_after_discard = hand.copy(update={
                "remaining": hand.remaining[0:i] + hand.remaining[i + 1:]
            })
            advance = _get_regular_advance(k, hand_after_discard)
            discard_to_advance[discard] = advance

            if discard not in discard_to_advance_aggregated:
                discard_to_advance_aggregated[discard] = advance
            else:
                discard_to_advance_aggregated[discard] |= advance
        discard_to_advance_of_each_hand.append((hand, discard_to_advance))

    return ShantenWithGotTileResult("regular", shanten, discard_to_advance_aggregated, discard_to_advance_of_each_hand)


# ======== 七对子 ========
def calc_chitoi_shanten(tiles: List[Tile]) -> ShantenResult:
    if len(tiles) != 13:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        if t.num == 0:
            t = tile(t.tile_type, 5)
        cnt[t] = cnt.get(t, 0) + 1

    pairs = []
    remaining = []
    advance = set()
    for t in cnt:
        if cnt[t] >= 2:
            pairs.append(t)
            for i in range(cnt[t] - 2):
                remaining.append(t)
        elif cnt[t] == 1:
            advance.add(t)
            remaining.append(t)

    hand = ChitoiHand(pairs=pairs, remaining=remaining)
    return ShantenResult("chitoi", 6 - len(pairs), advance,
                         [(hand, advance)])


def calc_chitoi_shanten_with_got_tile(tiles: List[Tile]) -> ShantenWithGotTileResult:
    if len(tiles) != 14:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        if t.num == 0:
            t = tile(t.tile_type, 5)
        cnt[t] = cnt.get(t, 0) + 1

    pairs = []
    remaining = []
    advance = set()
    for t in cnt:
        if cnt[t] >= 2:
            pairs.append(t)
            for i in range(cnt[t] - 2):
                remaining.append(t)
        elif cnt[t] == 1:
            advance.add(t)
            remaining.append(t)

    discard_to_advance = {}
    for t in remaining:
        advance_after_discard = advance.copy()
        if t in advance_after_discard:
            advance_after_discard.remove(t)
        discard_to_advance[t] = advance_after_discard

    hand = ChitoiHand(pairs=pairs, remaining=remaining)
    return ShantenWithGotTileResult("chitoi", 6 - len(pairs), discard_to_advance,
                                    [(hand, discard_to_advance)])


# ======== 国士无双 ========
def calc_kokushi_shanten(tiles: List[Tile]) -> ShantenResult:
    if len(tiles) != 13:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        if is_yaochu(t):
            cnt[t] = cnt.get(t, 0) + 1

    pair = None
    for t in cnt:
        if cnt[t] >= 2:
            pair = t
            break

    hand = KokushiHand(tiles=tiles)
    if pair is not None:
        advance = all_yaochu - cnt.keys()
        shanten = 12 - len(cnt)
    else:
        advance = all_yaochu.copy()
        shanten = 13 - len(cnt)
    return ShantenResult("kokushi", shanten, advance,
                         [(hand, advance)])


def calc_kokushi_shanten_with_got_tile(tiles: List[Tile]) -> ShantenWithGotTileResult:
    if len(tiles) != 14:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    discard = set()  # 非幺九牌可以打掉
    for t in tiles:
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

    shanten = 10000
    discard_to_advance = {}

    tiles = tiles.copy()
    for t in discard:
        tiles.remove(t)
        pending = calc_kokushi_shanten(tiles)

        if pending.shanten < shanten:
            shanten = pending.shanten
            discard_to_advance = {t: pending.advance}
        elif pending.shanten == shanten:
            discard_to_advance[t] = pending.advance
        tiles.append(t)

    hand = KokushiHand(tiles=tiles)
    return ShantenWithGotTileResult("kokushi", shanten, discard_to_advance,
                                    [(hand, discard_to_advance)])


# ======== union ========
def calc_shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> UnionShantenResult:
    if len(tiles) != 13:
        regular = calc_regular_shanten(tiles, furo)
        return UnionShantenResult("union", regular.shanten,
                                  regular.advance, regular.advance_of_each_hand,
                                  regular, None, None)

    regular = calc_regular_shanten(tiles, furo)
    chitoi = calc_chitoi_shanten(tiles)
    kokushi = calc_kokushi_shanten(tiles)

    shanten = min(regular.shanten, chitoi.shanten, kokushi.shanten)
    advance_aggregated = set()
    advance_of_each_hand = list()

    if regular.shanten == shanten:
        advance_aggregated |= regular.advance
        advance_of_each_hand += regular.advance_of_each_hand
    if chitoi.shanten == shanten:
        advance_aggregated |= chitoi.advance
        advance_of_each_hand += chitoi.advance_of_each_hand
    if kokushi.shanten == shanten:
        advance_aggregated |= kokushi.advance
        advance_of_each_hand += kokushi.advance_of_each_hand

    return UnionShantenResult("union", shanten, advance_aggregated, advance_of_each_hand,
                              regular, chitoi, kokushi)


def calc_shanten_with_got_tile(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> UnionShantenWithGotTileResult:
    if len(tiles) != 14:
        regular = calc_regular_shanten_with_got_tile(tiles, furo)
        return UnionShantenWithGotTileResult("union", regular.shanten,
                                             regular.discard_to_advance, regular.discard_to_advance_of_each_hand,
                                             regular, None, None)

    regular = calc_regular_shanten_with_got_tile(tiles)
    chitoi = calc_chitoi_shanten_with_got_tile(tiles)
    kokushi = calc_kokushi_shanten_with_got_tile(tiles)

    shanten = min(regular.shanten, chitoi.shanten, kokushi.shanten)
    discard_to_advance_aggregated = dict()
    discard_to_advance_of_each_hand = list()

    if regular.shanten == shanten:
        discard_to_advance_of_each_hand += regular.discard_to_advance_of_each_hand
        for discard, advance in regular.discard_to_advance.items():
            if discard not in discard_to_advance_aggregated:
                discard_to_advance_aggregated[discard] = set()
            discard_to_advance_aggregated[discard] |= advance
    if chitoi.shanten == shanten:
        discard_to_advance_of_each_hand += chitoi.discard_to_advance_of_each_hand
        for discard, advance in chitoi.discard_to_advance.items():
            if discard not in discard_to_advance_aggregated:
                discard_to_advance_aggregated[discard] = set()
            discard_to_advance_aggregated[discard] |= advance
    if kokushi.shanten == shanten:
        discard_to_advance_of_each_hand += kokushi.discard_to_advance_of_each_hand
        for discard, advance in kokushi.discard_to_advance.items():
            if discard not in discard_to_advance_aggregated:
                discard_to_advance_aggregated[discard] = set()
            discard_to_advance_aggregated[discard] |= advance

    return UnionShantenWithGotTileResult("union", shanten,
                                         discard_to_advance_aggregated, discard_to_advance_of_each_hand,
                                         regular, chitoi, kokushi)


__all__ = ("calc_regular_shanten", "calc_regular_shanten_with_got_tile",
           "calc_chitoi_shanten", "calc_chitoi_shanten_with_got_tile",
           "calc_kokushi_shanten", "calc_kokushi_shanten_with_got_tile",
           "calc_shanten", "calc_shanten_with_got_tile",
           "ShantenResult", "ShantenWithGotTileResult",
           "UnionShantenResult", "UnionShantenWithGotTileResult")
