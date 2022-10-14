from typing import Set

from mahjong_utils.yaku import Yaku

richi = Yaku(1, 1)
ippatsu = Yaku(1, 1)
rinshan = Yaku(1, 0)
chankan = Yaku(1, 0)
haitei = Yaku(1, 0)
houtei = Yaku(1, 0)
w_richi = Yaku(2, 2)
tenhou = Yaku(13, 13)
chihou = Yaku(13, 13)

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
