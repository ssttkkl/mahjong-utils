from typing import Set, Dict, Optional

from pydantic import BaseModel, Field

from mahjong_utils.models.tile import Tile


class Shanten(BaseModel):
    shanten: int


class ShantenWithoutGot(Shanten):
    advance: Set[Tile] = Field(default_factory=set)
    advance_num: Optional[int]
    well_shape_advance: Optional[Set[Tile]]
    well_shape_advance_num: Optional[int]


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
    def advance_num(self) -> Optional[int]:
        return getattr(self.shanten_info, "advance_num", None)

    @property
    def well_shape_advance(self) -> Optional[Set[Tile]]:
        return getattr(self.shanten_info, "well_shape_advance", None)

    @property
    def well_shape_advance_num(self) -> Optional[int]:
        return getattr(self.shanten_info, "well_shape_advance_num", None)

    @property
    def discard_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "discard_to_advance", None)

    @property
    def with_got(self) -> bool:
        return self.discard_to_advance is not None