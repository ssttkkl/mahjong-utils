from collections import defaultdict
from itertools import chain
from typing import List, Dict, Literal, Optional, Sequence, Set, Tuple, FrozenSet, Iterable

from pydantic import BaseModel

from mahjong_utils.internal.hand_utils import calc_regular_advance, regular_pattern_after_discard, calc_regular_shanten
from mahjong_utils.internal.legal_tiles_checker import ensure_legal_tiles
from mahjong_utils.internal.regular_hand_pattern_searcher import regular_hand_pattern_search
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import Hand
from mahjong_utils.models.hand_pattern import ChitoiHandPattern, KokushiHandPattern, RegularHandPattern
from mahjong_utils.models.shanten import ShantenWithGot, ShantenWithoutGot, Shanten, ShantenInfoMixin
from mahjong_utils.models.tile import Tile, is_yaochu, all_yaochu


class ShantenResult(BaseModel, ShantenInfoMixin):
    type: Literal['regular', 'chitoi', 'kokushi', 'union']
    hand: Hand
    shanten_info: Shanten


class UnionShantenResult(ShantenResult):
    regular: ShantenResult
    chitoi: Optional[ShantenResult]
    kokushi: Optional[ShantenResult]


def _fill_advance_num(
        shanten_info: Shanten,
        tiles: Iterable[Tile],
        tiles_furo: Optional[Iterable[Tile]] = None
):
    remaining = defaultdict(lambda: 4)
    for t in tiles:
        remaining[t] -= 1
    if tiles_furo is not None:
        for t in tiles_furo:
            remaining[t] -= 1

    if isinstance(shanten_info, ShantenWithoutGot):
        advance_num = 0
        for t in shanten_info.advance:
            advance_num += remaining[t]
        shanten_info.advance_num = advance_num
    elif isinstance(shanten_info, ShantenWithGot):
        for shanten_info_after_discard in shanten_info.discard_to_advance.values():
            advance_num = 0
            for t in shanten_info_after_discard.advance:
                advance_num += remaining[t]
            shanten_info_after_discard.advance_num = advance_num
            # _fill_advance_num(shanten_info_after_discard, tiles, tiles_furo)


def _select_best_patterns(
        patterns: Iterable[RegularHandPattern]
) -> Tuple[int, Sequence[RegularHandPattern]]:
    best_shanten = 100
    best_patterns = []

    for pat in patterns:
        pat_shanten = calc_regular_shanten(pat)
        if pat_shanten < best_shanten:
            best_shanten = pat_shanten
            best_patterns = []
        if pat_shanten == best_shanten:
            best_patterns.append(pat)

    return best_shanten, best_patterns


# ======== 标准形 ========
def _handle_regular_shanten_without_got(
        patterns: Iterable[RegularHandPattern]
) -> Tuple[ShantenWithoutGot, Sequence[RegularHandPattern]]:
    best_shanten, best_patterns = _select_best_patterns(patterns)

    advance = set()
    for pat in best_patterns:
        advance |= calc_regular_advance(pat)

    # if shanten_num == 1:
    #     # 一向听的牌计算好型率
    #     for adv in shanten_info.advance:
    #         patterns_after_advance = set()
    #         for pat in patterns:
    #             for pat_after_discard in regular_pattern_after_advance(pat, adv):
    #                 patterns_after_advance.add(pat_after_discard)
    #
    #         shanten_after_advance = _handle_regular_shanten_with_got(0, patterns_after_advance, tuple(tiles) + (adv,))
    #
    #         sorted(shanten_after_advance.discard_to_advance.values(), key=

    shanten_info = ShantenWithoutGot(shanten=best_shanten, advance=advance)
    return shanten_info, best_patterns


def _handle_regular_shanten_with_got(
        patterns: Iterable[RegularHandPattern],
        tiles: Iterable[Tile]
) -> Tuple[ShantenWithGot, Sequence[RegularHandPattern]]:
    best_shanten, best_patterns = _select_best_patterns(patterns)

    discard_to_advance = dict()
    for discard in set(tiles):
        patterns_after_discard = set()
        for pat in best_patterns:
            for pat_after_discard in regular_pattern_after_discard(pat, discard):
                patterns_after_discard.add(pat_after_discard)

        shanten_info_after_discard, _ = _handle_regular_shanten_without_got(patterns_after_discard)
        discard_to_advance[discard] = shanten_info_after_discard

    # TODO：一向听的牌分析好型率

    shanten_info = ShantenWithGot(shanten=best_shanten, discard_to_advance=discard_to_advance)
    return shanten_info, best_patterns


def regular_shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        calc_advance_num: bool = True
) -> ShantenResult:
    tiles = ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    patterns = regular_hand_pattern_search(tiles, furo)

    with_got = len(tiles) % 3 == 2
    if with_got:
        shanten_info, best_patterns = _handle_regular_shanten_with_got(patterns, tiles)
    else:
        shanten_info, best_patterns = _handle_regular_shanten_without_got(patterns)

    if calc_advance_num:
        _fill_advance_num(shanten_info, tiles, chain(map(lambda fr: fr.tiles, furo)))

    hand = Hand(tiles=tiles, furo=furo, patterns=best_patterns)
    result = ShantenResult(type="regular", hand=hand, shanten_info=shanten_info)
    return result


# ======== 七对子 ========
def chitoi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True
) -> ShantenResult:
    tiles = ensure_legal_tiles(tiles, False)

    cnt: Dict[Tile, int] = {}
    for t in tiles:
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
                advance=advance_after_discard
            )

        shanten_info = ShantenWithGot(
            shanten=shanten,
            discard_to_advance=discard_to_advance
        )

    if calc_advance_num:
        _fill_advance_num(shanten_info, tiles)

    hand = Hand(tiles=tiles, patterns=[pattern])
    result = ShantenResult(type="chitoi", hand=hand, shanten_info=shanten_info)
    return result


# ======== 国士无双 ========
def _handle_kokushi_shanten_without_got(
        yaochu: FrozenSet[Tile],
        repeated: Set[Tile],
        remaining: Tuple[Tile]
) -> Tuple[ShantenWithoutGot, Sequence[KokushiHandPattern]]:
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
        advance=advance
    ), patterns


def _handle_kokushi_shanten_with_got(
        yaochu: FrozenSet[Tile],
        repeated: Set[Tile],
        remaining: Tuple[Tile],
) -> Tuple[ShantenWithGot, Sequence[KokushiHandPattern]]:
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
                advance=all_yaochu,
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


def kokushi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True
) -> ShantenResult:
    tiles = ensure_legal_tiles(tiles, False)

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

    if calc_advance_num:
        _fill_advance_num(shanten_info, tiles)

    hand = Hand(tiles=tiles, patterns=patterns)
    result = ShantenResult(type="kokushi", hand=hand, shanten_info=shanten_info)
    return result


# ======== union ========
def shanten(
        tiles: List[Tile],
        furo: Optional[List[Furo]] = None,
        calc_advance_num: bool = True
) -> UnionShantenResult:
    tiles = ensure_legal_tiles(tiles)

    if furo is None:
        furo = []

    with_got = len(tiles) % 3 == 2
    k = len(tiles) // 3

    if k != 4:
        regular = regular_shanten(tiles, furo, calc_advance_num)
        return UnionShantenResult(type="union", hand=regular.hand, shanten_info=regular.shanten_info,
                                  regular=regular, chitoi=None, kokushi=None)

    regular = regular_shanten(tiles, furo, calc_advance_num=False)
    chitoi = chitoi_shanten(tiles, calc_advance_num=False)
    kokushi = kokushi_shanten(tiles, calc_advance_num=False)

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
        patterns += regular.hand.patterns

        for discard, advance in chitoi.discard_to_advance.items():
            if (discard not in discard_to_advance_aggregated
                    or discard_to_advance_aggregated[discard].shanten > advance.shanten):
                discard_to_advance_aggregated[discard] = advance
            elif discard_to_advance_aggregated[discard].shanten == advance.shanten:
                discard_to_advance_aggregated[discard].advance |= advance.advance
        patterns += chitoi.hand.patterns

        for discard, advance in kokushi.discard_to_advance.items():
            if (discard not in discard_to_advance_aggregated
                    or discard_to_advance_aggregated[discard].shanten > advance.shanten):
                discard_to_advance_aggregated[discard] = advance
            elif discard_to_advance_aggregated[discard].shanten == advance.shanten:
                discard_to_advance_aggregated[discard].advance |= advance.advance
        patterns += kokushi.hand.patterns

        shanten_info = ShantenWithGot(
            shanten=shanten,
            discard_to_advance=discard_to_advance_aggregated
        )

    if calc_advance_num:
        _fill_advance_num(shanten_info, tiles, chain(map(lambda fr: fr.tiles, furo)))

    hand = Hand(tiles=tiles, furo=furo, patterns=patterns)
    return UnionShantenResult(type="union", hand=hand, shanten_info=shanten_info,
                              regular=regular, chitoi=chitoi, kokushi=kokushi)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "shanten",
           "ShantenResult",
           "UnionShantenResult",)
