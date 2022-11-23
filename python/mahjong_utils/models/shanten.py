from abc import ABC, abstractmethod
from typing import Set, Dict, Optional

from pydantic import BaseModel

from mahjong_utils.models.tile import Tile, tile


class Shanten(BaseModel, ABC):
    shanten: int

    @abstractmethod
    def encode(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def decode(cls, data: dict) -> "Shanten":
        if data['type'] == 'ShantenWithoutGot':
            return ShantenWithoutGot.decode(data)
        elif data['type'] == 'ShantenWithGot':
            return ShantenWithGot.decode(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class ShantenWithoutGot(Shanten):
    advance: Set[Tile]
    advance_num: Optional[int]
    well_shape_advance: Optional[Set[Tile]]
    well_shape_advance_num: Optional[int]

    def encode(self) -> dict:
        return dict(
            type="ShantenWithoutGot",
            shantenNum=self.shanten,
            advance=[str(t) for t in self.advance],
            advanceNum=self.advance_num,
            wellShapeAdvance=[str(t) for t in well_shape_advance]
            if (well_shape_advance := self.well_shape_advance) is not None else None,
            wellShapeAdvanceNum=self.well_shape_advance_num
        )

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
    discard_to_advance: Dict[Tile, ShantenWithoutGot]

    def encode(self) -> dict:
        return dict(
            type="ShantenWithGot",
            shantenNum=self.shanten,
            discardToAdvance=dict((str(k), v.encode()) for (k, v) in self.discard_to_advance.items())
        )

    @classmethod
    def decode(cls, data: dict) -> "ShantenWithGot":
        return ShantenWithGot(
            shanten=data["shantenNum"],
            discard_to_advance=dict(
                (tile(k), ShantenWithoutGot.decode(v))
                for (k, v) in data["discardToAdvance"].items())
        )
