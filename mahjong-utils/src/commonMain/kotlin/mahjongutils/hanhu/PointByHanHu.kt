package mahjongutils.hanhu

private fun ceil100(x: ULong): ULong {
    return if (x % 100uL > 0uL) {
        x + (100uL - x % 100uL)
    } else {
        x
    }
}

// 理论最大符：荣和140符
// 234s11z 0110m 0110s 0990p 自风东 场风东
// 三杠子 三暗刻
// 底符20+门清荣和10+暗杠32*3+单骑2+连风雀头4=132
//private const val MAX_HU = 140


private fun calcParentPoint(han: Int, hu: Int, aotenjou: Boolean = false): ParentPoint {
    var a = hu.toULong() * (1uL shl (han + 2))
    if (a > 2000uL && !aotenjou) {
        a = 2000uL
    }

    val ron = ceil100(6uL * a)
    val tsumo = ceil100(2uL * a)
    return ParentPoint(ron, tsumo)
}

private fun calcChildPoint(han: Int, hu: Int, aotenjou: Boolean = false): ChildPoint {
    var a = hu.toULong() * (1uL shl (han + 2))
    if (a > 2000uL && !aotenjou) {
        a = 2000uL
    }

    val ron = ceil100(4uL * a)
    val tsumoParent = ceil100(2uL * a)
    val tsumoChild = ceil100(a)
    return ChildPoint(ron, tsumoParent, tsumoChild)
}

private fun validateHanHu(han: Int, hu: Int) {
    if (han < 0)
        throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")

    if (hu < 20 || hu != 25 && hu % 10 != 0) {
        throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")
    }
}

/**
 * 获取亲家（庄家）和牌点数
 *
 * @param han 番数
 * @param hu 符数
 * @param options 计算点数时应用的选项
 * @return 亲家（庄家）和牌点数
 */
fun getParentPointByHanHu(
    han: Int, hu: Int,
    options: HanHuOptions = HanHuOptions.Default
): ParentPoint {
    validateHanHu(han, hu)

    if (options.aotenjou) {
        return calcParentPoint(han, hu, true)
    }

    return if (han >= 5) {
        when (han) {
            in 5 until 6 -> ParentPoint.Mangan
            in 6 until 8 -> ParentPoint.Haneman
            in 8 until 11 -> ParentPoint.Baiman
            in 11 until 13 -> ParentPoint.Sanbaiman
            else -> if (options.hasKazoeYakuman) ParentPoint.Yakuman else ParentPoint.Sanbaiman
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ParentPoint.Mangan
    } else if (options.hasKiriageMangan && han == 4 && hu == 30) {
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
 * @param options 计算点数时应用的选项
 * @return 子家（闲家）和牌点数
 */
fun getChildPointByHanHu(
    han: Int, hu: Int,
    options: HanHuOptions = HanHuOptions.Default
): ChildPoint {
    validateHanHu(han, hu)

    if (options.aotenjou) {
        return calcChildPoint(han, hu, true)
    }

    return if (han >= 5) {
        when (han) {
            in 5 until 6 -> ChildPoint.Mangan
            in 6 until 8 -> ChildPoint.Haneman
            in 8 until 11 -> ChildPoint.Baiman
            in 11 until 13 -> ChildPoint.Sanbaiman
            else -> if (options.hasKazoeYakuman) ChildPoint.Yakuman else ChildPoint.Sanbaiman
        }
    } else if (han == 4 && hu >= 40 || han == 3 && hu >= 70) {
        ChildPoint.Mangan
    } else if (options.hasKiriageMangan && han == 4 && hu == 30) {
        ChildPoint.Mangan
    } else {
        calcChildPoint(han, hu)
    }
}