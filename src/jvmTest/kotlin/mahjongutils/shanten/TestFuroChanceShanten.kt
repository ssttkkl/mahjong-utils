package mahjongutils.shanten

import mahjongutils.models.Tile
import kotlin.test.Test

class TestFuroChanceShanten {
    @Test
    fun test1() {
        val tiles = Tile.parseTiles("3456778m123457p")
        val result = furoChanceShanten(tiles, Tile.get("7m"))
        print(result)
    }
}