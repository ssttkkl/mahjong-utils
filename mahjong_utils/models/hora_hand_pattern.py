from abc import ABC
from typing import List
from typing import Optional, Iterable

from pydantic import Field, root_validator

from mahjong_utils.internal.attr_dict import attr_dict
from mahjong_utils.internal.hand_utils import hand_exclude_got_regular
from mahjong_utils.models.furo import Pon, Kan
from mahjong_utils.models.hand_pattern import RegularHandPattern, HandPattern, ChitoiHandPattern, KokushiHandPattern
from mahjong_utils.models.mentsu import Kotsu
from mahjong_utils.models.tatsu import Penchan, Kanchan, Tatsu
from mahjong_utils.models.tile import Tile
from mahjong_utils.models.tile import all_yaochu
from mahjong_utils.models.tile import is_yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.models.wind import Wind


class HoraHandPattern(HandPattern, ABC):
    agari: Tile
    tsumo: bool
    hu: int
    self_wind: Optional[Wind]
    round_wind: Optional[Wind]


class RegularHoraHandPattern(HoraHandPattern, RegularHandPattern):
    agari_tatsu: Optional[Tatsu]
    hu: int = 0  # 在calc_hu中计算

    k: int = 4
    jyantou: Tile

    @root_validator
    def validate_k(cls, values):
        assert len(values["menzen_mentsu"]) + len(values["furo"]) == 4
        return values

    @root_validator(pre=True)
    def calc_hu(cls, values):
        values = attr_dict(values)

        ans = 20

        # 单骑、边张、坎张听牌
        if (values.agari_tatsu is None
                or isinstance(values.agari_tatsu, Penchan)
                or isinstance(values.agari_tatsu, Kanchan)):
            ans += 2

        # 明刻、杠
        for fr in values.furo:
            if isinstance(fr, Pon):
                if is_yaochu(fr.tile):
                    ans += 4
                else:
                    ans += 2
            elif isinstance(fr, Kan):
                if not fr.ankan:
                    if is_yaochu(fr.tile):
                        ans += 16
                    else:
                        ans += 8
                else:
                    if is_yaochu(fr.tile):
                        ans += 32
                    else:
                        ans += 16

        # 暗刻（不含暗杠）
        for mt in values.menzen_mentsu:
            if isinstance(mt, Kotsu):
                if is_yaochu(mt.tile):
                    ans += 8
                else:
                    ans += 4

        # 对碰荣和时该刻子计为明刻
        if isinstance(values.agari_tatsu, Kotsu):
            ans -= 2

        # 役牌雀头（连风算4符）
        if values.self_wind is not None and values.jyantou == values.self_wind.tile:
            ans += 2
        if values.round_wind is not None and values.jyantou == values.round_wind.tile:
            ans += 2
        if values.jyantou.tile_type == TileType.Z and values.jyantou.num >= 5:
            ans += 2

        # 门清荣和
        if values.menzen and not values.tsumo:
            ans += 10

        # 非平和自摸
        if ans != 20 and values.tsumo:
            ans += 2

        # 非门清最低30符
        if not values.menzen and ans < 30:
            ans = 30

        # 切上
        if ans % 10 != 0:
            ans += 10 - ans % 10

        values.hu = ans

        return values


class ChitoiHoraHandPattern(HoraHandPattern, ChitoiHandPattern):
    hu: int = 25


class KokushiHoraHandPattern(HoraHandPattern, KokushiHandPattern):
    hu: int = 20

    yaochu: List[Tile] = Field(default_factory=lambda: list(all_yaochu))
    repeated: Tile
    remaining: List[Tile] = Field(default_factory=list)

    @property
    def thirteen_waiting(self) -> bool:
        return self.agari == self.repeated


def _build_regular_hora_hand(hand: RegularHandPattern,
                             agari: Tile,
                             tsumo: bool,
                             self_wind: Optional[Wind] = None,
                             round_wind: Optional[Wind] = None) -> Iterable[RegularHoraHandPattern]:
    if not hand.with_got:
        if hand.jyantou is not None:
            yield RegularHoraHandPattern(with_got=False,
                                         agari=agari,
                                         tsumo=tsumo,
                                         self_wind=self_wind,
                                         round_wind=round_wind,
                                         agari_tatsu=hand.tatsu[0],
                                         jyantou=hand.jyantou,
                                         menzen_mentsu=hand.menzen_mentsu + [hand.tatsu[0].with_waiting(agari)],
                                         furo=hand.furo)
        else:
            yield RegularHoraHandPattern(with_got=True,
                                         agari=agari,
                                         tsumo=tsumo,
                                         self_wind=self_wind,
                                         round_wind=round_wind,
                                         agari_tatsu=None,
                                         jyantou=agari,
                                         menzen_mentsu=hand.menzen_mentsu,
                                         furo=hand.furo)
    else:
        for excluded_hand in hand_exclude_got_regular(hand, agari):
            for h in _build_regular_hora_hand(excluded_hand, agari, tsumo, self_wind, round_wind):
                yield h


def _build_chitoi_hora_hand(hand: ChitoiHandPattern,
                            agari: Tile,
                            tsumo: bool,
                            self_wind: Optional[Wind] = None,
                            round_wind: Optional[Wind] = None) -> Iterable[ChitoiHoraHandPattern]:
    if not hand.with_got:
        if agari not in hand.advance:
            raise ValueError("agari is not waiting")

        pairs = hand.pairs.copy()
        pairs.append(agari)

        yield ChitoiHoraHandPattern(with_got=False,
                                    agari=agari,
                                    tsumo=tsumo,
                                    self_wind=self_wind,
                                    round_wind=round_wind,
                                    pairs=pairs)
    else:
        if agari not in hand.pairs:
            raise ValueError("agari is not in hand")

        yield ChitoiHoraHandPattern(with_got=True,
                                    agari=agari,
                                    tsumo=tsumo,
                                    self_wind=self_wind,
                                    round_wind=round_wind,
                                    pairs=hand.pairs)


def _build_kokushi_hora_hand(hand: KokushiHandPattern,
                             agari: Tile,
                             tsumo: bool,
                             self_wind: Optional[Wind] = None,
                             round_wind: Optional[Wind] = None) -> Iterable[KokushiHoraHandPattern]:
    if not hand.with_got:
        if agari not in hand.advance:
            raise ValueError("agari is not waiting")

        if hand.repeated is None:
            repeated = agari
        else:
            repeated = hand.repeated
    else:
        repeated = hand.repeated

    yield KokushiHoraHandPattern(with_got=hand.with_got,
                                 agari=agari,
                                 tsumo=tsumo,
                                 self_wind=self_wind,
                                 round_wind=round_wind,
                                 repeated=repeated)


def build_hora_hand(hand: HandPattern,
                    agari: Tile,
                    tsumo: bool,
                    self_wind: Optional[Wind] = None,
                    round_wind: Optional[Wind] = None) -> Iterable[HoraHandPattern]:
    """
    根据给定Hand构造HoraHand。当Hand为摸牌状态时，返回所有可能的HoraHand。否则返回一个对应的HoraHand。

    :param hand: HandPattern
    :param agari: 和牌
    :param tsumo: 是否自摸
    :param self_wind: 自风
    :param round_wind: 场风
    :return: 当Hand为摸牌状态时，返回所有可能的HoraHand。否则返回一个对应的HoraHand。
    """
    if not hand.with_got:
        if hand.shanten != 0:
            raise ValueError("hand is not tenpai")
        if agari not in hand.advance:
            raise ValueError("agari is not waiting")
    else:
        if hand.shanten != -1:
            raise ValueError("hand is not agari")
        if agari not in hand.tiles:
            raise ValueError("agari is not in hand")

    if isinstance(hand, RegularHandPattern):
        return _build_regular_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, ChitoiHandPattern):
        return _build_chitoi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, KokushiHandPattern):
        return _build_kokushi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    else:
        raise TypeError(f"unexpected type of hand: {type(hand)}")
