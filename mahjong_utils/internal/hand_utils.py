from typing import Iterable, Set

from mahjong_utils.internal.mentsu_utils import mentsu_try_exclude_got
from mahjong_utils.internal.tile_cling import tile_cling
from mahjong_utils.models.hand import RegularHand
from mahjong_utils.models.tile import Tile


def calc_regular_shanten(hand: RegularHand):
    """
    计算向听数

    :param hand:
    :return:
    """
    shanten = 2 * (hand.k - len(hand.menzen_mentsu)) - len(hand.tatsu)
    if hand.jyantou is not None:
        shanten -= 1
    return shanten


def calc_regular_advance(hand: RegularHand) -> Set[Tile]:
    """
    计算进张

    :param hand:
    :return:
    """
    advance = set()

    # 搭子的进张
    for tt in hand.tatsu:
        advance |= tt.waiting

    # 浮张的靠张
    if len(hand.furo) + len(hand.menzen_mentsu) + len(hand.tatsu) < hand.k:
        for t in hand.remaining:
            advance |= tile_cling[t]

    # 无雀头
    if hand.jyantou is None:
        for t in hand.remaining:
            advance.add(t)

    return advance


def hand_exclude_got_regular(hand: RegularHand, got: Tile) -> Iterable[RegularHand]:
    """
    hand为已摸牌状态的手牌，获取所有可能的未摸牌状态的手牌
    :param hand:
    :param got:
    :return:
    """
    assert hand.with_got

    # 扣掉雀头
    if hand.jyantou == got:
        new_hand = hand.copy(update={
            "with_got": False,
            "jyantou": None,
            "remaining": hand.remaining + [got]
        })
        yield new_hand

    # 扣掉面子
    for i, mt in enumerate(hand.menzen_mentsu):
        tatsu = mentsu_try_exclude_got(mt, got)
        if tatsu is not None:
            new_hand = hand.copy(update={
                "with_got": False,
                "menzen_mentsu": hand.menzen_mentsu[:i] + hand.menzen_mentsu[i + 1:],
                "tatsu": hand.tatsu + [tatsu]
            })
            yield new_hand

    # 扣掉搭子
    for i, tt in enumerate(hand.tatsu):
        if got == tt.first:
            new_hand = hand.copy(update={
                "with_got": False,
                "tatsu": hand.tatsu[:i] + hand.tatsu[i + 1:],
                "remaining": hand.remaining + [tt.second]
            })
            yield new_hand
        elif got == tt.second:
            new_hand = hand.copy(update={
                "with_got": False,
                "tatsu": hand.tatsu[:i] + hand.tatsu[i + 1:],
                "remaining": hand.remaining + [tt.first]
            })
            yield new_hand

    # 扣掉浮张
    try:
        idx = hand.remaining.index(got)
    except ValueError:
        idx = -1

    if idx != -1:
        new_hand = hand.copy(update={
            "with_got": False,
            "remaining": hand.remaining[:idx] + hand.remaining[idx + 1:]
        })
        yield new_hand
