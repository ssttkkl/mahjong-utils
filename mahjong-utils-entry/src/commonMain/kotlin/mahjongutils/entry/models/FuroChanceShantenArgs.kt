package mahjongutils.entry.models

import kotlinx.serialization.Serializable
import mahjongutils.models.Tile

@Serializable
data class FuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val bestShantenOnly: Boolean = false,
    val allowKuikae: Boolean = false,
)