from abc import ABC, abstractmethod
from enum import Enum
from typing import Optional, Sequence, Set, Dict

from pydantic import BaseModel
from stringcase import snakecase, pascalcase

from mahjong_utils.lib import libmahjongutils
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.hand import Hand
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class Shanten(BaseModel, ABC):
    shanten: int

    @abstractmethod
    def __encode__(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def __decode__(cls, data: dict) -> "Shanten":
        if data['type'] == 'ShantenWithoutGot':
            return ShantenWithoutGot.__decode__(data)
        elif data['type'] == 'ShantenWithGot':
            return ShantenWithGot.__decode__(data)
        elif data['type'] == 'ShantenWithFuroChance':
            return ShantenWithFuroChance.__decode__(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class ShantenWithoutGot(Shanten):
    advance: Set[Tile]
    advance_num: Optional[int]
    good_shape_advance: Optional[Set[Tile]]
    good_shape_advance_num: Optional[int]

    def __encode__(self) -> dict:
        return dict(
            type="ShantenWithoutGot",
            shantenNum=self.shanten,
            advance=[t.__encode__() for t in self.advance],
            advanceNum=self.advance_num,
            goodShapeAdvance=[t.__encode__() for t in self.good_shape_advance]
            if self.good_shape_advance is not None else None,
            goodShapeAdvanceNum=self.good_shape_advance_num
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ShantenWithoutGot":
        return ShantenWithoutGot(
            shanten=data["shantenNum"],
            advance=set(Tile.__decode__(x) for x in data["advance"]),
            advance_num=data["advanceNum"]
            if data["advanceNum"] is not None else None,
            good_shape_advance=set(Tile.__decode__(x) for x in data["goodShapeAdvance"])
            if data["goodShapeAdvance"] is not None else None,
            good_shape_advance_num=data["goodShapeAdvanceNum"]
            if data["goodShapeAdvanceNum"] is not None else None,
        )


class ShantenWithGot(Shanten):
    discard_to_advance: Dict[Tile, ShantenWithoutGot]
    ankan_to_advance: Dict[Tile, ShantenWithoutGot]

    def __encode__(self) -> dict:
        return dict(
            type="ShantenWithGot",
            shantenNum=self.shanten,
            discardToAdvance=dict((k.__encode__(), v.__encode__()) for (k, v) in self.discard_to_advance.items()),
            ankanToAdvance=dict((k.__encode__(), v.__encode__()) for (k, v) in self.ankan_to_advance.items())
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ShantenWithGot":
        return ShantenWithGot(
            shanten=data["shantenNum"],
            discard_to_advance=dict(
                (Tile.__decode__(k), ShantenWithoutGot.__decode__(v))
                for (k, v) in data["discardToAdvance"].items()),
            ankan_to_advance=dict(
                (Tile.__decode__(k), ShantenWithoutGot.__decode__(v))
                for (k, v) in data["ankanToAdvance"].items())
        )


class ShantenWithFuroChance(Shanten):
    pass_: Optional[ShantenWithoutGot]
    chi: Dict[Tatsu, ShantenWithGot]
    pon: Optional[ShantenWithGot]
    minkan: Optional[ShantenWithGot]

    def __encode__(self) -> dict:
        return {
            'type': "ShantenWithFuroChance",
            'shantenNum': self.shanten,
            'pass': self.pass_.__encode__()
            if self.pass_ is not None else None,
            'chi': dict((k.__encode__(), v.__encode__()) for (k, v) in self.chi.items()),
            'pon': self.pon.__encode__()
            if self.pon is not None else None,
            'minkan': self.minkan.__encode__()
            if self.minkan is not None else None
        }

    @classmethod
    def __decode__(cls, data: dict) -> "ShantenWithFuroChance":
        return ShantenWithFuroChance(
            shanten=data["shantenNum"],
            pass_=ShantenWithoutGot.__decode__(data["pass"])
            if data["pass"] is not None else None,
            chi=dict((Tatsu.__decode__(k), ShantenWithGot.__decode__(v)) for (k, v) in data["chi"].items()),
            pon=ShantenWithGot.__decode__(data["pon"])
            if data["pon"] is not None else None,
            minkan=ShantenWithoutGot.__decode__(data["minkan"])
            if data["minkan"] is not None else None,
        )


class ShantenResultType(str, Enum):
    regular = "Regular"
    chitoi = "Chitoi"
    kokushi = "Kokushi"
    union = "Union"
    furo_chance = "FuroChance"


class ShantenResult(BaseModel):
    type: ShantenResultType
    hand: Hand
    shanten_info: Shanten
    regular: Optional["ShantenResult"]
    chitoi: Optional["ShantenResult"]
    kokushi: Optional["ShantenResult"]

    def __encode__(self) -> dict:
        return dict(
            type=pascalcase(self.type.name),
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__(),
            regular=self.regular.__encode__() if self.regular is not None else None,
            chitoi=self.chitoi.__encode__() if self.chitoi is not None else None,
            kokushi=self.kokushi.__encode__() if self.kokushi is not None else None,
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ShantenResult":
        return ShantenResult(
            type=ShantenResultType[snakecase(data["type"])],
            hand=Hand.__decode__(data["hand"]),
            shanten_info=Shanten.__decode__(data["shantenInfo"]),
            regular=ShantenResult.__decode__(data["regular"]) if data["regular"] is not None else None,
            chitoi=ShantenResult.__decode__(data["chitoi"]) if data["chitoi"] is not None else None,
            kokushi=ShantenResult.__decode__(data["kokushi"]) if data["kokushi"] is not None else None,
        )

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
    def good_shape_advance(self) -> Optional[Set[Tile]]:
        return getattr(self.shanten_info, "good_shape_advance", None)

    @property
    def good_shape_advance_num(self) -> Optional[int]:
        return getattr(self.shanten_info, "good_shape_advance_num", None)

    @property
    def discard_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "discard_to_advance", None)

    @property
    def ankan_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "ankan_to_advance", None)

    @property
    def with_got(self) -> bool:
        return self.discard_to_advance is not None


def regular_shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        calc_advance_num: bool = True,
        best_shanten_only: bool = False,
        allow_ankan: bool = True,
) -> ShantenResult:
    """
    标准形向听分析

    :param tiles: 门前的牌
    :param furo: 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
    :param calc_advance_num: 是否计算进张数
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :param allow_ankan: 是否允许暗杠
    :return 向听分析结果
    """
    result = libmahjongutils.call("regularShanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [fr.__encode__() for fr in furo] if furo is not None else [],
        "calcAdvanceNum": calc_advance_num,
        "bestShantenOnly": best_shanten_only,
        "allowAnkan": allow_ankan,
    })

    return ShantenResult.__decode__(result)


def chitoi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True,
        best_shanten_only: bool = False,
) -> ShantenResult:
    """
    七对子向听分析

    :param tiles: 门前的牌
    :param calc_advance_num: 是否计算进张数
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = libmahjongutils.call("chitoiShanten", {
        "tiles": [str(t) for t in tiles],
        "calcAdvanceNum": calc_advance_num,
        "bestShantenOnly": best_shanten_only,
    })

    return ShantenResult.__decode__(result)


def kokushi_shanten(
        tiles: Sequence[Tile],
        calc_advance_num: bool = True,
        best_shanten_only: bool = False,
) -> ShantenResult:
    """
    国士无双向听分析

    :param tiles: 门前的牌
    :param calc_advance_num: 是否计算进张数
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = libmahjongutils.call("kokushiShanten", {
        "tiles": [str(t) for t in tiles],
        "calcAdvanceNum": calc_advance_num,
        "bestShantenOnly": best_shanten_only,
    })

    return ShantenResult.__decode__(result)


def shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        calc_advance_num: bool = True,
        best_shanten_only: bool = False,
        allow_ankan: bool = True,
) -> ShantenResult:
    """
    向听分析

    :param tiles: 门前的牌
    :param furo: 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
    :param calc_advance_num: 是否计算进张数
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :param allow_ankan: 是否允许暗杠
    :return 向听分析结果
    """
    result = libmahjongutils.call("shanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [fr.__encode__() for fr in furo] if furo is not None else [],
        "calcAdvanceNum": calc_advance_num,
        "bestShantenOnly": best_shanten_only,
        "allowAnkan": allow_ankan,
    })

    return ShantenResult.__decode__(result)


def furo_chance_shanten(
        tiles: Sequence[Tile],
        chance_tile: Tile,
        allow_chi: bool = True,
        calc_advance_num: bool = True,
        best_shanten_only: bool = False,
        allow_kuikae: bool = False
):
    """
    副露判断分析

    :param tiles: 门前的牌
    :param chance_tile: 副露机会牌（能够吃、碰的牌）
    :param allow_chi: 是否允许吃
    :param calc_advance_num: 是否计算进张数
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :param allow_kuikae: 是否允许食替
    :return 向听分析结果
    """
    result = libmahjongutils.call("furoChanceShanten", {
        "tiles": [str(t) for t in tiles],
        "chanceTile": chance_tile.__encode__(),
        "allowChi": allow_chi,
        "calcAdvanceNum": calc_advance_num,
        "bestShantenOnly": best_shanten_only,
        "allowKuikae": allow_kuikae
    })

    return ShantenResult.__decode__(result)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "furo_chance_shanten",
           "shanten",
           "ShantenResult",)
