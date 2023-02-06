package mahjongutils

import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.Hora
import mahjongutils.hora.hora
import mahjongutils.shanten.*

internal fun <RAW_PARAMS : Any, RAW_RESULT : Any> buildEntry(
    paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    resultEncoder: ResultEncoder<RAW_RESULT>
): Entry<RAW_PARAMS, RAW_RESULT> {
    return Entry.Builder(paramsDecoder, resultEncoder).apply {
        register<ShantenArgs, ShantenResult>("shanten") { args ->
            shanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly, args.allowAnkan)
        }
        register<ShantenArgs, ShantenResult>("regularShanten") { args ->
            regularShanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly, args.allowAnkan)
        }
        register<ShantenArgs, ShantenResult>("chitoiShanten") { args ->
            chitoiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
        }
        register<ShantenArgs, ShantenResult>("kokushiShanten") { args ->
            kokushiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
        }
        register<FuroChanceShantenArgs, ShantenResult>("furoChanceShanten") { args ->
            furoChanceShanten(args.tiles, args.chanceTile, args.allowChi, args.calcAdvanceNum, args.bestShantenOnly)
        }

        register<HanHu, ParentPoint>("getParentPointByHanHu") { args ->
            getParentPointByHanHu(args.han, args.hu)
        }
        register<HanHu, ChildPoint>("getChildPointByHanHu") { args ->
            getChildPointByHanHu(args.han, args.hu)
        }

        register<HoraArgs, Hora>("hora") { args ->
            if (args.shantenResult != null) {
                hora(
                    args.shantenResult, args.agari, args.tsumo,
                    args.dora, args.selfWind, args.roundWind, args.extraYaku
                )
            } else if (args.tiles != null) {
                hora(
                    args.tiles, args.furo ?: emptyList(), args.agari, args.tsumo,
                    args.dora, args.selfWind, args.roundWind, args.extraYaku
                )
            } else {
                throw IllegalArgumentException("either shantenResult or tiles/furo muse be set")
            }
        }
    }.build()
}
