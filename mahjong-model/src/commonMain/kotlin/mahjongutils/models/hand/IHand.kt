package mahjongutils.models.hand

import mahjongutils.models.Furo
import mahjongutils.models.FuroType
import mahjongutils.models.Tile

/**
 * （手牌、牌型）
 */
interface IHand {
    /**
     * 门前的牌
     */
    val tilesInHand: List<Tile>
    /**
     * 副露
     */
    val furo: List<Furo>
}

/**
 * 是否门清
 */
val IHand.menzen: Boolean
    get() = furo.all { it.type == FuroType.Ankan }

/**
 * 手牌（包括门前与副露）
 */
val IHand.tiles: List<Tile>
    get() = tilesInHand + furo.flatMap { it.tiles }
