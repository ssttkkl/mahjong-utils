package mahjongutils.yaku

import mahjongutils.models.*
import mahjongutils.hora.ChitoiHoraHandPattern
import mahjongutils.hora.RegularHoraHandPattern

/**
 * 役牌系列checker工厂
 *
 * @param tile: 役牌
 */
internal fun yakuhaiCheckerFactory(
    tile: Tile? = null,
    tileFunc: ((RegularHoraHandPattern) -> Tile?)? = null
): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        (tile ?: tileFunc?.invoke(pattern))?.let { tile_ ->
            pattern.mentsu.filterIsInstance<Kotsu>().any { it.tile == tile_ }
        } ?: false
    }
}

/**
 * 幺九系列checker工厂（混全、混老头、纯全、清老头）
 *
 * @param shuntsu: true 必须含顺子，false 必须不含顺子
 * @param z: true 必须含字牌，false 必须不含字牌
 * @return: checker
 */
internal fun yaochuSeriesCheckerFactory(shuntsu: Boolean, z: Boolean): YakuChecker {
    return YakuChecker { pattern ->
        when (pattern) {
            is RegularHoraHandPattern -> {
                if (!pattern.jyantou.isYaochu) {
                    return@YakuChecker false
                }

                var shuntsuCnt = 0
                var kotsuCnt = 0
                var hasZ = pattern.jyantou.type == TileType.Z

                for (mt in pattern.mentsu) {
                    when (mt) {
                        is Shuntsu -> {
                            if (mt.tile.num != 1 && mt.tile.num != 7) {
                                return@YakuChecker false
                            }
                            shuntsuCnt += 1
                        }

                        is Kotsu -> {
                            if (!mt.tile.isYaochu) {
                                return@YakuChecker false
                            }
                            kotsuCnt += 1
                            if (mt.tile.type == TileType.Z) {
                                hasZ = true
                            }
                        }
                    }
                }

                (shuntsu && shuntsuCnt != 0 || !shuntsu && shuntsuCnt == 0) && (z && hasZ || !z && !hasZ)
            }

            is ChitoiHoraHandPattern -> {
                if (shuntsu) {
                    return@YakuChecker false
                }

                if (pattern.tiles.any { !it.isYaochu }) {
                    return@YakuChecker false
                }

                val hasZ = pattern.tiles.any { it.type == TileType.Z }
                (z && hasZ || !z && !hasZ)
            }

            else -> {
                false
            }
        }
    }
}

/**
 * 一色系列checker工厂（混一色、清一色）
 *
 * @param z: true 必须含字牌，false 必须不含字牌
 * @return: checker
 */
internal fun itsuSeriesCheckerFactory(z: Boolean): YakuChecker {
    return YakuChecker { pattern ->
        val cnt = IntArray(4)
        for (t in pattern.tiles) {
            cnt[t.type.ordinal] += 1
        }

        var zeroCnt = 0
        repeat(3) {
            if (cnt[it] == 0) {
                zeroCnt += 1
            }
        }

        zeroCnt == 2 && (z && cnt[3] != 0 || !z && cnt[3] == 0)
    }
}

/**
 * 杯口系列checker工厂（一杯口、两杯口）
 *
 * @param pekoCount: x杯口
 * @return: checker
 */
internal fun pekoSeriesCheckerFactory(pekoCount: Int): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern || !pattern.menzen) {
            return@YakuChecker false
        }

        var cnt = 0
        val shuntsu = pattern.menzenMentsu.filterIsInstance<Shuntsu>()
        for (i in shuntsu.indices) {
            for (j in i + 1 until shuntsu.size) {
                if (shuntsu[i] == shuntsu[j]) {
                    cnt += 1
                }
            }
        }

        cnt == pekoCount
    }
}

/**
 * 暗刻系列checker工厂（三暗刻、四暗刻、四暗刻单骑）
 *
 * @param ankoCount: x暗刻
 * @param tanki: true 必须单骑，false 必须非单骑，null 无所谓
 * @return: checker
 */
internal fun ankoSeriesCheckerFactory(ankoCount: Int, tanki: Boolean? = null): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        var anko = pattern.anko.count()
        // 双碰听牌荣和，算一个明刻
        if (pattern.agariTatsu is Toitsu && !pattern.tsumo) {
            anko -= 1
        }

        when (tanki) {
            null -> anko == ankoCount
            true -> anko == ankoCount && pattern.agariTatsu == null
            false -> anko == ankoCount && pattern.agariTatsu != null
        }
    }
}

/**
 * 杠子系列checker工厂（三杠子、四杠子）
 *
 * @param kanCount: x杠子
 * @return: checker
 */
internal fun kantsuSeriesCheckerFactory(kanCount: Int): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        val kan = pattern.furo.count { it is Kan }
        kan == kanCount
    }
}

/**
 * 三元系列checker工厂（小三元、大三元）
 *
 * @param sangenKotsuCount: 三元牌刻子数
 * @param sangenJyantou: true 必须含有三元牌雀头，false 必须不含有三元牌雀头
 * @return: checker
 */
internal fun sangenSeriesCheckerFactory(sangenKotsuCount: Int, sangenJyantou: Boolean): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        val sangenKotsu = pattern.mentsu.count { it is Kotsu && it.tile.isSangen }
        sangenKotsu == sangenKotsuCount && (sangenJyantou && pattern.jyantou.isSangen || !sangenJyantou && !pattern.jyantou.isSangen)
    }
}

/**
 * 四喜系列checker工厂（小四喜、大四喜）
 *
 * @param windKotsuCount: 风牌刻子数
 * @param windJyantou: true 必须含有风牌雀头，false 必须不含有风牌雀头
 * @return: checker
 */
internal fun sushiSeriesCheckerFactory(windKotsuCount: Int, windJyantou: Boolean): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        val sangenKotsu = pattern.mentsu.count { it is Kotsu && it.tile.isWind }
        sangenKotsu == windKotsuCount && (windJyantou && pattern.jyantou.isWind || !windJyantou && !pattern.jyantou.isWind)
    }
}

/**
 * 九莲系列checker工厂（九莲、纯九）
 *
 * @param nine_waiting: 是否纯九
 * @return: checker
 */
internal fun churenSeriesCheckerFactory(nineWaiting: Boolean): YakuChecker {
    return YakuChecker { pattern ->
        if (pattern !is RegularHoraHandPattern) {
            return@YakuChecker false
        }

        val typeFound = BooleanArray(4)
        val numCnt = IntArray(9)

        for (t in pattern.tiles) {
            typeFound[t.type.ordinal] = true
            numCnt[t.realNum - 1] += 1
        }

        val typeCnt = typeFound.count { it }
        if (typeCnt != 1 || typeFound[3]) {
            return@YakuChecker false
        }

        numCnt[0] -= 2
        numCnt[8] -= 2

        repeat(9) { i ->
            numCnt[i] -= 1
            if (numCnt[i] < 0) {
                return@YakuChecker false
            }
        }

        val even = numCnt.indexOf(1)
        nineWaiting && pattern.agari.num == even + 1 || !nineWaiting && pattern.agari.num != even + 1
    }
}