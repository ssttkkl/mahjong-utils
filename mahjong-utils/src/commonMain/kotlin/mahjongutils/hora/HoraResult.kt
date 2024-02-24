package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hora.helpers.*
import mahjongutils.yaku.DefaultYakuSerializer
import mahjongutils.yaku.Yaku


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
    val yaku: Set<@Serializable(DefaultYakuSerializer::class) Yaku> = calcYaku(pattern, options, extraYaku)

    /**
     * 是否含役满役种
     */
    @EncodeDefault
    val hasYakuman: Boolean = yaku.any { it.isYakuman }

    /**
     * 番
     */
    @EncodeDefault
    val han: Int = calcHan(pattern, options, yaku, dora)

    @EncodeDefault
    val hu: Int = calcHu(pattern, options.hasRenpuuJyantouHu)

    /**
     * 亲家（庄家）和牌点数
     */
    @EncodeDefault
    val parentPoint: ParentPoint = calcParentPoint(han, hu, options.hanHuOptions, tsumo, hasYakuman)

    /**
     * 子家（闲家）和牌点数
     */
    @EncodeDefault
    val childPoint: ChildPoint = calcChildPoint(han, hu, options.hanHuOptions, tsumo, hasYakuman)
}
