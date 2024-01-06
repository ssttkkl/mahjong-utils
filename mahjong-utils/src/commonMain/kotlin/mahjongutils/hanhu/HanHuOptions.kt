package mahjongutils.hanhu

import kotlinx.serialization.Serializable

@Serializable
data class HanHuOptions(
    /**
     * 是否为青天井规则
     */
    val aotenjou: Boolean = false,
    /**
     * 是否有切上满贯
     */
    val hasKiriageMangan: Boolean = false,
    /**
     * 是否有累计役满
     */
    val hasKazoeYakuman: Boolean = true
) {
    companion object {
        val Default = HanHuOptions(hasKiriageMangan = false, hasKazoeYakuman = true)
    }
}