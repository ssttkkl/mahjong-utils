from typing import Optional, Set, List

from stringcase import pascalcase

from mahjong_utils.bridge import bridge_mahjongutils
from mahjong_utils.hora.models import Hora, HoraOptions
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.tile import Tile
from mahjong_utils.models.wind import Wind
from mahjong_utils.shanten import ShantenResult
from mahjong_utils.yaku import Yaku


def build_hora(
        tiles: List[Tile], furo: Optional[List[Furo]], agari: Tile,
        tsumo: bool,
        *, dora: int = 0,
        self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
        extra_yaku: Optional[Set[Yaku]] = None,
        options: Optional[HoraOptions] = None
) -> Hora:
    """
    和牌分析

    :param tiles: 手牌
    :param furo: 副露
    :param agari: 和牌
    :param tsumo: 是否自摸
    :param dora: 宝牌数
    :param self_wind: 自风
    :param round_wind: 场风
    :param extra_yaku: 额外役
    :return: 和牌分析结果
    """
    args = {
        "tiles": [str(t) for t in tiles],
        "agari": str(agari),
        "tsumo": tsumo,
        "dora": dora,
    }
    if furo is not None:
        args["furo"] = [fr.__encode__() for fr in furo]
    if self_wind is not None:
        args["selfWind"] = pascalcase(self_wind.name)
    if round_wind is not None:
        args["roundWind"] = pascalcase(round_wind.name)
    if furo is not None:
        args["furo"] = [fr.__encode__() for fr in furo]
    if extra_yaku is not None:
        args["extraYaku"] = [pascalcase(yk.name) for yk in extra_yaku]
    if options is not None:
        args["options"] = options.__encode__()
    result = bridge_mahjongutils.call("hora", args)

    return Hora.__decode__(result)


def build_hora_from_shanten_result(
        shanten_result: ShantenResult,
        agari: Tile,
        tsumo: bool,
        *, dora: int = 0,
        self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
        extra_yaku: Optional[Set[Yaku]] = None,
        options: Optional[HoraOptions] = None
) -> Hora:
    """
    和牌分析（根据向听分析结果）

    :param shanten_result: 向听分析结果
    :param agari: 和牌
    :param tsumo: 是否自摸
    :param dora: 宝牌数
    :param self_wind: 自风
    :param round_wind: 场风
    :param extra_yaku: 额外役
    :return: 和牌分析结果
    """
    args = {
        "shantenResult": shanten_result.__encode__(),
        "agari": str(agari),
        "tsumo": tsumo,
        "dora": dora,
    }
    if self_wind is not None:
        args["selfWind"] = pascalcase(self_wind.name)
    if round_wind is not None:
        args["roundWind"] = pascalcase(round_wind.name)
    if extra_yaku is not None:
        args["extraYaku"] = [pascalcase(yk.name) for yk in extra_yaku]
    if options is not None:
        args["options"] = options.__encode__()
    result = bridge_mahjongutils.call("hora", args)

    return Hora.__decode__(result)


__all__ = ("Hora", "build_hora", "build_hora_from_shanten_result")
