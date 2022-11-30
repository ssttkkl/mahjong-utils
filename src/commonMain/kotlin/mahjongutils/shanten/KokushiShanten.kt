package mahjongutils.shanten

import mahjongutils.common.calcShanten
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.KokushiHandPattern
import mahjongutils.models.isYaochu

private fun buildKokushiPattern(tiles: List<Tile>): Collection<KokushiHandPattern> {
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

    return buildList {
        if (repeated.isNotEmpty()) {
            // 非十三面
            for (t in repeated) {
                val pat = KokushiHandPattern(yaochu, t, remaining + (repeated - t))
                add(pat)
            }
        } else {
            // 十三面
            val pat = KokushiHandPattern(yaochu, null, remaining)
            add(pat)
        }
    }
}

private fun handleKokushiShantenWithoutGot(tiles: List<Tile>): Pair<ShantenWithoutGot, Collection<KokushiHandPattern>> {
    val patterns = buildKokushiPattern(tiles)
    val (shantenNum, bestPatterns) = selectBestPatterns(patterns, KokushiHandPattern::calcShanten)

    val pat = bestPatterns.first()
    if (pat.repeated != null) {
        // 非十三面
        val advance = Tile.allYaochu - pat.yaochu
        val goodShapeAdvance = if (shantenNum == 1) emptySet<Tile>() else null
        val shantenInfo = ShantenWithoutGot(shantenNum, advance, goodShapeAdvance = goodShapeAdvance)
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


/**
 * 国士无双向听分析
 * @param tiles 门前的牌
 * @param calcAdvanceNum 是否计算进张数
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun kokushiShanten(
    tiles: List<Tile>,
    calcAdvanceNum: Boolean = true,
    bestShantenOnly: Boolean = false,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    var (shantenInfo, patterns) = if (tiles.size == 13) {
        handleKokushiShantenWithoutGot(tiles)
    } else {
        handleKokushiShantenWithGot(tiles, bestShantenOnly)
    }

    if (calcAdvanceNum) {
        shantenInfo = shantenInfo.fillAdvanceNum(tiles.countAsCodeArray())
    }

    val hand = Hand(tiles = tiles, furo = emptyList(), patterns = patterns)
    return ShantenResult(type = ShantenResult.Type.Kokushi, hand = hand, shantenInfo = shantenInfo)
}