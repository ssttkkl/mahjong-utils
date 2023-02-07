from abc import abstractmethod, ABC
from typing import Iterable, Optional, Tuple, FrozenSet

from pydantic import Field
from pydantic.main import BaseModel

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.mentsu import Mentsu, Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class HandPattern(BaseModel, ABC):
    @property
    @abstractmethod
    def menzen(self) -> bool:
        raise NotImplementedError()

    @property
    @abstractmethod
    def tiles(self) -> Iterable[Tile]:
        raise NotImplementedError()

    def __hash__(self):
        return hash(self.__class__) + hash(tuple(self.__dict__.values()))

    @abstractmethod
    def __encode__(self) -> dict:
        raise NotImplementedError()

    @classmethod
    def __decode__(cls, data: dict) -> "HandPattern":
        if data['type'] == 'RegularHandPattern':
            return RegularHandPattern.__decode__(data)
        elif data['type'] == 'ChitoiHandPattern':
            return ChitoiHandPattern.__decode__(data)
        elif data['type'] == 'KokushiHandPattern':
            return KokushiHandPattern.__decode__(data)
        else:
            raise ValueError("invalid type: " + data['type'])


class RegularHandPattern(HandPattern):
    """
    表示一个结构分析后的以标准形为目标的手牌
    """

    k: int
    jyantou: Optional[Tile] = None
    menzen_mentsu: Tuple[Mentsu, ...] = Field(default_factory=tuple)
    furo: Tuple[Furo, ...] = Field(default_factory=tuple)
    tatsu: Tuple[Tatsu, ...] = Field(default_factory=tuple)
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    def __encode__(self) -> dict:
        return dict(
            type="RegularHandPattern",
            k=self.k,
            jyantou=self.jyantou.__encode__() if self.jyantou is not None else None,
            menzenMentsu=[mt.__encode__() for mt in self.menzen_mentsu],
            furo=[fr.__encode__() for fr in self.furo],
            tatsu=[tt.__encode__() for tt in self.tatsu],
            remaining=[t.__encode__() for t in self.remaining],
        )

    @classmethod
    def __decode__(cls, data: dict) -> "RegularHandPattern":
        return RegularHandPattern(
            k=data["k"],
            jyantou=Tile.__decode__(data["jyantou"]) if data["jyantou"] is not None else None,
            menzen_mentsu=tuple(Mentsu.__decode__(x) for x in data["menzenMentsu"]),
            furo=tuple(Furo.__decode__(x) for x in data["furo"]),
            tatsu=tuple(Tatsu.__decode__(x) for x in data["tatsu"]),
            remaining=tuple(Tile.__decode__(x) for x in data["remaining"])
        )

    @property
    def mentsu(self) -> Iterable[Mentsu]:
        for mt in self.menzen_mentsu:
            yield mt
        for fr in self.furo:
            yield fr

    @property
    def shuntsu(self) -> Iterable[Shuntsu]:
        for mt in self.mentsu:
            if isinstance(mt, Shuntsu):
                yield mt

    @property
    def kotsu(self) -> Iterable[Kotsu]:
        for mt in self.mentsu:
            if isinstance(mt, Kotsu):
                yield mt

    @property
    def anko(self) -> Iterable[Kotsu]:
        for mt in self.menzen_mentsu:
            if isinstance(mt, Kotsu):
                yield mt
        for fr in self.furo:
            if isinstance(fr, Kan) and fr.ankan:
                yield fr

    @property
    def menzen(self) -> bool:
        for fr in self.furo:
            if not isinstance(fr, Kan) or not fr.ankan:
                return False
        return True

    @property
    def tiles(self) -> Iterable[Tile]:
        if self.jyantou is not None:
            yield self.jyantou
            yield self.jyantou
        for mt in self.mentsu:
            for t in mt.tiles:
                yield t
        for tt in self.tatsu:
            yield tt.first
            yield tt.second
        for t in self.remaining:
            yield t


class ChitoiHandPattern(HandPattern):
    """
    表示一个结构分析后的以七对子为目标的手牌
    """

    pairs: FrozenSet[Tile] = Field(default_factory=frozenset)
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    def __encode__(self) -> dict:
        return dict(
            type="ChitoiHandPattern",
            pairs=[t.__encode__() for t in self.pairs],
            remaining=[t.__encode__() for t in self.remaining],
        )

    @classmethod
    def __decode__(cls, data: dict) -> "ChitoiHandPattern":
        return ChitoiHandPattern(
            pairs=frozenset(Tile.__decode__(x) for x in data["pairs"]),
            remaining=tuple(Tile.__decode__(x) for x in data["remaining"])
        )

    @property
    def menzen(self) -> bool:
        return True

    @property
    def tiles(self) -> Iterable[Tile]:
        for p in self.pairs:
            yield p
            yield p
        for t in self.remaining:
            yield t


class KokushiHandPattern(HandPattern):
    """
    表示一个结构分析后的以国士无双为目标的手牌
    """

    yaochu: FrozenSet[Tile] = Field(default_factory=frozenset)
    repeated: Optional[Tile] = None
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    def __encode__(self) -> dict:
        return dict(
            type="KokushiHandPattern",
            yaochu=[t.__encode__() for t in self.yaochu],
            repeated=self.repeated.__encode__() if self.repeated is not None else None,
            remaining=[t.__encode__() for t in self.remaining],
        )

    @classmethod
    def __decode__(cls, data: dict) -> "KokushiHandPattern":
        return KokushiHandPattern(
            yaochu=frozenset(Tile.__decode__(x) for x in data["yaochu"]),
            repeated=Tile.__decode__(data["repeated"]) if data["repeated"] is not None else None,
            remaining=tuple(Tile.__decode__(x) for x in data["remaining"])
        )

    @property
    def menzen(self) -> bool:
        return True

    @property
    def tiles(self) -> Iterable[Tile]:
        for t in self.yaochu:
            if self.repeated == t:
                yield t
            yield t
        for t in self.remaining:
            yield t

    __all__ = ("HandPattern", "RegularHandPattern", "ChitoiHandPattern", "KokushiHandPattern")
