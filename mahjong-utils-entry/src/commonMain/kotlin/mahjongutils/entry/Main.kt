@file:JvmName("MainEntryBuilder")

package mahjongutils.entry

import mahjongutils.entry.coder.ParamsDecoder
import mahjongutils.entry.coder.ResultEncoder
import mahjongutils.entry.models.FuroChanceShantenArgs
import mahjongutils.entry.models.HanHu
import mahjongutils.entry.models.HoraArgs
import mahjongutils.entry.models.ShantenArgs
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.Hora
import mahjongutils.hora.hora
import mahjongutils.shanten.*
import kotlin.jvm.JvmName

internal fun <RAW_PARAMS : Any, RAW_RESULT : Any> buildEntry(
    paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    resultEncoder: ResultEncoder<RAW_RESULT>
): Entry<RAW_PARAMS, RAW_RESULT> {
    return Entry(paramsDecoder, resultEncoder).apply {
        register<ShantenArgs, UnionShantenResult>("shanten") { args ->
            shanten(args.tiles, args.furo, args.bestShantenOnly)
        }
        register<ShantenArgs, RegularShantenResult>("regularShanten") { args ->
            regularShanten(args.tiles, args.furo, args.bestShantenOnly)
        }
        register<ShantenArgs, ChitoiShantenResult>("chitoiShanten") { args ->
            chitoiShanten(args.tiles, args.bestShantenOnly)
        }
        register<ShantenArgs, KokushiShantenResult>("kokushiShanten") { args ->
            kokushiShanten(args.tiles, args.bestShantenOnly)
        }
        register<FuroChanceShantenArgs, FuroChanceShantenResult>("furoChanceShanten") { args ->
            furoChanceShanten(
                args.tiles,
                args.chanceTile,
                args.allowChi,
                args.bestShantenOnly,
                args.allowKuikae
            )
        }

        register<HanHu, ParentPoint>("getParentPointByHanHu") { args ->
            getParentPointByHanHu(args.han, args.hu, args.options)
        }
        register<HanHu, ChildPoint>("getChildPointByHanHu") { args ->
            getChildPointByHanHu(args.han, args.hu, args.options)
        }

        register<HoraArgs, Hora>("hora") { args ->
            if (args.shantenResult != null) {
                hora(
                    args.shantenResult, args.agari, args.tsumo,
                    args.dora, args.selfWind, args.roundWind, args.extraYaku,
                    args.options
                )
            } else if (args.tiles != null) {
                hora(
                    args.tiles, args.furo ?: emptyList(), args.agari, args.tsumo,
                    args.dora, args.selfWind, args.roundWind, args.extraYaku,
                    args.options
                )
            } else {
                throw IllegalArgumentException("either shantenResult or tiles/furo muse be set")
            }
        }
    }
}
