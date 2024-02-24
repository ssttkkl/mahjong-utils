package mahjongutils.shanten.helpers

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.HandPattern
import mahjongutils.shanten.CommonShanten
import mahjongutils.shanten.ShantenWithGot
import mahjongutils.shanten.ShantenWithoutGot


internal fun normalizeTiles(
    tiles: List<Tile>
): List<Tile> {
    val tiles = tiles.map { if (it.num == 0) Tile.get(it.type, 5) else it }
    return tiles
}

internal fun <T : HandPattern> selectBestPatterns(
    patterns: Sequence<T>,
    calcShanten: (T) -> Int
): Pair<Int, Collection<T>> {
    val selector = BestHandPatternsSelector(calcShanten)
    patterns.forEach {
        selector.receive(it)
    }
    return Pair(selector.bestShanten, selector.bestPatterns)
}

internal fun getTileCount(tiles: Collection<Tile>, furo: Collection<Furo> = emptyList()): IntArray {
    return (tiles + furo.flatMap { it.tiles }).countAsCodeArray()
}

internal fun getRemainingFromTileCount(tileCount: IntArray): IntArray {
    val remaining = IntArray(Tile.MAX_TILE_CODE + 1) { 4 }
    for (i in tileCount.indices) {
        remaining[i] -= tileCount[i]
    }
    return remaining
}

internal inline fun <reified T : CommonShanten> T.fillNum(tileCount: IntArray): T {
    val remaining = getRemainingFromTileCount(tileCount)
    return fillNumByRemaining(remaining)
}

internal fun <T : CommonShanten> T.fillNumByRemaining(remaining: IntArray): T {
    return when (this) {
        is ShantenWithoutGot -> {
            copy(
                advanceNum = advance.sumOf { remaining[it.code] },
                goodShapeAdvanceNum = goodShapeAdvance?.sumOf { remaining[it.code] },
                improvement = improvement?.mapValues { (k, v) ->
                    remaining[k.code] -= 1
                    val v = v.map { imp -> imp.copy(advanceNum = imp.advance.sumOf { remaining[it.code] }) }
                    remaining[k.code] += 1

                    v
                },
                goodShapeImprovement = goodShapeImprovement?.mapValues { (k, v) ->
                    remaining[k.code] -= 1
                    val v = v.map { imp -> imp.copy(advanceNum = imp.advance.sumOf { remaining[it.code] }) }
                    remaining[k.code] += 1

                    v
                },
                improvementNum = improvement?.keys?.sumOf { remaining[it.code] },
                goodShapeImprovementNum = goodShapeImprovement?.keys?.sumOf { remaining[it.code] },
            ) as T
        }

        is ShantenWithGot -> {
            copy(
                discardToAdvance = discardToAdvance.mapValues { (_, v) -> v.fillNumByRemaining(remaining) },
                ankanToAdvance = ankanToAdvance.mapValues { (_, v) -> v.fillNumByRemaining(remaining) },
            ) as T
        }

        else -> {
            error("unexpected type")
        }
    }
}
