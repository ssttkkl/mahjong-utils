from abc import ABC, abstractmethod
from typing import Optional, Set, Dict, Generic, TypeVar, List

from pydantic import BaseModel
from typing_extensions import Self

from mahjong_utils.models.hand import Hand, T_HandPattern
from mahjong_utils.models.hand_pattern import RegularHandPattern, ChitoiHandPattern, KokushiHandPattern, HandPattern
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class Shanten(BaseModel, ABC):
    shanten: int

    @abstractmethod
    def __encode__(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def __decode__(cls, data: dict) -> "Shanten":
        if data['type'] == 'ShantenWithFuroChance':
            return ShantenWithFuroChance.__decode__(data)
        else:
            return CommonShanten.__decode__(data)


class CommonShanten(Shanten, ABC):
    @classmethod
    def __decode__(cls, data: dict) -> "Shanten":
        if data['type'] == 'ShantenWithoutGot':
            return ShantenWithoutGot.__decode__(data)
        elif data['type'] == 'ShantenWithGot':
            return ShantenWithGot.__decode__(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class Improvement(BaseModel):
    discard: Tile
    advance: Set[Tile]
    advance_num: int

    def __encode__(self) -> dict:
        return dict(
            discard=self.discard.__encode__(),
            advance=[t.__encode__() for t in self.advance],
            advanceNum=self.advance_num
        )

    @classmethod
    def __decode__(cls, data: dict) -> "Improvement":
        return Improvement(
            discard=Tile.__decode__(data["discard"]),
            advance=set(Tile.__decode__(x) for x in data["advance"]),
            advance_num=data["advanceNum"]
        )


def _encode_improvement_dict(improvement: Dict[Tile, List[Improvement]]):
    d = dict()
    for t in improvement:
        d[t.__encode__()] = [x.__encode__() for x in improvement[t]]
    return d


def _decode_improvement_dict(improvement: dict) -> Dict[Tile, List[Improvement]]:
    d = dict()
    for t in improvement:
        d[Tile.__decode__(t)] = [Improvement.__decode__(x) for x in improvement[t]]
    return d


class ShantenWithoutGot(CommonShanten):
    advance: Set[Tile]
    advance_num: int
    good_shape_advance: Optional[Set[Tile]]
    good_shape_advance_num: Optional[int]
    improvement: Optional[Dict[Tile, List[Improvement]]]
    improvement_num: Optional[int]
    good_shape_improvement: Optional[Dict[Tile, List[Improvement]]]
    good_shape_improvement_num: Optional[int]

    def __encode__(self) -> dict:
        return dict(
            type="ShantenWithoutGot",
            shantenNum=self.shanten,
            advance=[t.__encode__() for t in self.advance],
            advanceNum=self.advance_num,
            goodShapeAdvance=[t.__encode__() for t in self.good_shape_advance]
            if self.good_shape_advance is not None else None,
            goodShapeAdvanceNum=self.good_shape_advance_num,
            improvement=_encode_improvement_dict(self.improvement)
            if self.improvement is not None else None,
            improvementNum=self.improvement_num,
            goodShapeImprovement=_encode_improvement_dict(self.good_shape_improvement)
            if self.good_shape_improvement is not None else None,
            goodShapeImprovementNum=self.good_shape_improvement_num,
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ShantenWithoutGot":
        return ShantenWithoutGot(
            shanten=data["shantenNum"],
            advance=set(Tile.__decode__(x) for x in data["advance"]),
            advance_num=data["advanceNum"],
            good_shape_advance=set(Tile.__decode__(x) for x in data["goodShapeAdvance"])
            if data["goodShapeAdvance"] is not None else None,
            good_shape_advance_num=data["goodShapeAdvanceNum"]
            if data["goodShapeAdvanceNum"] is not None else None,
            improvement=_decode_improvement_dict(data["improvement"])
            if data["improvement"] is not None else None,
            improvement_num=data["improvementNum"]
            if data["improvementNum"] is not None else None,
            good_shape_improvement=_decode_improvement_dict(data["goodShapeImprovement"])
            if data["goodShapeImprovement"] is not None else None,
            good_shape_improvement_num=data["goodShapeImprovementNum"]
            if data["goodShapeImprovementNum"] is not None else None,
        )


class ShantenWithGot(CommonShanten):
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
    minkan: Optional[ShantenWithoutGot]

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


T_Shanten = TypeVar('T_Shanten', bound=Shanten)


class ShantenResult(BaseModel, Generic[T_Shanten, T_HandPattern], ABC):
    hand: Hand[T_HandPattern]
    shanten_info: T_Shanten

    @abstractmethod
    def __encode__(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        if data['type'] == 'FuroChanceShantenResult':
            return FuroChanceShantenResult.__decode__(data)
        else:
            return CommonShantenResult.__decode__(data)

    @property
    def shanten(self) -> int:
        return self.shanten_info.shanten


class CommonShantenResult(ShantenResult[CommonShanten, T_HandPattern], ABC):
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
    def improvement(self) -> Optional[Dict[Tile, List[Improvement]]]:
        return getattr(self.shanten_info, "improvement", None)

    @property
    def improvement_num(self) -> Optional[int]:
        return getattr(self.shanten_info, "improvement_num", None)

    @property
    def good_shape_improvement(self) -> Optional[Dict[Tile, List[Improvement]]]:
        return getattr(self.shanten_info, "good_shape_improvement", None)

    @property
    def good_shape_improvement_num(self) -> Optional[int]:
        return getattr(self.shanten_info, "good_shape_improvement_num", None)

    @property
    def discard_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "discard_to_advance", None)

    @property
    def ankan_to_advance(self) -> Optional[Dict[Tile, ShantenWithoutGot]]:
        return getattr(self.shanten_info, "ankan_to_advance", None)

    @property
    def with_got(self) -> bool:
        return isinstance(self.shanten_info, ShantenWithGot)

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        if data['type'] == 'RegularShantenResult':
            return RegularShantenResult.__decode__(data)
        elif data['type'] == 'ChitoiShantenResult':
            return ChitoiShantenResult.__decode__(data)
        elif data['type'] == 'KokushiShantenResult':
            return KokushiShantenResult.__decode__(data)
        elif data['type'] == 'UnionShantenResult':
            return UnionShantenResult.__decode__(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class RegularShantenResult(CommonShantenResult[RegularHandPattern]):
    def __encode__(self) -> dict:
        return dict(
            type='RegularShantenResult',
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__()
        )

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        return RegularShantenResult(
            hand=Hand.__decode__(data["hand"], RegularHandPattern),
            shanten_info=Shanten.__decode__(data["shantenInfo"])
        )


class ChitoiShantenResult(CommonShantenResult[ChitoiHandPattern]):
    def __encode__(self) -> dict:
        return dict(
            type='ChitoiShantenResult',
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__()
        )

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        return ChitoiShantenResult(
            hand=Hand.__decode__(data["hand"], ChitoiHandPattern),
            shanten_info=Shanten.__decode__(data["shantenInfo"])
        )


class KokushiShantenResult(CommonShantenResult[KokushiHandPattern]):
    def __encode__(self) -> dict:
        return dict(
            type='KokushiShantenResult',
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__()
        )

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        return KokushiShantenResult(
            hand=Hand.__decode__(data["hand"], KokushiHandPattern),
            shanten_info=Shanten.__decode__(data["shantenInfo"])
        )


class UnionShantenResult(CommonShantenResult[HandPattern]):
    regular: ShantenResult
    chitoi: Optional["ShantenResult"]
    kokushi: Optional["ShantenResult"]

    def __encode__(self) -> dict:
        return dict(
            type='UnionShantenResult',
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__(),
            regular=self.regular.__encode__(),
            chitoi=self.chitoi.__encode__() if self.chitoi is not None else None,
            kokushi=self.kokushi.__encode__() if self.kokushi is not None else None,
        )

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        return UnionShantenResult(
            hand=Hand.__decode__(data["hand"]),
            shanten_info=Shanten.__decode__(data["shantenInfo"]),
            regular=RegularShantenResult.__decode__(data["regular"]),
            chitoi=ChitoiShantenResult.__decode__(data["chitoi"]) if data["chitoi"] is not None else None,
            kokushi=KokushiShantenResult.__decode__(data["kokushi"]) if data["kokushi"] is not None else None,
        )


class FuroChanceShantenResult(ShantenResult[ShantenWithFuroChance, RegularHandPattern]):
    def __encode__(self) -> dict:
        return dict(
            type='FuroChanceShantenResult',
            hand=self.hand.__encode__(),
            shantenInfo=self.shanten_info.__encode__()
        )

    @classmethod
    def __decode__(cls, data: dict) -> Self:
        return FuroChanceShantenResult(
            hand=Hand.__decode__(data["hand"], RegularHandPattern),
            shanten_info=ShantenWithFuroChance.__decode__(data["shantenInfo"])
        )
