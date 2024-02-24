package mahjongutils.shanten

import mahjongutils.CalcContext
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.KokushiHandPattern
import mahjongutils.models.isYaochu
import mahjongutils.shanten.helpers.calcShanten
import mahjongutils.shanten.helpers.fillNum
import mahjongutils.shanten.helpers.normalizeTiles
import mahjongutils.shanten.helpers.selectBestPatterns

/**
 * 国士无双向听分析
 * @param tiles 门前的牌
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun kokushiShanten(
    tiles: List<Tile>,
    bestShantenOnly: Boolean = false,
): KokushiShantenResult {
    return kokushiShanten(CommonShantenArgs(tiles = tiles, bestShantenOnly = bestShantenOnly))
}

/**
 * 国士无双向听分析
 * @param args 向听分析参数
 * @return 向听分析结果
 */
fun kokushiShanten(
    args: CommonShantenArgs
): KokushiShantenResult {
    args.throwOnValidationError()

    val internalShantenArgs = InternalShantenArgs(
        tiles = args.tiles,
        furo = args.furo,
        bestShantenOnly = args.bestShantenOnly
    )

    val context = CalcContext()
    return context.kokushiShanten(internalShantenArgs)
}

internal fun CalcContext.kokushiShanten(
    args: InternalShantenArgs
): KokushiShantenResult = memo(Pair("kokushiShanten", args)) {
    with(args) {
        val tiles = normalizeTiles(tiles)

        var (shantenInfo, patterns) = if (tiles.size == 13) {
            handleKokushiShantenWithoutGot(tiles)
        } else {
            handleKokushiShantenWithGot(tiles, bestShantenOnly)
        }

        if (calcAdvanceNum) {
            shantenInfo = shantenInfo.fillNum(tiles.countAsCodeArray())
        }

        val hand = Hand(tiles = tiles, furo = emptyList(), patterns = patterns)
        return KokushiShantenResult(hand = hand, shantenInfo = shantenInfo)
    }
}

private fun buildKokushiPattern(tiles: List<Tile>): Sequence<KokushiHandPattern> {
    return sequence {
        val yaochu = HashSet<Tile>()
        val repeated = HashSet<Tile>()
        val remaining = ArrayList<Tile>()

        for (t in tiles) {
            if (t.isYaochu) {
                if (t in yaochu) {
                    if (t in repeated) {
                        remaining.add(t)
                    } else {
                        repeated.add(t)
                    }
                } else {
                    yaochu.add(t)
                }
            } else {
                remaining.add(t)
            }
        }

        if (repeated.isNotEmpty()) {
            // 非十三面
            for (t in repeated) {
                val pat = KokushiHandPattern(yaochu, t, remaining + (repeated - t))
                yield(pat)
            }
        } else {
            // 十三面
            val pat = KokushiHandPattern(yaochu, null, remaining)
            yield(pat)
        }
    }
}

private fun handleKokushiShantenWithoutGot(tiles: List<Tile>): Pair<ShantenWithoutGot, Collection<KokushiHandPattern>> {
    val (shantenNum, bestPatterns) = selectBestPatterns(buildKokushiPattern(tiles), KokushiHandPattern::calcShanten)

    val pat = bestPatterns.first()
    if (pat.repeated != null) {
        // 非十三面
        val advance = Tile.allYaochu - pat.yaochu
        val shantenInfo = ShantenWithoutGot(shantenNum, advance)
        return Pair(shantenInfo, bestPatterns)
    } else {
        // 十三面
        val advance = Tile.allYaochu
        val goodShapeAdvance = if (shantenNum == 1) advance else null
        val shantenInfo = ShantenWithoutGot(shantenNum, advance, goodShapeAdvance = goodShapeAdvance)
        return Pair(shantenInfo, bestPatterns)
    }
}

private fun handleKokushiShantenWithGot(
    tiles: List<Tile>,
    bestShantenOnly: Boolean = false,
): Pair<ShantenWithGot, Collection<KokushiHandPattern>> {
    val patterns = buildKokushiPattern(tiles)
    val (shantenNum, bestPatterns) = selectBestPatterns(patterns, KokushiHandPattern::calcShanten)
    val discardToAdvance = buildMap {
        for (t in tiles.toSet()) {
            val idx = tiles.indexOf(t)
            val shantenAfterDiscard = handleKokushiShantenWithoutGot(
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
    return Pair(shantenInfo, bestPatterns)
}
