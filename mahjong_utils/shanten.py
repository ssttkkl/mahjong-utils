from typing import List, Dict, Literal, Optional, Sequence

from pydantic import BaseModel

from mahjong_utils.internal.hand_utils import calc_regular_advance
from mahjong_utils.internal.legal_tiles_checker import ensure_legal_tiles
from mahjong_utils.internal.regular_hand_searcher import regular_hand_search
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand_pattern import HandPattern, ChitoiHandPattern, KokushiHandPattern
from mahjong_utils.models.shanten import ShantenWithGot, ShantenWithoutGot, Shanten, ShantenInfoMixin
from mahjong_utils.models.tile import Tile, is_yaochu, all_yaochu, tile


class ShantenResult(BaseModel, ShantenInfoMixin):
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    hands: Sequence[HandPattern]
    shanten_info: Optional[Shanten]


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
        hand.furo = furo
        hand.k += len(furo)

    if with_got:
        discard_to_advance_aggregated = dict()

        for hand in hands:
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

        for discard in list(discard_to_advance_aggregated.keys()):
            advance = discard_to_advance_aggregated[discard]
            discard_to_advance_aggregated[discard] = ShantenWithoutGot(
                shanten=hands[0].shanten,
                advance=advance,
            )
            # TODO：一向听的牌分析好型率

        shanten_info = ShantenWithGot(shanten=hands[0].shanten,
                                      discard_to_advance=discard_to_advance_aggregated)
    else:
        advance_aggregated = set()

        for hand in hands:
            advance = calc_regular_advance(hand)
            hand.shanten_info.advance = advance
            # TODO：一向听的牌分析好型率
            advance_aggregated |= advance

        shanten_info = ShantenWithoutGot(
            shanten=hands[0].shanten,
            advance=advance_aggregated,
        )

    result = ShantenResult(type="regular",
                           hands=hands,
                           shanten_info=shanten_info)
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
        hand = ChitoiHandPattern(with_got=False,
                                 pairs=pairs,
                                 remaining=remaining)

        shanten_info = ShantenWithoutGot(
            shanten=shanten,
            advance=advance,
        )
    else:
        hand = ChitoiHandPattern(with_got=True,
                                 pairs=pairs,
                                 remaining=remaining)

        hand_tiles = list(hand.tiles)

        discard_to_advance = {}
        for t in remaining:
            advance_after_discard = advance.copy()
            if t in advance_after_discard:
                advance_after_discard.remove(t)

            hand_tiles.remove(t)
            discard_to_advance[t] = ShantenWithoutGot(
                shanten=shanten,
                advance=advance_after_discard,
            )
            hand_tiles.append(t)

        shanten_info = ShantenWithGot(shanten=shanten,
                                      discard_to_advance=discard_to_advance)

    hand.shanten_info = shanten_info
    return ShantenResult(type="chitoi",
                         hands=[hand],
                         shanten_info=shanten_info)


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
                hand = KokushiHandPattern(with_got=False,
                                          yaochu=list(yaochu),
                                          repeated=t,
                                          remaining=[*remaining, *repeated.difference([t])])
                hands.append(hand)
        else:
            shanten = 13 - len(yaochu)
            advance = all_yaochu.copy()

            hand = KokushiHandPattern(with_got=False,
                                      yaochu=list(yaochu),
                                      repeated=None,
                                      remaining=remaining)
            hands.append(hand)

        shanten_info = ShantenWithoutGot(
            shanten=shanten,
            advance=advance,
        )
        for hand in hands:
            hand.shanten_info = shanten_info
    else:
        hands = []

        if len(repeated) > 0:
            shanten = 12 - len(yaochu)
            advance = all_yaochu - yaochu

            for t in repeated:
                hand = KokushiHandPattern(with_got=True,
                                          yaochu=list(yaochu),
                                          repeated=t,
                                          remaining=[*remaining, *repeated.difference([t])])
                hands.append(hand)

                shanten_info = ShantenWithoutGot(
                    shanten=shanten,
                    advance=advance,
                )

                discard_to_advance = {}
                for discard in remaining:
                    discard_to_advance[discard] = shanten_info
                for discard in repeated:
                    if discard == t:
                        continue
                    discard_to_advance[discard] = shanten_info

                hand.shanten_info = ShantenWithGot(
                    shanten=shanten,
                    discard_to_advance=discard_to_advance
                )

            discard_to_advance_aggregated = dict()
            for hand in hands:
                for discard, shanten_info in hand.discard_to_advance.items():
                    if discard not in discard_to_advance_aggregated:
                        discard_to_advance_aggregated[discard] = set()
                    discard_to_advance_aggregated[discard] |= shanten_info.advance
            for discard in list(discard_to_advance_aggregated.keys()):
                advance = discard_to_advance_aggregated[discard]
                discard_to_advance_aggregated[discard] = ShantenWithoutGot(
                    shanten=shanten,
                    advance=advance,
                )
        else:
            shanten = 13 - len(yaochu)

            discard_to_advance = {}
            for discard in remaining:
                discard_to_advance[discard] = ShantenWithoutGot(
                    shanten=shanten,
                    advance=all_yaochu.copy()
                )

            hand = KokushiHandPattern(with_got=True,
                                      yaochu=list(yaochu),
                                      repeated=None,
                                      remaining=remaining)
            hand.shanten_info = ShantenWithGot(
                shanten=shanten,
                discard_to_advance=discard_to_advance,
            )
            hands.append(hand)

            discard_to_advance_aggregated = discard_to_advance

        shanten_info = ShantenWithGot(shanten=shanten,
                                      discard_to_advance=discard_to_advance_aggregated)

    return ShantenResult(type="kokushi",
                         hands=hands,
                         shanten_info=shanten_info)


# ======== union ========
def shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> UnionShantenResult:
    ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    with_got = len(tiles) % 3 == 2
    k = len(tiles) // 3

    if k != 4:
        regular = regular_shanten(tiles, furo)
        return UnionShantenResult(type="union", hands=regular.hands, shanten_info=regular.shanten_info,
                                  regular=regular, chitoi=None, kokushi=None)

    regular = regular_shanten(tiles, furo)
    chitoi = chitoi_shanten(tiles)
    kokushi = kokushi_shanten(tiles)

    shanten = min(regular.shanten, chitoi.shanten, kokushi.shanten)
    hands = list()

    if not with_got:
        advance_aggregated = set()

        if regular.shanten == shanten:
            advance_aggregated |= regular.advance
            hands += regular.hands
        if chitoi.shanten == shanten:
            advance_aggregated |= chitoi.advance
            hands += chitoi.hands
        if kokushi.shanten == shanten:
            advance_aggregated |= kokushi.advance
            hands += kokushi.hands

        shanten_info = ShantenWithoutGot(
            shanten=shanten,
            advance=advance_aggregated,
        )
    else:
        discard_to_advance_aggregated = dict()

        if regular.shanten == shanten:
            hands += regular.hands
            for discard, inner in regular.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= inner.advance
        if chitoi.shanten == shanten:
            hands += chitoi.hands
            for discard, inner in chitoi.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= inner.advance
        if kokushi.shanten == shanten:
            hands += kokushi.hands
            for discard, inner in kokushi.discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = set()
                discard_to_advance_aggregated[discard] |= inner.advance

        for discard in list(discard_to_advance_aggregated.keys()):
            advance = discard_to_advance_aggregated[discard]
            discard_to_advance_aggregated[discard] = ShantenWithoutGot(
                shanten=shanten,
                advance=advance,
            )

        shanten_info = ShantenWithGot(shanten=shanten,
                                      discard_to_advance=discard_to_advance_aggregated)

    return UnionShantenResult(type="union", hands=hands, shanten_info=shanten_info,
                              regular=regular, chitoi=chitoi, kokushi=kokushi)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "shanten",
           "ShantenResult",
           "UnionShantenResult",)
