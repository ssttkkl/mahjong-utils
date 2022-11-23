package mahjongutils.hanhu

import kotlinx.serialization.Serializable

private fun ceil100(x: Int): Int {
    return if (x % 100 > 0) {
        x + (100 - x % 100)
    } else {
        x
    }
}

private val noRon = setOf(
    Pair(1, 20), Pair(1, 25),
    Pair(2, 20),
    Pair(3, 20),
    Pair(4, 20)
)

private val noTsumo = setOf(
    Pair(1, 20), Pair(1, 25), Pair(1, 110),
    Pair(2, 25)
)

@Serializable
data class ParentPoint(
    val ron: Int,
    val tsumo: Int
)

@Serializable
data class ChildPoint(
    val ron: Int,
    val tsumoParent: Int,
    val tsumoChild: Int
)

private fun calcParentPoint(han: Int, hu: Int): ParentPoint? {
    var a = hu * (1 shl (han + 2))
    if (a > 2000) {
        a = 2000
    }

    var ron = ceil100(6 * a)
    var tsumo = ceil100(2 * a)

    if (Pair(han, hu) in noRon) {
        ron = 0
    }
    if (Pair(han, hu) in noTsumo) {
        tsumo = 0
    }

    if (ron == 0 && tsumo == 0) {
        return null
    }
    return ParentPoint(ron, tsumo)
}

private fun calcChildPoint(han: Int, hu: Int): ChildPoint? {
    var a = hu * (1 shl (han + 2))
    if (a > 2000) {
        a = 2000
    }

    var ron = ceil100(4 * a)
    var tsumoParent = ceil100(2 * a)
    var tsumoChild = ceil100(a)

    if (Pair(han, hu) in noRon) {
        ron = 0
    }
    if (Pair(han, hu) in noTsumo) {
        tsumoParent = 0
        tsumoChild = 0
    }

    if (ron == 0 && tsumoParent == 0) {
        return null
    }
    return ChildPoint(ron, tsumoParent, tsumoChild)
}

private val parentPointMapping = buildMap {
    for (han in 1..4) {
        for (hu in (20..110 step 10) + listOf(25)) {
            val point = calcParentPoint(han, hu)
            if (point != null) {
                this[han to hu] = point
            }
        }
    }

    this[5 to 20] = ParentPoint(12000, 4000)
    this[6 to 20] = ParentPoint(18000, 6000)
    this[7 to 20] = ParentPoint(18000, 6000)
    this[8 to 20] = ParentPoint(24000, 8000)
    this[9 to 20] = ParentPoint(24000, 8000)
    this[10 to 20] = ParentPoint(24000, 8000)
    this[11 to 20] = ParentPoint(36000, 12000)
    this[12 to 20] = ParentPoint(36000, 12000)
    this[13 to 20] = ParentPoint(48000, 16000)
}

private val childPointMapping = buildMap {
    for (han in 1..4) {
        for (hu in (20..110 step 10) + listOf(25)) {
            val point = calcChildPoint(han, hu)
            if (point != null) {
                this[han to hu] = point
            }
        }
    }

    this[5 to 20] = ChildPoint(8000, 4000, 2000)
    this[6 to 20] = ChildPoint(12000, 6000, 3000)
    this[7 to 20] = ChildPoint(12000, 6000, 3000)
    this[8 to 20] = ChildPoint(16000, 8000, 4000)
    this[9 to 20] = ChildPoint(16000, 8000, 4000)
    this[10 to 20] = ChildPoint(16000, 8000, 4000)
    this[11 to 20] = ChildPoint(24000, 12000, 6000)
    this[12 to 20] = ChildPoint(24000, 12000, 6000)
    this[13 to 20] = ChildPoint(32000, 16000, 8000)
}

fun getParentPointByHanHu(han: Int, hu: Int): ParentPoint {
    val han_ = if (han > 13) 13 else han
    val hu_ = if (han >= 5) 20 else hu
    return parentPointMapping[han_ to hu_] ?: throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")
}

fun getChildPointByHanHu(han: Int, hu: Int): ChildPoint {
    val han_ = if (han > 13) 13 else han
    val hu_ = if (han >= 5) 20 else hu
    return childPointMapping[han_ to hu_] ?: throw IllegalArgumentException("invalid arguments: han=${han}, hu=${hu}")
}