from typing import List, Dict, Literal, Optional, Sequence, Set, Tuple, FrozenSet

from pydantic import BaseModel

from mahjong_utils.internal.hand_utils import calc_regular_advance, regular_pattern_after_discard, calc_regular_shanten
from mahjong_utils.internal.legal_tiles_checker import ensure_legal_tiles
from mahjong_utils.internal.regular_hand_pattern_searcher import regular_hand_pattern_search
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import Hand
from mahjong_utils.models.hand_pattern import ChitoiHandPattern, KokushiHandPattern, RegularHandPattern
from mahjong_utils.models.shanten import ShantenWithGot, ShantenWithoutGot, Shanten, ShantenInfoMixin
from mahjong_utils.models.tile import Tile, is_yaochu, all_yaochu, tile


class ShantenResult(BaseModel, ShantenInfoMixin):
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    hand: Hand
    shanten_info: Shanten


class UnionShantenResult(ShantenResult):
    regular: ShantenResult
    chitoi: Optional[ShantenResult]
    kokushi: Optional[ShantenResult]


# ======== 标准形 ========
def _handle_regular_shanten_without_got(shanten_num: int,
                                        patterns: Sequence[RegularHandPattern]) -> ShantenWithoutGot:
    shanten_info = ShantenWithoutGot(shanten=shanten_num)

    for pat in patterns:
        shanten_info.advance |= calc_regular_advance(pat)

    # TODO：一向听的牌分析好型率

    return shanten_info


def _handle_regular_shanten_with_got(shanten_num: int,
                                     patterns: Sequence[RegularHandPattern],
                                     tiles: Sequence[Tile]) -> ShantenWithGot:
    shanten_info = ShantenWithGot(shanten=shanten_num)

    tiles_set = set()

    for t in tiles:
        if t.num != 0:
            tiles_set.add(t)
        else:
            tiles_set.add(tile(t.tile_type, 5))

    for discard in tiles_set:
        patterns_after_discard = set()
        for pat in patterns:
            for pat_after_discard in regular_pattern_after_discard(pat, discard):
                patterns_after_discard.add(pat_after_discard)

        shanten_info_after_discard = ShantenWithoutGot(shanten=100)
        for pat in patterns_after_discard:
            shanten_num_after_discard = calc_regular_shanten(pat)
            if shanten_info_after_discard.shanten > shanten_num_after_discard:
                shanten_info_after_discard.shanten = shanten_num_after_discard
                shanten_info_after_discard.advance = set()
            if shanten_info_after_discard.shanten == shanten_num_after_discard:
                shanten_info_after_discard.advance |= calc_regular_advance(pat)

        shanten_info.discard_to_advance[discard] = shanten_info_after_discard

    # TODO：一向听的牌分析好型率

    return shanten_info


def regular_shanten(tiles: Sequence[Tile], furo: Optional[Sequence[Furo]] = None) -> ShantenResult:
    ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    shanten_num, patterns = regular_hand_pattern_search(tiles, furo)

    with_got = len(tiles) % 3 == 2
    if with_got:
        shanten_info = _handle_regular_shanten_with_got(shanten_num, patterns, tiles)
    else:
        shanten_info = _handle_regular_shanten_without_got(shanten_num, patterns)

    hand = Hand(tiles=tiles, furo=furo, patterns=patterns)
    result = ShantenResult(type="regular", hand=hand, shanten_info=shanten_info)
    return result


# ======== 七对子 ========
def chitoi_shanten(tiles: Sequence[Tile]) -> ShantenResult:
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
    pattern = ChitoiHandPattern(
        pairs=frozenset(pairs),
        remaining=tuple(remaining)
    )

    if len(tiles) == 13:
        shanten_info = ShantenWithoutGot(
            shanten=shanten,
            advance=advance
        )
    else:
        discard_to_advance = {}
        for t in remaining:
            advance_after_discard = advance.copy()
            if t in advance_after_discard:
                advance_after_discard.remove(t)

            discard_to_advance[t] = ShantenWithoutGot(
                shanten=shanten,
                advance=advance_after_discard,
            )

        shanten_info = ShantenWithGot(
            shanten=shanten,
            discard_to_advance=discard_to_advance
        )

    hand = Hand(tiles=tiles, patterns=[pattern])
    result = ShantenResult(type="chitoi", hand=hand, shanten_info=shanten_info)
    return result


# ======== 国士无双 ========
def _handle_kokushi_shanten_without_got(
        yaochu: FrozenSet[Tile],
        repeated: Set[Tile],
        remaining: Tuple[Tile]
) -> Tuple[ShantenWithoutGot, List[KokushiHandPattern]]:
    patterns = []

    if len(repeated) > 0:
        shanten = 12 - len(yaochu)
        advance = all_yaochu - yaochu

        for t in repeated:
            pat = KokushiHandPattern(yaochu=yaochu,
                                     repeated=t,
                                     remaining=(*remaining, *repeated.difference([t])))
            patterns.append(pat)
    else:
        shanten = 13 - len(yaochu)
        advance = all_yaochu.copy()

        pat = KokushiHandPattern(yaochu=yaochu,
                                 repeated=None,
                                 remaining=remaining)
        patterns.append(pat)

    return ShantenWithoutGot(
        shanten=shanten,
        advance=advance,
    ), patterns


def _handle_kokushi_shanten_with_got(
        yaochu: FrozenSet[Tile],
        repeated: Set[Tile],
        remaining: Tuple[Tile]
) -> Tuple[ShantenWithGot, List[KokushiHandPattern]]:
    patterns = []

    if len(repeated) > 0:
        # 非十三面
        shanten = 12 - len(yaochu)
        advance = all_yaochu - yaochu

        discard_to_advance_aggregated = dict()

        for t in repeated:
            pat = KokushiHandPattern(yaochu=yaochu,
                                     repeated=t,
                                     remaining=(*remaining, *repeated.difference([t])))
            patterns.append(pat)

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

            for discard, shanten_info in discard_to_advance.items():
                if discard not in discard_to_advance_aggregated:
                    discard_to_advance_aggregated[discard] = ShantenWithoutGot(
                        shanten=shanten,
                        advance=set(),
                    )
                discard_to_advance_aggregated[discard].advance |= shanten_info.advance
    else:
        # 十三面
        shanten = 13 - len(yaochu)

        discard_to_advance = {}
        for discard in remaining:
            discard_to_advance[discard] = ShantenWithoutGot(
                shanten=shanten,
                advance=all_yaochu
            )

        pat = KokushiHandPattern(yaochu=yaochu,
                                 repeated=None,
                                 remaining=remaining)
        patterns.append(pat)

        discard_to_advance_aggregated = discard_to_advance

    return ShantenWithGot(
        shanten=shanten,
        discard_to_advance=discard_to_advance_aggregated
    ), patterns


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

    yaochu = frozenset(yaochu)
    remaining = tuple(remaining)

    if len(tiles) == 13:
        shanten_info, patterns = _handle_kokushi_shanten_without_got(yaochu, repeated, remaining)
    else:
        shanten_info, patterns = _handle_kokushi_shanten_with_got(yaochu, repeated, remaining)

    hand = Hand(tiles=tiles, patterns=patterns)
    result = ShantenResult(type="kokushi", hand=hand, shanten_info=shanten_info)
    return result


# ======== union ========
def shanten(tiles: List[Tile], furo: Optional[List[Furo]] = None) -> UnionShantenResult:
    ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    with_got = len(tiles) % 3 == 2
    k = len(tiles) // 3

    if k != 4:
        regular = regular_shanten(tiles, furo)
        return UnionShantenResult(type="union", hand=regular.hand, shanten_info=regular.shanten_info,
                                  regular=regular, chitoi=None, kokushi=None)

    regular = regular_shanten(tiles, furo)
    chitoi = chitoi_shanten(tiles)
    kokushi = kokushi_shanten(tiles)

    shanten = min(regular.shanten, chitoi.shanten, kokushi.shanten)
    patterns = []

    if not with_got:
        advance_aggregated = set()

        if regular.shanten == shanten:
            advance_aggregated |= regular.advance
            patterns += regular.hand.patterns
        if chitoi.shanten == shanten:
            advance_aggregated |= chitoi.advance
            patterns += chitoi.hand.patterns
        if kokushi.shanten == shanten:
            advance_aggregated |= kokushi.advance
            patterns += kokushi.hand.patterns

        shanten_info = ShantenWithoutGot(
            shanten=shanten,
            advance=advance_aggregated,
        )
    else:
        discard_to_advance_aggregated = dict()

        for discard, advance in regular.discard_to_advance.items():
            if (discard not in discard_to_advance_aggregated
                    or discard_to_advance_aggregated[discard].shanten > advance.shanten):
                discard_to_advance_aggregated[discard] = advance
            elif discard_to_advance_aggregated[discard].shanten == advance.shanten:
                discard_to_advance_aggregated[discard].advance |= advance.advance
        for discard, advance in chitoi.discard_to_advance.items():
            if (discard not in discard_to_advance_aggregated
                    or discard_to_advance_aggregated[discard].shanten > advance.shanten):
                discard_to_advance_aggregated[discard] = advance
            elif discard_to_advance_aggregated[discard].shanten == advance.shanten:
                discard_to_advance_aggregated[discard].advance |= advance.advance
        for discard, advance in kokushi.discard_to_advance.items():
            if (discard not in discard_to_advance_aggregated
                    or discard_to_advance_aggregated[discard].shanten > advance.shanten):
                discard_to_advance_aggregated[discard] = advance
            elif discard_to_advance_aggregated[discard].shanten == advance.shanten:
                discard_to_advance_aggregated[discard].advance |= advance.advance

        shanten_info = ShantenWithGot(
            shanten=shanten,
            discard_to_advance=discard_to_advance_aggregated
        )

    hand = Hand(tiles=tiles, furo=furo, patterns=patterns)
    return UnionShantenResult(type="union", hand=hand, shanten_info=shanten_info,
                              regular=regular, chitoi=chitoi, kokushi=kokushi)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "shanten",
           "ShantenResult",
           "UnionShantenResult",)
