from typing import Set, Dict, Optional

from pydantic import BaseModel, Field

from mahjong_utils.models.tile import Tile


class Shanten(BaseModel):
    shanten: int


class ShantenWithoutGot(Shanten):
    advance: Set[Tile] = Field(default_factory=set)
    well_shape_rate: Optional[float]


class ShantenWithGot(Shanten):
    discard_to_advance: Dict[Tile, ShantenWithoutGot] = Field(default_factory=dict)


class ShantenInfoMixin:
    shanten_info: Shanten

    @property
    def shanten(self) -> Optional[int]:
        return getattr(self.shanten_info, "shanten", None)

    @property
    def advance(self) -> Optional[Set[Tile]]:
        return getattr(self.shanten_info, "advance", None)

    @property
    def well_shape_rate(self) -> Optional[float]:
        return getattr(self.shanten_info, "well_shape_rate", None)

    @property
    def discard_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "discard_to_advance", None)
