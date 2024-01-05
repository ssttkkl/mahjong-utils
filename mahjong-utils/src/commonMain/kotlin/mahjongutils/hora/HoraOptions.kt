@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mahjongutils.hanhu.HanHuOptions

/**
 * 和牌分析选项
 */
@Serializable
data class HoraOptions(
    /**
     * 是否允许食断
     */
    @EncodeDefault
    val allowKuitan: Boolean,
    /**
     * 连风雀头是否记4符（true则记4符，false则记2符）
     */
    @EncodeDefault
    val hasRenpuuJyantouHu: Boolean,
    /**
     * 是否有切上满贯
     */
    @EncodeDefault
    val hasKiriageMangan: Boolean,
    /**
     * 是否有累计役满（false则除役满牌型外最高三倍满）
     */
    @EncodeDefault
    val hasKazoeYakuman: Boolean,
    /**
     * 是否有多倍役满（false则大四喜、国士无双十三面、纯正九莲宝灯只记单倍役满）
     */
    @EncodeDefault
    val hasMultipleYakuman: Boolean,
    /**
     * 是否有复合役满
     */
    @EncodeDefault
    val hasComplexYakuman: Boolean
) {
    val hanHuOptions: HanHuOptions
        get() = HanHuOptions(hasKiriageMangan, hasKazoeYakuman)

    companion object {
        val Default = HoraOptions(
            allowKuitan = true,
            hasRenpuuJyantouHu = true,
            hasKiriageMangan = false,
            hasKazoeYakuman = true,
            hasMultipleYakuman = true,
            hasComplexYakuman = true
        )
    }
}