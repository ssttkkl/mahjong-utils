from typing import Optional, Set, List, Tuple

from pydantic import BaseModel
from stringcase import snakecase, pascalcase

from mahjong_utils.lib import libmahjongutils
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hora_hand_pattern import HoraHandPattern
from mahjong_utils.models.tile import Tile
from mahjong_utils.models.wind import Wind
from mahjong_utils.point_by_han_hu import get_parent_point_by_han_hu, get_child_point_by_han_hu
from mahjong_utils.shanten import ShantenResult
from mahjong_utils.yaku import Yaku, get_yaku


class Hora(BaseModel):
    pattern: HoraHandPattern
    han: int
    dora: int
    yaku: Set[Yaku]
    extra_yaku: Set[Yaku]
    has_yakuman: bool

    @classmethod
    def decode(cls, data: dict) -> "Hora":
        return Hora(
            pattern=HoraHandPattern.__decode__(data["pattern"]),
            han=data["han"],
            dora=data["dora"],
            yaku=set(get_yaku(snakecase(yk)) for yk in data["yaku"]),
            extra_yaku=set(get_yaku(snakecase(yk)) for yk in data["extraYaku"]),
            has_yakuman=data["hasYakuman"]
        )

    @property
    def hu(self) -> int:
        return self.pattern.hu

    @property
    def tsumo(self) -> bool:
        return self.pattern.tsumo

    @property
    def self_wind(self) -> Optional[Wind]:
        return self.pattern.self_wind

    @property
    def round_wind(self) -> Optional[Wind]:
        return self.pattern.round_wind

    @property
    def agari(self) -> Tile:
        return self.pattern.agari

    @property
    def parent_point(self) -> Tuple[int, int]:
        """
        亲家和牌点数

        :return: (荣和点数, 自摸各家点数)
        """
        if len(self.yaku) == 0:
            return 0, 0
        elif self.has_yakuman:
            times = 0
            for yaku in self.yaku:
                times += yaku.han // 13

            ans = get_parent_point_by_han_hu(13, 20)
            return ans[0] * times, ans[1] * times
        else:
            return get_parent_point_by_han_hu(self.han, self.hu)

    @property
    def child_point(self) -> Tuple[int, int, int]:
        """
        子家X番Y符的点数

        :return: (荣和点数, 自摸庄家点数, 自摸闲家点数)
        """
        if len(self.yaku) == 0:
            return 0, 0, 0
        elif self.has_yakuman:
            times = 0
            for yaku in self.yaku:
                times += yaku.han // 13

            ans = get_child_point_by_han_hu(13, 20)
            return ans[0] * times, ans[1] * times, ans[2] * times
        else:
            return get_child_point_by_han_hu(self.han, self.hu)


def build_hora(
        tiles: List[Tile], furo: Optional[List[Furo]], agari: Tile,
        tsumo: bool,
        *, dora: int = 0,
        self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
        extra_yaku: Optional[Set[Yaku]] = None
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
    result = libmahjongutils.call("hora", {
        "tiles": [str(t) for t in tiles],
        "furo": [fr.__encode__() for fr in furo] if furo is not None else [],
        "agari": str(agari),
        "tsumo": tsumo,
        "dora": dora,
        "selfWind": pascalcase(self_wind.name) if self_wind is not None else None,
        "roundWind": pascalcase(round_wind.name) if round_wind is not None else None,
        "extraYaku": [pascalcase(yk.name) for yk in extra_yaku] if extra_yaku is not None else []
    })

    return Hora.decode(result)


def build_hora_from_shanten_result(
        shanten_result: ShantenResult,
        agari: Tile,
        tsumo: bool,
        *, dora: int = 0,
        self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
        extra_yaku: Optional[Set[Yaku]] = None
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
    result = libmahjongutils.call("hora", {
        "shantenResult": shanten_result.__encode__(),
        "agari": str(agari),
        "tsumo": tsumo,
        "dora": dora,
        "selfWind": pascalcase(self_wind.name) if self_wind is not None else None,
        "roundWind": pascalcase(round_wind.name) if round_wind is not None else None,
        "extraYaku": [pascalcase(yk.name) for yk in extra_yaku] if extra_yaku is not None else []
    })

    return Hora.decode(result)


__all__ = ("Hora", "build_hora", "build_hora_from_shanten_result")
