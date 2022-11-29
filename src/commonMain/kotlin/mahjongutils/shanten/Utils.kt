package mahjongutils.shanten

import mahjongutils.models.Tile
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

internal fun ShantenWithoutGot.fillAdvanceNum(remaining: IntArray): ShantenWithoutGot {
    val advanceNum = advance.sumOf { remaining[it.code] }
    val goodShapeAdvanceNum = goodShapeAdvance?.sumOf { remaining[it.code] }

    return copy(advanceNum = advanceNum, goodShapeAdvanceNum = goodShapeAdvanceNum)
}

internal inline fun <reified T : Shanten> T.fillAdvanceNum(vararg tiles: Tile): T {
    val remaining = IntArray(Tile.MAX_TILE_CODE + 1) { 4 }
    tiles.forEach {
        remaining[it.code] -= 1
    }

    return when (this) {
        is ShantenWithoutGot -> {
            this.fillAdvanceNum(remaining) as T
        }

        is ShantenWithGot -> {
            copy(discardToAdvance = discardToAdvance.mapValues { (k, v) -> v.fillAdvanceNum(remaining) }) as T
        }

        else -> {
            error("unexpected type")
        }
    }
}