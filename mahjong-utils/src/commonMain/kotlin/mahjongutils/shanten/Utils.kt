package mahjongutils.shanten

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.HandPattern


internal fun ensureLegalTiles(
    tiles: List<Tile>,
    allowAnyK: Boolean = true,
    allowWithGot: Boolean = true,
    allowWithoutGot: Boolean = true
): List<Tile> {
    if (!allowAnyK && tiles.size / 3 != 4 ||
        tiles.size !in 1..14 ||
        tiles.size % 3 == 0 ||
        !allowWithGot && tiles.size % 3 == 2 ||
        !allowWithoutGot && tiles.size % 3 == 1
    ) {
        throw IllegalArgumentException("invalid length of hand: ${tiles.size}")
    }

    val tiles = tiles.map { if (it.num == 0) Tile.get(it.type, 5) else it }

    val cnt = IntArray(Tile.MAX_TILE_CODE + 1)
    for (t in tiles) {
        cnt[t.code] += 1
        if (cnt[t.code] > 4) {
            throw IllegalArgumentException("invalid num of tile: $t")
        }
    }

    return tiles
}

internal fun <T : HandPattern> selectBestPatterns(
    patterns: Collection<T>,
    calcShanten: (T) -> Int
): Pair<Int, Collection<T>> {
    var bestShanten = 100
    var bestPattern = ArrayList<T>()

    for (pat in patterns) {
        val patShanten = calcShanten(pat)
        if (patShanten < bestShanten) {
            bestShanten = patShanten
            bestPattern = ArrayList()
        }
        if (patShanten == bestShanten) {
            bestPattern.add(pat)
        }
    }

    return Pair(bestShanten, bestPattern)
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
