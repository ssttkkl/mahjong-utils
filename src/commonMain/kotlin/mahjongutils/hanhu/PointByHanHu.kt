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
            in 5 until 6 -> ParentPoint(12000, 4000)
            in 6 until 8 -> ParentPoint(18000, 6000)
            in 8 until 11 -> ParentPoint(24000, 8000)
            in 11 until 13 -> ParentPoint(36000, 12000)
            else -> ParentPoint(48000, 16000)
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ParentPoint(12000, 4000)
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
            in 5 until 6 -> ChildPoint(8000, 4000, 2000)
            in 6 until 8 -> ChildPoint(12000, 6000, 3000)
            in 8 until 11 -> ChildPoint(18000, 8000, 4000)
            in 11 until 13 -> ChildPoint(24000, 12000, 6000)
            else -> ChildPoint(32000, 16000, 8000)
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ChildPoint(8000, 4000, 2000)
    } else {
        calcChildPoint(han, hu)
    }
}