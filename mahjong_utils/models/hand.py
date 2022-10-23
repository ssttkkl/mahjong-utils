from abc import ABC, abstractmethod
from typing import List, Iterable, Optional, Set, Dict

from pydantic import BaseModel, Field

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.mentsu import Mentsu, Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class Hand(BaseModel, ABC):
    """
    表示一个结构分析后的手牌
    """

    @property
    @abstractmethod
    def tiles(self) -> Iterable[Tile]:
        raise NotImplementedError()

    @property
    @abstractmethod
    def menzen(self) -> bool:
        raise NotImplementedError()

    with_got: bool = False
    shanten: Optional[int] = None
    advance: Optional[Set[Tile]] = None
    discard_to_advance: Optional[Dict[Tile, Set[Tile]]] = None


class RegularHand(Hand):
    """
    表示一个结构分析后的以标准形为目标的手牌
    """

    k: int
    jyantou: Optional[Tile] = None
    menzen_mentsu: List[Mentsu] = Field(default_factory=list)
    furo: List[Furo] = Field(default_factory=list)
    tatsu: List[Tatsu] = Field(default_factory=list)
    remaining: List[Tile] = Field(default_factory=list)

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

    @property
    def menzen(self) -> bool:
        for fr in self.furo:
            if not (isinstance(fr, Kan) and fr.ankan):
                return False
        return True


class ChitoiHand(Hand):
    """
    表示一个结构分析后的以七对子为目标的手牌
    """

    pairs: List[Tile] = Field(default_factory=list)
    remaining: List[Tile] = Field(default_factory=list)

    @property
    def tiles(self) -> Iterable[Tile]:
        for p in self.pairs:
            yield p
            yield p
        for t in self.remaining:
            yield t

    @property
    def menzen(self) -> bool:
        return True


class KokushiHand(Hand):
    """
    表示一个结构分析后的以国士无双为目标的手牌
    """

    yaochu: List[Tile] = Field(default_factory=list)
    repeated: Optional[Tile] = None
    remaining: List[Tile] = Field(default_factory=list)

    @property
    def tiles(self) -> Iterable[Tile]:
        for t in self.yaochu:
            if self.repeated == t:
                yield t
            yield t
        for t in self.remaining:
            yield t

    @property
    def menzen(self) -> bool:
        return True


__all__ = ("Hand", "RegularHand", "ChitoiHand", "KokushiHand")
