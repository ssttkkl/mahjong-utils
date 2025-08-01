package mahjongutils.models.hand

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile

/**
 * 手牌
 */
@Serializable
data class Hand(
    /**
     * 门前的牌
     */
    override val tilesInHand: List<Tile>,
    override val furo: List<Furo>
) : IHand

val Hand.isWithDraw: Boolean
    get() = tilesInHand.size % 3 == 2
