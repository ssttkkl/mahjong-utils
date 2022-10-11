from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List, Optional, Iterable

from mahjong_utils.models.furo import Furo, Pon, Kan
from mahjong_utils.models.mentsu import Mentsu, Kotsu, Shuntsu
from mahjong_utils.models.tatsu import Tatsu, Penchan, Kanchan
from mahjong_utils.models.tile import Tile, is_yaochu, yaochu
from mahjong_utils.models.tile_type import TileType
from mahjong_utils.models.wind import Wind


@dataclass(frozen=True)
class Hora(ABC):
    agari: Tile
    tsumo: bool
    dora: int = 0
    richi: bool = False
    ippatsu: bool = False
    self_wind: Optional[Wind] = None
    round_wind: Optional[Wind] = None

    @property
    @abstractmethod
    def tiles(self) -> Iterable[Tile]:
        pass

    @property
    @abstractmethod
    def menzen(self) -> bool:
        pass

    @property
    @abstractmethod
    def hu(self) -> int:
        pass


@dataclass(frozen=True)
class _StdHoraBase:
    jyantou: Tile
    menzen_mentsu: List[Mentsu]
    furo: List[Furo]
    tatsu: Optional[Tatsu]  # 为None表示单骑听牌

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


@dataclass(frozen=True)
class StdHora(Hora, _StdHoraBase):
    @property
    def tiles(self) -> Iterable[Tile]:
        yield self.jyantou
        yield self.jyantou
        for mt in self.mentsu:
            for t in mt.tiles:
                yield t

    @property
    def menzen(self) -> bool:
        ans = True
        for fr in self.furo:
            if not isinstance(fr, Kan) or not fr.ankan:
                ans = False
                break
        return ans

    @property
    def hu(self) -> int:
        ans = 20

        # 单骑、边张、坎张听牌
        if self.tatsu is None or isinstance(self.tatsu, Penchan) or isinstance(self.tatsu, Kanchan):
            ans += 2

        # 明刻、杠
        for fr in self.furo:
            if isinstance(fr, Pon):
                if is_yaochu(fr.tile):
                    ans += 4
                else:
                    ans += 2
            elif isinstance(fr, Kan):
                if not fr.ankan:
                    if is_yaochu(fr.tile):
                        ans += 16
                    else:
                        ans += 8
                else:
                    if is_yaochu(fr.tile):
                        ans += 32
                    else:
                        ans += 16

        # 暗刻（不含暗杠）
        for mt in self.menzen_mentsu:
            if isinstance(mt, Kotsu):
                if is_yaochu(mt.tile):
                    ans += 8
                else:
                    ans += 4

        # 对碰荣和时该刻子计为明刻
        if isinstance(self.tatsu, Kotsu):
            ans -= 2

        # 役牌雀头（连风算4符）
        if self.self_wind is not None and self.jyantou == self.self_wind.tile:
            ans += 2
        if self.round_wind is not None and self.jyantou == self.round_wind.tile:
            ans += 2
        if self.jyantou.tile_type == TileType.Z and self.jyantou.num >= 5:
            ans += 2

        # 门清荣和
        if self.menzen and not self.tsumo:
            ans += 10

        # 非平和自摸
        if ans != 20 and self.tsumo:
            ans += 2

        # 非门清最低30符
        if not self.menzen and ans < 30:
            ans = 30

        return ans


@dataclass(frozen=True)
class _ChitoiHoraBase:
    pairs: List[Tile]


@dataclass(frozen=True)
class ChitoiHora(Hora, _ChitoiHoraBase):
    @property
    def tiles(self) -> Iterable[Tile]:
        for pr in self.pairs:
            yield pr
            yield pr

    @property
    def menzen(self) -> bool:
        return True

    @property
    def hu(self) -> int:
        return 25


@dataclass(frozen=True)
class _KokushiHoraBase:
    repeated: Tile
    thirteen_waiting: bool = False


@dataclass(frozen=True)
class KokushiHora(Hora, _KokushiHoraBase):
    @property
    def tiles(self) -> Iterable[Tile]:
        for t in yaochu:
            yield t
        yield self.repeated

    @property
    def menzen(self) -> bool:
        return True

    @property
    def hu(self) -> int:
        return 20
