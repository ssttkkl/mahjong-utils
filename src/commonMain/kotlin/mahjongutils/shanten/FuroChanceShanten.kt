package mahjongutils.shanten

import mahjongutils.models.*
import kotlin.math.min

/**
 * 副露判断向听分析
 * @param tiles 门前的牌
 * @param chanceTile 副露机会牌（能够吃、碰的牌）
 * @param allowChi 是否允许吃
 * @param calcAdvanceNum 是否计算进张数
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @return 向听分析结果（其中shantenInfo必定为ShantenWithFuroChance类型）
 */
fun furoChanceShanten(
    tiles: List<Tile>,
    chanceTile: Tile,
    allowChi: Boolean = true,
    calcAdvanceNum: Boolean = true,
    bestShantenOnly: Boolean = false,
): ShantenResult {
    val tiles = ensureLegalTiles(tiles, allowWithGot = false)
    val tilesGroup = tiles.groupByCode()

    val passShanten = regularShanten(tiles)
    val pass = passShanten.shantenInfo as ShantenWithoutGot

    // 碰
    val pon = if (tilesGroup[chanceTile.code] >= 2) {
        val tilesAfterPon = tiles - chanceTile - chanceTile
        val shantenAfterPon = regularShanten(
            tilesAfterPon,
            calcAdvanceNum = calcAdvanceNum,
            bestShantenOnly = bestShantenOnly
        )
        shantenAfterPon.shantenInfo as ShantenWithGot
    } else {
        null
    }

    // 吃
    val chi = if (allowChi && chanceTile.type != TileType.Z) {
        val materials = buildList {
            // 边张
            if (chanceTile.num == 3 && tilesGroup[chanceTile.code - 1] >= 1 && tilesGroup[chanceTile.code - 2] >= 1) {
                add(Penchan(chanceTile.advance(-2)))
            }
            if (chanceTile.num == 7 && tilesGroup[chanceTile.code + 1] >= 1 && tilesGroup[chanceTile.code + 2] >= 1) {
                add(Penchan(chanceTile.advance(1)))
            }

            // 两面
            if (chanceTile.num in 4..9 && tilesGroup[chanceTile.code - 1] >= 1 && tilesGroup[chanceTile.code - 2] >= 1) {
                add(Ryanmen(chanceTile.advance(-2)))
            }
            if (chanceTile.num in 1..6 && tilesGroup[chanceTile.code + 1] >= 1 && tilesGroup[chanceTile.code + 2] >= 1) {
                add(Ryanmen(chanceTile.advance(1)))
            }

            // 坎张
            if (chanceTile.num in 2..8 && tilesGroup[chanceTile.code - 1] >= 1 && tilesGroup[chanceTile.code + 1] >= 1) {
                add(Kanchan(chanceTile.advance(-1)))
            }
        }

        materials.associateWith { tt ->
            val tilesAfterChi = tiles - tt.first - tt.second
            val shantenAfterChi = regularShanten(
                tilesAfterChi,
                calcAdvanceNum = calcAdvanceNum,
                bestShantenOnly = bestShantenOnly
            )
            shantenAfterChi.shantenInfo as ShantenWithGot
        }
    } else {
        emptyMap()
    }

    // minkan
    val minkan = if (tilesGroup[chanceTile.code] >= 3) {
        val tilesAfterMinkan = tiles - chanceTile - chanceTile - chanceTile
        val shantenAfterMinkan = regularShanten(
            tilesAfterMinkan,
            calcAdvanceNum = calcAdvanceNum,
            bestShantenOnly = bestShantenOnly
        )
        shantenAfterMinkan.shantenInfo as ShantenWithoutGot
    } else {
        null
    }

    var shantenNum = pass.shantenNum
    chi.minOfOrNull { it.value.shantenNum }?.let {
        shantenNum = min(shantenNum, it)
    }
    pon?.shantenNum?.let {
        shantenNum = min(shantenNum, it)
    }
    minkan?.shantenNum?.let {
        shantenNum = min(shantenNum, it)
    }

    return ShantenResult(
        type = ShantenResult.Type.FuroChance, hand = passShanten.hand,
        shantenInfo = ShantenWithFuroChance(shantenNum, pass, chi, pon, minkan)
    )
}