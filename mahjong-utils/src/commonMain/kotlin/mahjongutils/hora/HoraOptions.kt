package mahjongutils.hora

import kotlinx.serialization.Serializable
import mahjongutils.hanhu.HanHuOptions

/**
 * 和牌分析选项
 */
@Serializable
data class HoraOptions(
    /**
     * 是否为青天井规则
     */
    val aotenjou: Boolean,
    /**
     * 是否允许食断
     */
    val allowKuitan: Boolean,
    /**
     * 连风雀头是否记4符（true则记4符，false则记2符）
     */
    val hasRenpuuJyantouHu: Boolean,
    /**
     * 是否有切上满贯
     */
    val hasKiriageMangan: Boolean,
    /**
     * 是否有累计役满（false则除役满牌型外最高三倍满）
     */
    val hasKazoeYakuman: Boolean,
    /**
     * 是否有多倍役满（false则大四喜、国士无双十三面、纯正九莲宝灯只记单倍役满）
     */
    val hasMultipleYakuman: Boolean,
    /**
     * 是否有复合役满
     */
    val hasComplexYakuman: Boolean
) {
    val hanHuOptions: HanHuOptions
        get() = HanHuOptions(
            hasKiriageMangan = hasKiriageMangan,
            hasKazoeYakuman = hasKazoeYakuman,
            aotenjou = aotenjou
        )

    companion object {
        val Default = HoraOptions(
            aotenjou = false,
            allowKuitan = true,
            hasRenpuuJyantouHu = true,
            hasKiriageMangan = false,
            hasKazoeYakuman = true,
            hasMultipleYakuman = true,
            hasComplexYakuman = true
        )
    }
}