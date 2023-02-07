from abc import ABC, abstractmethod
from typing import Optional, FrozenSet, Tuple

from pydantic import Field
from stringcase import pascalcase, snakecase

from mahjong_utils.models.hand_pattern import RegularHandPattern, HandPattern, ChitoiHandPattern, KokushiHandPattern
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile, all_yaochu
from mahjong_utils.models.wind import Wind


class HoraHandPattern(HandPattern, ABC):
    agari: Tile
    tsumo: bool
    hu: int
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


class RegularHoraHandPattern(HoraHandPattern, RegularHandPattern):
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
            pattern=RegularHandPattern.__encode__(self)
        )

    @classmethod
    def __decode__(cls, data: dict) -> "RegularHoraHandPattern":
        return RegularHoraHandPattern(
            agari=Tile.__decode__(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(data["selfWind"])] if data["selfWind"] is not None else None,
            round_wind=Wind[snakecase(data["roundWind"])] if data["roundWind"] is not None else None,
            agari_tatsu=Tatsu.__decode__(data["agariTatsu"]) if data["agariTatsu"] is not None else None,
            hu=data["hu"],
            **RegularHandPattern.__decode__(data["pattern"]).dict()
        )


class ChitoiHoraHandPattern(HoraHandPattern, ChitoiHandPattern):
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


class KokushiHoraHandPattern(HoraHandPattern, KokushiHandPattern):
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
