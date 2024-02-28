from typing import NamedTuple

from pydantic.dataclasses import dataclass


@dataclass(frozen=True)
class HanHuOptions:
    aotenjou: bool = False
    """是否为青天井规则"""
    has_kiriage_mangan: bool = False
    """是否有切上满贯"""
    has_kazoe_yakuman: bool = True
    """是否有累计役满"""

    def __encode__(self) -> dict:
        return dict(aotenjou=self.aotenjou,
                    hasKiriageMangan=self.has_kiriage_mangan,
                    hasKazoeYakuman=self.has_kazoe_yakuman)

    @classmethod
    def __decode__(cls, data: dict) -> "HanHuOptions":
        return HanHuOptions(
            aotenjou=data["aotenjou"],
            has_kiriage_mangan=data["hasKiriageMangan"],
            has_kazoe_yakuman=data["hasKazoeYakuman"],
        )


class ParentPoint(NamedTuple):
    ron: int
    tsumo: int

    @classmethod
    def __decode__(cls, data: dict) -> "ParentPoint":
        return ParentPoint(
            ron=data["ron"],
            tsumo=data["tsumo"],
        )


class ChildPoint(NamedTuple):
    ron: int
    tsumo_parent: int
    tsumo_child: int

    @classmethod
    def __decode__(cls, data: dict) -> "ChildPoint":
        return ChildPoint(
            ron=data["ron"],
            tsumo_parent=data["tsumoParent"],
            tsumo_child=data["tsumoChild"],
        )
