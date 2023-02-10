package mahjongutils.models.hand

import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Tile

/**
 * 手牌
 */
@Serializable
data class Hand<out P : HandPattern>(
    /**
     * 门前的牌
     */
    val tiles: List<Tile>,
    override val furo: List<Furo>,
    /**
     * 手牌形
     */
    val patterns: Collection<P>
) : IHasFuro