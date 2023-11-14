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
    return EntryBuilder(paramsDecoder, resultEncoder).apply {
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
