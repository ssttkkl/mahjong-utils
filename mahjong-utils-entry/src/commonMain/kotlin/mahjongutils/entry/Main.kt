@file:JvmName("MainEntryBuilder")

package mahjongutils.entry

import mahjongutils.entry.coder.ParamsDecoder
import mahjongutils.entry.coder.ResultEncoder
import mahjongutils.entry.models.HanHu
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.Hora
import mahjongutils.hora.HoraArgs
import mahjongutils.hora.hora
import mahjongutils.shanten.*
import kotlin.jvm.JvmName

internal fun <RAW_PARAMS : Any, RAW_RESULT : Any> buildEntry(
    paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    resultEncoder: ResultEncoder<RAW_RESULT>
): Entry<RAW_PARAMS, RAW_RESULT> {
    return Entry(paramsDecoder, resultEncoder).apply {
        register<CommonShantenArgs, UnionShantenResult>("shanten") { args ->
            shanten(args)
        }
        register<CommonShantenArgs, RegularShantenResult>("regularShanten") { args ->
            regularShanten(args)
        }
        register<CommonShantenArgs, ChitoiShantenResult>("chitoiShanten") { args ->
            chitoiShanten(args)
        }
        register<CommonShantenArgs, KokushiShantenResult>("kokushiShanten") { args ->
            kokushiShanten(args)
        }
        register<FuroChanceShantenArgs, FuroChanceShantenResult>("furoChanceShanten") { args ->
            furoChanceShanten(args)
        }

        register<HanHu, ParentPoint>("getParentPointByHanHu") { args ->
            getParentPointByHanHu(args.han, args.hu, args.options)
        }
        register<HanHu, ChildPoint>("getChildPointByHanHu") { args ->
            getChildPointByHanHu(args.han, args.hu, args.options)
        }

        register<HoraArgs, Hora>("hora") { args ->
            hora(args)
        }
    }
}
