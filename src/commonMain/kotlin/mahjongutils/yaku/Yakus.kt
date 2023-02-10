package mahjongutils.yaku

import mahjongutils.models.*
import mahjongutils.hora.ChitoiHoraHandPattern
import mahjongutils.hora.KokushiHoraHandPattern
import mahjongutils.hora.RegularHoraHandPattern

/**
 * 包含所有役种
 */
object Yakus {
    // ========== Common ==========

    /**
     * 自摸
     */
    val Tsumo = Yaku("Tsumo", 1, 1) { pattern ->
        pattern.menzen && pattern.tsumo
    }

    /**
     * 平和
     */
    val Pinhu = Yaku("Pinhu", 1, 1) { pattern ->
        if (pattern !is RegularHoraHandPattern || !pattern.menzen)
            return@Yaku false

        if (pattern.tsumo)
            pattern.hu == 20
        else
            pattern.hu == 30
    }

    /**
     * 断幺
     */
    val Tanyao = Yaku("Tanyao", 1) { pattern ->
        pattern.tiles.none { t -> t.isYaochu }
    }

    /**
     * 一杯口
     */
    val Ipe = Yaku("Ipe", 1, 1, checker = pekoSeriesCheckerFactory(1))

    /**
     * 自风
     */
    val SelfWind = Yaku("SelfWind", 1, checker = yakuhaiCheckerFactory { it.selfWind?.tile })

    /**
     * 场风
     */
    val RoundWind = Yaku("RoundWind", 1, checker = yakuhaiCheckerFactory { it.roundWind?.tile })

    /**
     * 白
     */
    val Haku = Yaku("Haku", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 5)))

    /**
     * 发
     */
    val Hatsu = Yaku("Hatsu", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 6)))

    /**
     * 中
     */
    val Chun = Yaku("Chun", 1, checker = yakuhaiCheckerFactory(Tile.get(TileType.Z, 7)))

    /**
     * 三色同顺
     */
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

    /**
     * 一气通贯
     */
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

    /**
     * 混全带幺九
     */
    val Chanta = Yaku("Chanta", 2, 1, checker = yaochuSeriesCheckerFactory(shuntsu = true, z = true))

    /**
     * 七对子
     */
    val Chitoi = Yaku("Chitoi", 2, 2) { pattern ->
        pattern is ChitoiHoraHandPattern
    }

    /**
     * 对对和
     */
    val Toitoi = Yaku("Toitoi", 2) { pattern ->
        if (pattern !is RegularHoraHandPattern)
            return@Yaku false

        if (pattern.mentsu.count { it is Shuntsu } != 0) {
            false
        } else {
            !pattern.menzen || (pattern.agariTatsu is Toitsu && !pattern.tsumo) // 非四暗刻的情况
        }
    }

    /**
     * 三暗刻
     */
    val Sananko = Yaku("Sananko", 2, checker = ankoSeriesCheckerFactory(3))

    /**
     * 混老头
     */
    val Honroto = Yaku("Honroto", 2, checker = yaochuSeriesCheckerFactory(shuntsu = false, z = true))

    /**
     * 三色同刻
     */
    val Sandoko = Yaku("Sandoko", 2) { pattern ->
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

    /**
     * 三杠子
     */
    val Sankantsu = Yaku("Sankantsu", 2, checker = kantsuSeriesCheckerFactory(3))

    /**
     * 小三元
     */
    val Shosangen = Yaku("Shosangen", 2, checker = sangenSeriesCheckerFactory(2, sangenJyantou = true))

    /**
     * 混一色
     */
    val Honitsu = Yaku("Honitsu", 3, 1, checker = itsuSeriesCheckerFactory(z = true))

    /**
     * 纯全带幺九
     */
    val Junchan = Yaku("Junchan", 3, 1, checker = yaochuSeriesCheckerFactory(shuntsu = true, z = false))

    /**
     * 两杯口
     */
    val Ryanpe = Yaku("Ryanpe", 3, 3, checker = pekoSeriesCheckerFactory(2))

    /**
     * 清一色
     */
    val Chinitsu = Yaku("Chinitsu", 6, 1, checker = itsuSeriesCheckerFactory(z = false))


    // ========== Yakuman ==========

    /**
     * 国士无双
     */
    val Kokushi = Yaku("Kokushi", 13, 13, true) { pattern ->
        pattern is KokushiHoraHandPattern && !pattern.thirteenWaiting
    }

    /**
     * 四暗刻
     */
    val Suanko = Yaku("Suanko", 13, 13, true, ankoSeriesCheckerFactory(4, tanki = false))

    /**
     * 大三元
     */
    val Daisangen = Yaku("Daisangen", 13, 0, true, sangenSeriesCheckerFactory(3, sangenJyantou = false))

    /**
     * 字一色
     */
    val Tsuiso = Yaku("Tsuiso", 13, 0, true) { pattern ->
        pattern.tiles.all { it.type == TileType.Z }
    }

    /**
     * 小四喜
     */
    val Shousushi = Yaku("Shousushi", 13, 0, true, sushiSeriesCheckerFactory(3, windJyantou = true))

    private val lyuTiles = Tile.parseTiles("23468s6z").toSet()

    /**
     * 绿一色
     */
    val Lyuiso = Yaku("Lyuiso", 13, 0, true) { pattern ->
        pattern.tiles.all { it in lyuTiles }
    }

    /**
     * 清老头
     */
    val Chinroto = Yaku("Chinroto", 13, 0, true, yaochuSeriesCheckerFactory(shuntsu = false, z = false))

    /**
     * 四杠子
     */
    val Sukantsu = Yaku("Sukantsu", 13, 0, true, kantsuSeriesCheckerFactory(4))

    /**
     * 九莲宝灯
     */
    val Churen = Yaku("Churen", 13, 13, true, churenSeriesCheckerFactory(nineWaiting = false))

    /**
     * 大四喜
     */
    val Daisushi = Yaku("Daisushi", 26, 0, true, sushiSeriesCheckerFactory(4, windJyantou = false))

    /**
     * 纯正九莲宝灯（九莲宝灯九面）
     */
    val ChurenNineWaiting = Yaku("ChurenNineWaiting", 26, 26, true, churenSeriesCheckerFactory(nineWaiting = true))

    /**
     * 四暗刻单骑
     */
    val SuankoTanki = Yaku("SuankoTanki", 26, 26, true, ankoSeriesCheckerFactory(4, true))

    /**
     * 国士无双十三面
     */
    val KokushiThirteenWaiting = Yaku("KokushiThirteenWaiting", 26, 26, true) { pattern ->
        pattern is KokushiHoraHandPattern && pattern.thirteenWaiting
    }


    // ========== Extra ==========

    private val extraYakuChecker = YakuChecker { false }

    /**
     * 立直
     */
    val Richi = Yaku("Richi", 1, 1, checker = extraYakuChecker)

    /**
     * 一发
     */
    val Ippatsu = Yaku("Ippatsu", 1, 1, checker = extraYakuChecker)

    /**
     * 岭上自摸
     */
    val Rinshan = Yaku("Rinshan", 1, 0, checker = extraYakuChecker)

    /**
     * 枪杠
     */
    val Chankan = Yaku("Chankan", 1, 0, checker = extraYakuChecker)

    /**
     * 海底捞月
     */
    val Haitei = Yaku("Haitei", 1, 0, checker = extraYakuChecker)

    /**
     * 河底捞鱼
     */
    val Houtei = Yaku("Houtei", 1, 0, checker = extraYakuChecker)

    /**
     * 两立直
     */
    val WRichi = Yaku("WRichi", 2, 2, checker = extraYakuChecker)

    /**
     * 天和
     */
    val Tenhou = Yaku("Tenhou", 13, 13, true, checker = extraYakuChecker)

    /**
     * 地和
     */
    val Chihou = Yaku("Chihou", 13, 13, true, checker = extraYakuChecker)


    // ========== Set ==========

    /**
     * 所有通常役种
     */
    val allCommonYaku = setOf(
        Tsumo, Pinhu, Tanyao, Ipe, SelfWind, RoundWind, Haku, Hatsu, Chun,
        Sanshoku, Ittsu, Chanta, Chitoi, Toitoi, Sananko, Honroto, Sandoko, Sankantsu, Shosangen,
        Honitsu, Junchan, Ryanpe, Chinitsu
    )

    /**
     * 所有役满役种
     */
    val allYakuman = setOf(
        Tenhou, Chihou,
        Kokushi, Suanko, Daisangen, Tsuiso, Shousushi, Lyuiso, Chinroto, Sukantsu, Churen,
        Daisushi, SuankoTanki, ChurenNineWaiting, KokushiThirteenWaiting
    )

    /**
     * 所有额外役种（与手牌无关的役种）
     */
    val allExtraYaku = setOf(
        Richi, Ippatsu, Rinshan, Chankan, Haitei, Houtei,
        WRichi,
        Tenhou, Chihou
    )

    /**
     * 所有额外役满（与手牌无关的役种）
     */
    val allExtraYakuman = setOf(
        Tenhou, Chihou
    )

    /**
     * 所有役种
     */
    val allYaku = allCommonYaku + allYakuman + allExtraYaku

    private val allYakuMapping = allYaku.associateBy { it.name }

    /**
     * 根据役种名获取役种
     * @param name 役种名
     * @return 役种
     */
    fun getYaku(name: String): Yaku {
        return allYakuMapping[name] ?: throw IllegalArgumentException("$name is not a yaku")
    }
}