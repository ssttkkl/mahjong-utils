from typing import Set

from mahjong_utils.models.hora_hand import HoraHand, KokushiHoraHand
from mahjong_utils.models.tile import is_z, parse_tiles
from mahjong_utils.yaku import _yaku, Yaku
from mahjong_utils.yaku.checker_factory import kantsu_series_checker_factory, anko_series_checker_factory, \
    yaochu_series_checker_factory, \
    sangen_series_checker_factory, sushi_series_checker_factory, churen_series_checker_factory
from mahjong_utils.yaku.extra import tenhou, chihou


@_yaku(13, 13)
def kokushi(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, KokushiHoraHand) and not hora_hand.thirteen_waiting


suanko = Yaku(13, 13, anko_series_checker_factory(4))

daisangen = Yaku(13, 0, sangen_series_checker_factory(3, False))


@_yaku(13, 0)
def tsuiso(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if not is_z(t):
            return False
    return True


shousushi = Yaku(13, 0, sushi_series_checker_factory(3, True))

lyu = {*parse_tiles("23468s6z")}


@_yaku(13, 0)
def lyuiso(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if t not in lyu:
            return False
    return True


chinroto = Yaku(13, 0, yaochu_series_checker_factory(False, False))

sukantsu = Yaku(13, 0, kantsu_series_checker_factory(4))

churen = Yaku(13, 13, churen_series_checker_factory(False))

daisushi = Yaku(26, 0, sushi_series_checker_factory(4, False))

churen9 = Yaku(26, 26, churen_series_checker_factory(True))

suanko_tanki = Yaku(26, 26, anko_series_checker_factory(4, True))


@_yaku(26, 26)
def kokushi13(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, KokushiHoraHand) and hora_hand.thirteen_waiting


all_yakuman: Set[Yaku] = {
    tenhou, chihou, kokushi, suanko, daisangen, shousushi, lyuiso, chinroto, sukantsu, churen,
    daisushi, churen9, suanko_tanki, kokushi13
}

__all__ = (
    "all_yakuman",
    "tenhou", "chihou", "kokushi", "suanko", "daisangen", "shousushi", "lyuiso", "chinroto", "sukantsu", "churen",
    "daisushi", "churen9", "suanko_tanki", "kokushi13"
)
