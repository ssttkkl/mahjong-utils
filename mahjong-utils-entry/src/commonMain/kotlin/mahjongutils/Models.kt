package mahjongutils

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.CommonShantenResult
import mahjongutils.shanten.ShantenResult
import mahjongutils.yaku.Yaku

@Serializable
data class ShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = listOf(),
    val calcAdvanceNum: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowAnkan: Boolean = true
)

@Serializable
data class FuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val calcAdvanceNum: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowKuikae: Boolean = false,
)

@Serializable
data class HanHu(
    val han: Int,
    val hu: Int
)


@Serializable
data class HoraArgs(
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

