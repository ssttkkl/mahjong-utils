from abc import ABC, abstractmethod
from typing import Optional, FrozenSet, Tuple
from typing import Set

from pydantic import BaseModel
from pydantic import Field
from pydantic.dataclasses import dataclass
from stringcase import pascalcase, snakecase

from mahjong_utils.models.hand_pattern import RegularHandPattern, HandPattern, _BaseRegularHandPattern, \
    _BaseChitoiHandPattern, _BaseKokushiHandPattern
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile, all_yaochu
from mahjong_utils.models.wind import Wind
from mahjong_utils.point_by_han_hu.models import ParentPoint, ChildPoint
from mahjong_utils.yaku import Yaku, get_yaku


@dataclass(frozen=True)
class HoraOptions:
    aotenjou: bool = False
    """是否为青天井规则"""
    allow_kuitan: bool = True
    """是否允许食断"""
    has_renpuu_jyantou_hu: bool = True
    """连风雀头是否记4符（true则记4符，false则记2符）"""
    has_kiriage_mangan: bool = False
    """是否有切上满贯"""
    has_kazoe_yakuman: bool = True
    """是否有累计役满"""
    has_multiple_yakuman: bool = True
    """是否有多倍役满"""
    has_complex_yakuman: bool = True
    """是否有复合役满"""

    def __encode__(self) -> dict:
        return dict(aotenjou=self.aotenjou,
                    allowKuitan=self.allow_kuitan,
                    hasRenpuuJyantouHu=self.has_renpuu_jyantou_hu,
                    hasKiriageMangan=self.has_kiriage_mangan,
                    hasKazoeYakuman=self.has_kazoe_yakuman,
                    hasMultipleYakuman=self.has_multiple_yakuman,
                    hasComplexYakuman=self.has_complex_yakuman)

    @classmethod
    def __decode__(cls, data: dict) -> "HoraOptions":
        return HoraOptions(
            aotenjou=data["aotenjou"],
            allow_kuitan=data["allowKuitan"],
            has_renpuu_jyantou_hu=data["hasRenpuuJyantouHu"],
            has_kiriage_mangan=data["hasKiriageMangan"],
            has_kazoe_yakuman=data["hasKazoeYakuman"],
            has_multiple_yakuman=data["hasMultipleYakuman"],
            has_complex_yakuman=data["hasComplexYakuman"],
        )


class HoraHandPattern(HandPattern, ABC):
    agari: Tile
    tsumo: bool
    self_wind: Optional[Wind]
    round_wind: Optional[Wind]

    @abstractmethod
    def __encode__(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def __decode__(cls, data: dict) -> "HoraHandPattern":
        if data['type'] == 'RegularHoraHandPattern':
            return RegularHoraHandPattern.__decode__(data)
        elif data['type'] == 'ChitoiHoraHandPattern':
            return ChitoiHoraHandPattern.__decode__(data)
        elif data['type'] == 'KokushiHoraHandPattern':
            return KokushiHoraHandPattern.__decode__(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class RegularHoraHandPattern(HoraHandPattern, _BaseRegularHandPattern):
    agari_tatsu: Optional[Tatsu]
    jyantou: Tile

    def __encode__(self) -> dict:
        return dict(
            type="RegularHoraHandPattern",
            agari=self.agari.__encode__(),
            tsumo=self.tsumo,
            self_wind=pascalcase(self.self_wind.name) if self.self_wind is not None else None,
            round_wind=pascalcase(self.round_wind.name) if self.round_wind is not None else None,
            agari_tatsu=self.agari_tatsu.__encode__() if self.agari_tatsu is not None else None,
            pattern=RegularHandPattern(**self.dict()).__encode__()
        )

    @classmethod
    def __decode__(cls, data: dict) -> "RegularHoraHandPattern":
        return RegularHoraHandPattern(
            agari=Tile.__decode__(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(data["selfWind"])] if data["selfWind"] is not None else None,
            round_wind=Wind[snakecase(data["roundWind"])] if data["roundWind"] is not None else None,
            agari_tatsu=Tatsu.__decode__(data["agariTatsu"]) if data["agariTatsu"] is not None else None,
            **RegularHandPattern.__decode__(data["pattern"]).dict()
        )


class ChitoiHoraHandPattern(HoraHandPattern, _BaseChitoiHandPattern):
    hu: int = 25
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    def __encode__(self) -> dict:
        return dict(
            type="ChitoiHoraHandPattern",
            pairs=[t.__encode__() for t in self.pairs],
            agari=self.agari.__encode__(),
            tsumo=self.tsumo,
            self_wind=pascalcase(self.self_wind.name) if self.self_wind is not None else None,
            round_wind=pascalcase(self.round_wind.name) if self.round_wind is not None else None,
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ChitoiHoraHandPattern":
        return ChitoiHoraHandPattern(
            agari=Tile.__decode__(data["agari"]),
            pairs=frozenset(Tile.__decode__(t) for t in data["pairs"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(data["selfWind"])] if data["selfWind"] is not None else None,
            round_wind=Wind[snakecase(data["roundWind"])] if data["roundWind"] is not None else None,
        )


class KokushiHoraHandPattern(HoraHandPattern, _BaseKokushiHandPattern):
    hu: int = 20
    repeated: Tile
    yaochu: FrozenSet[Tile] = all_yaochu
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    @property
    def thirteen_waiting(self) -> bool:
        return self.agari == self.repeated

    def __encode__(self) -> dict:
        return dict(
            type="KokushiHoraHandPattern",
            repeated=self.repeated.__encode__(),
            agari=self.agari.__encode__(),
            tsumo=self.tsumo,
            self_wind=pascalcase(self.self_wind.name) if self.self_wind is not None else None,
            round_wind=pascalcase(self.round_wind.name) if self.round_wind is not None else None,
        )

    @classmethod
    def __decode__(cls, data: dict) -> "KokushiHoraHandPattern":
        return KokushiHoraHandPattern(
            repeated=Tile.__decode__(data["repeated"]),
            agari=Tile.__decode__(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(data["selfWind"])] if data["selfWind"] is not None else None,
            round_wind=Wind[snakecase(data["roundWind"])] if data["roundWind"] is not None else None,
        )


class Hora(BaseModel):
    pattern: HoraHandPattern
    han: int
    hu: int
    dora: int
    yaku: Set[Yaku]
    extra_yaku: Set[Yaku]
    has_yakuman: bool
    parent_point: ParentPoint
    child_point: ChildPoint

    @classmethod
    def __decode__(cls, data: dict) -> "Hora":
        return Hora(
            pattern=HoraHandPattern.__decode__(data["pattern"]),
            han=data["han"],
            hu=data["hu"],
            dora=data["dora"],
            yaku=set(get_yaku(snakecase(yk)) for yk in data["yaku"]),
            extra_yaku=set(get_yaku(snakecase(yk)) for yk in data["extraYaku"]),
            has_yakuman=data["hasYakuman"],
            parent_point=ParentPoint.__decode__(data["parentPoint"]),
            child_point=ChildPoint.__decode__(data["childPoint"]),
        )

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
