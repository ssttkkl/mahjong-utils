from abc import ABC
from typing import List
from typing import Optional, Iterable

from pydantic import Field, root_validator

from mahjong_utils.internal.attr_dict import attr_dict
from mahjong_utils.internal.hand_utils import regular_pattern_after_discard
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


def _build_regular_hora_hand_pattern(
        pattern: RegularHandPattern,
        agari: Tile,
        tsumo: bool,
        self_wind: Optional[Wind] = None,
        round_wind: Optional[Wind] = None,
        *, with_got: bool = True
) -> Iterable[RegularHoraHandPattern]:
    if not with_got:
        if pattern.jyantou is not None:
            yield RegularHoraHandPattern(agari=agari,
                                         tsumo=tsumo,
                                         self_wind=self_wind,
                                         round_wind=round_wind,
                                         agari_tatsu=pattern.tatsu[0],
                                         jyantou=pattern.jyantou,
                                         menzen_mentsu=pattern.menzen_mentsu + (pattern.tatsu[0].with_waiting(agari),),
                                         furo=pattern.furo)
        else:
            yield RegularHoraHandPattern(agari=agari,
                                         tsumo=tsumo,
                                         self_wind=self_wind,
                                         round_wind=round_wind,
                                         agari_tatsu=None,
                                         jyantou=agari,
                                         menzen_mentsu=pattern.menzen_mentsu,
                                         furo=pattern.furo)
    else:
        for pat_after_discard in regular_pattern_after_discard(pattern, agari):
            for h in _build_regular_hora_hand_pattern(pat_after_discard,
                                                      agari, tsumo,
                                                      self_wind, round_wind,
                                                      with_got=False):
                yield h


def _build_chitoi_hora_hand(
        pattern: ChitoiHandPattern,
        agari: Tile,
        tsumo: bool,
        self_wind: Optional[Wind] = None,
        round_wind: Optional[Wind] = None
) -> Iterable[ChitoiHoraHandPattern]:
    yield ChitoiHoraHandPattern(agari=agari,
                                tsumo=tsumo,
                                self_wind=self_wind,
                                round_wind=round_wind,
                                pairs=pattern.pairs)


def _build_kokushi_hora_hand(
        pattern: KokushiHandPattern,
        agari: Tile,
        tsumo: bool,
        self_wind: Optional[Wind] = None,
        round_wind: Optional[Wind] = None
) -> Iterable[KokushiHoraHandPattern]:
    yield KokushiHoraHandPattern(agari=agari,
                                 tsumo=tsumo,
                                 self_wind=self_wind,
                                 round_wind=round_wind,
                                 repeated=pattern.repeated)


def build_hora_hand(
        pattern: HandPattern,
        agari: Tile,
        tsumo: bool,
        self_wind: Optional[Wind] = None,
        round_wind: Optional[Wind] = None
) -> Iterable[HoraHandPattern]:
    """
    根据给定Hand构造HoraHand。返回所有可能的HoraHand。

    :param pattern: HandPattern
    :param agari: 和牌
    :param tsumo: 是否自摸
    :param self_wind: 自风
    :param round_wind: 场风
    :return: 当Hand为摸牌状态时，返回所有可能的HoraHand。否则返回一个对应的HoraHand。
    """
    if isinstance(pattern, RegularHandPattern):
        return _build_regular_hora_hand_pattern(pattern, agari, tsumo, self_wind, round_wind)
    elif isinstance(pattern, ChitoiHandPattern):
        return _build_chitoi_hora_hand(pattern, agari, tsumo, self_wind, round_wind)
    elif isinstance(pattern, KokushiHandPattern):
        return _build_kokushi_hora_hand(pattern, agari, tsumo, self_wind, round_wind)
    else:
        raise TypeError(f"unexpected type of pattern: {type(pattern)}")
