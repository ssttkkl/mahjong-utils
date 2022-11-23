package mahjongutils.yaku

import mahjongutils.models.*
import mahjongutils.models.hand.ChitoiHoraHandPattern
import mahjongutils.models.hand.RegularHoraHandPattern

val Tsumo = Yaku("Tsumo", 1, 1) { pattern ->
    pattern.menzen && pattern.tsumo
}

val Pinhu = Yaku("Pinhu", 1, 1) { pattern ->
    if (pattern !is RegularHoraHandPattern)
        return@Yaku false

    if (pattern.tsumo)
        pattern.hu == 20
    else
        pattern.hu == 30
}

val Tanyao = Yaku("Tanyao", 1) { pattern ->
    pattern.tiles.none { t -> t.isYaochu }
}

val Ipe = Yaku("Ipe", 1, 1, checker = pekoSeriesCheckerFactory(1))

val SelfWind = Yaku("SelfWind", 1, checker = yakuhaiCheckerFactory { it.selfWind?.tile })

val RoundWind = Yaku("RoundWind", 1, checker = yakuhaiCheckerFactory { it.roundWind?.tile })

val Haku = Yaku("Haku", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 5)))

val Hatsu = Yaku("Hatsu", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 6)))

val Chun = Yaku("Chun", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 7)))

val Sanshoku = Yaku("Sanshoku", 2, 1) { pattern ->
    if (pattern !is RegularHoraHandPattern)
        return@Yaku false

    val shuntsu = pattern.mentsu.filterIsInstance<Shuntsu>()
    for (i in shuntsu.indices) {
        for (j in i + 1 until shuntsu.size) {
            for (k in j + 1 until shuntsu.size) {
                val x = shuntsu[i]
                val y = shuntsu[j]
                val z = shuntsu[k]

                if (x.tile.type != y.tile.type
                    && y.tile.type != z.tile.type
                    && z.tile.type != x.tile.type
                    && x.tile.realNum == y.tile.realNum
                    && y.tile.realNum == z.tile.realNum
                ) {
                    return@Yaku true
                }
            }
        }
    }

    false
}

val Ittsu = Yaku("Ittsu", 2, 1) { pattern ->
    if (pattern !is RegularHoraHandPattern)
        return@Yaku false

    val shuntsu = pattern.mentsu.filterIsInstance<Shuntsu>()
    for (i in shuntsu.indices) {
        for (j in i + 1 until shuntsu.size) {
            for (k in j + 1 until shuntsu.size) {
                val x = shuntsu[i]
                val y = shuntsu[j]
                val z = shuntsu[k]

                if (x.tile.type == y.tile.type && y.tile.type == z.tile.type) {
                    val nums = setOf(x.tile.realNum, y.tile.realNum, z.tile.realNum)
                    if (nums == setOf(1, 4, 7)) {
                        return@Yaku true
                    }
                }
            }
        }
    }

    false
}

val Chanta = Yaku("Chanta", 2, 1, checker = yaochuSeriesCheckerFactory(shuntsu = true, z = true))

val Chitoi = Yaku("Chitoi", 2, 2) { pattern ->
    pattern is ChitoiHoraHandPattern
}

val Toitoi = Yaku("Toitoi", 2) { pattern ->
    if (pattern !is RegularHoraHandPattern)
        return@Yaku false

    if (pattern.mentsu.count { it is Shuntsu } != 0) {
        false
    } else {
        !pattern.menzen || (pattern.agariTatsu is Toitsu && !pattern.tsumo) // 非四暗刻的情况
    }
}

val Sananko = Yaku("Sananko", 2, checker = ankoSeriesCheckerFactory(3))

val Honroto = Yaku("Honroto", 2, checker = yaochuSeriesCheckerFactory(shuntsu = false, z = true))

val Sandoku = Yaku("Sandoku", 2) { pattern ->
    if (pattern !is RegularHoraHandPattern)
        return@Yaku false

    val kotsu = pattern.mentsu.filterIsInstance<Kotsu>()
    for (i in kotsu.indices) {
        for (j in i + 1 until kotsu.size) {
            for (k in j + 1 until kotsu.size) {
                val x = kotsu[i]
                val y = kotsu[j]
                val z = kotsu[k]

                if (x.tile.type != y.tile.type
                    && y.tile.type != z.tile.type
                    && z.tile.type != x.tile.type
                    && x.tile.realNum == y.tile.realNum
                    && y.tile.realNum == z.tile.realNum
                ) {
                    return@Yaku true
                }
            }
        }
    }

    false
}

val Sankantsu = Yaku("Sankantsu", 2, checker = kantsuSeriesCheckerFactory(3))

val Shosangen = Yaku("Shosangen", 2, checker = sangenSeriesCheckerFactory(2, sangenJyantou = true))

val Honitsu = Yaku("Honitsu", 3, 1, checker = itsuSeriesCheckerFactory(z = true))

val Junchan = Yaku("Junchan", 3, 1, checker = yaochuSeriesCheckerFactory(shuntsu = true, z = false))

val Ryanpe = Yaku("Ryanpe", 3, 3, checker = pekoSeriesCheckerFactory(2))

val Chinitsu = Yaku("Chinitsu", 6, 1, checker = itsuSeriesCheckerFactory(z = false))
