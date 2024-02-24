package mahjongutils.hora

import kotlinx.serialization.Serializable
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_DORA
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_EXTRA_YAKU
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_FURO
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_OPTIONS
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_ROUND_WIND
import mahjongutils.hora.HoraArgs.Companion.DEFAULT_SELF_WIND
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.yaku.Yaku

/**
 * 和牌分析参数
 */
@Serializable
sealed interface HoraArgs {
    val agari: Tile
    val tsumo: Boolean
    val dora: Int
    val selfWind: Wind?
    val roundWind: Wind?
    val extraYaku: Set<Yaku>
    val options: HoraOptions

    companion object {
        internal val DEFAULT_FURO: List<Furo> = emptyList()
        internal val DEFAULT_DORA: Int = 0
        internal val DEFAULT_SELF_WIND: Wind? = null
        internal val DEFAULT_ROUND_WIND: Wind? = null
        internal val DEFAULT_EXTRA_YAKU: Set<Yaku> = emptySet()
        internal val DEFAULT_OPTIONS: HoraOptions = HoraOptions.Default

        fun create(
            tiles: List<Tile>,
            furo: List<Furo> = DEFAULT_FURO,
            agari: Tile,
            tsumo: Boolean,
            dora: Int = DEFAULT_DORA,
            selfWind: Wind? = DEFAULT_SELF_WIND,
            roundWind: Wind? = DEFAULT_ROUND_WIND,
            extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
            options: HoraOptions = DEFAULT_OPTIONS,
        ) = HoraArgsWithTiles(
            tiles = tiles,
            furo = furo,
            agari = agari,
            tsumo = tsumo,
            dora = dora,
            selfWind = selfWind,
            roundWind = roundWind,
            extraYaku = extraYaku,
            options = options
        )

        fun create(
            shantenResult: CommonShantenResult<*>,
            agari: Tile,
            tsumo: Boolean,
            dora: Int = DEFAULT_DORA,
            selfWind: Wind? = DEFAULT_SELF_WIND,
            roundWind: Wind? = DEFAULT_ROUND_WIND,
            extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
            options: HoraOptions = DEFAULT_OPTIONS,
        ) = HoraArgsWithShantenResult(
            shantenResult = shantenResult,
            agari = agari,
            tsumo = tsumo,
            dora = dora,
            selfWind = selfWind,
            roundWind = roundWind,
            extraYaku = extraYaku,
            options = options
        )
    }
}

/**
 * 和牌分析参数
 *
 * @param tiles 门前的牌
 * @param furo 副露
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种（不会对役种合法性进行检查）
 */
data class HoraArgsWithTiles(
    val tiles: List<Tile>,
    val furo: List<Furo> = DEFAULT_FURO,
    override val agari: Tile,
    override val tsumo: Boolean,
    override val dora: Int = DEFAULT_DORA,
    override val selfWind: Wind? = DEFAULT_SELF_WIND,
    override val roundWind: Wind? = DEFAULT_ROUND_WIND,
    override val extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
    override val options: HoraOptions = DEFAULT_OPTIONS,
) : HoraArgs

/**
 * 和牌分析参数
 *
 * @param shantenResult 向听分析结果
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种（不会对役种合法性进行检查）
 */
data class HoraArgsWithShantenResult(
    val shantenResult: CommonShantenResult<*>,
    override val agari: Tile,
    override val tsumo: Boolean,
    override val dora: Int = DEFAULT_DORA,
    override val selfWind: Wind? = DEFAULT_SELF_WIND,
    override val roundWind: Wind? = DEFAULT_ROUND_WIND,
    override val extraYaku: Set<Yaku> = DEFAULT_EXTRA_YAKU,
    override val options: HoraOptions = DEFAULT_OPTIONS,
) : HoraArgs