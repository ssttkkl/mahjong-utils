package mahjongutils.shanten

import mahjongutils.ErrorInfo
import mahjongutils.ValidationError
import mahjongutils.ValidationException
import mahjongutils.models.Tile

/**
 * 副露判断向听分析参数
 * @param tiles 门前的牌
 * @param chanceTile 副露机会牌（能够吃、碰的牌）
 * @param allowChi 是否允许吃
 * @param bestShantenOnly 仅计算最优向听数的打法（不计算退向打法）
 * @param allowKuikae 是否允许食替
 */
data class FuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowKuikae: Boolean = false
)

enum class FuroChanceShantenArgsErrorInfo(override val message: String) : ErrorInfo

typealias FuroChanceShantenArgsValidationError = ValidationError<FuroChanceShantenArgsErrorInfo>

class FuroChanceShantenArgsValidationException(
    val args: FuroChanceShantenArgs,
    errors: Collection<ValidationError<FuroChanceShantenArgsErrorInfo>>
) : ValidationException(errors)


fun FuroChanceShantenArgs.validate(): Collection<FuroChanceShantenArgsValidationError> = emptyList()

fun FuroChanceShantenArgs.throwOnValidationError() {
    val errors = validate()
    if (errors.isNotEmpty()) {
        throw FuroChanceShantenArgsValidationException(this, errors)
    }
}