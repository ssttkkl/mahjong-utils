package mahjongutils.models

import kotlin.test.Test
import kotlin.test.assertEquals

class WindTest {
    @Test
    fun testWindTileMapping() {
        // 测试风与牌的对应关系
        assertEquals(Tile["1z"], Wind.East.tile)
        assertEquals(Tile["2z"], Wind.South.tile)
        assertEquals(Tile["3z"], Wind.West.tile)
        assertEquals(Tile["4z"], Wind.North.tile)
    }
}

