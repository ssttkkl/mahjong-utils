package mahjongutils.yaku

import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.hand.KokushiHoraHandPattern

val Kokushi = Yaku("Kokushi", 13, 13, true) { pattern ->
    pattern is KokushiHoraHandPattern && !pattern.thirteenWaiting
}

val Suanko = Yaku("Suanko", 13, 13, true, ankoSeriesCheckerFactory(4, tanki = false))

val Daisangen = Yaku("Daisangen", 13, 0, true, sangenSeriesCheckerFactory(3, sangenJyantou = false))

val Tsuiso = Yaku("Tsuiso", 13, 0, true) { pattern ->
    pattern.tiles.all { it.type == TileType.Z }
}

val Shousushi = Yaku("Shousushi", 13, 0, true, sushiSeriesCheckerFactory(3, windJyantou = true))

private val lyuTiles = Tile.parseTiles("23468s6z").toSet()

val Lyuiso = Yaku("Lyuiso", 13, 0, true) { pattern ->
    pattern.tiles.all { it in lyuTiles }
}

val Chinroto = Yaku("Chinroto", 13, 0, true, yaochuSeriesCheckerFactory(shuntsu = false, z = false))

val Sukantsu = Yaku("Sukantsu", 13, 0, true, kantsuSeriesCheckerFactory(4))

val Churen = Yaku("Churen", 13, 13, true, churenSeriesCheckerFactory(nineWaiting = false))

val Daisushi = Yaku("Daisushi", 26, 0, true, sushiSeriesCheckerFactory(4, windJyantou = false))

val ChurenNineWaiting = Yaku("ChurenNineWaiting", 26, 26, true, churenSeriesCheckerFactory(nineWaiting = true))

val SuankoTanki = Yaku("SuankoTanki", 26, 26, true, ankoSeriesCheckerFactory(4, true))

val KokushiThirteenWaiting = Yaku("KokushiThirteenWaiting", 26, 26, true) { pattern ->
    pattern is KokushiHoraHandPattern && pattern.thirteenWaiting
}
