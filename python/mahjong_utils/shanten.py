from enum import Enum
from typing import Optional, Sequence

from pydantic import BaseModel
from stringcase import lowercase

from mahjong_utils.lib import libmahjongutils
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import Hand
from mahjong_utils.models.shanten import ShantenInfoMixin, Shanten
from mahjong_utils.models.tile import Tile


class ShantenResultType(str, Enum):
    regular = "Regular"
    chitoi = "Chitoi"
    kokushi = "Kokushi"
    union = "Union"


class ShantenResult(BaseModel, ShantenInfoMixin):
    type: ShantenResultType
    hand: Hand
    shanten_info: Shanten
    regular: Optional["ShantenResult"]
    chitoi: Optional["ShantenResult"]
    kokushi: Optional["ShantenResult"]

    @classmethod
    def decode(cls, data: dict) -> "ShantenResult":
        return ShantenResult(
            type=ShantenResultType[lowercase(data["type"])],
            hand=Hand.decode(data["hand"]),
            shanten_info=Shanten.decode(data["shantenInfo"]),
            regular=ShantenResult.decode(regular) if (regular := data["regular"]) is not None else None,
            chitoi=ShantenResult.decode(chitoi) if (chitoi := data["chitoi"]) is not None else None,
            kokushi=ShantenResult.decode(kokushi) if (kokushi := data["kokushi"]) is not None else None,
        )


def regular_shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        calc_advance_num: bool = True
) -> ShantenResult:
    result = libmahjongutils.call("regularShanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [Furo.encode(fr) for fr in furo] if furo is not None else [],
        "calcAdvanceNum": calc_advance_num
    })

    return ShantenResult.decode(result)


def chitoi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True
) -> ShantenResult:
    result = libmahjongutils.call("chitoiShanten", {
        "tiles": [str(t) for t in tiles],
        "calcAdvanceNum": calc_advance_num
    })

    return ShantenResult.decode(result)


def kokushi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True
) -> ShantenResult:
    result = libmahjongutils.call("kokushiShanten", {
        "tiles": [str(t) for t in tiles],
        "calcAdvanceNum": calc_advance_num
    })

    return ShantenResult.decode(result)


def shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        calc_advance_num: bool = True
) -> ShantenResult:
    result = libmahjongutils.call("shanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [Furo.encode(fr) for fr in furo] if furo is not None else [],
        "calcAdvanceNum": calc_advance_num
    })

    return ShantenResult.decode(result)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "shanten",
           "ShantenResult",)
