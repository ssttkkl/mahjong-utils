from typing import Set, Dict, Optional

from pydantic import BaseModel, Field

from mahjong_utils.models.tile import Tile, tile


class Shanten(BaseModel):
    shanten: int

    @classmethod
    def decode(cls, data: dict) -> "Shanten":
        if data['type'] == 'ShantenWithoutGot':
            return ShantenWithoutGot.decode(data)
        elif data['type'] == 'ShantenWithGot':
            return ShantenWithGot.decode(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class ShantenWithoutGot(Shanten):
    advance: Set[Tile] = Field(default_factory=set)
    advance_num: Optional[int]
    well_shape_advance: Optional[Set[Tile]]
    well_shape_advance_num: Optional[int]

    @classmethod
    def decode(cls, data: dict) -> "ShantenWithoutGot":
        return ShantenWithoutGot(
            shanten=data["shantenNum"],
            advance=set(tile(x) for x in data["advance"]),
            advance_num=advance_num
            if (advance_num := data["advanceNum"]) is not None else None,
            well_shape_advance=set(tile(x) for x in well_shape_advance)
            if (well_shape_advance := data["wellShapeAdvance"]) is not None else None,
            well_shape_advance_num=well_shape_advance_num
            if (well_shape_advance_num := data["wellShapeAdvanceNum"]) is not None else None,
        )


class ShantenWithGot(Shanten):
    discard_to_advance: Dict[Tile, ShantenWithoutGot] = Field(default_factory=dict)

    @classmethod
    def decode(cls, data: dict) -> "ShantenWithGot":
        return ShantenWithGot(
            shanten=data["shantenNum"],
            discard_to_advance=dict(
                (tile(k), ShantenWithoutGot.decode(v))
                for (k, v) in data["discardToAdvance"].items())
        )


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
