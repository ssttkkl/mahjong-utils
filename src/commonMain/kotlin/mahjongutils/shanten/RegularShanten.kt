package mahjongutils.shanten

import mahjongutils.common.afterAdvanceForOneShantenHand
import mahjongutils.common.calcAdvance
import mahjongutils.common.calcShanten
import mahjongutils.common.regularHandPatternSearch
import mahjongutils.models.Furo
import mahjongutils.models.Kan
import mahjongutils.models.Tile
import mahjongutils.models.countAsMap
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.RegularHandPattern

private fun getGoodShapeAdvance(
    bestPatterns: Collection<RegularHandPattern>,
    advance: Set<Tile>,
    usedTiles: Collection<Tile>
): Set<Tile> {
    return buildSet {
        for (adv in advance) {
            val patternsAfterAdv = bestPatterns.map { it.afterAdvanceForOneShantenHand(adv) }.flatten().toSet()
            val shantenAfterAdv = handleRegularShantenWithGot(
                patternsAfterAdv,
                calcGoodShapeAdvance = false,
                bestShantenOnly = true
            ).first.fillAdvanceNum(adv, *usedTiles.toTypedArray())
            val maxAdvAfterAdv = shantenAfterAdv.discardToAdvance.values.maxBy { it.advanceNum!! }
            if (maxAdvAfterAdv.advanceNum!! > 4) {
                add(adv)
            }
        }
    }
}

private fun handleRegularShantenWithoutGot(
    patterns: Collection<RegularHandPattern>,
    calcGoodShapeAdvance: Boolean = true,
): Pair<ShantenWithoutGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val advance = bestPatterns.map { it.calcAdvance() }.flatten().toSet()

    val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
        val pat = bestPatterns.first()
        val usedTiles = pat.tiles + pat.furo.flatMap { it.asMentsu().tiles }
        getGoodShapeAdvance(bestPatterns, advance, usedTiles)
    } else {
        null
    }

    val shanten = ShantenWithoutGot(shantenNum = bestShanten, advance = advance, goodShapeAdvance = goodShape)
    return Pair(shanten, bestPatterns)
}

private fun handleRegularShantenWithGot(
    patterns: Collection<RegularHandPattern>,
    calcGoodShapeAdvance: Boolean = true,
    bestShantenOnly: Boolean = false,
): Pair<ShantenWithGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val tiles = bestPatterns.first().tiles
    val furo = bestPatterns.first().furo

    // 先计算不退向的打法
    val discardToShanten = HashMap<Tile, ShantenWithoutGot>().run {
        val discardToAdvance = HashMap<Tile, MutableSet<Tile>>()
        val patternsAfterDiscard = HashMap<Tile, MutableSet<RegularHandPattern>>()

        bestPatterns.forEach { pat ->
            pat.remaining.forEachIndexed { i, discard ->
                val patAfterDiscard = pat.copy(
                    remaining = pat.remaining.slice(0 until i) +
                            pat.remaining.slice(i + 1 until pat.remaining.size)
                )

                (patternsAfterDiscard.getOrPut(discard) { HashSet() }).add(patAfterDiscard)

                val advance = patAfterDiscard.calcAdvance()
                if (!discardToAdvance.containsKey(discard)) {
                    discardToAdvance[discard] = advance.toMutableSet()
                } else {
                    discardToAdvance[discard]!!.addAll(advance)
                }
            }
        }

        for ((discard, advance) in discardToAdvance) {
            val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
                val usedTiles = tiles + furo.flatMap { it.asMentsu().tiles }
                getGoodShapeAdvance(patternsAfterDiscard[discard]!!, advance, usedTiles)
            } else {
                null
            }
            this[discard] = ShantenWithoutGot(shantenNum = bestShanten, advance = advance, goodShapeAdvance = goodShape)
        }

        this
    }

    // 再计算退向的打法
    if (!bestShantenOnly) {
        val nonBestShantenTiles = tiles.toSet() - discardToShanten.keys
        for (discard in nonBestShantenTiles) {
            val tilesAfterDiscard = tiles - discard
            val patternsAfterDiscard = regularHandPatternSearch(tilesAfterDiscard, furo)
            val shantenAfterDiscard = handleRegularShantenWithoutGot(patternsAfterDiscard, calcGoodShapeAdvance).first
            discardToShanten[discard] = shantenAfterDiscard
        }
    }

    // 最后计算暗杠
    val tilesCount = tiles.countAsMap()
    var ankanToAdvance = buildMap {
        for ((t, count) in tilesCount) {
            if (count == 4) {
                val tilesAfterAnkan = tiles - t - t - t - t
                val patternsAfterAnkan = regularHandPatternSearch(tilesAfterAnkan, furo + Kan(t, true))
                this[t] = handleRegularShantenWithoutGot(patternsAfterAnkan).first
            }
        }
    }

    if (bestShantenOnly) {
        ankanToAdvance = ankanToAdvance.filterValues { it.shantenNum == bestShanten }
    }

    val shantenInfo = ShantenWithGot(
        shantenNum = bestShanten,
        discardToAdvance = discardToShanten,
        ankanToAdvance = ankanToAdvance
    )
    return Pair(shantenInfo, bestPatterns)
}


/**
 * 标准形向听分析
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param calcAdvanceNum 是否计算进张数
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun regularShanten(
    tiles: List<Tile>, furo: List<Furo> = listOf(),
    calcAdvanceNum: Boolean = true,
    bestShantenOnly: Boolean = false,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val patterns = regularHandPatternSearch(tiles, furo)
    val withGot = tiles.size % 3 == 2
    var (shantenInfo, bestPatterns) = if (!withGot) {
        handleRegularShantenWithoutGot(patterns)
    } else {
        handleRegularShantenWithGot(patterns, bestShantenOnly = bestShantenOnly)
    }

    if (calcAdvanceNum) {
        shantenInfo = shantenInfo.fillAdvanceNum(
            *tiles.toTypedArray(),
            *(furo.flatMap { it.asMentsu().tiles }.toTypedArray())
        )
    }

    val hand = Hand(tiles = tiles, furo = furo, patterns = bestPatterns)
    return ShantenResult(type = ShantenResult.Type.Regular, hand = hand, shantenInfo = shantenInfo)
}