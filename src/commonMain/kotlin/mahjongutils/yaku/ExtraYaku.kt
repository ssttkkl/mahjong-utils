package mahjongutils.yaku

private val extraYakuChecker = YakuChecker { false }

val Richi = Yaku("Richi", 1, 1, checker = extraYakuChecker)
val Ippatsu = Yaku("Ippatsu", 1, 1, checker = extraYakuChecker)
val Rinshan = Yaku("Rinshan", 1, 0, checker = extraYakuChecker)
val Chankan = Yaku("Chankan", 1, 0, checker = extraYakuChecker)
val Haitei = Yaku("Haitei", 1, 0, checker = extraYakuChecker)
val Houtei = Yaku("Houtei", 1, 0, checker = extraYakuChecker)
val WRichi = Yaku("WRichi", 2, 2, checker = extraYakuChecker)
val Tenhou = Yaku("Tenhou", 13, 13, true, checker = extraYakuChecker)
val Chihou = Yaku("Chihou", 13, 13, true, checker = extraYakuChecker)
