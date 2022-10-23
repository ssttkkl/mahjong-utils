from abc import ABC
from typing import List
from typing import Optional, Iterable

from pydantic import Field, root_validator

from mahjong_utils.internal.hand_utils import hand_exclude_got_regular
from mahjong_utils.models.furo import Pon, Kan
from mahjong_utils.models.hand import RegularHand, Hand, ChitoiHand, KokushiHand
from mahjong_utils.models.mentsu import Kotsu
from mahjong_utils.models.tatsu import Penchan, Kanchan, Tatsu
from mahjong_utils.models.tile import Tile
from mahjong_utils.models.tile import all_yaochu
from mahjong_utils.models.tile import is_yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.models.wind import Wind


class HoraHand(Hand, ABC):
    agari: Tile
    tsumo: bool
    hu: int
    self_wind: Optional[Wind]
    round_wind: Optional[Wind]


class RegularHoraHand(HoraHand, RegularHand):
    agari_tatsu: Optional[Tatsu]
    hu: int = 0  # 在__init__中计算

    k: int = 4
    jyantou: Tile

    @root_validator
    def validate_k(cls, values):
        assert len(values["menzen_mentsu"]) + len(values["furo"]) == 4
        return values

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.hu = self._calc_hu()

    def _calc_hu(self) -> int:
        ans = 20

        # 单骑、边张、坎张听牌
        if self.agari_tatsu is None or isinstance(self.agari_tatsu, Penchan) or isinstance(self.agari_tatsu, Kanchan):
            ans += 2

        # 明刻、杠
        for fr in self.furo:
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
        for mt in self.menzen_mentsu:
            if isinstance(mt, Kotsu):
                if is_yaochu(mt.tile):
                    ans += 8
                else:
                    ans += 4

        # 对碰荣和时该刻子计为明刻
        if isinstance(self.agari_tatsu, Kotsu):
            ans -= 2

        # 役牌雀头（连风算4符）
        if self.self_wind is not None and self.jyantou == self.self_wind.tile:
            ans += 2
        if self.round_wind is not None and self.jyantou == self.round_wind.tile:
            ans += 2
        if self.jyantou.tile_type == TileType.Z and self.jyantou.num >= 5:
            ans += 2

        # 门清荣和
        if self.menzen and not self.tsumo:
            ans += 10

        # 非平和自摸
        if ans != 20 and self.tsumo:
            ans += 2

        # 非门清最低30符
        if not self.menzen and ans < 30:
            ans = 30

        # 切上
        if ans % 10 != 0:
            ans += 10 - ans % 10

        return ans


class ChitoiHoraHand(HoraHand, ChitoiHand):
    hu: int = 25


class KokushiHoraHand(HoraHand, KokushiHand):
    repeated: Tile

    hu: int = 20

    yaochu: List[Tile] = Field(default_factory=lambda: list(all_yaochu))
    remaining: List[Tile] = Field(default_factory=list)

    @property
    def thirteen_waiting(self) -> bool:
        return self.agari == self.repeated


def _build_regular_hora_hand(hand: RegularHand,
                             agari: Tile,
                             tsumo: bool,
                             self_wind: Optional[Wind] = None,
                             round_wind: Optional[Wind] = None) -> Iterable[RegularHoraHand]:
    if hand.k != 4:
        raise ValueError("hand.k must be 4")

    if not hand.with_got:
        if hand.jyantou is not None:
            yield RegularHoraHand(agari=agari,
                                  tsumo=tsumo,
                                  self_wind=self_wind,
                                  round_wind=round_wind,
                                  agari_tatsu=hand.tatsu[0],
                                  jyantou=hand.jyantou,
                                  menzen_mentsu=hand.menzen_mentsu + [hand.tatsu[0].with_waiting(agari)],
                                  furo=hand.furo)
        else:
            yield RegularHoraHand(agari=agari,
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


def _build_chitoi_hora_hand(hand: ChitoiHand,
                            agari: Tile,
                            tsumo: bool,
                            self_wind: Optional[Wind] = None,
                            round_wind: Optional[Wind] = None) -> Iterable[ChitoiHoraHand]:
    if not hand.with_got:
        if agari not in hand.advance:
            raise ValueError("agari is not waiting")

        pairs = hand.pairs.copy()
        pairs.append(agari)

        yield ChitoiHoraHand(agari=agari,
                             tsumo=tsumo,
                             self_wind=self_wind,
                             round_wind=round_wind,
                             pairs=pairs)
    else:
        if agari not in hand.pairs:
            raise ValueError("agari is not in hand")

        yield ChitoiHoraHand(agari=agari,
                             tsumo=tsumo,
                             self_wind=self_wind,
                             round_wind=round_wind,
                             pairs=hand.pairs)


def _build_kokushi_hora_hand(hand: KokushiHand,
                             agari: Tile,
                             tsumo: bool,
                             self_wind: Optional[Wind] = None,
                             round_wind: Optional[Wind] = None) -> Iterable[KokushiHoraHand]:
    if not hand.with_got:
        if agari not in hand.advance:
            raise ValueError("agari is not waiting")

        if hand.repeated is None:
            repeated = agari
        else:
            repeated = hand.repeated
    else:
        repeated = hand.repeated

    yield KokushiHoraHand(agari=agari,
                          tsumo=tsumo,
                          self_wind=self_wind,
                          round_wind=round_wind,
                          repeated=repeated)


def build_hora_hand(hand: Hand,
                    agari: Tile,
                    tsumo: bool,
                    self_wind: Optional[Wind] = None,
                    round_wind: Optional[Wind] = None) -> Iterable[HoraHand]:
    """
    根据给定Hand构造HoraHand。当Hand为摸牌状态时，返回所有可能的HoraHand。否则返回一个对应的HoraHand。

    :param hand: Hand
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

    if isinstance(hand, RegularHand):
        return _build_regular_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, ChitoiHand):
        return _build_chitoi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, KokushiHand):
        return _build_kokushi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    else:
        raise TypeError(f"unexpected type of hand: {type(hand)}")
