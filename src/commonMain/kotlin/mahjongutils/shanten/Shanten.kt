@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.shanten

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.common.*
import mahjongutils.models.*
import mahjongutils.models.hand.*
import kotlin.math.min


@Serializable
sealed interface Shanten {
    val shantenNum: Int
}

@Serializable
@SerialName("ShantenWithoutGot")
data class ShantenWithoutGot(
    override val shantenNum: Int,
    val advance: Set<Tile>,
    @EncodeDefault val advanceNum: Int? = null,
    @EncodeDefault val wellShapeAdvance: Set<Tile>? = null,
    @EncodeDefault val wellShapeAdvanceNum: Int? = null
) : Shanten

@Serializable
@SerialName("ShantenWithGot")
data class ShantenWithGot(
    override val shantenNum: Int,
    val discardToAdvance: Map<Tile, ShantenWithoutGot>
) : Shanten

@Serializable
data class ShantenResult internal constructor(
    val type: Type,
    val hand: Hand,
    val shantenInfo: Shanten,
    @EncodeDefault val regular: ShantenResult? = null,
    @EncodeDefault val chitoi: ShantenResult? = null,
    @EncodeDefault val kokushi: ShantenResult? = null,
) {
    enum class Type {
        Union, Regular, Chitoi, Kokushi
    }
}

private fun ensureLegalTiles(tiles: List<Tile>, allowAnyK: Boolean = true): List<Tile> {
    if (!allowAnyK && tiles.size / 3 != 4) {
        throw IllegalArgumentException("invalid length of hand: ${tiles.size}")
    }
    if (tiles.size !in 1..14 || tiles.size % 3 == 0) {
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

private fun <T : HandPattern> selectBestPatterns(
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

private fun ShantenWithoutGot.fillAdvanceNum(remaining: IntArray): ShantenWithoutGot {
    val advanceNum = advance.sumOf { remaining[it.code] }
    val wellShapeAdvanceNum = wellShapeAdvance?.sumOf { remaining[it.code] }

    return copy(advanceNum = advanceNum, wellShapeAdvanceNum = wellShapeAdvanceNum)
}

private inline fun <reified T : Shanten> T.fillAdvanceNum(vararg tiles: Tile): T {
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

// ======== 标准形 ========
private fun handleRegularShantenWithoutGot(
    patterns: Collection<RegularHandPattern>,
    calcWellShapeAdvance: Boolean = true,
): Pair<ShantenWithoutGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val advance = bestPatterns.map { it.calcAdvance() }.flatten().toSet()

    val wellShape: Set<Tile>? = if (calcWellShapeAdvance && bestShanten == 1) {
        buildSet {
            val pat = bestPatterns.first()
            val tiles = (pat.tiles + pat.furo.flatMap { it.asMentsu().tiles }).toTypedArray()

            for (adv in advance) {
                val patternsAfterAdv = bestPatterns.map { it.afterAdvance(adv) }.flatten().toSet()
                val shantenAfterAdv = handleRegularShantenWithGot(
                    patternsAfterAdv,
                    calcWellShapeAdvance = false,
                    bestShantenOnly = true
                ).first.fillAdvanceNum(adv, *tiles)
                val maxAdvAfterAdv = shantenAfterAdv.discardToAdvance.values.maxBy { it.advanceNum!! }
                if (maxAdvAfterAdv.advanceNum!! > 4) {
                    add(adv)
                }
            }
        }
    } else {
        null
    }

    val shanten = ShantenWithoutGot(shantenNum = bestShanten, advance = advance, wellShapeAdvance = wellShape)
    return Pair(shanten, bestPatterns)
}

private fun handleRegularShantenWithGot(
    patterns: Collection<RegularHandPattern>,
    calcWellShapeAdvance: Boolean = true,
    bestShantenOnly: Boolean = false,
): Pair<ShantenWithGot, Collection<RegularHandPattern>> {
    val (bestShanten, bestPatterns) = selectBestPatterns(patterns, RegularHandPattern::calcShanten)

    val discardToAdvance = HashMap<Tile, ShantenWithoutGot>()
    for (discard in bestPatterns.first().tiles.toSet()) {
        val patternsAfterDiscard = bestPatterns.map { it.afterDiscard(discard) }.flatten().toSet()
        val shantenAfterDiscard = handleRegularShantenWithoutGot(patternsAfterDiscard, calcWellShapeAdvance).first
        discardToAdvance[discard] = shantenAfterDiscard
    }

    if (bestShantenOnly) {
        val iter = discardToAdvance.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            if (entry.value.shantenNum != bestShanten) {
                iter.remove()
            }
        }
    }

    val shantenInfo = ShantenWithGot(shantenNum = bestShanten, discardToAdvance = discardToAdvance)
    return Pair(shantenInfo, bestPatterns)
}


fun regularShanten(tiles: List<Tile>, furo: List<Furo> = listOf(), calcAdvanceNum: Boolean = true): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val patterns = regularHandPatternSearch(tiles, furo)
    val withGot = tiles.size % 3 == 2
    var (shantenInfo, bestPatterns) = if (!withGot) {
        handleRegularShantenWithoutGot(patterns)
    } else {
        handleRegularShantenWithGot(patterns)
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

// ========== 七对子 ==========
fun buildChitoiPattern(tiles: List<Tile>): ChitoiHandPattern {
    val cnt = buildMap<Tile, Int> {
        tiles.forEach {
            if (it !in this) {
                this[it] = 0
            }
            this[it] = this[it]!! + 1
        }
    }

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

fun handleChitoiShantenWithoutGot(tiles: List<Tile>): Pair<ShantenWithoutGot, ChitoiHandPattern> {
    val pattern = buildChitoiPattern(tiles)
    val shantenNum = pattern.calcShanten()
    val advance = pattern.remaining - pattern.pairs
    val wellShapeAdvance = if (shantenNum == 1) emptySet<Tile>() else null
    val shantenInfo = ShantenWithoutGot(
        shantenNum = shantenNum,
        advance = advance.toSet(),
        wellShapeAdvance = wellShapeAdvance
    )
    return Pair(shantenInfo, pattern)
}

fun handleChitoiShantenWithGot(tiles: List<Tile>): Pair<ShantenWithGot, ChitoiHandPattern> {
    val pattern = buildChitoiPattern(tiles)
    val shantenNum = pattern.calcShanten()
    val discardToAdvance = buildMap {
        if (shantenNum != -1) {
            for (t in tiles.toSet()) {
                val idx = tiles.indexOf(t)
                val shantenAfterDiscard = handleChitoiShantenWithoutGot(
                    tiles.slice(0 until idx) + tiles.slice(idx + 1 until tiles.size)
                ).first
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


fun chitoiShanten(tiles: List<Tile>, calcAdvanceNum: Boolean = true): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    var (shantenInfo, pattern) = if (tiles.size == 13) {
        handleChitoiShantenWithoutGot(tiles)
    } else {
        handleChitoiShantenWithGot(tiles)
    }

    if (calcAdvanceNum) {
        shantenInfo = shantenInfo.fillAdvanceNum(*tiles.toTypedArray())
    }

    val hand = Hand(tiles = tiles, furo = emptyList(), patterns = listOf(pattern))
    return ShantenResult(type = ShantenResult.Type.Chitoi, hand = hand, shantenInfo = shantenInfo)
}

// ========== 国士无双 ==========
fun buildKokushiPattern(tiles: List<Tile>): Collection<KokushiHandPattern> {
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

fun handleKokushiShantenWithoutGot(tiles: List<Tile>): Pair<ShantenWithoutGot, Collection<KokushiHandPattern>> {
    val patterns = buildKokushiPattern(tiles)
    val (shantenNum, bestPatterns) = selectBestPatterns(patterns, KokushiHandPattern::calcShanten)

    val pat = bestPatterns.first()
    if (pat.repeated != null) {
        // 非十三面
        val advance = Tile.allYaochu - pat.yaochu
        val wellShapeAdvance = if (shantenNum == 1) emptySet<Tile>() else null
        val shantenInfo = ShantenWithoutGot(shantenNum, advance, wellShapeAdvance = wellShapeAdvance)
        return Pair(shantenInfo, bestPatterns)
    } else {
        // 十三面
        val advance = Tile.allYaochu
        val wellShapeAdvance = if (shantenNum == 1) advance else null
        val shantenInfo = ShantenWithoutGot(shantenNum, advance, wellShapeAdvance = wellShapeAdvance)
        return Pair(shantenInfo, bestPatterns)
    }
}

fun handleKokushiShantenWithGot(tiles: List<Tile>): Pair<ShantenWithGot, Collection<KokushiHandPattern>> {
    val patterns = buildKokushiPattern(tiles)
    val (shantenNum, bestPatterns) = selectBestPatterns(patterns, KokushiHandPattern::calcShanten)
    val discardToAdvance = buildMap {
        if (shantenNum != -1) {
            for (t in tiles.toSet()) {
                val idx = tiles.indexOf(t)
                val shantenAfterDiscard = handleKokushiShantenWithoutGot(
                    tiles.slice(0 until idx) + tiles.slice(idx + 1 until tiles.size)
                ).first
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

fun kokushiShanten(tiles: List<Tile>, calcAdvanceNum: Boolean = true): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    var (shantenInfo, patterns) = if (tiles.size == 13) {
        handleKokushiShantenWithoutGot(tiles)
    } else {
        handleKokushiShantenWithGot(tiles)
    }

    if (calcAdvanceNum) {
        shantenInfo = shantenInfo.fillAdvanceNum(*tiles.toTypedArray())
    }

    val hand = Hand(tiles = tiles, furo = emptyList(), patterns = patterns)
    return ShantenResult(type = ShantenResult.Type.Kokushi, hand = hand, shantenInfo = shantenInfo)
}

// ========== Union ==========
fun mergeIntoWithoutGot(
    targetShantenNum: Int,
    advance: MutableSet<Tile>,
    wellShapeAdvance: MutableSet<Tile>,
    patterns: MutableCollection<HandPattern>,
    result: ShantenResult
) {
    if (result.shantenInfo.shantenNum == targetShantenNum) {
        val shantenInfo = result.shantenInfo as ShantenWithoutGot
        advance += shantenInfo.advance
        patterns += result.hand.patterns
        if (shantenInfo.wellShapeAdvance != null) {
            wellShapeAdvance += shantenInfo.wellShapeAdvance
        }
    }
}

fun mergeIntoWithGot(
    targetShantenNum: Int,
    discardToAdvance: MutableMap<Tile, ShantenWithoutGot>,
    patterns: MutableCollection<HandPattern>,
    result: ShantenResult
) {
    val shantenInfo = result.shantenInfo as ShantenWithGot
    for ((discard, shantenAfterDiscard) in shantenInfo.discardToAdvance) {
        val oldShantenAfterDiscard = discardToAdvance[discard]
        if (oldShantenAfterDiscard == null || oldShantenAfterDiscard.shantenNum > shantenAfterDiscard.shantenNum) {
            discardToAdvance[discard] = shantenAfterDiscard
        } else if (oldShantenAfterDiscard.shantenNum == shantenAfterDiscard.shantenNum) {
            discardToAdvance[discard] = oldShantenAfterDiscard.copy(
                advance = oldShantenAfterDiscard.advance + shantenAfterDiscard.advance,
                wellShapeAdvance = shantenAfterDiscard.wellShapeAdvance?.let {
                    oldShantenAfterDiscard.wellShapeAdvance?.plus(it)
                }
            )
        }
    }

    if (targetShantenNum == shantenInfo.shantenNum) {
        patterns += result.hand.patterns
    }
}

fun shanten(tiles: List<Tile>, furo: List<Furo> = emptyList(), calcAdvanceNum: Boolean = true): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val withGot = tiles.size % 3 == 2
    val k = tiles.size / 3

    if (k != 4) {
        val regular = regularShanten(tiles, furo, calcAdvanceNum)
        return ShantenResult(
            type = ShantenResult.Type.Union, hand = regular.hand, shantenInfo = regular.shantenInfo,
            regular = regular
        )
    }

    val regular = regularShanten(tiles, furo, calcAdvanceNum = false)
    val chitoi = chitoiShanten(tiles, calcAdvanceNum = false)
    val kokushi = kokushiShanten(tiles, calcAdvanceNum = false)

    val shantenNum = min(
        min(
            regular.shantenInfo.shantenNum,
            chitoi.shantenInfo.shantenNum
        ),
        kokushi.shantenInfo.shantenNum
    )
    val patterns = ArrayList<HandPattern>()

    var shantenInfo = if (!withGot) {
        val advance = HashSet<Tile>()
        val wellShapeAdvance = HashSet<Tile>()

        mergeIntoWithoutGot(shantenNum, advance, wellShapeAdvance, patterns, regular)
        mergeIntoWithoutGot(shantenNum, advance, wellShapeAdvance, patterns, chitoi)
        mergeIntoWithoutGot(shantenNum, advance, wellShapeAdvance, patterns, kokushi)

        ShantenWithoutGot(shantenNum, advance, wellShapeAdvance = if (shantenNum == 1) wellShapeAdvance else null)
    } else {
        val discardToAdvance = HashMap<Tile, ShantenWithoutGot>()

        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, regular)
        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, chitoi)
        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, kokushi)

        ShantenWithGot(shantenNum, discardToAdvance)
    }

    if (calcAdvanceNum) {
        shantenInfo = shantenInfo.fillAdvanceNum(
            *tiles.toTypedArray(),
            *(furo.flatMap { it.asMentsu().tiles }.toTypedArray())
        )
    }

    val hand = Hand(tiles = tiles, furo = furo, patterns = patterns)
    return ShantenResult(
        type = ShantenResult.Type.Union, hand = hand, shantenInfo = shantenInfo,
        regular = regular, chitoi = chitoi, kokushi = kokushi
    )
}