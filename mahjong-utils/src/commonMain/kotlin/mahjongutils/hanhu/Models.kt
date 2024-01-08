package mahjongutils.hanhu

import kotlinx.serialization.Serializable


interface Point {
    /**
     * 荣和点数
     */
    val ron: ULong

    /**
     * 自摸总点数
     */
    val tsumoTotal: ULong
}

/**
 * 亲家（庄家）和牌点数
 */
@Serializable
data class ParentPoint(
    override val ron: ULong,
    /**
     * 自摸各家点数
     */
    val tsumo: ULong
) : Point {
    override val tsumoTotal: ULong
        get() = tsumo * 3uL

    companion object {
        val Mangan = ParentPoint(12000uL, 4000uL)
        val Haneman = ParentPoint(18000uL, 6000uL)
        val Baiman = ParentPoint(24000uL, 8000uL)
        val Sanbaiman = ParentPoint(36000uL, 12000uL)
        val Yakuman = ParentPoint(48000uL, 16000uL)
    }
}

/**
 * 子家（闲家）和牌点数
 */
@Serializable
data class ChildPoint(
    override val ron: ULong,
    /**
     * 自摸亲家（庄家）点数
     */
    val tsumoParent: ULong,
    /**
     * 自摸子家（闲家）点数
     */
    val tsumoChild: ULong
) : Point {
    override val tsumoTotal: ULong
        get() = tsumoParent + tsumoChild * 2uL

    companion object {
        val Mangan = ChildPoint(8000uL, 4000uL, 2000uL)
        val Haneman = ChildPoint(12000uL, 6000uL, 3000uL)
        val Baiman = ChildPoint(16000uL, 8000uL, 4000uL)
        val Sanbaiman = ChildPoint(24000uL, 12000uL, 6000uL)
        val Yakuman = ChildPoint(32000uL, 16000uL, 8000uL)
    }
}