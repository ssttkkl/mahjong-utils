package mahjongutils.models.hand

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile

@Serializable
data class Hand(
    val tiles: List<Tile>,
    override val furo: List<Furo>,
    val patterns: Collection<HandPattern>
) : IHasFuro