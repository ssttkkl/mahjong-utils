from typing import Set

from mahjong_utils.models.hora_hand import HoraHand, KokushiHoraHand
from mahjong_utils.models.tile import is_z, parse_tiles
from mahjong_utils.yaku import _yaku, Yaku
from mahjong_utils.yaku.checker_factory import kantsu_series_checker_factory, anko_series_checker_factory, \
    yaochu_series_checker_factory, \
    sangen_series_checker_factory, sushi_series_checker_factory, churen_series_checker_factory
from mahjong_utils.yaku.extra import tenhou, chihou


@_yaku(13, 13, True)
def kokushi(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, KokushiHoraHand) and not hora_hand.thirteen_waiting


suanko = Yaku("suanko", 13, 13, True, anko_series_checker_factory(4, False))

daisangen = Yaku("daisangen", 13, 0, True, sangen_series_checker_factory(3, False))


@_yaku(13, 0, True)
def tsuiso(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if not is_z(t):
            return False
    return True


shousushi = Yaku("shousushi", 13, 0, True, sushi_series_checker_factory(3, True))

lyu = {*parse_tiles("23468s6z")}


@_yaku(13, 0, True)
def lyuiso(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if t not in lyu:
            return False
    return True


chinroto = Yaku("chinroto", 13, 0, True, yaochu_series_checker_factory(False, False))

sukantsu = Yaku("sukantsu", 13, 0, True, kantsu_series_checker_factory(4))

churen = Yaku("churen", 13, 13, True, churen_series_checker_factory(False))

daisushi = Yaku("daisushi", 26, 0, True, sushi_series_checker_factory(4, False))

churen9 = Yaku("churen9", 26, 26, True, churen_series_checker_factory(True))

suanko_tanki = Yaku("suanko_tanki", 26, 26, True, anko_series_checker_factory(4, True))


@_yaku(26, 26, True)
def kokushi13(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, KokushiHoraHand) and hora_hand.thirteen_waiting


all_yakuman: Set[Yaku] = {
    tenhou, chihou, kokushi, suanko, daisangen, tsuiso, shousushi, lyuiso, chinroto, sukantsu, churen,
    daisushi, churen9, suanko_tanki, kokushi13
}

__all__ = (
    "all_yakuman",
    "tenhou", "chihou", "kokushi", "suanko", "daisangen", "tsuiso", "shousushi", "lyuiso", "chinroto", "sukantsu", "churen",
    "daisushi", "churen9", "suanko_tanki", "kokushi13"
)
