package mahjongutils.shanten

import mahjongutils.CalcContext
import mahjongutils.models.Ankan
import mahjongutils.models.Furo
import mahjongutils.models.Kan
import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.RegularHandPattern
import mahjongutils.shanten.helpers.*

/**
 * 标准形向听分析（只考虑4面子+1雀头和牌的形状）
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun regularShanten(
    tiles: List<Tile>,
    furo: List<Furo> = listOf(),
    bestShantenOnly: Boolean = false,
): RegularShantenResult {
    return regularShanten(CommonShantenArgs(tiles = tiles, furo = furo, bestShantenOnly = bestShantenOnly))
}

/**
 * 标准形向听分析（只考虑4面子+1雀头和牌的形状）
 * @param args 向听分析参数
 * @return 向听分析结果
 */
fun regularShanten(
    args: CommonShantenArgs
): RegularShantenResult {
    args.throwOnValidationError()

    val internalShantenArgs = InternalShantenArgs(
        tiles = args.tiles,
        furo = args.furo,
        bestShantenOnly = args.bestShantenOnly
    )

    val context = CalcContext()
    return context.regularShanten(internalShantenArgs)
}

internal fun CalcContext.regularShanten(
    args: InternalShantenArgs
): RegularShantenResult = memo(Pair("regularShanten", args)) {
    with(args) {
        val tiles = normalizeTiles(tiles)

        val withGot = tiles.size % 3 == 2
        var (shantenInfo, bestPatterns) = if (!withGot) {
            handleRegularShantenWithoutGot(
                tiles, furo,
                calcGoodShapeAdvance = calcGoodShapeAdvance,
                calcImprovement = calcImprovement
            )
        } else {
            handleRegularShantenWithGot(
                tiles,
                furo,
                calcGoodShapeAdvance = calcGoodShapeAdvance,
                bestShantenOnly = bestShantenOnly,
                allowAnkan = allowAnkan,
                calcImprovement = calcImprovement
            )
        }

        if (calcAdvanceNum) {
            val tilesCount = getTileCount(tiles, furo)
            shantenInfo = shantenInfo.fillNum(tilesCount)
        }

        val hand = Hand(tiles = tiles, furo = furo, patterns = bestPatterns)
        return RegularShantenResult(hand = hand, shantenInfo = shantenInfo)
    }
}

private fun bestRegularHandPatternSearch(tiles: List<Tile>, furo: List<Furo>): Pair<Int, List<RegularHandPattern>> {
    val selector = BestHandPatternsSelector(RegularHandPattern::calcShanten)
    regularHandPatternSearch(tiles, furo) {
        selector.receive(it)
    }
    return Pair(selector.bestShanten, selector.bestPatterns)
}

private fun getGoodShapeAdvance(
    tiles: List<Tile>, furo: List<Furo>,
    remaining: IntArray,
    advance: Set<Tile>
): Set<Tile> {
    return buildSet {
        for (adv in advance) {
            val tilesAfterAdv = tiles + adv
            var shantenAfterAdv = handleRegularShantenWithGot(
                tilesAfterAdv, furo,
                calcGoodShapeAdvance = false,
                bestShantenOnly = true,
                allowAnkan = false,
                calcImprovement = false
            ).first

            remaining[adv.code] -= 1
            shantenAfterAdv = shantenAfterAdv.fillNumByRemaining(remaining)
            remaining[adv.code] += 1

            val maxAdvAfterAdv = shantenAfterAdv.discardToAdvance.values.maxBy { it.advanceNum }
            if (maxAdvAfterAdv.advanceNum > 4) {
                add(adv)
            }
        }
    }
}

private fun ShantenWithoutGot.fillImprovement(
    tiles: List<Tile>,
    furo: List<Furo>,
    remaining: IntArray,
    shantenNum: Int,
    advanceMoreThan: Int,
    tryAllTile: Boolean = false
): ShantenWithoutGot {
    val zone = if (!tryAllTile) {
        // 解空间为所有数牌的靠张
        tiles.filter { it.type !== TileType.Z }
            .flatMap { TILE_CLING[it] ?: emptyList() }
            .toSet()
            .filter { remaining[it.code] > 0 }
    } else {
        Tile.allExcludeAkaDora
    }

    val improvement = zone.mapNotNull { t ->
        // 摸上这张牌之后的向听信息
        val (shantenAfterGot, _) = handleRegularShantenWithGot(
            tiles + t, furo,
            bestShantenOnly = true,
            calcImprovement = false,
            calcGoodShapeAdvance = false,
            allowAnkan = false
        )

        if (shantenAfterGot.shantenNum != shantenNum) {
            return@mapNotNull null
        }

        var improvement = mutableListOf<Improvement>()
        var maxAdvanceNum = advanceMoreThan

        remaining[t.code] -= 1

        shantenAfterGot.discardToAdvance.forEach { (discard, improvedShanten) ->
            // 此时还未计算advanceNum
            val advanceNum = improvedShanten.advance.sumOf { remaining[it.code] }
            if (advanceNum > maxAdvanceNum) {
                improvement = mutableListOf()
                maxAdvanceNum = advanceNum
            }
            if (advanceNum == maxAdvanceNum) {
                improvement.add(Improvement(discard, improvedShanten.advance, advanceNum))
            }
        }

        remaining[t.code] += 1

        // 保证顺序（为了单测）
        improvement.sortBy { it.discard }

        if (maxAdvanceNum != advanceMoreThan) {
            Pair(t, improvement)
        } else {
            null
        }
    }.associate { it }

    val goodShapeImprovement = improvement.filterValues { it.first().advanceNum >= 5 }

    return copy(improvement = improvement, goodShapeImprovement = goodShapeImprovement)
}

private fun handleRegularShantenWithoutGot(
    tiles: List<Tile>, furo: List<Furo>,
    calcGoodShapeAdvance: Boolean = true,
    calcImprovement: Boolean = true
): Pair<ShantenWithoutGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = bestRegularHandPatternSearch(tiles, furo)

    val tilesCount = getTileCount(tiles, furo)
    val remaining = getRemainingFromTileCount(tilesCount)

    val advance = bestPatterns.flatMap { it.calcAdvance() }.filter { remaining[it.code] > 0 }.toSet()

    // 一向听时计算好型进张
    val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
        getGoodShapeAdvance(tiles, furo, remaining, advance)
    } else {
        null
    }

    var shanten = ShantenWithoutGot(
        shantenNum = bestShanten,
        advance = advance,
        goodShapeAdvance = goodShape
    )

    // 听牌时计算改良张
    if (bestShanten == 0) {
        shanten = if (calcImprovement) {
            val advanceNum = advance.sumOf { remaining[it.code] }
            val tryAllTile = bestPatterns.any { it.remaining.isNotEmpty() }  // 如果有单吊听牌就计算换听

            shanten.fillImprovement(tiles, furo, remaining, bestShanten, advanceNum, tryAllTile)
        } else {
            shanten.copy(
                improvement = null,
                improvementNum = null,
                goodShapeImprovement = null,
                goodShapeImprovementNum = null
            )
        }
    }

    return Pair(shanten, bestPatterns)
}

private fun handleRegularShantenWithGot(
    tiles: List<Tile>, furo: List<Furo>,
    calcGoodShapeAdvance: Boolean = true,
    bestShantenOnly: Boolean = false,
    allowAnkan: Boolean = true,
    calcImprovement: Boolean = true,
): Pair<ShantenWithGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = bestRegularHandPatternSearch(tiles, furo)

    val tilesCount = getTileCount(tiles, furo)
    val remaining = getRemainingFromTileCount(tilesCount)

    // 先计算不退向的打法（打浮牌）
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
            // 一向听时计算好型进张
            val goodShape = if (calcGoodShapeAdvance && bestShanten == 1) {
                getGoodShapeAdvance(tiles - discard, furo, remaining, advance)
            } else {
                null
            }

            var shanten = ShantenWithoutGot(
                shantenNum = bestShanten,
                advance = advance,
                goodShapeAdvance = goodShape
            )

            // 听牌时计算改良张
            if (bestShanten == 0) {
                shanten = if (calcImprovement) {
                    val advanceNum = advance.sumOf { remaining[it.code] }
                    val tryAllTile = bestPatterns.any { it.remaining.any { it != discard } }  // 如果有单吊听牌就计算换听

                    shanten.fillImprovement(tiles - discard, furo, remaining, bestShanten, advanceNum, tryAllTile)
                } else {
                    shanten.copy(
                        improvement = null,
                        improvementNum = null,
                        goodShapeImprovement = null,
                        goodShapeImprovementNum = null
                    )
                }
            }

            this[discard] = shanten
        }

        this
    }

    // 再计算退向的打法
    if (!bestShantenOnly) {
        val nonBestShantenTiles = tiles.toSet() - discardToShanten.keys
        for (discard in nonBestShantenTiles) {
            val tilesAfterDiscard = tiles - discard
            val shantenAfterDiscard = handleRegularShantenWithoutGot(
                tilesAfterDiscard, furo,
                calcGoodShapeAdvance = calcGoodShapeAdvance,
                calcImprovement = calcImprovement
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
                    val furoAfterAnkan = furo + Ankan(t)
                    this[t] = handleRegularShantenWithoutGot(
                        tilesAfterAnkan, furoAfterAnkan,
                        calcGoodShapeAdvance, calcImprovement
                    ).first
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
