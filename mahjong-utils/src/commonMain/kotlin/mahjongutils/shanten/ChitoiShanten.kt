package mahjongutils.shanten

import mahjongutils.CalcContext
import mahjongutils.models.Tile
import mahjongutils.models.countAsMap
import mahjongutils.models.hand.ChitoiHandPattern
import mahjongutils.models.hand.Hand
import mahjongutils.shanten.helpers.fillNum
import mahjongutils.shanten.helpers.getTileCount
import mahjongutils.shanten.helpers.normalizeTiles

/**
 * 七对子向听分析
 * @param tiles 门前的牌
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun chitoiShanten(
    tiles: List<Tile>,
    bestShantenOnly: Boolean = false,
): ChitoiShantenResult {
    return chitoiShanten(CommonShantenArgs(tiles = tiles, bestShantenOnly = bestShantenOnly))
}

/**
 * 七对子向听分析
 * @param args 向听分析参数
 * @return 向听分析结果
 */
fun chitoiShanten(
    args: CommonShantenArgs
): ChitoiShantenResult {
    args.throwOnValidationError()

    val internalShantenArgs = InternalShantenArgs(
        tiles = args.tiles,
        furo = args.furo,
        bestShantenOnly = args.bestShantenOnly
    )

    val context = CalcContext()
    return context.chitoiShanten(internalShantenArgs)
}

internal fun CalcContext.chitoiShanten(
    args: InternalShantenArgs
): ChitoiShantenResult = memo(Pair("chitoiShanten", args)) {
    with(args) {
        val tiles = normalizeTiles(tiles)

        var (shantenInfo, pattern) = if (tiles.size == 13) {
            handleChitoiShantenWithoutGot(tiles)
        } else {
            handleChitoiShantenWithGot(tiles, bestShantenOnly)
        }

        if (calcAdvanceNum) {
            shantenInfo = shantenInfo.fillNum(getTileCount(tiles))
        }

        val hand = Hand(tiles = tiles, furo = emptyList(), patterns = listOf(pattern))
        return ChitoiShantenResult(hand = hand, shantenInfo = shantenInfo)
    }
}


private fun buildChitoiPattern(tiles: List<Tile>): ChitoiHandPattern {
    val cnt = tiles.countAsMap()

    val pairs = ArrayList<Tile>()
    val remaining = ArrayList<Tile>()

    for ((t, t_cnt) in cnt) {
        if (t_cnt >= 2) {
            pairs.add(t)
            repeat(t_cnt - 2) {
                remaining.add(t)
            }
        } else if (t_cnt == 1) {
            remaining.add(t)
        }
    }

    return ChitoiHandPattern(pairs.toSet(), remaining)
}

private fun handleChitoiShantenWithoutGot(tiles: List<Tile>): Pair<ShantenWithoutGot, ChitoiHandPattern> {
    val pattern = buildChitoiPattern(tiles)

    val shantenNum: Int
    val advance: Set<Tile>

    val tileSet = buildSet {
        addAll(pattern.pairs)
        addAll(pattern.remaining)
    }
    if (tileSet.size >= 7) {
        shantenNum = 6 - pattern.pairs.size
        advance = (pattern.remaining - pattern.pairs).toSet()
    } else {
        shantenNum = 6 - pattern.pairs.size + (7 - tileSet.size)
        advance = (Tile.allExcludeAkaDora - pattern.pairs).toSet()
    }

    val shantenInfo = ShantenWithoutGot(
        shantenNum = shantenNum,
        advance = advance.toSet()
    )
    return Pair(shantenInfo, pattern)
}

private fun handleChitoiShantenWithGot(
    tiles: List<Tile>,
    bestShantenOnly: Boolean = false
): Pair<ShantenWithGot, ChitoiHandPattern> {
    val pattern = buildChitoiPattern(tiles)

    val tileSet = buildSet {
        addAll(pattern.pairs)
        addAll(pattern.remaining)
    }
    val shantenNum = if (tileSet.size >= 7) {
        6 - pattern.pairs.size
    } else {
        6 - pattern.pairs.size + (7 - tileSet.size)
    }

    val discardToAdvance = buildMap {
        for (t in tiles.toSet()) {
            val idx = tiles.indexOf(t)
            val shantenAfterDiscard = handleChitoiShantenWithoutGot(
                tiles.slice(0 until idx) + tiles.slice(idx + 1 until tiles.size)
            ).first

            if (!bestShantenOnly || shantenAfterDiscard.shantenNum == shantenNum) {
                this[t] = shantenAfterDiscard
            }
        }
    }
    val shantenInfo = ShantenWithGot(
        shantenNum = shantenNum,
        discardToAdvance = discardToAdvance
    )
    return Pair(shantenInfo, pattern)
}
