package mahjongutils.models.hand

import mahjongutils.models.Melded
import mahjongutils.models.MeldedType
import mahjongutils.models.Tile

/**
 * （手牌、牌型）
 */
interface IHand {
    /**
     * 手牌（手里的牌，不包含副露的牌）
     */
    val tiles: List<Tile>
    /**
     * 副露
     */
    val meldeds: List<Melded>
}

/**
 * 是否已摸牌状态
 */
val IHand.isAfterDrawn: Boolean
    get() = tiles.size % 3 == 2

/**
 * 是否门清
 */
val IHand.isClosed: Boolean
    get() = meldeds.all { it.type == MeldedType.ConcealedKong }

/**
 * 手牌（包括门前与副露）
 */
val IHand.allTiles: List<Tile>
    get() = tiles + meldeds.flatMap { it.tiles }
