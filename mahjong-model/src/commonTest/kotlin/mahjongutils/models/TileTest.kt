package mahjongutils.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TileTest {
    @Test
    fun testTileCreation() {
        // 测试通过编码获取牌
        val tile1 = Tile[11]
        assertEquals(TileType.Dot, tile1.type)
        assertEquals(1, tile1.num)

        // 测试通过类型和数字获取牌
        val tile2 = Tile[TileType.Dot, 5]
        assertEquals(TileType.Dot, tile2.type)
        assertEquals(5, tile2.num)

        // 测试通过文本获取牌
        val tile3 = Tile["7s"]
        assertEquals(TileType.Bamboo, tile3.type)
        assertEquals(7, tile3.num)

        // 测试无效编码
        assertFailsWith<IllegalArgumentException> { Tile[0] }
        assertFailsWith<IllegalArgumentException> { Tile[38] }

        // 测试getOrNull方法
        assertNull(Tile.getOrNull(0))
        assertNull(Tile.getOrNull(38))
        assertNotNull(Tile.getOrNull(11))
    }

    @Test
    fun testTileAdvance() {
        val tile = Tile["1p"] // 1筒
        val advanced = tile.advance(2)
        assertEquals(13, advanced.code) // 应该是3筒
        assertEquals(TileType.Dot, advanced.type)
        assertEquals(3, advanced.num)
    }

    @Test
    fun testTileDistance() {
        val tile1 = Tile["1p"] // 1筒
        val tile2 = Tile["4p"] // 4筒
        assertEquals(-3, tile1.distance(tile2))
        assertEquals(3, tile2.distance(tile1))
    }

    @Test
    fun testTileToString() {
        val tile = Tile["1s"] // 1索
        assertEquals("1s", tile.toString())
    }

    @Test
    fun testTileComparison() {
        val tile1 = Tile["1m"] // 1万
        val tile2 = Tile["1p"] // 1筒
        val tile3 = Tile["1s"] // 1索

        assertTrue(tile1 < tile2)
        assertTrue(tile2 < tile3)
    }

    @Test
    fun testParseTiles() {
        val tiles = Tile.parseTiles("123m456p789s")
        assertEquals(9, tiles.size)
        assertEquals(Tile["1m"], tiles[0])
        assertEquals(Tile["2m"], tiles[1])
        assertEquals(Tile["3m"], tiles[2])
        assertEquals(Tile["4p"], tiles[3])
        assertEquals(Tile["5p"], tiles[4])
        assertEquals(Tile["6p"], tiles[5])
        assertEquals(Tile["7s"], tiles[6])
        assertEquals(Tile["8s"], tiles[7])
        assertEquals(Tile["9s"], tiles[8])

        // 测试无效输入
        assertFailsWith<IllegalArgumentException> { Tile.parseTiles("123") }
        assertFailsWith<IllegalArgumentException> { Tile.parseTiles("123x") }
    }

    @Test
    fun testToTilesString() {
        val tiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["4p"], Tile["5p"], Tile["6p"])
        assertEquals("123M456P", tiles.toTilesString(false))
        assertEquals("123m456p", tiles.toTilesString())
    }

    @Test
    fun testCountAsMap() {
        val tiles = listOf(Tile["1m"], Tile["1m"], Tile["2m"], Tile["3m"], Tile["3m"])
        val countMap = tiles.countAsMap()
        assertEquals(2, countMap[Tile["1m"]])
        assertEquals(1, countMap[Tile["2m"]])
        assertEquals(2, countMap[Tile["3m"]])
    }

    @Test
    fun testCountAsCodeArray() {
        val tiles = listOf(Tile["1m"], Tile["1m"], Tile["2m"], Tile["3m"], Tile["3m"])
        val countArray = tiles.countAsCodeArray()
        assertEquals(2, countArray[1])
        assertEquals(1, countArray[2])
        assertEquals(2, countArray[3])
    }

    @Test
    fun testTileProperties() {
        // 测试幺九牌
        assertTrue(Tile["1m"].isYaochu) // 1万
        assertTrue(Tile["9m"].isYaochu) // 9万
        assertTrue(Tile["1s"].isYaochu) // 1索
        assertTrue(Tile["7z"].isYaochu) // 7字
        assertFalse(Tile["5p"].isYaochu) // 5筒

        // 测试三元牌
        assertTrue(Tile["5z"].isSangen) // 白
        assertTrue(Tile["6z"].isSangen) // 发
        assertTrue(Tile["7z"].isSangen) // 中
        assertFalse(Tile["4z"].isSangen) // 北

        // 测试风牌
        assertTrue(Tile["1z"].isWind) // 东
        assertTrue(Tile["2z"].isWind) // 南
        assertTrue(Tile["3z"].isWind) // 西
        assertTrue(Tile["4z"].isWind) // 北
        assertFalse(Tile["5z"].isWind) // 白
    }

    @Test
    fun testToStringAndParse() {
        // 测试toString得到的字符串拿去parse得到相同的结果
        val testTiles = listOf(
            Tile["1m"], Tile["2m"], Tile["3m"], Tile["4m"], Tile["5m"],
            Tile["6m"], Tile["7m"], Tile["8m"], Tile["9m"],
            Tile["1p"], Tile["2p"], Tile["3p"], Tile["4p"], Tile["5p"],
            Tile["6p"], Tile["7p"], Tile["8p"], Tile["9p"],
            Tile["1s"], Tile["2s"], Tile["3s"], Tile["4s"], Tile["5s"],
            Tile["6s"], Tile["7s"], Tile["8s"], Tile["9s"],
            Tile["1z"], Tile["2z"], Tile["3z"], Tile["4z"], Tile["5z"],
            Tile["6z"], Tile["7z"]
        )

        for (tile in testTiles) {
            val tileString = tile.toString()
            val parsedTile = Tile[tileString]
            assertEquals(tile, parsedTile, "toString和parse往返测试失败: $tile -> $tileString -> $parsedTile")
        }
    }
}

