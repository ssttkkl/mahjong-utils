package mahjongutils.shanten

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
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
 * ????????????
 * @param tiles ????????????
 * @param furo ????????????????????????????????????????????????????????????????????????????????????????????????
 * @param calcAdvanceNum ?????????????????????
 * @param bestShantenOnly ????????????????????????????????????????????????????????????
 * @return ??????????????????
 */
fun shanten(
    tiles: List<Tile>, furo: List<Furo> = emptyList(),
    calcAdvanceNum: Boolean = true,
    bestShantenOnly: Boolean = false,
    allowAnkan: Boolean = true,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles)

    val withGot = tiles.size % 3 == 2
    val k = tiles.size / 3

    if (k != 4) {
        val regular = regularShanten(tiles, furo, calcAdvanceNum, bestShantenOnly, allowAnkan)
        return ShantenResult(
            type = ShantenResult.Type.Union, hand = regular.hand, shantenInfo = regular.shantenInfo,
            regular = regular
        )
    }

    val regular = regularShanten(tiles, furo, false, bestShantenOnly, allowAnkan)
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

        ShantenWithGot(shantenNum, discardToAdvance, (regular.shantenInfo as ShantenWithGot).ankanToAdvance)
    }

    if (calcAdvanceNum) {
        val tilesCount = (tiles + furo.flatMap { it.asMentsu().tiles }).countAsCodeArray()
        shantenInfo = shantenInfo.fillAdvanceNum(tilesCount)
    }

    val hand = Hand(tiles = tiles, furo = furo, patterns = patterns)
    return ShantenResult(
        type = ShantenResult.Type.Union, hand = hand, shantenInfo = shantenInfo,
        regular = regular, chitoi = chitoi, kokushi = kokushi
    )
}