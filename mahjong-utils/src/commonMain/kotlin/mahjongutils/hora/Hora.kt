@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSerializationApi::class)

package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.shanten.ShantenWithGot
import mahjongutils.shanten.UnionShantenResult
import mahjongutils.shanten.shanten
import mahjongutils.yaku.DefaultYakuSerializer
import mahjongutils.yaku.Yaku
import mahjongutils.yaku.Yakus

/**
 * 和牌分析结果
 */
@Serializable
data class Hora internal constructor(
    /**
     * 和牌形
     */
    val pattern: HoraHandPattern,
    /**
     * 宝牌数目
     */
    val dora: Int,
    /**
     * 额外役种
     */
    val extraYaku: Set<@Serializable(DefaultYakuSerializer::class) Yaku>,
    /**
     * 和牌规则选项
     */
    val options: HoraOptions,
) : HoraInfo by pattern {
    init {
        if (dora < 0) {
            throw IllegalArgumentException("dora cannot be less than 0")
        }
    }

    /**
     * 役种
     */
    @EncodeDefault
    val yaku: Set<@Serializable(DefaultYakuSerializer::class) Yaku> = buildSet {
        val yakus = Yakus(options)
        if (pattern.menzen) {
            addAll(yakus.allYakuman.filter { it.check(pattern) })
        } else {
            addAll(yakus.allYakuman.filter { !it.menzenOnly && it.check(pattern) })
        }
        addAll(extraYaku.filter { it.isYakuman })

        if (isEmpty()) {
            // 非役满情况才判断其他役种
            if (pattern.menzen) {
                addAll(yakus.allCommonYaku.filter { it.check(pattern) })
            } else {
                addAll(yakus.allCommonYaku.filter { !it.menzenOnly && it.check(pattern) })
            }
            addAll(extraYaku.filter { !it.isYakuman })
        }
    }

    /**
     * 是否含役满役种
     */
    @EncodeDefault
    val hasYakuman: Boolean = yaku.any { it.isYakuman }

    /**
     * 番
     */
    @EncodeDefault
    val han: Int = run {
        var ans = if (hasYakuman && !options.hasComplexYakuman) {
            if (pattern.menzen) {
                yaku.maxOf { it.han }
            } else {
                yaku.maxOf { it.han - it.furoLoss }
            }
        } else {
            if (pattern.menzen) {
                yaku.sumOf { it.han }
            } else {
                yaku.sumOf { it.han - it.furoLoss }
            }
        }

        if (ans > 0 && !hasYakuman) {
            ans += dora
        }

        ans
    }

    @EncodeDefault
    val hu: Int = when (pattern) {
        is RegularHoraHandPattern -> pattern.calcHu(options.hasRenpuuJyantouHu)
        is ChitoiHoraHandPattern -> 25
        is KokushiHoraHandPattern -> 30
    }


    /**
     * 亲家（庄家）和牌点数
     */
    @EncodeDefault
    val parentPoint: ParentPoint = run {
        if (han == 0) {
            ParentPoint(0, 0)
        } else {
            val raw = if (hasYakuman) {
                val oneTimeYakuman = getParentPointByHanHu(13, hu, options.hanHuOptions)
                val times = han / 13
                ParentPoint(oneTimeYakuman.ron * times, oneTimeYakuman.tsumo * times)
            } else {
                getParentPointByHanHu(
                    han, hu, options.hanHuOptions
                )
            }

            if (tsumo) {
                ParentPoint(0, raw.tsumo)
            } else {
                ParentPoint(raw.ron, 0)
            }
        }
    }

    /**
     * 子家（闲家）和牌点数
     */
    @EncodeDefault
    val childPoint: ChildPoint = run {
        if (han == 0) {
            ChildPoint(0, 0, 0)
        } else {
            val raw = if (hasYakuman) {
                val oneTimeYakuman = getChildPointByHanHu(13, hu, options.hanHuOptions)
                val times = han / 13
                ChildPoint(
                    oneTimeYakuman.ron * times,
                    oneTimeYakuman.tsumoParent * times,
                    oneTimeYakuman.tsumoChild * times
                )
            } else {
                getChildPointByHanHu(
                    han, hu, options.hanHuOptions
                )
            }

            if (tsumo) {
                ChildPoint(0, raw.tsumoParent, raw.tsumoChild)
            } else {
                ChildPoint(raw.ron, 0, 0)
            }
        }
    }
}

/**
 * 和牌分析
 *
 * @param tiles 门前的牌
 * @param furo 副露
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种
 * @return 和牌分析结果
 */
fun hora(
    tiles: List<Tile>, furo: List<Furo> = emptyList(), agari: Tile, tsumo: Boolean,
    dora: Int = 0, selfWind: Wind? = null, roundWind: Wind? = null, extraYaku: Set<Yaku> = emptySet(),
    options: HoraOptions = HoraOptions.Default,
): Hora {
    val tiles = if (tiles.size + furo.size * 3 == 13) {
        tiles + agari
    } else {
        tiles
    }

    require(tiles.size + furo.size * 3 == 14) { "invalid length of tiles" }
    require(agari in tiles) { "agari not in tiles" }

    val shantenResult = shanten(tiles, furo, calcAdvanceNum = false, bestShantenOnly = true, allowAnkan = false)
    return hora(shantenResult, agari, tsumo, dora, selfWind, roundWind, extraYaku, options)

}

/**
 * 和牌分析
 *
 * @param shantenResult 向听分析结果
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种（不会对役种合法性进行检查）
 * @return 和牌分析结果
 */
fun hora(
    shantenResult: CommonShantenResult<*>, agari: Tile, tsumo: Boolean,
    dora: Int = 0, selfWind: Wind? = null, roundWind: Wind? = null, extraYaku: Set<Yaku> = emptySet(),
    options: HoraOptions = HoraOptions.Default,
): Hora {
    require(shantenResult.shantenInfo is ShantenWithGot) { "shantenResult is not with got" }
    require(shantenResult.shantenInfo.shantenNum == -1) { "shantenResult is not hora yet" }
    require(agari in shantenResult.hand.tiles) { "agari not in tiles" }
    require(shantenResult.hand.tiles.size + shantenResult.hand.furo.size * 3 == 14) { "invalid length of tiles" }

    val patterns = buildList {
        when (shantenResult) {
            is UnionShantenResult -> {
                if (shantenResult.regular.shantenInfo.shantenNum == -1) {
                    addAll(shantenResult.regular.hand.patterns)
                }
                if (shantenResult.chitoi?.shantenInfo?.shantenNum == -1) {
                    addAll(shantenResult.chitoi.hand.patterns)
                }
                if (shantenResult.kokushi?.shantenInfo?.shantenNum == -1) {
                    addAll(shantenResult.kokushi.hand.patterns)
                }
            }

            else -> {
                if (shantenResult.shantenInfo.shantenNum == -1) {
                    addAll(shantenResult.hand.patterns)
                }
            }
        }
    }

    val possibleHora = patterns.map { pat ->
        HoraHandPattern.build(pat, agari, tsumo, selfWind, roundWind).map { horaHandPat ->
            Hora(horaHandPat, dora, extraYaku, options)
        }
    }.flatten()
    val hora = possibleHora.maxBy {
        it.han * 1000 + it.hu  // first key: han, second key: hu
    }
    return hora
}