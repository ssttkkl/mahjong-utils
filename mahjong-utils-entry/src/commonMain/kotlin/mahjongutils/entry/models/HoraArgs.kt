package mahjongutils.entry.models

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.yaku.Yaku

@Serializable
internal data class HoraArgs(
    val tiles: List<Tile>? = null,
    val furo: List<Furo>? = null,
    val shantenResult: CommonShantenResult<*>? = null,
    val agari: Tile,
    val tsumo: Boolean,
    val dora: Int = 0,
    val selfWind: Wind? = null,
    val roundWind: Wind? = null,
    val extraYaku: Set<Yaku> = emptySet()
)