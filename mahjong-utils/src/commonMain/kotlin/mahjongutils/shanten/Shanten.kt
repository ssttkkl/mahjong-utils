package mahjongutils.shanten

import mahjongutils.CalcContext
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray
import mahjongutils.models.hand.CommonHandPattern
import mahjongutils.models.hand.Hand
import kotlin.jvm.JvmOverloads
import kotlin.math.min

/**
 * 向听分析参数（内部计算使用）
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param calcAdvanceNum 是否计算进张数
 * @param calcGoodShapeAdvance 是否计算好型进张
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @param allowAnkan 是否允许暗杠
 * @param calcImprovement 是否计算改良
 * @return 向听分析结果
 */
internal data class InternalShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = emptyList(),
    val calcAdvanceNum: Boolean = true,
    val calcGoodShapeAdvance: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowAnkan: Boolean = true,
    val calcImprovement: Boolean = true,
)

/**
 * 向听分析
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果
 */
@JvmOverloads
fun shanten(
    tiles: List<Tile>,
    furo: List<Furo> = emptyList(),
    bestShantenOnly: Boolean = false,
): UnionShantenResult {
    val internalShantenArgs = InternalShantenArgs(
        tiles = tiles,
        furo = furo,
        bestShantenOnly = bestShantenOnly
    )

    val context = CalcContext()
    return context.shanten(internalShantenArgs)
}

/**
 * 向听分析
 * @param args 向听分析参数
 * @return 向听分析结果
 */
fun shanten(
    args: ShantenArgs
): UnionShantenResult {
    val internalShantenArgs = InternalShantenArgs(
        tiles = args.tiles,
        furo = args.furo,
        bestShantenOnly = args.bestShantenOnly
    )

    val context = CalcContext()
    return context.shanten(internalShantenArgs)
}


internal fun CalcContext.shanten(
    args: InternalShantenArgs
): UnionShantenResult = memo(Pair("shanten", args)) {
    with(args) {
        val tiles = ensureLegalTiles(tiles)

        val withGot = tiles.size % 3 == 2
        val k = tiles.size / 3

        if (k != 4) {
            val regular = regularShanten(
                copy(tiles = tiles)
            )
            return UnionShantenResult(
                hand = regular.hand, shantenInfo = regular.shantenInfo,
                regular = regular
            )
        }

        val regular = regularShanten(
            copy(tiles = tiles, calcAdvanceNum = false)
        )
        val chitoi = chitoiShanten(
            InternalShantenArgs(tiles = tiles, calcAdvanceNum = false, bestShantenOnly = bestShantenOnly)
        )
        val kokushi = kokushiShanten(
            InternalShantenArgs(tiles = tiles, calcAdvanceNum = false, bestShantenOnly = bestShantenOnly)
        )

        val shantenNum = min(
            min(
                regular.shantenInfo.shantenNum,
                chitoi.shantenInfo.shantenNum
            ),
            kokushi.shantenInfo.shantenNum
        )
        val patterns = ArrayList<CommonHandPattern>()

        var shantenInfo = if (!withGot) {
            val advance = HashSet<Tile>()
            val goodShapeAdvance = HashSet<Tile>()

            mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, regular)
            mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, chitoi)
            mergeIntoWithoutGot(shantenNum, advance, goodShapeAdvance, patterns, kokushi)

            ShantenWithoutGot(
                shantenNum, advance,
                goodShapeAdvance = if (shantenNum == 1) goodShapeAdvance else null,
                improvement = (regular.shantenInfo.asWithoutGot).improvement
                    ?: (if (shantenNum == 0) emptyMap() else null),
                goodShapeImprovement = (regular.shantenInfo.asWithoutGot).goodShapeImprovement
                    ?: (if (shantenNum == 0) emptyMap() else null)
            )
        } else {
            val discardToAdvance = HashMap<Tile, ShantenWithoutGot>()

            mergeIntoWithGot(shantenNum, discardToAdvance, patterns, regular, bestShantenOnly)
            mergeIntoWithGot(shantenNum, discardToAdvance, patterns, chitoi, bestShantenOnly)
            mergeIntoWithGot(shantenNum, discardToAdvance, patterns, kokushi, bestShantenOnly)

            ShantenWithGot(
                shantenNum, discardToAdvance,
                ankanToAdvance = (regular.shantenInfo.asWithGot).ankanToAdvance
            )
        }

        if (calcAdvanceNum) {
            val tilesCount = (tiles + furo.flatMap { it.asMentsu().tiles }).countAsCodeArray()
            shantenInfo = shantenInfo.fillNum(tilesCount)
        }

        val hand = Hand(tiles = tiles, furo = furo, patterns = patterns)
        return UnionShantenResult(
            hand = hand, shantenInfo = shantenInfo,
            regular = regular, chitoi = chitoi, kokushi = kokushi
        )
    }
}


private fun <S : CommonShanten> mergeIntoWithoutGot(
    targetShantenNum: Int,
    advance: MutableSet<Tile>,
    goodShapeAdvance: MutableSet<Tile>,
    patterns: MutableCollection<CommonHandPattern>,
    result: ShantenResult<S, *>
) {
    if (result.shantenInfo.shantenNum == targetShantenNum) {
        val shantenInfo = result.shantenInfo.asWithoutGot
        advance += shantenInfo.advance
        patterns += result.hand.patterns
        if (shantenInfo.goodShapeAdvance != null) {
            goodShapeAdvance += shantenInfo.goodShapeAdvance
        }
    }
}

private fun <S : CommonShanten> mergeIntoWithGot(
    targetShantenNum: Int,
    discardToAdvance: MutableMap<Tile, ShantenWithoutGot>,
    patterns: MutableCollection<CommonHandPattern>,
    result: ShantenResult<S, *>,
    bestShantenOnly: Boolean
) {
    val shantenInfo = result.shantenInfo.asWithGot
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