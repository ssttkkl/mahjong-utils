from typing import Set

from mahjong_utils.models.hora_hand import HoraHand, RegularHoraHand, ChitoiHoraHand
from mahjong_utils.models.tatsu import Toitsu
from mahjong_utils.models.tile import tile, is_yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.yaku import _yaku, Yaku
from mahjong_utils.yaku.checker_factory import kantsu_series_checker_factory, yakuhai_checker_factory, \
    peko_series_checker_factory, anko_series_checker_factory, yaochu_series_checker_factory, \
    sangen_series_checker_factory, itsu_series_checker_factory


@_yaku(1, 1)
def tsumo(hora_hand: HoraHand) -> bool:
    return hora_hand.menzen and hora_hand.tsumo


@_yaku(1, 1)
def pinhu(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand):
        return False
    if hora_hand.tsumo:
        return hora_hand.hu == 20
    else:
        return hora_hand.hu == 30


@_yaku(1, 0)
def tanyao(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if is_yaochu(t):
            return False
    return True


ipe = Yaku("ipe", 1, 1, checker=peko_series_checker_factory(1))


@_yaku(1, 0)
def self_wind(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand) or hora_hand.self_wind is None:
        return False

    for mt in hora_hand.mentsu:
        if mt.tile == hora_hand.self_wind.tile:
            return True
    return False


@_yaku(1, 0)
def round_wind(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand) or hora_hand.round_wind is None:
        return False

    for mt in hora_hand.mentsu:
        if mt.tile == hora_hand.round_wind.tile:
            return True
    return False


haku = Yaku("haku", 1, 0, checker=yakuhai_checker_factory(tile(TileType.Z, 5)))

hatsu = Yaku("hatsu", 1, 0, checker=yakuhai_checker_factory(tile(TileType.Z, 6)))

chun = Yaku("chun", 1, 0, checker=yakuhai_checker_factory(tile(TileType.Z, 7)))


@_yaku(2, 1)
def sanshoku(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand):
        return False

    shuntsu = list(hora_hand.shuntsu)

    for i in range(len(shuntsu)):
        for j in range(i + 1, len(shuntsu)):
            for k in range(j + 1, len(shuntsu)):
                x = shuntsu[i]
                y = shuntsu[j]
                z = shuntsu[k]

                if x.tile.tile_type != y.tile.tile_type \
                        and y.tile.tile_type != z.tile.tile_type \
                        and z.tile.tile_type != x.tile.tile_type \
                        and x.tile.real_num == y.tile.real_num == z.tile.real_num:
                    return True

    return False


@_yaku(2, 1)
def ittsu(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand):
        return False

    shuntsu = list(hora_hand.mentsu)

    for i in range(len(shuntsu)):
        for j in range(i + 1, len(shuntsu)):
            for k in range(j + 1, len(shuntsu)):
                x = shuntsu[i]
                y = shuntsu[j]
                z = shuntsu[k]

                if x.tile.tile_type == y.tile.tile_type == z.tile.tile_type:
                    nums = {x.tile.real_num, y.tile.real_num, z.tile.real_num}
                    if nums == {1, 4, 7}:
                        return True

    return False


chanta = Yaku("chanta", 2, 1, checker=yaochu_series_checker_factory(True, True))


@_yaku(2, 2)
def chitoi(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, ChitoiHoraHand)


@_yaku(2, 0)
def toitoi(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand):
        return False

    if len(list(hora_hand.shuntsu)) != 0:
        return False

    return not hora_hand.menzen or (isinstance(hora_hand.agari_tatsu, Toitsu) and not hora_hand.tsumo)  # 非四暗刻的情况


sananko = Yaku("sananko", 2, 0, checker=anko_series_checker_factory(3))

honroto = Yaku("honroto", 2, 0, checker=yaochu_series_checker_factory(False, True))


@_yaku(2, 0)
def sandoko(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, RegularHoraHand):
        return False

    kotsu = list(hora_hand.kotsu)

    for i in range(len(kotsu)):
        for j in range(i + 1, len(kotsu)):
            for k in range(j + 1, len(kotsu)):
                x = kotsu[i]
                y = kotsu[j]
                z = kotsu[k]

                if x.tile.tile_type != y.tile.tile_type \
                        and y.tile.tile_type != z.tile.tile_type \
                        and z.tile.tile_type != x.tile.tile_type \
                        and x.tile.real_num == y.tile.real_num == z.tile.real_num:
                    return True

    return False


sankantsu = Yaku("sankantsu", 2, 0, checker=kantsu_series_checker_factory(3))

shosangen = Yaku("shosangen", 2, 0, checker=sangen_series_checker_factory(2, True))

honitsu = Yaku("honitsu", 3, 1, checker=itsu_series_checker_factory(True))

junchan = Yaku("junchan", 3, 1, checker=yaochu_series_checker_factory(True, False))

ryanpe = Yaku("ryanpe", 3, 3, checker=peko_series_checker_factory(2))

chinitsu = Yaku("chinitsu", 6, 1, checker=itsu_series_checker_factory(False))

all_common_yaku: Set[Yaku] = {
    tsumo, pinhu, tanyao, ipe, self_wind, round_wind, haku, hatsu, chun,
    sanshoku, ittsu, chanta, chitoi, toitoi, sananko, honroto, sandoko, sankantsu, shosangen,
    honitsu, junchan, ryanpe, chinitsu
}

__all__ = (
    "all_common_yaku",
    "tsumo", "pinhu", "tanyao", "ipe", "self_wind", "round_wind", "haku", "hatsu", "chun",
    "sanshoku", "ittsu", "chanta", "chitoi", "toitoi", "sananko", "honroto", "sandoko", "sankantsu", "shosangen",
    "honitsu", "junchan", "ryanpe", "chinitsu"
)
