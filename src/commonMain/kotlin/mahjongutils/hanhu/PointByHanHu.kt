package mahjongutils.hanhu

import kotlinx.serialization.Serializable

private fun ceil100(x: Int): Int {
    return if (x % 100 > 0) {
        x + (100 - x % 100)
    } else {
        x
    }
}

// 理论最大符：荣和140符
// 234s11z 0110m 0110s 0990p 自风东 场风东
// 三杠子 三暗刻
// 底符20+门清荣和10+暗杠32*3+单骑2+连风雀头4=132
private const val MAX_HU = 140

/**
 * 亲家（庄家）和牌点数
 */
@Serializable
data class ParentPoint(
    /**
     * 荣和点数
     */
    val ron: Int,
    /**
     * 自摸各家点数
     */
    val tsumo: Int
) {
    val tsumoTotal: Int
        get() = tsumo * 3

    companion object {
        val Mangan = ParentPoint(12000, 4000)
        val Haneman = ParentPoint(18000, 6000)
        val Baiman = ParentPoint(24000, 8000)
        val Sanbaiman = ParentPoint(36000, 12000)
        val Yakuman = ParentPoint(48000, 16000)
    }
}

/**
 * 子家（闲家）和牌点数
 */
@Serializable
data class ChildPoint(
    /**
     * 荣和点数
     */
    val ron: Int,
    /**
     * 自摸亲家（庄家）点数
     */
    val tsumoParent: Int,
    /**
     * 自摸子家（闲家）点数
     */
    val tsumoChild: Int
) {
    val tsumoTotal: Int
        get() = tsumoParent + tsumoChild * 2

    companion object {
        val Mangan = ChildPoint(8000, 4000, 2000)
        val Haneman = ChildPoint(12000, 6000, 3000)
        val Baiman = ChildPoint(16000, 8000, 4000)
        val Sanbaiman = ChildPoint(24000, 12000, 6000)
        val Yakuman = ChildPoint(32000, 16000, 8000)
    }
}

private fun calcParentPoint(han: Int, hu: Int): ParentPoint {
    var a = hu * (1 shl (han + 2))
    if (a > 2000) {
        a = 2000
    }

    val ron = ceil100(6 * a)
    val tsumo = ceil100(2 * a)
    return ParentPoint(ron, tsumo)
}

private fun calcChildPoint(han: Int, hu: Int): ChildPoint {
    var a = hu * (1 shl (han + 2))
    if (a > 2000) {
        a = 2000
    }

    val ron = ceil100(4 * a)
    val tsumoParent = ceil100(2 * a)
    val tsumoChild = ceil100(a)
    return ChildPoint(ron, tsumoParent, tsumoChild)
}

private fun validateHanHu(han: Int, hu: Int) {
    if (han < 0)
        throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")

    if ((hu != 25 && hu % 10 != 0) || hu !in 20..MAX_HU) {
        throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")
    }
}

/**
 * 获取亲家（庄家）和牌点数
 *
 * @param han 番数
 * @param hu 符数
 * @return 亲家（庄家）和牌点数
 */
fun getParentPointByHanHu(han: Int, hu: Int): ParentPoint {
    validateHanHu(han, hu)

    return if (han >= 5) {
        when (han) {
            in 5 until 6 -> ParentPoint.Mangan
            in 6 until 8 -> ParentPoint.Haneman
            in 8 until 11 -> ParentPoint.Baiman
            in 11 until 13 -> ParentPoint.Sanbaiman
            else -> ParentPoint.Yakuman
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ParentPoint.Mangan
    } else {
        calcParentPoint(han, hu)
    }
}

/**
 * 获取子家（闲家）和牌点数
 *
 * @param han 番数
 * @param hu 符数
 * @return 子家（闲家）和牌点数
 */
fun getChildPointByHanHu(han: Int, hu: Int): ChildPoint {
    validateHanHu(han, hu)

    return if (han >= 5) {
        when (han) {
            in 5 until 6 -> ChildPoint.Mangan
            in 6 until 8 -> ChildPoint.Haneman
            in 8 until 11 -> ChildPoint.Baiman
            in 11 until 13 -> ChildPoint.Sanbaiman
            else -> ChildPoint.Yakuman
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ChildPoint.Mangan
    } else {
        calcChildPoint(han, hu)
    }
}