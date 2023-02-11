from typing import Sequence, Generic, TypeVar, Type

from pydantic.fields import Field
from pydantic.main import BaseModel

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.hand_pattern import HandPattern, CommonHandPattern
from mahjong_utils.models.tile import Tile

T_HandPattern = TypeVar("T_HandPattern", bound=CommonHandPattern)


class Hand(BaseModel, Generic[T_HandPattern]):
    tiles: Sequence[Tile]
    furo: Sequence[Furo] = Field(default_factory=tuple)
    patterns: Sequence[T_HandPattern]

    @property
    def menzen(self) -> bool:
        for fr in self.furo:
            if not isinstance(fr, Kan) or not fr.ankan:
                return False
        return True

    def __encode__(self) -> dict:
        return dict(
            tiles=[t.__encode__() for t in self.tiles],
            furo=[fr.__encode__() for fr in self.furo],
            patterns=[p.__encode__() for p in self.patterns]
        )

    @classmethod
    def __decode__(cls, data: dict, hand_pattern_type: Type[T_HandPattern] = CommonHandPattern) -> "Hand[T_HandPattern]":
        return Hand(
            tiles=[Tile.__decode__(x) for x in data["tiles"]],
            furo=[Furo.__decode__(x) for x in data["furo"]],
            patterns=[hand_pattern_type.__decode__(x) for x in data["patterns"]]
        )
