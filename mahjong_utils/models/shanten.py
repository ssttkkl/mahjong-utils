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
    @property
    def shanten(self) -> Optional[int]:
        if (shanten_info := getattr(self, "shanten_info", None)) is not None:
            return getattr(shanten_info, "shanten", None)
        return None

    @property
    def advance(self) -> Optional[Set[Tile]]:
        if (shanten_info := getattr(self, "shanten_info", None)) is not None:
            return getattr(shanten_info, "advance", None)
        return None

    @property
    def well_shape_rate(self) -> Optional[float]:
        if (shanten_info := getattr(self, "shanten_info", None)) is not None:
            return getattr(shanten_info, "well_shape_rate", None)
        return None

    @property
    def discard_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        if (shanten_info := getattr(self, "shanten_info", None)) is not None:
            return getattr(shanten_info, "discard_to_advance", None)
        return None
