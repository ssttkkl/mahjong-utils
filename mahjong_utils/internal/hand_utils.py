from typing import Iterable, Set

from mahjong_utils.internal.mentsu_utils import mentsu_after_discard
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.hand_pattern import RegularHandPattern
from mahjong_utils.models.tile import Tile


def calc_regular_shanten(pattern: RegularHandPattern):
    """
    计算向听数

    :param pattern:
    :return:
    """
    shanten = 2 * (pattern.k - len(pattern.menzen_mentsu)) - len(pattern.tatsu)
    if pattern.jyantou is not None:
        shanten -= 1
    return shanten


def calc_regular_advance(pattern: RegularHandPattern) -> Set[Tile]:
    """
    计算进张

    :param pattern:
    :return:
    """
    advance = set()

    # 搭子的进张
    for tt in pattern.tatsu:
        advance |= tt.waiting

    # 浮张的靠张
    if len(pattern.furo) + len(pattern.menzen_mentsu) + len(pattern.tatsu) < pattern.k:
        for t in pattern.remaining:
            advance |= tile_cling[t]

    # 无雀头
    if pattern.jyantou is None:
        for t in pattern.remaining:
            advance.add(t)

    return advance


def regular_pattern_after_discard(pattern: RegularHandPattern, discard: Tile) -> Iterable[RegularHandPattern]:
    """

    :param pattern:
    :param discard:
    :return:
    """
    # 扣掉雀头
    if pattern.jyantou == discard:
        new_hand = pattern.copy(update={
            "jyantou": None,
            "remaining": pattern.remaining + (discard,)
        })
        yield new_hand

    # 扣掉面子
    for i, mt in enumerate(pattern.menzen_mentsu):
        tatsu = mentsu_after_discard(mt, discard)
        if tatsu is not None:
            new_hand = pattern.copy(update={
                "menzen_mentsu": pattern.menzen_mentsu[:i] + pattern.menzen_mentsu[i + 1:],
                "tatsu": pattern.tatsu + (tatsu,)
            })
            yield new_hand

    # 扣掉搭子
    for i, tt in enumerate(pattern.tatsu):
        if discard == tt.first:
            new_hand = pattern.copy(update={
                "tatsu": pattern.tatsu[:i] + pattern.tatsu[i + 1:],
                "remaining": pattern.remaining + (tt.second,)
            })
            yield new_hand
        elif discard == tt.second:
            new_hand = pattern.copy(update={
                "tatsu": pattern.tatsu[:i] + pattern.tatsu[i + 1:],
                "remaining": pattern.remaining + (tt.first,)
            })
            yield new_hand

    # 扣掉浮张
    try:
        idx = pattern.remaining.index(discard)
    except ValueError:
        idx = -1

    if idx != -1:
        new_hand = pattern.copy(update={
            "remaining": pattern.remaining[:idx] + pattern.remaining[idx + 1:]
        })
        yield new_hand
