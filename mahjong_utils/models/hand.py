from dataclasses import dataclass
from typing import List

from mahjong_utils.models.mentsu import Mentsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


@dataclass(frozen=True)
class StdHand:
    jyantou: Tile
    mentsu: List[Mentsu]
    tatsu: List[Tatsu]
    remaining: List[Tile]

    @property
    def tiles(self):
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


__all__ = ("StdHand",)
