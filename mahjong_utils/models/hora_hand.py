from abc import ABC
from typing import Optional

from pydantic import BaseModel, root_validator

from mahjong_utils.models.furo import Pon, Kan
from mahjong_utils.models.hand import RegularHand, Hand, ChitoiHand, KokushiHand
from mahjong_utils.models.mentsu import Kotsu
from mahjong_utils.models.tatsu import Penchan, Kanchan, Tatsu
from mahjong_utils.models.tile import Tile, all_yaochu
from mahjong_utils.models.tile import is_yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.models.wind import Wind


class HoraHand(BaseModel, Hand, ABC):
    agari: Tile
    tsumo: bool
    hu: int
    self_wind: Optional[Wind]
    round_wind: Optional[Wind]


class RegularHoraHand(HoraHand, RegularHand):
    agari_tatsu: Optional[Tatsu]
    hu: int = 0  # 在__init__中计算

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
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

    @root_validator
    def validator(cls, values):
        if len(values["pairs"]) != 6:
            raise ValueError("must have exactly 6 pairs")
        elif len(values["remaining"]) != 1:
            raise ValueError("must have exactly 1 remaining")
        elif values["agari"] != values["remaining"][0]:
            raise ValueError("agari is not waiting")
        return values


class KokushiHoraHand(HoraHand, KokushiHand):
    repeated: Tile
    thirteen_waiting: bool = False

    hu: int = 20

    @root_validator
    def validator(cls, values):
        # fill tiles
        tiles = []
        for t in all_yaochu:
            tiles.append(t)
        if not values["thirteen_waiting"]:
            tiles.append(values["repeated"])
            tiles.remove(values["agari"])
        values["tiles"] = tiles
        return values


def _build_std_hora_hand(hand: RegularHand,
                         agari: Tile,
                         tsumo: bool,
                         self_wind: Optional[Wind] = None,
                         round_wind: Optional[Wind] = None) -> RegularHoraHand:
    if not hand.tenpai:
        raise ValueError("hand is not tenpai")

    if hand.jyantou is not None:
        agari_tatsu = hand.tatsu[0]
        menzen_mentsu = hand.menzen_mentsu.copy()
        menzen_mentsu.append(agari_tatsu.with_waiting(agari))

        return RegularHoraHand(agari=agari,
                               tsumo=tsumo,
                               self_wind=self_wind,
                               round_wind=round_wind,
                               agari_tatsu=agari_tatsu,
                               jyantou=hand.jyantou,
                               menzen_mentsu=menzen_mentsu,
                               furo=hand.furo)
    else:
        return RegularHoraHand(agari=agari,
                               tsumo=tsumo,
                               self_wind=self_wind,
                               round_wind=round_wind,
                               jyantou=agari,
                               menzen_mentsu=hand.menzen_mentsu,
                               furo=hand.furo)


def _build_chitoi_hora_hand(hand: ChitoiHand,
                            agari: Tile,
                            tsumo: bool,
                            self_wind: Optional[Wind] = None,
                            round_wind: Optional[Wind] = None) -> ChitoiHoraHand:
    if not hand.tenpai:
        raise ValueError("hand is not tenpai")

    if hand.remaining[0] != agari:
        raise ValueError("agari is not waiting")

    pairs = hand.pairs.copy()
    pairs.append(agari)

    return ChitoiHoraHand(agari=agari,
                          tsumo=tsumo,
                          self_wind=self_wind,
                          round_wind=round_wind,
                          pairs=pairs)


def _build_kokushi_hora_hand(hand: KokushiHand,
                             agari: Tile,
                             tsumo: bool,
                             self_wind: Optional[Wind] = None,
                             round_wind: Optional[Wind] = None) -> KokushiHoraHand:
    if not hand.tenpai:
        raise ValueError("hand is not tenpai")

    if agari in hand.tiles:
        repeated = agari
        thirteen_waiting = True
    else:
        repeated = None
        thirteen_waiting = False

        cnt = {}
        for t in hand.tiles:
            cnt[t] = cnt.get(t, 0) + 1

        for t in cnt:
            if cnt[t] == 2:
                repeated = t
                break

    return KokushiHoraHand(agari=agari,
                           tsumo=tsumo,
                           self_wind=self_wind,
                           round_wind=round_wind,
                           repeated=repeated,
                           thirteen_waiting=thirteen_waiting)


def build_hora_hand(hand: Hand,
                    agari: Tile,
                    tsumo: bool,
                    self_wind: Optional[Wind] = None,
                    round_wind: Optional[Wind] = None) -> HoraHand:
    if isinstance(hand, RegularHand):
        return _build_std_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, ChitoiHand):
        return _build_chitoi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    elif isinstance(hand, KokushiHand):
        return _build_kokushi_hora_hand(hand, agari, tsumo, self_wind, round_wind)
    else:
        raise TypeError(f"unexpected type of hand: {type(hand)}")
