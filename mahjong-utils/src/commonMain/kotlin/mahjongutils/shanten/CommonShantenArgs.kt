package mahjongutils.shanten

import kotlinx.serialization.Serializable
import mahjongutils.ErrorInfo
import mahjongutils.ValidationError
import mahjongutils.ValidationException
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.countAsCodeArray


/**
 * 向听分析参数
 * @param tiles 门前的牌
 * @param furo 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @param mode 向听分析模式
 *
 * @see [ShantenMode]
 */
@Serializable
data class CommonShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = emptyList(),
    val bestShantenOnly: Boolean = false
)

enum class CommonShantenArgsErrorInfo(override val message: String) : ErrorInfo {
    tilesIsEmpty("tiles is empty"),
    tooManyFuro("you have too many furo"),
    tooManyTiles("you have too many tiles"),
    anyTileMoreThan4("you cannot have any tile more than 4"),
    tilesDividedInto3("you should have 3n+1 or 3n+2 tiles for shanten calculation")
}

typealias CommonShantenArgsValidationError = ValidationError<CommonShantenArgsErrorInfo>

class CommonShantenArgsValidationException(
    val args: CommonShantenArgs,
    errors: Collection<ValidationError<CommonShantenArgsErrorInfo>>
) : ValidationException(errors)

fun CommonShantenArgs.validate(): Collection<CommonShantenArgsValidationError> = buildList {
    if (tiles.isEmpty()) {
        add(
            CommonShantenArgsValidationError(
                this@validate::tiles.name,
                CommonShantenArgsErrorInfo.tilesIsEmpty
            )
        )
    }

    if (furo.size > 4) {
        add(
            CommonShantenArgsValidationError(
                this@validate::tiles.name,
                CommonShantenArgsErrorInfo.tooManyFuro
            )
        )
    }

    if (tiles.size + furo.size * 3 > 14) {
        add(
            CommonShantenArgsValidationError(
                this@validate::tiles.name,
                CommonShantenArgsErrorInfo.tooManyTiles
            )
        )
    }

    if ((tiles + furo.flatMap { it.tiles }).countAsCodeArray().any { it > 4 }) {
        add(
            CommonShantenArgsValidationError(
                this@validate::tiles.name,
                CommonShantenArgsErrorInfo.anyTileMoreThan4
            )
        )
    }

    if (tiles.size % 3 == 0) {
        add(
            CommonShantenArgsValidationError(
                this@validate::tiles.name,
                CommonShantenArgsErrorInfo.tilesDividedInto3
            )
        )
    }
}

fun CommonShantenArgs.throwOnValidationError() {
    val errors = validate()
    if (errors.isNotEmpty()) {
        throw CommonShantenArgsValidationException(this, errors)
    }
}