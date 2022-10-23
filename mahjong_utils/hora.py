from typing import Optional, Set, List
from typing import Tuple

from pydantic import BaseModel, Field

from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hora_hand import HoraHand, build_hora_hand
from mahjong_utils.models.tile import Tile
from mahjong_utils.models.wind import Wind
from mahjong_utils.point_by_han_hu import get_parent_point_by_han_hu, get_child_point_by_han_hu
from mahjong_utils.shanten import ShantenResult, shanten
from mahjong_utils.yaku import Yaku
from mahjong_utils.yaku.check import check_yaku


def _calc_han(yaku: Set[Yaku], menzen: bool) -> int:
    if menzen:
        han = sum(map(lambda y: y.han, yaku))
    else:
        han = sum(map(lambda y: y.han - y.furo_loss, yaku))
    return han


class Hora(BaseModel):
    hand: HoraHand
    dora: int = 0
    extra_yaku: Optional[Set[Yaku]]
    han: int = 0  # 在__init__中计算
    yaku: Set[Yaku] = Field(default_factory=set)  # 在__init__中计算

    def __init__(self, **kwargs):
        super().__init__(**kwargs)

        self.yaku = check_yaku(self.hand, self.extra_yaku)
        self.han = _calc_han(self.yaku, self.hand.menzen)
        if self.han > 0:
            self.han += self.dora

    @property
    def parent_point(self) -> Tuple[int, int]:
        """
        亲家和牌点数
        :return: (荣和点数, 自摸各家点数)
        """
        if len(self.yaku) == 0:
            return 0, 0
        elif next(iter(self.yaku)).is_yakuman:
            times = 0
            for yaku in self.yaku:
                times += yaku.han // 13

            ans = get_parent_point_by_han_hu(self.han, self.hand.hu)
            return ans[0] * times, ans[1] * times
        else:
            return get_parent_point_by_han_hu(self.han, self.hand.hu)

    @property
    def child_point(self) -> Tuple[int, int, int]:
        """
        子家X番Y符的点数
        :return: (荣和点数, 自摸庄家点数, 自摸闲家点数)
        """
        if len(self.yaku) == 0:
            return 0, 0, 0
        elif next(iter(self.yaku)).is_yakuman:
            times = 0
            for yaku in self.yaku:
                times += yaku.han // 13

            ans = get_child_point_by_han_hu(13, 20)
            return ans[0] * times, ans[1] * times, ans[2] * times
        else:
            return get_child_point_by_han_hu(self.han, self.hand.hu)


def build_hora(tiles: List[Tile], furo: Optional[List[Furo]], agari: Tile,
               tsumo: bool,
               *,
               dora: int = 0,
               self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
               extra_yaku: Optional[Set[Yaku]] = None) -> Hora:
    shanten_result = shanten(tiles, furo)
    return build_hora_from_shanten_result(shanten_result, agari, tsumo,
                                          dora=dora, self_wind=self_wind, round_wind=round_wind,
                                          extra_yaku=extra_yaku)


def build_hora_from_shanten_result(shanten_result: ShantenResult, agari: Tile,
                                   tsumo: bool,
                                   *,
                                   dora: int = 0,
                                   self_wind: Optional[Wind] = None, round_wind: Optional[Wind] = None,
                                   extra_yaku: Optional[Set[Yaku]] = None) -> Hora:
    if shanten_result.shanten != 0:
        raise ValueError("shanten_result.shanten must be 0")

    possible_hora = []

    for hand in shanten_result.hands:
        if agari not in hand.advance:
            continue

        hora_hand = build_hora_hand(hand, agari, tsumo, self_wind, round_wind)
        hora = Hora(hand=hora_hand, dora=dora, extra_yaku=extra_yaku)
        possible_hora.append(hora)

    best_hora = possible_hora[0]

    for hora in possible_hora:
        if hora.han > best_hora.han or (hora.han == best_hora.han and hora.hand.hu > best_hora.hand.hu):
            best_hora = hora

    return best_hora


__all__ = ("Hora", "build_hora", "build_hora_from_shanten_result")
