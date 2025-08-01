package mahjongutils.models.hand

import kotlinx.serialization.Serializable
import mahjongutils.models.Melded
import mahjongutils.models.Tile

/**
 * 手牌
 */
@Serializable
data class Hand(
    override val tiles: List<Tile>,
    override val meldeds: List<Melded>
) : IHand
