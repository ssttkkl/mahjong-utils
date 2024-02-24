package mahjongutils.hora.helpers

import mahjongutils.hora.HoraHandPattern
import mahjongutils.hora.HoraOptions
import mahjongutils.yaku.Yaku
import mahjongutils.yaku.Yakus

internal fun calcYaku(
    pattern: HoraHandPattern,
    options: HoraOptions,
    extraYaku: Set<Yaku>
) = buildSet {
    val yakus = Yakus(options)
    if (pattern.menzen) {
        addAll(yakus.allYakuman.filter { it.check(pattern) })
    } else {
        addAll(yakus.allYakuman.filter { !it.menzenOnly && it.check(pattern) })
    }
    addAll(extraYaku.filter { it.isYakuman })

    if (isEmpty()) {
        // 非役满情况才判断其他役种
        if (pattern.menzen) {
            addAll(yakus.allCommonYaku.filter { it.check(pattern) })
        } else {
            addAll(yakus.allCommonYaku.filter { !it.menzenOnly && it.check(pattern) })
        }
        addAll(extraYaku.filter { !it.isYakuman })
    }
}