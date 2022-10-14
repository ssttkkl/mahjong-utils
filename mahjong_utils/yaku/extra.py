from typing import Set

from mahjong_utils.yaku import Yaku

richi = Yaku("Richi", 1, 1)
ippatsu = Yaku("Ippatsu", 1, 1)
rinshan = Yaku("Rinshan", 1, 0)
chankan = Yaku("Chankan", 1, 0)
haitei = Yaku("Haitei", 1, 0)
houtei = Yaku("Houtei", 1, 0)
w_richi = Yaku("WRichi", 2, 2)
tenhou = Yaku("Tenhou", 13, 13)
chihou = Yaku("Chihou", 13, 13)

all_extra_yaku: Set[Yaku] = {
    richi,
    ippatsu,
    rinshan,
    chankan,
    haitei,
    houtei,
    w_richi,
    tenhou,
    chihou
}

__all__ = (
    "all_extra_yaku",
    "richi", "ippatsu", "rinshan", "chankan", "haitei", "houtei",
    "w_richi",
    "tenhou", "chihou"
)
