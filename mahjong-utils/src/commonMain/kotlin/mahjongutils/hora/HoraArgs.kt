package mahjongutils.hora

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.yaku.DefaultYakuSerializer
import mahjongutils.yaku.Yaku
import mahjongutils.yaku.YakuSerializer


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
