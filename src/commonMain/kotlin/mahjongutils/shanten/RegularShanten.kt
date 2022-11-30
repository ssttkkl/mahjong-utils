package mahjongutils.shanten

import mahjongutils.common.calcAdvance
import mahjongutils.common.calcShanten
import mahjongutils.common.regularHandPatternSearch
import mahjongutils.models.Furo
import mahjongutils.models.Kan
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.RegularHandPattern

private fun getGoodShapeAdvance(
    tiles: List<Tile>, furo: List<Furo>,
    advance: Set<Tile>,
    otherUsedTiles: Collection<Tile> = emptyList()
): Set<Tile> {
    return buildSet {
        for (adv in advance) {
            val tilesAfterAdv = tiles + adv
            var shantenAfterAdv = handleRegularShantenWithGot(
                tilesAfterAdv, furo,
                calcGoodShapeAdvance = false,
                bestShantenOnly = true,
                allowAnkan = false
            ).first

            val tilesCount = (tilesAfterAdv + furo.flatMap { it.tiles }).countAsCodeArray()
            otherUsedTiles.forEach {
                tilesCount[it.code] += 1
            }
            shantenAfterAdv = shantenAfterAdv.fillAdvanceNum(tilesCount)

            val maxAdvAfterAdv = shantenAfterAdv.discardToAdvance.values.maxBy { it.advanceNum!! }
            if (maxAdvAfterAdv.advanceNum!! > 4) {
                add(adv)
            }
        }
    }
}

private fun handleRegularShantenWithoutGot(
    tiles: List<Tile>, furo: List<Furo>,
    calcGoodShapeAdvance: Boolean = true,
): Pair<ShantenWithoutGot, Collection<RegularHandPattern>> {
    val patterns = regularHandPatternSearch(tiles, furo)
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val tilesCount = (tiles + furo.flatMap { it.tiles }).countAsCodeArray()
    val advance = bestPatterns.map { it.calcAdvance() }.flatten().filter { tilesCount[it.code] < 4 }.toSet()

    val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
        getGoodShapeAdvance(tiles, furo, advance)
    } else {
        null
    }

    val shanten = ShantenWithoutGot(shantenNum = bestShanten, advance = advance, goodShapeAdvance = goodShape)
    return Pair(shanten, bestPatterns)
}

private fun handleRegularShantenWithGot(
    tiles: List<Tile>, furo: List<Furo>,
    calcGoodShapeAdvance: Boolean = true,
    bestShantenOnly: Boolean = false,
    allowAnkan: Boolean = true,
): Pair<ShantenWithGot, Collection<RegularHandPattern>> {
    val patterns = regularHandPatternSearch(tiles, furo)
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val tilesCount = (tiles + furo.flatMap { it.tiles }).countAsCodeArray()

    // 先计算不退向的打法
    val discardToShanten = HashMap<Tile, ShantenWithoutGot>().run {
        val discardToAdvance = HashMap<Tile, MutableSet<Tile>>()

        bestPatterns.forEach { pat ->
            pat.remaining.forEachIndexed { i, discard ->
                val patAfterDiscard = pat.copy(
                    remaining = pat.remaining.slice(0 until i) +
                            pat.remaining.slice(i + 1 until pat.remaining.size)
                )

                val advance = patAfterDiscard.calcAdvance().filter { tilesCount[it.code] < 4 }
                if (!discardToAdvance.containsKey(discard)) {
                    discardToAdvance[discard] = advance.toMutableSet()
                } else {
                    discardToAdvance[discard]!!.addAll(advance)
                }
            }
        }

        for ((discard, advance) in discardToAdvance) {
            val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
                getGoodShapeAdvance(tiles - discard, furo, advance, listOf(discard))
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
            val shantenAfterDiscard = handleRegularShantenWithoutGot(
                tilesAfterDiscard, furo, calcGoodShapeAdvance
            ).first
            discardToShanten[discard] = shantenAfterDiscard
        }
    }

    // 最后计算暗杠
    var ankanToAdvance: Map<Tile, ShantenWithoutGot>? = null
    if (allowAnkan) {
        ankanToAdvance = buildMap {
            for (t in Tile.allExcludeAkaDora) {
                val count = tilesCount[t.code]
                if (count == 4) {
                    val tilesAfterAnkan = tiles - t - t - t - t
                    val furoAfterAnkan = furo + Kan(t, true)
                    this[t] = handleRegularShantenWithoutGot(tilesAfterAnkan, furoAfterAnkan).first
                }
            }
        }

        if (bestShantenOnly) {
            ankanToAdvance = ankanToAdvance.filterValues { it.shantenNum == bestShanten }
        }
    }

    val shantenInfo = ShantenWithGot(
        shantenNum = bestShanten,
        discardToAdvance = discardToShanten,
        ankanToAdvance = ankanToAdvance ?: emptyMap()
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
    allowAnkan: Boolean = true,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val withGot = tiles.size % 3 == 2
    var (shantenInfo, bestPatterns) = if (!withGot) {
        handleRegularShantenWithoutGot(tiles, furo)
    } else {
        handleRegularShantenWithGot(tiles, furo, bestShantenOnly = bestShantenOnly, allowAnkan = allowAnkan)
    }

    if (calcAdvanceNum) {
        val tilesCount = (tiles + furo.flatMap { it.tiles }).countAsCodeArray()
        shantenInfo = shantenInfo.fillAdvanceNum(tilesCount)
    }

    val hand = Hand(tiles = tiles, furo = furo, patterns = bestPatterns)
    return ShantenResult(type = ShantenResult.Type.Regular, hand = hand, shantenInfo = shantenInfo)
}