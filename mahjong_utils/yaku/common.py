from typing import Set

from mahjong_utils.models.hora_hand import HoraHand, StdHoraHand, ChitoiHoraHand
from mahjong_utils.models.tatsu import Toitsu
from mahjong_utils.models.tile import tile, is_yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.yaku import _yaku, Yaku
from mahjong_utils.yaku.checker_factory import kantsu_series_checker_factory, yakuhai_checker_factory, \
    peko_series_checker_factory, anko_series_checker_factory, yaochu_series_checker_factory, \
    sangen_series_checker_factory, itsu_series_checker_factory


@_yaku("Tsumo", 1, 1)
def tsumo(hora_hand: HoraHand) -> bool:
    return hora_hand.menzen and hora_hand.tsumo


@_yaku("Pinhu", 1, 1)
def pinhu(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand):
        return False
    if hora_hand.tsumo:
        return hora_hand.hu == 20
    else:
        return hora_hand.hu == 30


@_yaku("Tanyao", 1, 1)
def tanyao(hora_hand: HoraHand) -> bool:
    for t in hora_hand.tiles:
        if is_yaochu(t):
            return False
    return True


ipe = Yaku("Ipe", 1, 1, peko_series_checker_factory(1))


@_yaku("SelfWind", 1, 0)
def self_wind(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand) or hora_hand.self_wind is None:
        return False

    for mt in hora_hand.mentsu:
        if mt.tile == hora_hand.self_wind.tile:
            return True


@_yaku("RoundWind", 1, 0)
def round_wind(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand) or hora_hand.round_wind is None:
        return False

    for mt in hora_hand.mentsu:
        if mt.tile == hora_hand.round_wind.tile:
            return True


haku = Yaku("Haku", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 5)))

hatsu = Yaku("Hatsu", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 6)))

chun = Yaku("Chun", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 7)))


@_yaku("Sanshoku", 2, 1)
def sanshoku(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand):
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


@_yaku("Ittsu", 2, 1)
def ittsu(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand):
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


chanta = Yaku("Chanta", 2, 1, yaochu_series_checker_factory(True, True))


@_yaku("Chitoi", 2, 2)
def chitoi(hora_hand: HoraHand) -> bool:
    return isinstance(hora_hand, ChitoiHoraHand)


@_yaku("Toitoi", 2, 0)
def toitoi(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand):
        return False

    if len(list(hora_hand.shuntsu)) != 0:
        return False

    return not hora_hand.menzen or (isinstance(hora_hand.tatsu, Toitsu) and not hora_hand.tsumo)  # 非四暗刻的情况


sananko = Yaku("Sananko", 2, 0, anko_series_checker_factory(3))

honroto = Yaku("Honroto", 2, 0, yaochu_series_checker_factory(False, True))


@_yaku("Sandoko", 2, 0)
def sandoko(hora_hand: HoraHand) -> bool:
    if not isinstance(hora_hand, StdHoraHand):
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


sankantsu = Yaku("Sankantsu", 2, 0, kantsu_series_checker_factory(3))

shosangen = Yaku("Shosangen", 2, 0, sangen_series_checker_factory(2, True))

honitsu = Yaku("Honitsu", 3, 1, itsu_series_checker_factory(True))

junchan = Yaku("Junchan", 3, 1, yaochu_series_checker_factory(True, False))

ryanpe = Yaku("Ryanpe", 3, 3, peko_series_checker_factory(2))

chinitsu = Yaku("Chinitsu", 6, 1, itsu_series_checker_factory(False))

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
