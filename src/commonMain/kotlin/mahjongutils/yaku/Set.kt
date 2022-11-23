package mahjongutils.yaku


val allCommonYaku = setOf(
    Tsumo, Pinhu, Tanyao, Ipe, SelfWind, RoundWind, Haku, Hatsu, Chun,
    Sanshoku, Ittsu, Chanta, Chitoi, Toitoi, Sananko, Honroto, Sandoku, Sankantsu, Shosangen,
    Honitsu, Junchan, Ryanpe, Chinitsu
).associateBy { it.name }

val allYakuman = setOf(
    Tenhou, Chihou,
    Kokushi, Suanko, Daisangen, Tsuiso, Shousushi, Lyuiso, Chinroto, Sukantsu, Churen,
    Daisushi, SuankoTanki, ChurenNineWaiting, KokushiThirteenWaiting
).associateBy { it.name }

val allExtraYaku = setOf(
    Richi, Ippatsu, Rinshan, Chankan, Haitei, Houtei,
    WRichi,
    Tenhou, Chihou
).associateBy { it.name }

val allExtraYakuman = setOf(
    Tenhou, Chihou
).associateBy { it.name }

val allYaku = allCommonYaku + allYakuman + allExtraYaku