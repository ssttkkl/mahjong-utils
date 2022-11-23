package mahjongutils.hora

import mahjongutils.models.Tile
import mahjongutils.models.Wind

interface HoraInfo {
    val agari: Tile
    val tsumo: Boolean
    val hu: Int
    val selfWind: Wind?
    val roundWind: Wind?
}