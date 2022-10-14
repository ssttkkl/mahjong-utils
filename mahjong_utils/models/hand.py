from abc import ABC
from typing import List, Iterable, Optional

from pydantic import BaseModel, Field

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.mentsu import Mentsu, Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class Hand(ABC):
    """
    表示一个结构分析后的手牌
    """

    tiles: Iterable[Tile]

    @property
    def menzen(self) -> bool:
        raise NotImplementedError()

    @property
    def tenpai(self) -> bool:
        raise NotImplementedError()


class RegularHand(BaseModel, Hand):
    """
    表示一个结构分析后的以标准形为目标的手牌
    """

    jyantou: Optional[Tile]
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
    def menzen_tiles(self) -> Iterable[Tile]:
        yield self.jyantou
        yield self.jyantou
        for mt in self.menzen_mentsu:
            for t in mt.tiles:
                yield t
        for fr in self.furo:
            if isinstance(fr, Kan) and fr.ankan:
                for t in fr.tiles:
                    yield t
        for tt in self.tatsu:
            yield tt.first
            yield tt.second
        for t in self.remaining:
            yield t

    @property
    def furo_tiles(self) -> Iterable[Tile]:
        for fr in self.furo:
            if not (isinstance(fr, Kan) and fr.ankan):
                for t in fr.tiles:
                    yield t

    @property
    def tiles(self) -> Iterable[Tile]:
        for t in self.menzen_tiles:
            yield t
        for t in self.furo_tiles:
            yield t

    @property
    def menzen(self) -> bool:
        for fr in self.furo:
            if not (isinstance(fr, Kan) and fr.ankan):
                return False
        return True

    @property
    def tenpai(self) -> bool:
        return (len(self.tatsu) == 1 and len(self.remaining) == 0 and self.jyantou is not None) or \
               (len(self.tatsu) == 0 and len(self.remaining) == 1 and self.jyantou is None)


class ChitoiHand(BaseModel, Hand):
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

    @property
    def tenpai(self) -> bool:
        return len(self.pairs) == 6 and len(self.remaining) == 1


class KokushiHand(BaseModel, Hand):
    """
    表示一个结构分析后的以国士无双为目标的手牌
    """

    tiles: List[Tile] = Field(default_factory=list)

    @property
    def menzen(self) -> bool:
        return True

    @property
    def tenpai(self) -> bool:
        cnt = {}
        for t in self.tiles:
            cnt[t] = cnt.get(t, 0) + 1

        pair = None
        for t in cnt:
            if cnt[t] >= 2:
                pair = t
                break

        if pair is not None:
            shanten = 12 - len(cnt)
        else:
            shanten = 13 - len(cnt)

        return shanten == 0


__all__ = ("Hand", "RegularHand", "ChitoiHand", "KokushiHand")
