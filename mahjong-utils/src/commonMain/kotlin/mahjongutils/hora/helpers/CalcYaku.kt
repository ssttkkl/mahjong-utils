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

    // 青天井规则：役满可以和非役满复合
    // 普通规则：非役满情况才判断其他役种
    if (options.aotenjou || isEmpty()) {
        if (pattern.menzen) {
            addAll(yakus.allCommonYaku.filter { it.check(pattern) })
        } else {
            addAll(yakus.allCommonYaku.filter { !it.menzenOnly && it.check(pattern) })
        }
        addAll(extraYaku.filter { !it.isYakuman })
    }
}