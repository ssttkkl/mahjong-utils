package mahjongutils.cli

import mahjongutils.models.Tile
import mahjongutils.models.Furo

object Formatter {
    fun formatTiles(tiles: List<Tile>): String = tiles.joinToString("")
    
    fun formatHand(tiles: List<Tile>, furo: List<Furo> = emptyList()): String {
        val sorted = if (tiles.size % 3 == 2) {
            tiles.dropLast(1).sorted() + tiles.last()
        } else tiles.sorted()
        return buildString {
            append(formatTiles(sorted))
            if (furo.isNotEmpty()) {
                append(" ")
                append(furo.joinToString(" "))
            }
        }
    }
}
