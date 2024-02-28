package mahjongutils.shanten

import kotlinx.serialization.Serializable
import mahjongutils.ErrorInfo
import mahjongutils.ValidationException
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray

/**
 * 副露判断向听分析参数
 * @param tiles 门前的牌
 * @param chanceTile 副露机会牌（能够吃、碰的牌）
 * @param allowChi 是否允许吃
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @param allowKuikae 是否允许食替
 */
@Serializable
data class FuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowKuikae: Boolean = false
)

enum class FuroChanceShantenArgsErrorInfo(override val message: String) : ErrorInfo {
    tilesIsEmpty("tiles is empty"),
    tooManyTiles("you have too many tiles"),
    anyTileMoreThan4("you cannot have any tile more than 4"),
    tilesNumIllegal("you should have 3n+1 tiles for furo chance shanten calculation")
}

class FuroChanceShantenArgsValidationException(
    val args: FuroChanceShantenArgs,
    errors: Collection<FuroChanceShantenArgsErrorInfo>
) : ValidationException(errors)


fun FuroChanceShantenArgs.validate(): Collection<FuroChanceShantenArgsErrorInfo> = buildList {
    if (tiles.isEmpty()) {
        add(FuroChanceShantenArgsErrorInfo.tilesIsEmpty)
    }

    if (tiles.size > 14) {
        add(FuroChanceShantenArgsErrorInfo.tooManyTiles)
    }

    if ((tiles + chanceTile).countAsCodeArray().any { it > 4 }) {
        add(FuroChanceShantenArgsErrorInfo.anyTileMoreThan4)
    }

    if (tiles.size % 3 == 0 || tiles.size % 3 == 2) {
        add(FuroChanceShantenArgsErrorInfo.tilesNumIllegal)
    }
}

@Throws(FuroChanceShantenArgsValidationException::class)
fun FuroChanceShantenArgs.throwOnValidationError() {
    val errors = validate()
    if (errors.isNotEmpty()) {
        throw FuroChanceShantenArgsValidationException(this, errors)
    }
}