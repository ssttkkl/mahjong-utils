package mahjongutils.shanten

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.HandPattern
import kotlin.math.min


private fun mergeIntoWithoutGot(
    targetShantenNum: Int,
    advance: MutableSet<Tile>,
    goodShapeAdvance: MutableSet<Tile>,
    patterns: MutableCollection<HandPattern>,
    result: ShantenResult
) {
    if (result.shantenInfo.shantenNum == targetShantenNum) {
        val shantenInfo = result.shantenInfo as ShantenWithoutGot
        advance += shantenInfo.advance
        patterns += result.hand.patterns
        if (shantenInfo.goodShapeAdvance != null) {
            goodShapeAdvance += shantenInfo.goodShapeAdvance
        }
    }
}

private fun mergeIntoWithGot(
    targetShantenNum: Int,
    discardToAdvance: MutableMap<Tile, ShantenWithoutGot>,
    patterns: MutableCollection<HandPattern>,
    result: ShantenResult,
    bestShantenOnly: Boolean
) {
    val shantenInfo = result.shantenInfo as ShantenWithGot
    for ((discard, shantenAfterDiscard) in shantenInfo.discardToAdvance) {
        if (!bestShantenOnly || shantenAfterDiscard.shantenNum == targetShantenNum) {
            val oldShantenAfterDiscard = discardToAdvance[discard]
            if (oldShantenAfterDiscard == null || oldShantenAfterDiscard.shantenNum > shantenAfterDiscard.shantenNum) {
                discardToAdvance[discard] = shantenAfterDiscard
            } else if (oldShantenAfterDiscard.shantenNum == shantenAfterDiscard.shantenNum) {
                discardToAdvance[discard] = oldShantenAfterDiscard.copy(
                    advance = oldShantenAfterDiscard.advance + shantenAfterDiscard.advance,
                    goodShapeAdvance = shantenAfterDiscard.goodShapeAdvance?.let {
                        oldShantenAfterDiscard.goodShapeAdvance?.plus(it)
                    }
                )
            }
        }
    }

    if (targetShantenNum == shantenInfo.shantenNum) {
        patterns += result.hand.patterns
    }
}

/**
 * 向听分析
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param calcAdvanceNum 是否计算进张数
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
fun shanten(
    tiles: List<Tile>, furo: List<Furo> = emptyList(),
    calcAdvanceNum: Boolean = true,
    bestShantenOnly: Boolean = false,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val withGot = tiles.size % 3 == 2
    val k = tiles.size / 3

    if (k != 4) {
        val regular = regularShanten(tiles, furo, calcAdvanceNum, bestShantenOnly)
        return ShantenResult(
            type = ShantenResult.Type.Union, hand = regular.hand, shantenInfo = regular.shantenInfo,
            regular = regular
        )
    }

    val regular = regularShanten(tiles, furo, false, bestShantenOnly)
    val chitoi = chitoiShanten(tiles, false, bestShantenOnly)
    val kokushi = kokushiShanten(tiles, false, bestShantenOnly)

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
        val goodShapeAdvance = HashSet<Tile>()

        mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, regular)
        mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, chitoi)
        mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, kokushi)

        ShantenWithoutGot(shantenNum, advance, goodShapeAdvance = if (shantenNum == 1) goodShapeAdvance else null)
    } else {
        val discardToAdvance = HashMap<Tile, ShantenWithoutGot>()

        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, regular, bestShantenOnly)
        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, chitoi, bestShantenOnly)
        mergeIntoWithGot(shantenNum, discardToAdvance, patterns, kokushi, bestShantenOnly)

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