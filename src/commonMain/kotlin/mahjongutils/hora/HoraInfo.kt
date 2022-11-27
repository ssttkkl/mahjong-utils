package mahjongutils.hora

import mahjongutils.models.Tile
import mahjongutils.models.Wind

/**
 * 和牌信息
 */
interface HoraInfo {
    /**
     * 和牌张
     */
    val agari: Tile

    /**
     * 是否自摸
     */
    val tsumo: Boolean

    /**
     * 符数
     */
    val hu: Int

    /**
     * 自风
     */
    val selfWind: Wind?

    /**
     * 场风
     */
    val roundWind: Wind?
}