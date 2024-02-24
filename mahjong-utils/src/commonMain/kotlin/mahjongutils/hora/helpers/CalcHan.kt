package mahjongutils.hora.helpers

import mahjongutils.hora.HoraHandPattern
import mahjongutils.hora.HoraOptions
import mahjongutils.yaku.Yaku

fun calcHan(
    pattern: HoraHandPattern,
    options: HoraOptions,
    yaku: Set<Yaku>,
    dora: Int
): Int {
    val hasYakuman: Boolean = yaku.any { it.isYakuman }
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

    return ans
}