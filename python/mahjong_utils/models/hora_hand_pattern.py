from abc import ABC, abstractmethod
from typing import Optional

from stringcase import pascalcase, snakecase

from mahjong_utils.models.hand_pattern import RegularHandPattern, HandPattern, ChitoiHandPattern, KokushiHandPattern
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile, tile
from mahjong_utils.models.wind import Wind


class HoraHandPattern(HandPattern, ABC):
    agari: Tile
    tsumo: bool
    hu: int
    self_wind: Optional[Wind]
    round_wind: Optional[Wind]

    @abstractmethod
    def encode(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def decode(cls, data: dict) -> "HoraHandPattern":
        if data['type'] == 'RegularHoraHandPattern':
            return RegularHoraHandPattern.decode(data)
        elif data['type'] == 'ChitoiHoraHandPattern':
            return ChitoiHoraHandPattern.decode(data)
        elif data['type'] == 'KokushiHoraHandPattern':
            return KokushiHoraHandPattern.decode(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class RegularHoraHandPattern(HoraHandPattern, RegularHandPattern):
    agari_tatsu: Optional[Tatsu]
    jyantou: Tile

    def encode(self) -> dict:
        return dict(
            type="RegularHoraHandPattern",
            agari=str(self.agari),
            tsumo=self.tsumo,
            self_wind=pascalcase(self_wind.name) if (self_wind := self.self_wind) is not None else None,
            round_wind=pascalcase(round_wind.name) if (round_wind := self.round_wind) is not None else None,
            agari_tatsu=agari_tatsu.encode() if (agari_tatsu := self.agari_tatsu) is not None else None,
            pattern=RegularHandPattern.encode(self)
        )

    @classmethod
    def decode(cls, data: dict) -> "RegularHoraHandPattern":
        return RegularHoraHandPattern(
            agari=tile(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(self_wind)] if (self_wind := data["selfWind"]) is not None else None,
            round_wind=Wind[snakecase(round_wind)] if (round_wind := data["roundWind"]) is not None else None,
            agari_tatsu=Tatsu.decode(agari_tatsu) if (agari_tatsu := data["agariTatsu"]) is not None else None,
            hu=data["hu"],
            **RegularHandPattern.decode(data["pattern"]).dict()
        )


class ChitoiHoraHandPattern(HoraHandPattern, ChitoiHandPattern):
    hu: int = 25

    def encode(self) -> dict:
        return dict(
            type="ChitoiHoraHandPattern",
            agari=str(self.agari),
            tsumo=self.tsumo,
            self_wind=pascalcase(self_wind.name) if (self_wind := self.self_wind) is not None else None,
            round_wind=pascalcase(round_wind.name) if (round_wind := self.round_wind) is not None else None,
            pattern=ChitoiHandPattern.encode(self)
        )

    @classmethod
    def decode(cls, data: dict) -> "ChitoiHoraHandPattern":
        return ChitoiHoraHandPattern(
            agari=tile(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(self_wind)] if (self_wind := data["selfWind"]) is not None else None,
            round_wind=Wind[snakecase(round_wind)] if (round_wind := data["roundWind"]) is not None else None,
            **ChitoiHandPattern.decode(data["pattern"]).dict()
        )


class KokushiHoraHandPattern(HoraHandPattern, KokushiHandPattern):
    hu: int = 20
    repeated: Tile

    @property
    def thirteen_waiting(self) -> bool:
        return self.agari == self.repeated

    def encode(self) -> dict:
        return dict(
            type="KokushiHoraHandPattern",
            agari=str(self.agari),
            tsumo=self.tsumo,
            self_wind=pascalcase(self_wind.name) if (self_wind := self.self_wind) is not None else None,
            round_wind=pascalcase(round_wind.name) if (round_wind := self.round_wind) is not None else None,
            pattern=KokushiHandPattern.encode(self)
        )

    @classmethod
    def decode(cls, data: dict) -> "KokushiHoraHandPattern":
        return KokushiHoraHandPattern(
            agari=tile(data["agari"]),
            tsumo=data["tsumo"],
            self_wind=Wind[snakecase(self_wind)] if (self_wind := data["selfWind"]) is not None else None,
            round_wind=Wind[snakecase(round_wind)] if (round_wind := data["roundWind"]) is not None else None,
            **KokushiHandPattern.decode(data["pattern"]).dict()
        )
