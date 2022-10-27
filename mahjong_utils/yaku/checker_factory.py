from typing import Optional

from mahjong_utils.internal.tile_type_mapping import tile_type_index_mapping
from mahjong_utils.models.furo import Kan
from mahjong_utils.models.hora_hand import HoraHand, RegularHoraHand
from mahjong_utils.models.mentsu import Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Toitsu
from mahjong_utils.models.tile import is_yaochu, Tile, is_sangen, is_z, is_wind


def yakuhai_checker_factory(tile: Tile):
    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        for kt in hora_hand.kotsu:
            if kt.tile == tile:
                return True
        return False

    return checker


def yaochu_series_checker_factory(shuntsu: bool, z: bool):
    """
    幺九系列checker工厂（混全、混老头、纯全、清老头）
    :param shuntsu: True 必须含顺子，False 必须不含顺子
    :param z: True 必须含字牌，False 必须不含字牌
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        if not is_yaochu(hora_hand.jyantou):
            return False

        shuntsu_cnt = 0
        kotsu_cnt = 0
        has_z = is_z(hora_hand.jyantou)

        for st in hora_hand.shuntsu:
            if st.tile.num != 1 and st.tile.num != 7:
                return False
            shuntsu_cnt += 1

        for kt in hora_hand.kotsu:
            if not is_yaochu(kt.tile):
                return False
            kotsu_cnt += 1
            if is_z(kt.tile):
                has_z = True

        return (shuntsu and shuntsu_cnt != 0 or not shuntsu and shuntsu_cnt == 0) and \
               (z and has_z or not z and not has_z)

    return checker


def itsu_series_checker_factory(z: bool):
    """
    一色系列checker工厂（混一色、清一色）
    :param z: True 必须含字牌，False 必须不含字牌
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        cnt = [0] * 4

        for t in hora_hand.tiles:
            cnt[tile_type_index_mapping[t.tile_type]] += 1

        zero_cnt = 0
        for i in range(3):
            if cnt[i] == 0:
                zero_cnt += 1

        return zero_cnt == 2 and (
                z and cnt[3] != 0 or
                not z and cnt[3] == 0
        )

    return checker


def peko_series_checker_factory(peko_count: int):
    """
    杯口系列checker工厂（一杯口、两杯口）
    :param peko_count: x杯口
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand) or not hora_hand.menzen:
            return False

        cnt = 0
        for i in range(len(hora_hand.menzen_mentsu)):
            for j in range(i + 1, len(hora_hand.menzen_mentsu)):
                if isinstance(hora_hand.menzen_mentsu[i], Shuntsu) \
                        and hora_hand.menzen_mentsu[i] == hora_hand.menzen_mentsu[j]:
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

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        anko = 0
        for mt in hora_hand.anko:
            if isinstance(mt, Kotsu):
                anko += 1

        if isinstance(hora_hand.agari_tatsu, Toitsu) and not hora_hand.tsumo:  # 双碰听牌荣和，算一个明刻
            anko -= 1

        if tanki is None:
            return anko == anko_count
        elif tanki:
            return anko == anko_count and hora_hand.agari_tatsu is None
        else:
            return anko == anko_count and hora_hand.agari_tatsu is not None

    return checker


def kantsu_series_checker_factory(kan_count: int):
    """
    杠子系列checker工厂（三杠子、四杠子）
    :param kan_count: x杠子
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        kan = 0
        for fr in hora_hand.furo:
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

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        sangen_kotsu = 0
        for kt in hora_hand.kotsu:
            if is_sangen(kt.tile):
                sangen_kotsu += 1
        return sangen_kotsu == sangen_kotsu_count and (
                sangen_jyantou and is_sangen(hora_hand.jyantou) or
                not sangen_jyantou and not is_sangen(hora_hand.jyantou)
        )

    return checker


def sushi_series_checker_factory(wind_kotsu_count: int, wind_jyantou: bool):
    """
    四喜系列checker工厂（小四喜、大四喜）
    :param wind_kotsu_count: 风牌刻子数
    :param wind_jyantou: True 必须含有风牌雀头，False 必须不含有风牌雀头
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        wind_kotsu = 0
        for kt in hora_hand.kotsu:
            if is_wind(kt.tile):
                wind_kotsu += 1
        return wind_kotsu == wind_kotsu_count and (
                wind_jyantou and is_wind(hora_hand.jyantou) or
                not wind_jyantou and not is_wind(hora_hand.jyantou)
        )

    return checker


def churen_series_checker_factory(nine_waiting: bool):
    """
    九莲系列checker工厂（九莲、纯九）
    :param nine_waiting: 是否纯九
    :return: checker
    """

    def checker(hora_hand: HoraHand) -> bool:
        if not isinstance(hora_hand, RegularHoraHand):
            return False

        type_found = [False] * 4
        num_cnt = [0] * 9

        for t in hora_hand.tiles:
            type_found[tile_type_index_mapping[t.tile_type]] = True
            num_cnt[t.real_num - 1] += 1

        type_cnt = 0
        for i in range(3):
            if type_found[i]:
                type_cnt += 1

        if type_cnt != 1 or type_found[3]:
            return False

        num_cnt[0] -= 2
        num_cnt[8] -= 2

        for i in range(9):
            num_cnt[i] -= 1
            if num_cnt[i] < 0:
                return False

        even = -1
        for i in range(9):
            if num_cnt[i] == 1:
                even = i

        return nine_waiting and hora_hand.agari.num == even + 1 or not nine_waiting and hora_hand.agari.num != even + 1

    return checker
