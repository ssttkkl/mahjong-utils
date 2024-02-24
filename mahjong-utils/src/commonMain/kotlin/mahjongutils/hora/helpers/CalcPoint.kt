package mahjongutils.hora.helpers

import mahjongutils.hanhu.*

internal fun calcParentPoint(
    han: Int,
    hu: Int,
    options: HanHuOptions,
    tsumo: Boolean,
    hasYakuman: Boolean
): ParentPoint {
    return if (han == 0) {
        ParentPoint(0uL, 0uL)
    } else {
        val raw = if (hasYakuman && !options.aotenjou) {
            val oneTimeYakuman = getParentPointByHanHu(13, hu, options)
            val times = (han / 13).toULong()
            ParentPoint(oneTimeYakuman.ron * times, oneTimeYakuman.tsumo * times)
        } else {
            getParentPointByHanHu(
                han, hu, options
            )
        }

        if (tsumo) {
            ParentPoint(0uL, raw.tsumo)
        } else {
            ParentPoint(raw.ron, 0uL)
        }
    }
}

internal fun calcChildPoint(
    han: Int,
    hu: Int,
    options: HanHuOptions,
    tsumo: Boolean,
    hasYakuman: Boolean
): ChildPoint {
    return if (han == 0) {
        ChildPoint(0uL, 0uL, 0uL)
    } else {
        val raw = if (hasYakuman && !options.aotenjou) {
            val oneTimeYakuman = getChildPointByHanHu(13, hu, options)
            val times = (han / 13).toULong()
            ChildPoint(
                oneTimeYakuman.ron * times,
                oneTimeYakuman.tsumoParent * times,
                oneTimeYakuman.tsumoChild * times
            )
        } else {
            getChildPointByHanHu(
                han, hu, options
            )
        }

        if (tsumo) {
            ChildPoint(0uL, raw.tsumoParent, raw.tsumoChild)
        } else {
            ChildPoint(raw.ron, 0uL, 0uL)
        }
    }
}