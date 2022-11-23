from typing import Sequence

from pydantic.fields import Field
from pydantic.main import BaseModel

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.hand_pattern import HandPattern
from mahjong_utils.models.tile import Tile, tile


class Hand(BaseModel):
    tiles: Sequence[Tile]
    furo: Sequence[Furo] = Field(default_factory=tuple)
    patterns: Sequence[HandPattern]

    @property
    def menzen(self) -> bool:
        for fr in self.furo:
            if not isinstance(fr, Kan) or not fr.ankan:
                return False
        return True

    def encode(self) -> dict:
        return dict(
            tiles=[str(t) for t in self.tiles],
            furo=[fr.encode() for fr in self.furo],
            patterns=[p.encode() for p in self.patterns]
        )

    @classmethod
    def decode(cls, data: dict) -> "Hand":
        return Hand(
            tiles=[tile(x) for x in data["tiles"]],
            furo=[Furo.decode(x) for x in data["furo"]],
            patterns=[HandPattern.decode(x) for x in data["patterns"]]
        )
