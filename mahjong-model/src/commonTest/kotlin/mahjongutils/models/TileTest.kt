package mahjongutils.models


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class TileTest {

    @Test
    fun testTileCreation() {
        // 测试通过类型和数字创建牌
        val tile1 = Tile.get(TileType.M, 1)
        assertEquals(TileType.M, tile1.type)
        assertEquals(1, tile1.num)
        assertEquals(1, tile1.realNum)

        // 测试红宝牌
        val redDora = Tile.get(TileType.M, 0)
        assertEquals(TileType.M, redDora.type)
        assertEquals(0, redDora.num)
        assertEquals(5, redDora.realNum)

        // 测试通过文本创建牌
        val tile2 = Tile.get("1m")
        assertEquals(TileType.M, tile2.type)
        assertEquals(1, tile2.num)

        // 测试字牌
        val windTile = Tile.get(TileType.Z, 1)
        assertEquals(TileType.Z, windTile.type)
        assertEquals(1, windTile.num)
        assertTrue(windTile.isWind)
        assertFalse(windTile.isSangen)

        val sangenTile = Tile.get(TileType.Z, 5)
        assertTrue(sangenTile.isSangen)
        assertFalse(sangenTile.isWind)
    }

    @Test
    fun testTileComparison() {
        val tile1 = Tile.get(TileType.M, 1)
        val tile2 = Tile.get(TileType.M, 2)
        val tile3 = Tile.get(TileType.P, 1)

        assertTrue(tile1 < tile2)
        assertTrue(tile2 < tile3)
        assertTrue(tile1 < tile3)

        // 测试红宝牌比较
        val redDora = Tile.get(TileType.M, 0)
        val tile5 = Tile.get(TileType.M, 5)
        val tile6 = Tile.get(TileType.M, 6)

        assertTrue(redDora > tile1)
        assertTrue(redDora < tile6)
        assertNotEquals(redDora, tile5) // 红5和普通5不相等
    }

    @Test
    fun testTileAdvance() {
        val tile1 = Tile.get(TileType.M, 1)
        val tile2 = Tile.get(TileType.M, 2)
        val tile3 = Tile.get(TileType.M, 3)

        assertEquals(tile2, tile1.advance(1))
        assertEquals(tile3, tile1.advance(2))
        assertEquals(tile1, tile3.advance(-2))

        // 测试红宝牌的advance
        val redDora = Tile.get(TileType.M, 0)
        assertEquals(Tile.get(TileType.M, 6), redDora.advance(1))
        assertEquals(Tile.get(TileType.M, 4), redDora.advance(-1))
    }

    @Test
    fun testTileDistance() {
        val tile1 = Tile.get(TileType.M, 1)
        val tile3 = Tile.get(TileType.M, 3)
        val redDora = Tile.get(TileType.M, 0)

        assertEquals(2, tile3.distance(tile1))
        assertEquals(-2, tile1.distance(tile3))
        assertEquals(0, tile1.distance(tile1))

        // 测试红宝牌的distance
        assertEquals(0, redDora.distance(Tile.get(TileType.M, 5)))
        assertEquals(-1, redDora.distance(Tile.get(TileType.M, 6)))
        assertEquals(1, redDora.distance(Tile.get(TileType.M, 4)))
    }

    @Test
    fun testParseTiles() {
        val tiles = Tile.parseTiles("123m456p789s")
        assertEquals(9, tiles.size)
        assertEquals(Tile.get(TileType.M, 1), tiles[0])
        assertEquals(Tile.get(TileType.M, 2), tiles[1])
        assertEquals(Tile.get(TileType.M, 3), tiles[2])
        assertEquals(Tile.get(TileType.P, 4), tiles[3])
        assertEquals(Tile.get(TileType.P, 5), tiles[4])
        assertEquals(Tile.get(TileType.P, 6), tiles[5])
        assertEquals(Tile.get(TileType.S, 7), tiles[6])
        assertEquals(Tile.get(TileType.S, 8), tiles[7])
        assertEquals(Tile.get(TileType.S, 9), tiles[8])

        // 测试红宝牌
        val tilesWithRedDora = Tile.parseTiles("0m0p0s")
        assertEquals(3, tilesWithRedDora.size)
        assertEquals(Tile.get(TileType.M, 0), tilesWithRedDora[0])
        assertEquals(Tile.get(TileType.P, 0), tilesWithRedDora[1])
        assertEquals(Tile.get(TileType.S, 0), tilesWithRedDora[2])

        // 测试字牌
        val tilesWithZ = Tile.parseTiles("1234567z")
        assertEquals(7, tilesWithZ.size)
        for (i in 1..7) {
            assertEquals(Tile.get(TileType.Z, i), tilesWithZ[i-1])
        }
    }

    @Test
    fun testToTilesString() {
        val tiles = listOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 3),
            Tile.get(TileType.P, 4),
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 6),
            Tile.get(TileType.S, 7),
            Tile.get(TileType.S, 8),
            Tile.get(TileType.S, 9)
        )

        assertEquals("123m456p789s", tiles.toTilesString())
        assertEquals("123M456P789S", tiles.toTilesString(false))

        // 测试红宝牌
        val tilesWithRedDora = listOf(
            Tile.get(TileType.M, 0),
            Tile.get(TileType.P, 0),
            Tile.get(TileType.S, 0)
        )

        assertEquals("0m0p0s", tilesWithRedDora.toTilesString())
    }

    @Test
    fun testCountAsMap() {
        val tiles = Tile.parseTiles("11122233m")
        val countMap = tiles.countAsMap()

        assertEquals(3, countMap.size)
        assertEquals(3, countMap[Tile.get(TileType.M, 1)])
        assertEquals(3, countMap[Tile.get(TileType.M, 2)])
        assertEquals(2, countMap[Tile.get(TileType.M, 3)])
    }

    @Test
    fun testCountAsCodeArray() {
        val tiles = Tile.parseTiles("11122233m")
        val codeArray = tiles.countAsCodeArray()

        assertEquals(3, codeArray[Tile.get(TileType.M, 1).code])
        assertEquals(3, codeArray[Tile.get(TileType.M, 2).code])
        assertEquals(2, codeArray[Tile.get(TileType.M, 3).code])
        assertEquals(0, codeArray[Tile.get(TileType.M, 4).code])
    }

    @Test
    fun testInvalidTileCreation() {
        // 测试无效的牌编号
        assertFailsWith(IllegalArgumentException::class) {
            Tile.get(100)
        }

        // 测试无效的牌文本
        assertFailsWith(IllegalArgumentException::class) {
            Tile.get("10m")
        }

        assertFailsWith(IllegalArgumentException::class) {
            Tile.get("1x")
        }

        // 测试无效的牌解析
        assertFailsWith(IllegalArgumentException::class) {
            Tile.parseTiles("123m456x")
        }

        assertFailsWith(IllegalArgumentException::class) {
            Tile.parseTiles("123m456")
        }
    }
}

