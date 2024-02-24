package mahjongutils.hora

import kotlinx.serialization.Serializable
import mahjongutils.ErrorInfo
import mahjongutils.ValidationException
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.models.countAsCodeArray
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.shanten.ShantenWithGot
import mahjongutils.yaku.DefaultYakuSerializer
import mahjongutils.yaku.Yaku


/**
 * 和牌分析参数
 *
 * @param tiles 门前的牌
 * @param furo 副露
 * @param shantenResult 向听分析结果
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种（不会对役种合法性进行检查）
 */
@Serializable
data class HoraArgs(
    val tiles: List<Tile>? = null,
    val furo: List<Furo> = DEFAULT_FURO,
    val shantenResult: CommonShantenResult<*>? = null,
    val agari: Tile,
    val tsumo: Boolean,
    val dora: Int = DEFAULT_DORA,
    val selfWind: Wind? = DEFAULT_SELF_WIND,
    val roundWind: Wind? = DEFAULT_ROUND_WIND,
    val extraYaku: Set<@Serializable(DefaultYakuSerializer::class) Yaku> = DEFAULT_EXTRA_YAKU,
    val options: HoraOptions = DEFAULT_OPTIONS,
) {
    companion object {
        internal val DEFAULT_FURO: List<Furo> = emptyList()
        internal val DEFAULT_DORA: Int = 0
        internal val DEFAULT_SELF_WIND: Wind? = null
        internal val DEFAULT_ROUND_WIND: Wind? = null
        internal val DEFAULT_EXTRA_YAKU: Set<Yaku> = emptySet()
        internal val DEFAULT_OPTIONS: HoraOptions = HoraOptions.Default
    }
}

enum class HoraArgsErrorInfo(override val message: String) : ErrorInfo {
    bothTilesAndShantenResultAreNull("either tiles or shantenResult should not be null"),
    tilesIsEmpty("tiles is empty"),
    tooManyFuro("you have too many furo"),
    anyTileMoreThan4("you cannot have any tile more than 4"),
    tilesNumIllegal("the number of your tiles is invalid for hora calculation"),
    agariNotInTiles("agari not in tiles"),
    shantenNotWithGot("your shantenResult must be with got"),
    shantenNotHora("your shantenResult is not hora yet (shantenNum must be -1)")
}


class HoraArgsValidationException(
    val args: HoraArgs,
    errors: Collection<HoraArgsErrorInfo>
) : ValidationException(errors)


fun HoraArgs.validate(): Collection<HoraArgsErrorInfo> = buildList {
    if (tiles == null && shantenResult == null) {
        add(HoraArgsErrorInfo.bothTilesAndShantenResultAreNull)
        return@buildList
    }

    if (tiles != null) {
        if (tiles.isEmpty()) {
            add(HoraArgsErrorInfo.tilesIsEmpty)
        }

        if (furo.size > 4) {
            add(HoraArgsErrorInfo.tooManyFuro)
        }

        val normalizedTilesNum = tiles.size + furo.size * 3
        if (normalizedTilesNum != 14 && normalizedTilesNum != 13) {
            add(HoraArgsErrorInfo.tilesNumIllegal)
        }

        if (normalizedTilesNum == 14 && agari !in tiles) {
            add(HoraArgsErrorInfo.agariNotInTiles)
        }

        if ((tiles + furo.flatMap { it.tiles }).countAsCodeArray().any { it > 4 }) {
            add(HoraArgsErrorInfo.anyTileMoreThan4)
        }
    } else if (shantenResult != null) {
        if (shantenResult.shantenInfo !is ShantenWithGot) {
            add(HoraArgsErrorInfo.shantenNotWithGot)
        } else {
            if (shantenResult.shantenInfo.shantenNum != -1) {
                add(HoraArgsErrorInfo.shantenNotHora)
            }
            if (agari !in shantenResult.hand.tiles) {
                add(HoraArgsErrorInfo.agariNotInTiles)
            }

            val normalizedTilesNum = shantenResult.hand.tiles.size + shantenResult.hand.furo.size * 3
            if (normalizedTilesNum != 14) {
                add(HoraArgsErrorInfo.tilesNumIllegal)
            }
        }
    }
}

@Throws(HoraArgsValidationException::class)
fun HoraArgs.throwOnValidationError() {
    val errors = validate()
    if (errors.isNotEmpty()) {
        throw HoraArgsValidationException(this, errors)
    }
}