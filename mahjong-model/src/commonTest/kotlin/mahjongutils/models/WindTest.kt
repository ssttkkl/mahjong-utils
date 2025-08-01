package mahjongutils.models


import kotlin.test.Test
import kotlin.test.assertEquals

class WindTest {

    @Test
    fun testWindTile() {
        // 测试东风的牌
        assertEquals(Tile.get(TileType.Z, 1), Wind.East.tile)

        // 测试南风的牌
        assertEquals(Tile.get(TileType.Z, 2), Wind.South.tile)

        // 测试西风的牌
        assertEquals(Tile.get(TileType.Z, 3), Wind.West.tile)

        // 测试北风的牌
        assertEquals(Tile.get(TileType.Z, 4), Wind.North.tile)
    }

    @Test
    fun testWindValues() {
        // 测试风的枚举值
        val winds = Wind.values()
        assertEquals(4, winds.size)
        assertEquals(Wind.East, winds[0])
        assertEquals(Wind.South, winds[1])
        assertEquals(Wind.West, winds[2])
        assertEquals(Wind.North, winds[3])
    }
}

