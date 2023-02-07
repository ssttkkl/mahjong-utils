from typing import Set

from .extra import tenhou, chihou
from .yaku import Yaku

kokushi = Yaku("kokushi", 13, 13, True)
suanko = Yaku("suanko", 13, 13, True)
daisangen = Yaku("daisangen", 13, 0, True)
tsuiso = Yaku("tsuiso", 13, 0, True)
shousushi = Yaku("shousushi", 13, 0, True)
lyuiso = Yaku("lyuiso", 13, 0, True)
chinroto = Yaku("chinroto", 13, 0, True)
sukantsu = Yaku("sukantsu", 13, 0, True)
churen = Yaku("churen", 13, 13, True)
daisushi = Yaku("daisushi", 26, 0, True)
churen_nine_waiting = Yaku("churen_nine_waiting", 26, 26, True)
suanko_tanki = Yaku("suanko_tanki", 26, 26, True)
kokushi_thirteen_waiting = Yaku("kokushi_thirteen_waiting", 26, 26, True)

all_yakuman: Set[Yaku] = {
    tenhou, chihou,
    kokushi, suanko, daisangen, tsuiso, shousushi, lyuiso, chinroto, sukantsu, churen,
    daisushi, churen_nine_waiting, suanko_tanki, kokushi_thirteen_waiting
}

__all__ = (
    "all_yakuman",
    "tenhou", "chihou",
    "kokushi", "suanko", "daisangen", "tsuiso", "shousushi", "lyuiso", "chinroto", "sukantsu", "churen",
    "daisushi", "churen_nine_waiting", "suanko_tanki", "kokushi_thirteen_waiting"
)
