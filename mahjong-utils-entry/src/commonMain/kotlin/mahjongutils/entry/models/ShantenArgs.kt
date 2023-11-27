package mahjongutils.entry.models

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile

@Serializable
data class ShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = listOf(),
    val bestShantenOnly: Boolean = false
)