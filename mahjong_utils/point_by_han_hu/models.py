from typing import NamedTuple


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
