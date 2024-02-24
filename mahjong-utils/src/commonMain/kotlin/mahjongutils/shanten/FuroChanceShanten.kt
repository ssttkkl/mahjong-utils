package mahjongutils.shanten

import mahjongutils.CalcContext
import mahjongutils.models.*
import mahjongutils.shanten.helpers.normalizeTiles
import kotlin.math.min

internal data class InternalFuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val calcAdvanceNum: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowKuikae: Boolean = false
)

/**
 * 副露判断向听分析
 * @param tiles 门前的牌
 * @param chanceTile 副露机会牌（能够吃、碰的牌）
 * @param allowChi 是否允许吃
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @param allowKuikae 是否允许食替
 * @return 向听分析结果（其中shantenInfo必定为ShantenWithFuroChance类型）
 */
fun furoChanceShanten(
    tiles: List<Tile>,
    chanceTile: Tile,
    allowChi: Boolean = true,
    bestShantenOnly: Boolean = false,
    allowKuikae: Boolean = false
): FuroChanceShantenResult {
    return furoChanceShanten(
        FuroChanceShantenArgs(
            tiles = tiles,
            chanceTile = chanceTile,
            allowChi = allowChi,
            bestShantenOnly = bestShantenOnly,
            allowKuikae = allowKuikae
        )
    )
}

fun furoChanceShanten(
    args: FuroChanceShantenArgs
): FuroChanceShantenResult {
    args.throwOnValidationError()

    val internalShantenArgs = InternalFuroChanceShantenArgs(
        tiles = args.tiles,
        chanceTile = args.chanceTile,
        allowChi = args.allowChi,
        bestShantenOnly = args.bestShantenOnly,
        allowKuikae = args.allowKuikae,
    )

    val context = CalcContext()
    return context.furoChanceShanten(internalShantenArgs)
}

internal fun CalcContext.furoChanceShanten(
    args: InternalFuroChanceShantenArgs
): FuroChanceShantenResult = memo(Pair("furoChanceShanten", args)) {
    with(args) {
        val tiles = normalizeTiles(tiles)
        val tilesCount = tiles.countAsCodeArray()

        val passShanten = regularShanten(InternalShantenArgs(tiles))
        val pass = passShanten.shantenInfo.asWithoutGot

        val canRon = pass.shantenNum == 0 && chanceTile in pass.advance

        // 碰
        val pon = if (tilesCount[chanceTile.code] >= 2) {
            val tilesAfterPon = tiles - chanceTile - chanceTile
            val shantenAfterPon = regularShanten(
                InternalShantenArgs(
                    tilesAfterPon,
                    listOf(Pon(chanceTile)),
                    calcAdvanceNum = calcAdvanceNum,
                    bestShantenOnly = bestShantenOnly,
                    allowAnkan = false
                )
            )
            shantenAfterPon.shantenInfo.asWithGot
        } else {
            null
        }

        // 吃
        val chi = if (allowChi && chanceTile.type != TileType.Z) {
            val materials = buildList {
                // 边张
                if (chanceTile.num == 3 && tilesCount[chanceTile.code - 1] >= 1 && tilesCount[chanceTile.code - 2] >= 1) {
                    add(Penchan(chanceTile.advance(-2)))
                }
                if (chanceTile.num == 7 && tilesCount[chanceTile.code + 1] >= 1 && tilesCount[chanceTile.code + 2] >= 1) {
                    add(Penchan(chanceTile.advance(1)))
                }

                // 两面
                if (chanceTile.num in 4..9 && tilesCount[chanceTile.code - 1] >= 1 && tilesCount[chanceTile.code - 2] >= 1) {
                    add(Ryanmen(chanceTile.advance(-2)))
                }
                if (chanceTile.num in 1..6 && tilesCount[chanceTile.code + 1] >= 1 && tilesCount[chanceTile.code + 2] >= 1) {
                    add(Ryanmen(chanceTile.advance(1)))
                }

                // 坎张
                if (chanceTile.num in 2..8 && tilesCount[chanceTile.code - 1] >= 1 && tilesCount[chanceTile.code + 1] >= 1) {
                    add(Kanchan(chanceTile.advance(-1)))
                }
            }

            materials.associateWith { tt ->
                val tilesAfterChi = tiles - tt.first - tt.second
                val shantenAfterChi = regularShanten(
                    InternalShantenArgs(
                        tilesAfterChi,
                        listOf(Chi((tt.withWaiting(chanceTile) as Shuntsu).tile)),
                        calcAdvanceNum = calcAdvanceNum,
                        bestShantenOnly = bestShantenOnly,
                        allowAnkan = false
                    )
                )
                val shantenInfo = shantenAfterChi.shantenInfo.asWithGot
                if (!allowKuikae) {
                    val discardToAdvance = shantenInfo.discardToAdvance.filterKeys {
                        it !== chanceTile && (tt !is Ryanmen || (
                                chanceTile > tt.second && it !== tt.first.advance(-1)
                                        || chanceTile < tt.second && it !== tt.second.advance(1))
                                )
                    }
                    shantenInfo.copy(
                        shantenNum = discardToAdvance.values.minOfOrNull { it.shantenNum } ?: 9999,
                        discardToAdvance = discardToAdvance
                    )
                } else {
                    shantenInfo
                }
            }.filterValues { it.discardToAdvance.isNotEmpty() }
        } else {
            emptyMap()
        }

        // minkan
        val minkan = if (tilesCount[chanceTile.code] >= 3) {
            val tilesAfterMinkan = tiles - chanceTile - chanceTile - chanceTile
            val shantenAfterMinkan = regularShanten(
                InternalShantenArgs(
                    tilesAfterMinkan,
                    listOf(Kan(chanceTile)),
                    calcAdvanceNum = calcAdvanceNum,
                    bestShantenOnly = bestShantenOnly,
                    allowAnkan = false
                )
            )
            shantenAfterMinkan.shantenInfo.asWithoutGot
        } else {
            null
        }

        var shantenNum: Int
        if (canRon) {
            shantenNum = -1
        } else {
            shantenNum = pass.shantenNum
            chi.minOfOrNull { it.value.shantenNum }?.let {
                shantenNum = min(shantenNum, it)
            }
            pon?.shantenNum?.let {
                shantenNum = min(shantenNum, it)
            }
            minkan?.shantenNum?.let {
                shantenNum = min(shantenNum, it)
            }
        }

        return if (bestShantenOnly) {
            val pass_ = if (pass.shantenNum == shantenNum) pass else null
            val chi_ = chi.filterValues { it.shantenNum == shantenNum }
            val pon_ = if (pon?.shantenNum == shantenNum) pon else null
            val minkan_ = if (minkan?.shantenNum == shantenNum) minkan else null

            FuroChanceShantenResult(
                hand = passShanten.hand,
                shantenInfo = ShantenWithFuroChance(shantenNum, canRon, pass_, chi_, pon_, minkan_)
            )
        } else {
            FuroChanceShantenResult(
                hand = passShanten.hand,
                shantenInfo = ShantenWithFuroChance(shantenNum, canRon, pass, chi, pon, minkan)
            )
        }
    }
}