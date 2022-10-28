from dataclasses import dataclass
from typing import List, Set, Dict, Literal, Optional, Mapping, Sequence

from mahjong_utils.internal.hand_utils import calc_regular_advance
from mahjong_utils.internal.legal_tiles_checker import ensure_legal_tiles
from mahjong_utils.internal.regular_hand_searcher import regular_hand_search
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import Hand, ChitoiHand, KokushiHand
from mahjong_utils.models.tile import Tile, is_yaochu, all_yaochu, tile


@dataclass
class ShantenResult:
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    shanten: int
    hands: Sequence[Hand]
    advance: Optional[Set[Tile]]
    discard_to_advance: Optional[Mapping[Tile, Set[Tile]]]


@dataclass
class UnionShantenResult(ShantenResult):
    regular: ShantenResult
    chitoi: Optional[ShantenResult]
    kokushi: Optional[ShantenResult]


# ======== 标准形 ========
def regular_shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> ShantenResult:
    ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    with_got = len(tiles) % 3 == 2
    k = len(tiles) // 3

    hands = regular_hand_search(k, tiles)

    for hand in hands:
        hand.with_got = with_got
        hand.furo = furo
        hand.k += len(furo)

    if with_got:
        advance_aggregated = None
        discard_to_advance_aggregated = dict()

        for hand in hands:
            hand.discard_to_advance = {}
            for i, discard in enumerate(hand.remaining):
                hand_after_discard = hand.copy(update={
                    "remaining": hand.remaining[0:i] + hand.remaining[i + 1:]
                })

                advance = calc_regular_advance(hand_after_discard)
                hand.discard_to_advance[discard] = advance

                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = advance
                else:
                    discard_to_advance_aggregated[discard] |= advance
    else:
        advance_aggregated = set()
        discard_to_advance_aggregated = None

        for hand in hands:
            hand.advance = calc_regular_advance(hand)
            advance_aggregated |= hand.advance

    result = ShantenResult(type="regular",
                           shanten=hands[0].shanten,
                           hands=hands,
                           advance=advance_aggregated,
                           discard_to_advance=discard_to_advance_aggregated)
    return result


# ======== 七对子 ========
def chitoi_shanten(tiles: List[Tile]) -> ShantenResult:
    ensure_legal_tiles(tiles, False)

    cnt: Dict[Tile, int] = {}
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

    shanten = 6 - len(pairs)

    if len(tiles) == 13:
        hand = ChitoiHand(with_got=False,
                          shanten=shanten,
                          advance=advance,
                          pairs=pairs,
                          remaining=remaining)

        return ShantenResult(type="chitoi",
                             shanten=shanten,
                             hands=[hand],
                             advance=advance,
                             discard_to_advance=None)
    else:
        discard_to_advance = {}
        for t in remaining:
            advance_after_discard = advance.copy()
            if t in advance_after_discard:
                advance_after_discard.remove(t)
            discard_to_advance[t] = advance_after_discard

        hand = ChitoiHand(with_got=True,
                          shanten=shanten,
                          discard_to_advance=discard_to_advance,
                          pairs=pairs,
                          remaining=remaining)

        return ShantenResult(type="chitoi",
                             shanten=shanten,
                             hands=[hand],
                             advance=None,
                             discard_to_advance=discard_to_advance)


# ======== 国士无双 ========
def kokushi_shanten(tiles: List[Tile]) -> ShantenResult:
    ensure_legal_tiles(tiles, False)

    yaochu = set()
    repeated = set()
    remaining = []

    for t in tiles:
        if is_yaochu(t):
            if t in yaochu:
                if t in repeated:
                    remaining.append(t)
                else:
                    repeated.add(t)
            else:
                yaochu.add(t)
        else:
            remaining.append(t)

    if len(tiles) == 13:
        hands = []

        if len(repeated) > 0:
            shanten = 12 - len(yaochu)
            advance = all_yaochu - yaochu

            for t in repeated:
                hand = KokushiHand(with_got=False,
                                   shanten=shanten,
                                   advance=advance,
                                   yaochu=list(yaochu),
                                   repeated=t,
                                   remaining=[*remaining, *repeated.difference([t])])
                hands.append(hand)
        else:
            shanten = 13 - len(yaochu)
            advance = all_yaochu.copy()

            hand = KokushiHand(with_got=False,
                               shanten=shanten,
                               advance=advance,
                               yaochu=list(yaochu),
                               repeated=None,
                               remaining=remaining)
            hands.append(hand)

        return ShantenResult(type="kokushi",
                             shanten=shanten,
                             hands=hands,
                             advance=advance,
                             discard_to_advance=None)
    else:
        hands = []

        if len(repeated) > 0:
            shanten = 12 - len(yaochu)
            advance = all_yaochu - yaochu

            for t in repeated:
                discard_to_advance = {}
                for discard in remaining:
                    discard_to_advance[discard] = advance
                for discard in repeated:
                    if discard == t:
                        continue
                    discard_to_advance[discard] = advance

                hand = KokushiHand(with_got=True,
                                   shanten=shanten,
                                   discard_to_advance=discard_to_advance,
                                   yaochu=list(yaochu),
                                   repeated=t,
                                   remaining=[*remaining, *repeated.difference([t])])
                hands.append(hand)

            discard_to_advance = {}

            for h in hands:
                for discard, advance in h.discard_to_advance.items():
                    if discard not in discard_to_advance:
                        discard_to_advance[discard] = set()
                    discard_to_advance[discard] |= advance
        else:
            shanten = 13 - len(yaochu)
            advance = all_yaochu.copy()

            discard_to_advance = {}
            for discard in remaining:
                discard_to_advance[discard] = advance

            hand = KokushiHand(with_got=True,
                               shanten=shanten,
                               discard_to_advance=discard_to_advance,
                               yaochu=list(yaochu),
                               repeated=None,
                               remaining=remaining)
            hands.append(hand)

        return ShantenResult(type="kokushi",
                             shanten=shanten,
                             hands=hands,
                             advance=None,
                             discard_to_advance=discard_to_advance)


# ======== union ========
def shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> UnionShantenResult:
    ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    with_got = len(tiles) % 3 == 2
    k = len(tiles) // 3

    if k != 4:
        regular = regular_shanten(tiles, furo)
        return UnionShantenResult(type="union", shanten=regular.shanten, hands=regular.hands,
                                  advance=regular.advance, discard_to_advance=regular.discard_to_advance,
                                  regular=regular, chitoi=None, kokushi=None)

    regular = regular_shanten(tiles, furo)
    chitoi = chitoi_shanten(tiles)
    kokushi = kokushi_shanten(tiles)

    shanten = min(regular.shanten, chitoi.shanten, kokushi.shanten)
    hands = list()

    if not with_got:
        advance_aggregated = set()
        discard_to_advance_aggregated = None

        if regular.shanten == shanten:
            advance_aggregated |= regular.advance
            hands += regular.hands
        if chitoi.shanten == shanten:
            advance_aggregated |= chitoi.advance
            hands += chitoi.hands
        if kokushi.shanten == shanten:
            advance_aggregated |= kokushi.advance
            hands += kokushi.hands
    else:
        advance_aggregated = None
        discard_to_advance_aggregated = dict()

        if regular.shanten == shanten:
            hands += regular.hands
            for discard, advance in regular.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= advance
        if chitoi.shanten == shanten:
            hands += chitoi.hands
            for discard, advance in chitoi.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= advance
        if kokushi.shanten == shanten:
            hands += kokushi.hands
            for discard, advance in kokushi.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= advance

    return UnionShantenResult(type="union", shanten=shanten, hands=hands,
                              advance=advance_aggregated, discard_to_advance=discard_to_advance_aggregated,
                              regular=regular, chitoi=None, kokushi=None)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "shanten",
           "ShantenResult",
           "UnionShantenResult",)
