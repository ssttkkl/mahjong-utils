from typing import FrozenSet

from .yaku import Yaku

richi = Yaku("richi", 1, 1)
ippatsu = Yaku("ippatsu", 1, 1)
rinshan = Yaku("rinshan", 1, 0)
chankan = Yaku("chankan", 1, 0)
haitei = Yaku("haitei", 1, 0)
houtei = Yaku("houtei", 1, 0)
w_richi = Yaku("w_richi", 2, 2)
tenhou = Yaku("tenhou", 13, 13, True)
chihou = Yaku("chihou", 13, 13, True)

all_extra_yaku: FrozenSet[Yaku] = frozenset({
    richi, ippatsu, rinshan, chankan, haitei, houtei,
    w_richi,
    tenhou, chihou
})

__all__ = (
    "all_extra_yaku",
    "richi", "ippatsu", "rinshan", "chankan", "haitei", "houtei",
    "w_richi",
    "tenhou", "chihou"
)
