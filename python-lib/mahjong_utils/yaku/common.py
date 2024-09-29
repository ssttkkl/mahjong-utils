from typing import FrozenSet

from .yaku import Yaku

tsumo = Yaku("tsumo", 1, 1)
pinhu = Yaku("pinhu", 1, 1)
tanyao = Yaku("tanyao", 1, 0)
ipe = Yaku("ipe", 1, 1)
self_wind = Yaku("self_wind", 1, 0)
round_wind = Yaku("round_wind", 1, 0)
haku = Yaku("haku", 1, 0)
hatsu = Yaku("hatsu", 1, 0)
chun = Yaku("chun", 1, 0)
sanshoku = Yaku("sanshoku", 2, 1)
ittsu = Yaku("ittsu", 2, 1)
chanta = Yaku("chanta", 2, 1)
chitoi = Yaku("chitoi", 2, 2)
toitoi = Yaku("toitoi", 2, 0)
sananko = Yaku("sananko", 2, 0)
honroto = Yaku("honroto", 2, 0)
sandoko = Yaku("sandoko", 2, 0)
sankantsu = Yaku("sankantsu", 2, 0)
shosangen = Yaku("shosangen", 2, 0)
honitsu = Yaku("honitsu", 3, 1)
junchan = Yaku("junchan", 3, 1)
ryanpe = Yaku("ryanpe", 3, 3)
chinitsu = Yaku("chinitsu", 6, 1)

all_common_yaku: FrozenSet[Yaku] = frozenset({
    tsumo, pinhu, tanyao, ipe, self_wind, round_wind, haku, hatsu, chun,
    sanshoku, ittsu, chanta, chitoi, toitoi, sananko, honroto, sandoko, sankantsu, shosangen,
    honitsu, junchan, ryanpe, chinitsu
})

__all__ = (
    "all_common_yaku",
    "tsumo", "pinhu", "tanyao", "ipe", "self_wind", "round_wind", "haku", "hatsu", "chun",
    "sanshoku", "ittsu", "chanta", "chitoi", "toitoi", "sananko", "honroto", "sandoko", "sankantsu", "shosangen",
    "honitsu", "junchan", "ryanpe", "chinitsu"
)
