from typing import Iterable, Optional, Union, Tuple, FrozenSet

from pydantic import Field
from pydantic.main import BaseModel

from mahjong_utils.models.furo import Furo, Kan
from mahjong_utils.models.mentsu import Mentsu, Shuntsu, Kotsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


class RegularHandPattern(BaseModel):
    """
    表示一个结构分析后的以标准形为目标的手牌
    """

    k: int
    jyantou: Optional[Tile] = None
    menzen_mentsu: Tuple[Mentsu, ...] = Field(default_factory=tuple)
    furo: Tuple[Furo, ...] = Field(default_factory=tuple)
    tatsu: Tuple[Tatsu, ...] = Field(default_factory=tuple)
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

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

    class Config:
        frozen = True


class ChitoiHandPattern(BaseModel):
    """
    表示一个结构分析后的以七对子为目标的手牌
    """

    pairs: FrozenSet[Tile] = Field(default_factory=frozenset)
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    class Config:
        frozen = True


class KokushiHandPattern(BaseModel):
    """
    表示一个结构分析后的以国士无双为目标的手牌
    """

    yaochu: FrozenSet[Tile] = Field(default_factory=frozenset)
    repeated: Optional[Tile] = None
    remaining: Tuple[Tile, ...] = Field(default_factory=tuple)

    class Config:
        frozen = True


HandPattern = Union[RegularHandPattern, ChitoiHandPattern, KokushiHandPattern]

__all__ = ("HandPattern", "RegularHandPattern", "ChitoiHandPattern", "KokushiHandPattern")
