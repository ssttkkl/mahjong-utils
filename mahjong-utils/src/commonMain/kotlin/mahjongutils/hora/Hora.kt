package mahjongutils.hora

import mahjongutils.CalcContext
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_DORA
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_EXTRA_YAKU
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_FURO
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_OPTIONS
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_ROUND_WIND
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_SELF_WIND
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.*
import mahjongutils.yaku.Yaku

/**
 * 和牌分析
 *
 * @param tiles 门前的牌
 * @param furo 副露
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种
 * @return 和牌分析结果
 */
fun hora(
    tiles: List<Tile>,
    furo: List<Furo> = DEFAULT_FURO,
    agari: Tile,
    tsumo: Boolean,
    dora: Int = DEFAULT_DORA,
    selfWind: Wind? = DEFAULT_SELF_WIND,
    roundWind: Wind? = DEFAULT_ROUND_WIND,
    extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
    options: HoraOptions = DEFAULT_OPTIONS,
): Hora {
    val context = CalcContext()
    return context.hora(
        HoraArgsWithTiles(
            tiles = tiles,
            furo = furo,
            agari = agari,
            tsumo = tsumo,
            dora = dora,
            selfWind = selfWind,
            roundWind = roundWind,
            extraYaku = extraYaku,
            options = options
        )
    )
}


/**
 * 和牌分析
 *
 * @param shantenResult 向听分析结果
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种（不会对役种合法性进行检查）
 * @return 和牌分析结果
 */
fun hora(
    shantenResult: CommonShantenResult<*>,
    agari: Tile,
    tsumo: Boolean,
    dora: Int = DEFAULT_DORA,
    selfWind: Wind? = DEFAULT_SELF_WIND,
    roundWind: Wind? = DEFAULT_ROUND_WIND,
    extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
    options: HoraOptions = DEFAULT_OPTIONS,
): Hora {
    val context = CalcContext()
    return context.hora(
        HoraArgsWithShantenResult(
            shantenResult = shantenResult,
            agari = agari,
            tsumo = tsumo,
            dora = dora,
            selfWind = selfWind,
            roundWind = roundWind,
            extraYaku = extraYaku,
            options = options
        )
    )
}

internal fun CalcContext.hora(
    args: HoraArgsWithTiles
): Hora = memo(Pair("hora", args)) {
    with(args) {
        val tiles = if (tiles.size + furo.size * 3 == 13) {
            tiles + agari
        } else {
            tiles
        }

        require(tiles.size + furo.size * 3 == 14) { "invalid length of tiles" }
        require(agari in tiles) { "agari not in tiles" }

        val shantenResult = shanten(
            InternalShantenArgs(
                tiles,
                furo,
                calcAdvanceNum = false,
                bestShantenOnly = true,
                allowAnkan = false
            )
        )
        return hora(
            HoraArgsWithShantenResult(
                shantenResult = shantenResult,
                agari = agari,
                tsumo = tsumo,
                dora = dora,
                selfWind = selfWind,
                roundWind = roundWind,
                extraYaku = extraYaku,
                options = options
            )
        )
    }
}

internal fun CalcContext.hora(
    args: HoraArgsWithShantenResult
): Hora = memo(Pair("hora", args)) {
    with(args) {
        require(shantenResult.shantenInfo is ShantenWithGot) { "shantenResult is not with got" }
        require(shantenResult.shantenInfo.shantenNum == -1) { "shantenResult is not hora yet" }
        require(agari in shantenResult.hand.tiles) { "agari not in tiles" }
        require(shantenResult.hand.tiles.size + shantenResult.hand.furo.size * 3 == 14) { "invalid length of tiles" }

        val patterns = buildList {
            when (shantenResult) {
                is UnionShantenResult -> {
                    if (shantenResult.regular.shantenInfo.shantenNum == -1) {
                        addAll(shantenResult.regular.hand.patterns)
                    }
                    if (shantenResult.chitoi?.shantenInfo?.shantenNum == -1) {
                        addAll(shantenResult.chitoi.hand.patterns)
                    }
                    if (shantenResult.kokushi?.shantenInfo?.shantenNum == -1) {
                        addAll(shantenResult.kokushi.hand.patterns)
                    }
                }

                else -> {
                    if (shantenResult.shantenInfo.shantenNum == -1) {
                        addAll(shantenResult.hand.patterns)
                    }
                }
            }
        }

        val possibleHora = patterns.map { pat ->
            HoraHandPattern.build(pat, agari, tsumo, selfWind, roundWind).map { horaHandPat ->
                Hora(horaHandPat, dora, extraYaku, options)
            }
        }.flatten()
        val hora = possibleHora.maxBy {
            it.han * 1000 + it.hu  // first key: han, second key: hu
        }
        return hora
    }
}