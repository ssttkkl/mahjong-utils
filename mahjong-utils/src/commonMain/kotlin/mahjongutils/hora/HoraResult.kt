package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.helpers.calcHu
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
            ParentPoint(0uL, 0uL)
        } else {
            val raw = if (hasYakuman) {
                val oneTimeYakuman = getParentPointByHanHu(13, hu, options.hanHuOptions)
                val times = (han / 13).toULong()
                ParentPoint(oneTimeYakuman.ron * times, oneTimeYakuman.tsumo * times)
            } else {
                getParentPointByHanHu(
                    han, hu, options.hanHuOptions
                )
            }

            if (tsumo) {
                ParentPoint(0uL, raw.tsumo)
            } else {
                ParentPoint(raw.ron, 0uL)
            }
        }
    }

    /**
     * 子家（闲家）和牌点数
     */
    @EncodeDefault
    val childPoint: ChildPoint = run {
        if (han == 0) {
            ChildPoint(0uL, 0uL, 0uL)
        } else {
            val raw = if (hasYakuman) {
                val oneTimeYakuman = getChildPointByHanHu(13, hu, options.hanHuOptions)
                val times = (han / 13).toULong()
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
                ChildPoint(0uL, raw.tsumoParent, raw.tsumoChild)
            } else {
                ChildPoint(raw.ron, 0uL, 0uL)
            }
        }
    }
}
