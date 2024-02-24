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

    var ans = 0

    if (hasYakuman && !options.aotenjou && !options.hasComplexYakuman) {
        ans = if (pattern.menzen) {
            yaku.maxOf { it.han }
        } else {
            yaku.maxOf { it.han - it.furoLoss }
        }
    } else {
        ans = if (pattern.menzen) {
            yaku.sumOf { it.han }
        } else {
            yaku.sumOf { it.han - it.furoLoss }
        }

        if (ans > 0) {
            ans += dora
        }
    }

    return ans
}