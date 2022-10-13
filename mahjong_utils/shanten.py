from dataclasses import dataclass
from typing import List, Tuple, Set, Dict, Union

from mahjong_utils.internal.hand_searcher import StdHandSearcher
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.hand import StdHand
from mahjong_utils.models.tile import Tile, is_yaochu, yaochu, parse_tiles


@dataclass(frozen=True)
class ShantenResult:
    shanten: int
    advance: Set[Tile]


@dataclass(frozen=True)
class StdShantenResult(ShantenResult):
    possible_hand: List[StdHand]


@dataclass(frozen=True)
class UnionShantenResult(ShantenResult):
    std: StdShantenResult
    chitoi: ShantenResult
    kokushi: ShantenResult


@dataclass(frozen=True)
class ShantenWithGotTileResult:
    shanten: int
    discard_to_advance: Dict[Tile, Set[Tile]]


@dataclass(frozen=True)
class StdShantenWithGotTileResult(ShantenWithGotTileResult):
    possible_hands: List[StdHand]


@dataclass(frozen=True)
class UnionShantenWithGotTileResult(ShantenWithGotTileResult):
    std: StdShantenWithGotTileResult
    chitoi: ShantenWithGotTileResult
    kokushi: ShantenWithGotTileResult


# ======== 标准形 ========
def _std_search(k: int, hand: List[Tile]) -> Tuple[int, List[StdHand]]:
    shanten = 10000
    hands = []

    def callback(hand: StdHand):
        nonlocal shanten, hands

        pending_shanten = 2 * (k - len(hand.mentsu)) - len(hand.tatsu)
        if hand.jyantou is not None:
            pending_shanten -= 1

        if pending_shanten < shanten:
            shanten = pending_shanten
            hands = [hand]
        elif pending_shanten == shanten:
            hands.append(hand)

    searcher = StdHandSearcher(k, hand, callback)
    searcher.run()

    return shanten, hands


def _get_std_advance(k: int, result: StdHand) -> Set[Tile]:
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


def std_shanten(tiles: Union[List[Tile], str]) -> StdShantenResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    if len(tiles) < 1 or len(tiles) > 13 or len(tiles) % 3 != 1:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    k = (len(tiles) - 1) // 3
    shanten, hands = _std_search(k, tiles)

    advance = set()
    for result in hands:
        advance |= _get_std_advance(k, result)

    return StdShantenResult(shanten, advance, hands)


def std_shanten_with_got_tile(tiles: Union[List[Tile], str]) -> StdShantenWithGotTileResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    if len(tiles) < 2 or len(tiles) > 14 or len(tiles) % 3 != 2:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    k = (len(tiles) - 2) // 3
    shanten, hands = _std_search(k, tiles)

    discard_to_advance: Dict[Tile, Set[Tile]] = {}

    for result in hands:
        for i, discard in enumerate(result.remaining):
            advance = _get_std_advance(k, StdHand(result.jyantou, result.mentsu, result.tatsu,
                                                  result.remaining[0:i] + result.remaining[i + 1:]))

            if discard not in discard_to_advance:
                discard_to_advance[discard] = advance
            else:
                discard_to_advance[discard] |= advance

    return StdShantenWithGotTileResult(shanten, discard_to_advance, hands)


# ======== 七对子 ========
def chitoi_shanten(tiles: Union[List[Tile], str]) -> ShantenResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    if len(tiles) != 13:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        cnt[t] = cnt.get(t, 0) + 1

    tot_pair = 0
    advance = set()
    for t in cnt:
        if cnt[t] >= 2:
            tot_pair += 1
        elif cnt[t] == 1:
            advance.add(t)

    return ShantenResult(6 - tot_pair, advance)


def chitoi_shanten_with_got_tile(tiles: Union[List[Tile], str]) -> ShantenWithGotTileResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    if len(tiles) != 14:
        raise ValueError(f"invalid length of hand: {len(tiles)}")

    cnt = {}
    for t in tiles:
        cnt[t] = cnt.get(t, 0) + 1

    tot_pair = 0
    advance = set()
    for t in cnt:
        if cnt[t] >= 2:
            tot_pair += 1
        elif cnt[t] == 1:
            advance.add(t)

    discard_to_advance = {}
    for t in advance:
        advance_after_discard = advance.copy()
        advance_after_discard.remove(t)
        discard_to_advance[t] = advance_after_discard

    return ShantenWithGotTileResult(6 - tot_pair, discard_to_advance)


# ======== 国士无双 ========
def kokushi_shanten(hand: Union[List[Tile], str]) -> ShantenResult:
    if isinstance(hand, str):
        hand = parse_tiles(hand)

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
        return ShantenResult(12 - len(cnt), advance)
    else:
        return ShantenResult(13 - len(cnt), yaochu.copy())


def kokushi_shanten_with_got_tile(tiles: Union[List[Tile], str]) -> ShantenWithGotTileResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

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
        pending = kokushi_shanten(tiles)

        if pending.shanten < shanten:
            shanten = pending.shanten
            discard_to_advance = {t: pending.advance}
        elif pending.shanten == shanten:
            discard_to_advance[t] = pending.advance
        tiles.append(t)

    return ShantenWithGotTileResult(shanten, discard_to_advance)


# ======== union ========
def shanten(tiles: Union[List[Tile], str]) -> UnionShantenResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    std = std_shanten(tiles)
    chitoi = chitoi_shanten(tiles)
    kokushi = kokushi_shanten(tiles)

    shanten = min(std.shanten, chitoi.shanten, kokushi.shanten)
    advance = set()

    if std.shanten == shanten:
        advance |= std.advance
    if chitoi.shanten == shanten:
        advance |= chitoi.advance
    if kokushi.shanten == shanten:
        advance |= kokushi.advance

    return UnionShantenResult(shanten, advance, std, chitoi, kokushi)


def shanten_with_got_tile(tiles: Union[List[Tile], str]) -> UnionShantenWithGotTileResult:
    if isinstance(tiles, str):
        tiles = parse_tiles(tiles)

    std = std_shanten_with_got_tile(tiles)
    chitoi = chitoi_shanten_with_got_tile(tiles)
    kokushi = kokushi_shanten_with_got_tile(tiles)

    shanten = min(std.shanten, chitoi.shanten, kokushi.shanten)
    mapping = dict()

    if std.shanten == shanten:
        for discard, advance in std.discard_to_advance.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance
    if chitoi.shanten == shanten:
        for discard, advance in chitoi.discard_to_advance.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance
    if kokushi.shanten == shanten:
        for discard, advance in kokushi.discard_to_advance.items():
            if discard not in mapping:
                mapping[discard] = set()
            mapping[discard] |= advance

    return UnionShantenWithGotTileResult(shanten, mapping, std, chitoi, kokushi)


__all__ = ("std_shanten", "std_shanten_with_got_tile",
           "chitoi_shanten", "chitoi_shanten_with_got_tile",
           "kokushi_shanten", "kokushi_shanten_with_got_tile",
           "shanten", "shanten_with_got_tile",
           "ShantenResult", "ShantenWithGotTileResult",
           "StdShantenResult", "StdShantenWithGotTileResult",
           "UnionShantenResult", "UnionShantenWithGotTileResult")
