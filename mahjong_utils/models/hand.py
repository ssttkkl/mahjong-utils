from typing import Sequence

from pydantic.fields import Field
from pydantic.main import BaseModel

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.hand_pattern import HandPattern
from mahjong_utils.models.tile import Tile


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
