from typing import Callable, NamedTuple, Optional, Set

from mahjong_utils.models.furo import Kan
from mahjong_utils.models.hora import Hora, StdHora, ChitoiHora, KokushiHora
from mahjong_utils.models.mentsu import Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Toitsu
from mahjong_utils.models.tile import is_yaochu, Tile, tile, is_sangen, is_z, is_wind, tiles
from mahjong_utils.models.tile_type import TileType, tile_type_index_mapping


class Yaku(NamedTuple):
    name: str
    han: int
    furo_loss: int
    checker: Optional[Callable[[Hora], bool]] = None

    def __call__(self, hora: Hora) -> bool:
        if self.checker:
            return self.checker(hora)
        else:
            return False


def yaku(name: str, han: int, furo_loss: int):
    def decorator(func):
        return Yaku(name, han, furo_loss, func)

    return decorator


# ========== extra yaku ==========
richi = Yaku("Richi", 1, 1)
ippatsu = Yaku("Ippatsu", 1, 1)
rinshan = Yaku("Rinshan", 1, 0)
chankan = Yaku("Chankan", 1, 0)
haitei = Yaku("Haitei", 1, 0)
houtei = Yaku("Houtei", 1, 0)
w_richi = Yaku("WRichi", 2, 2)
tenhou = Yaku("Tenhou", 13, 13)
chihou = Yaku("Chihou", 13, 13)

all_extra_yaku: Set[Yaku] = {
    richi,
    ippatsu,
    rinshan,
    chankan,
    haitei,
    houtei,
    w_richi,
    tenhou,
    chihou
}


# ========== factory ==========
def yakuhai_checker_factory(tile: Tile):
    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        for kt in hora.kotsu:
            if kt.tile == tile:
                return True

    return checker


def yaochu_series_checker_factory(shuntsu: bool, z: bool):
    """
    幺九系列checker工厂（混全、混老头、纯全、清老头）
    :param shuntsu: True 必须含顺子，False 必须不含顺子
    :param z: True 必须含字牌，False 必须不含字牌
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        if not is_yaochu(hora.jyantou):
            return False

        shuntsu_cnt = 0
        kotsu_cnt = 0
        has_z = is_z(hora.jyantou)

        for st in hora.shuntsu:
            if st.tile.num != 1 and st.tile.num != 7:
                return False
            shuntsu_cnt += 1

        for kt in hora.kotsu:
            if not is_yaochu(kt.tile):
                return False
            kotsu_cnt += 1
            if is_z(kt.tile):
                has_z = True

        return (shuntsu and shuntsu_cnt != 0 or shuntsu_cnt == 0) and \
               (z and has_z or not has_z)

    return checker


def itsu_series_checker_factory(z: bool):
    """
    一色系列checker工厂（混一色、清一色）
    :param z: True 必须含字牌，False 必须不含字牌
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        cnt = [0] * 4

        for t in hora.tiles:
            cnt[tile_type_index_mapping[t.tile_type]] += 1

        zero_cnt = 0
        for i in range(3):
            if cnt[i] == 0:
                zero_cnt += 1

        return zero_cnt == 2 and (z and cnt[3] != 0 or cnt[3] == 0)

    return checker


def peko_series_checker_factory(peko_count: int):
    """
    杯口系列checker工厂（一杯口、两杯口）
    :param peko_count: x杯口
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora) or not hora.menzen:
            return False

        cnt = 0
        for i in range(len(hora.menzen_mentsu)):
            for j in range(i + 1, len(hora.menzen_mentsu)):
                if isinstance(hora.menzen_mentsu[i], Shuntsu) and hora.menzen_mentsu[i] == hora.menzen_mentsu[j]:
                    cnt += 1
        return cnt == peko_count

    return checker


def anko_series_checker_factory(anko_count: int, tanki: Optional[bool] = None):
    """
    暗刻系列checker工厂（三暗刻、四暗刻、四暗刻单骑）
    :param anko_count: x暗刻
    :param tanki: True 必须单骑，False 必须非单骑，None 无所谓
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        anko = 0
        for mt in hora.menzen_mentsu:
            if isinstance(mt, Kotsu):
                anko += 1

        if isinstance(hora.tatsu, Toitsu) and not hora.tsumo:  # 双碰听牌荣和，算一个明刻
            anko -= 1

        if tanki is None:
            return anko == anko_count
        elif tanki:
            return anko == anko_count and hora.tatsu is None
        else:
            return anko == anko_count and hora.tatsu is not None

    return checker


def kantsu_series_checker_factory(kan_count: int):
    """
    杠子系列checker工厂（三杠子、四杠子）
    :param kan_count: x杠子
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        kan = 0
        for fr in hora.furo:
            if isinstance(fr, Kan):
                kan += 1

        return kan == kan_count

    return checker


def sangen_series_checker_factory(sangen_kotsu_count: int, sangen_jyantou: bool):
    """
    三元系列checker工厂（小三元、大三元）
    :param sangen_kotsu_count: 三元牌刻子数
    :param sangen_jyantou: True 必须含有三元牌雀头，False 必须不含有三元牌雀头
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        sangen_kotsu = 0
        for kt in hora.kotsu:
            if is_sangen(kt.tile):
                sangen_kotsu += 1
        return sangen_kotsu == sangen_kotsu_count and \
               (sangen_jyantou and is_sangen(hora.jyantou) or not is_sangen(hora.jyantou))

    return checker


def sushi_series_checker_factory(wind_kotsu_count: int, wind_jyantou: bool):
    """
    四喜系列checker工厂（小四喜、大四喜）
    :param wind_kotsu_count: 风牌刻子数
    :param wind_jyantou: True 必须含有风牌雀头，False 必须不含有风牌雀头
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        wind_kotsu = 0
        for kt in hora.kotsu:
            if is_wind(kt.tile):
                wind_kotsu += 1
        return wind_kotsu == wind_kotsu_count and \
               (wind_jyantou and is_wind(hora.jyantou) or not is_wind(hora.jyantou))

    return checker


def churen_series_checker_factory(nine_waiting: bool):
    """
    九莲系列checker工厂（九莲、纯九）
    :param nine_waiting: 是否纯九
    :return: checker
    """

    def checker(hora: Hora) -> bool:
        if not isinstance(hora, StdHora):
            return False

        type_found = [False] * 4
        num_cnt = [0] * 9

        for t in hora.tiles:
            type_found[tile_type_index_mapping[t.tile_type]] = True
            num_cnt[t.real_num - 1] += 1

        type_cnt = 0
        for i in range(3):
            if type_found[i]:
                type_cnt += 1

        if type_cnt != 1 or type_found[3]:
            return False

        even = -1
        for i in range(9):
            if num_cnt[i] % 2 == 0:
                if even == -1:
                    even = i
                else:
                    return False

        return nine_waiting and hora.agari.num == even + 1 or hora.agari.num != even + 1

    return checker


# ========== common checker ==========
@yaku("Tsumo", 1, 1)
def tsumo(hora: Hora) -> bool:
    return hora.menzen and hora.tsumo


@yaku("Pinhu", 1, 1)
def pinhu(hora: Hora) -> bool:
    if not isinstance(hora, StdHora):
        return False
    if hora.tsumo:
        return hora.hu == 20
    else:
        return hora.hu == 30


@yaku("Tanyao", 1, 1)
def tanyao(hora: Hora) -> bool:
    for t in hora.tiles:
        if is_yaochu(t):
            return False
    return True


ipe = Yaku("Ipe", 1, 1, peko_series_checker_factory(1))


@yaku("SelfWind", 1, 0)
def self_wind(hora: Hora) -> bool:
    if not isinstance(hora, StdHora) or hora.self_wind is None:
        return False

    for mt in hora.mentsu:
        if mt.tile == hora.self_wind.tile:
            return True


@yaku("RoundWind", 1, 0)
def round_wind(hora: Hora) -> bool:
    if not isinstance(hora, StdHora) or hora.round_wind is None:
        return False

    for mt in hora.mentsu:
        if mt.tile == hora.round_wind.tile:
            return True


haku = Yaku("Haku", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 5)))

hatsu = Yaku("Hatsu", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 6)))

chun = Yaku("Chun", 1, 0, yakuhai_checker_factory(tile(TileType.Z, 7)))


@yaku("Sanshoku", 2, 1)
def sanshoku(hora: Hora) -> bool:
    if not isinstance(hora, StdHora):
        return False

    shuntsu = list(hora.shuntsu)

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


@yaku("Ittsu", 2, 1)
def ittsu(hora: Hora) -> bool:
    if not isinstance(hora, StdHora):
        return False

    shuntsu = list(hora.mentsu)

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


@yaku("Chitoi", 2, 2)
def chitoi(hora: Hora) -> bool:
    return isinstance(hora, ChitoiHora)


@yaku("Toitoi", 2, 0)
def toitoi(hora: Hora) -> bool:
    if not isinstance(hora, StdHora):
        return False

    if len(list(hora.shuntsu)) != 0:
        return False

    return not hora.menzen or (isinstance(hora.tatsu, Toitsu) and not hora.tsumo)  # 非四暗刻的情况


sananko = Yaku("Sananko", 2, 0, anko_series_checker_factory(3))

honroto = Yaku("Honroto", 2, 0, yaochu_series_checker_factory(False, True))


@yaku("Sandoko", 2, 0)
def sandoko(hora: Hora) -> bool:
    if not isinstance(hora, StdHora):
        return False

    kotsu = list(hora.kotsu)

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


# ========== yakuman ==========
@yaku("Kokushi", 13, 13)
def kokushi(hora: Hora) -> bool:
    return isinstance(hora, KokushiHora) and not hora.thirteen_waiting


suanko = Yaku("Suanko", 13, 13, anko_series_checker_factory(4))

daisangen = Yaku("Daisangen", 13, 0, sangen_series_checker_factory(3, False))


@yaku("Tsuiso", 13, 0)
def tsuiso(hora: Hora) -> bool:
    for t in hora.tiles:
        if not is_z(t):
            return False
    return True


shousushi = Yaku("Shousushi", 13, 0, sushi_series_checker_factory(3, True))

lyu = {*tiles("23468s6z")}


@yaku("Lyuiso", 13, 0)
def lyuiso(hora: Hora) -> bool:
    for t in hora.tiles:
        if t not in lyu:
            return False
    return True


chinroto = Yaku("Chinroto", 13, 0, yaochu_series_checker_factory(False, False))

sukantsu = Yaku("Sukantsu", 13, 0, kantsu_series_checker_factory(4))

churen = Yaku("Churen", 13, 13, churen_series_checker_factory(False))

daisushi = Yaku("Daisushi", 26, 0, sushi_series_checker_factory(4, False))

churen9 = Yaku("Churen9", 26, 26, churen_series_checker_factory(True))

suanko_tanki = Yaku("SuankoTanki", 26, 26, anko_series_checker_factory(4, True))


@yaku("Kokushi13", 26, 26)
def kokushi13(hora: Hora) -> bool:
    return isinstance(hora, KokushiHora) and hora.thirteen_waiting


all_yakuman: Set[Yaku] = {
    tenhou, chihou, kokushi, suanko, daisangen, shousushi, lyuiso, chinroto, sukantsu, churen,
    daisushi, churen9, suanko_tanki, kokushi13
}

all_yaku = all_extra_yaku | all_common_yaku | all_yakuman

__all__ = {
    "all_yaku", "all_common_yaku", "all_extra_yaku", "all_yakuman",
    "richi", "ippatsu", "rinshan", "chankan", "haitei", "houtei", "w_richi",
    "tsumo", "pinhu", "tanyao", "ipe", "self_wind", "round_wind", "haku", "hatsu", "chun",
    "sanshoku", "ittsu", "chanta", "chitoi", "toitoi", "sananko", "honroto", "sandoko", "sankantsu", "shosangen",
    "honitsu", "junchan", "ryanpe", "chinitsu",
    "tenhou", "chihou", "kokushi", "suanko", "daisangen", "shousushi", "lyuiso", "chinroto", "sukantsu", "churen",
    "daisushi", "churen9", "suanko_tanki", "kokushi13"
}
