package mahjongutils.models

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        for (i in 1..9) {
            val tile = Tile[i]
            assertEquals(TileType.Character, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile[i + 10]
            assertEquals(TileType.Dot, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile[i + 20]
            assertEquals(TileType.Bamboo, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..7) {
            val tile = Tile[i + 30]
            assertEquals(TileType.Honour, tile.type)
            assertEquals(i, tile.num)
        }

        // 测试通过类型和数字获取牌
        for (i in 1..9) {
            val tile = Tile[TileType.Character, i]
            assertEquals(TileType.Character, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile[TileType.Dot, i]
            assertEquals(TileType.Dot, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile[TileType.Bamboo, i]
            assertEquals(TileType.Bamboo, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..7) {
            val tile = Tile[TileType.Honour, i]
            assertEquals(TileType.Honour, tile.type)
            assertEquals(i, tile.num)
        }

        // 测试通过文本获取牌
        for (i in 1..9) {
            val tile = Tile["${i}m"]
            assertEquals(TileType.Character, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile["${i}p"]
            assertEquals(TileType.Dot, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..9) {
            val tile = Tile["${i}s"]
            assertEquals(TileType.Bamboo, tile.type)
            assertEquals(i, tile.num)
        }
        for (i in 1..7) {
            val tile = Tile["${i}z"]
            assertEquals(TileType.Honour, tile.type)
            assertEquals(i, tile.num)
        }

        // 测试无效编码
        assertFailsWith<IllegalArgumentException> { Tile[-1] }
        assertFailsWith<IllegalArgumentException> { Tile[0] }
        assertFailsWith<IllegalArgumentException> { Tile[10] }
        assertFailsWith<IllegalArgumentException> { Tile[20] }
        assertFailsWith<IllegalArgumentException> { Tile[30] }
        assertFailsWith<IllegalArgumentException> { Tile[38] }

        // 测试getOrNull方法
        assertNull(Tile.getOrNull(-1))
        assertNull(Tile.getOrNull(0))
        assertNull(Tile.getOrNull(10))
        assertNull(Tile.getOrNull(20))
        assertNull(Tile.getOrNull(30))
        assertNull(Tile.getOrNull(38))
        assertNull(Tile.getOrNull("0m"))
        assertNull(Tile.getOrNull("0s"))
        assertNull(Tile.getOrNull("0z"))
        assertNull(Tile.getOrNull("2x"))
        assertNull(Tile.getOrNull("23m"))
        for (i in 1..9) {
            assertNotNull(Tile.getOrNull(i))
            assertNotNull(Tile.getOrNull(i + 10))
            assertNotNull(Tile.getOrNull(i + 20))
            assertNotNull(Tile.getOrNull("${i}m"))
            assertNotNull(Tile.getOrNull("${i}p"))
            assertNotNull(Tile.getOrNull("${i}s"))
            assertNotNull(Tile.getOrNull(TileType.Character, i))
            assertNotNull(Tile.getOrNull(TileType.Dot, i))
            assertNotNull(Tile.getOrNull(TileType.Bamboo, i))
        }
        for (i in 1..7) {
            assertNotNull(Tile.getOrNull(i + 30))
            assertNotNull(Tile.getOrNull("${i}z"))
            assertNotNull(Tile.getOrNull(TileType.Honour, i))
        }
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
        assertFailsWith<IllegalArgumentException> { Tile.parseTiles("m23") }
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
        assertTrue(Tile["1m"].isTerminalsAndHonors) // 1万
        assertTrue(Tile["9m"].isTerminalsAndHonors) // 9万
        assertTrue(Tile["1s"].isTerminalsAndHonors) // 1索
        assertTrue(Tile["7z"].isTerminalsAndHonors) // 7字
        assertFalse(Tile["5p"].isTerminalsAndHonors) // 5筒

        // 测试三元牌
        assertTrue(Tile["5z"].isDragon) // 白
        assertTrue(Tile["6z"].isDragon) // 发
        assertTrue(Tile["7z"].isDragon) // 中
        assertFalse(Tile["4z"].isDragon) // 北

        // 测试风牌
        assertTrue(Tile["1z"].isWind) // 东
        assertTrue(Tile["2z"].isWind) // 南
        assertTrue(Tile["3z"].isWind) // 西
        assertTrue(Tile["4z"].isWind) // 北
        assertFalse(Tile["5z"].isWind) // 白
    }

    @Test
    fun testToStringAndParse() {
        val testTiles = Tile.all

        for (tile in testTiles) {
            // 测试toString得到的字符串拿去parse得到相同的结果
            val tileString = tile.toString()
            val parsedTile = Tile[tileString]
            assertEquals(tile, parsedTile, "toString和parse往返测试失败: $tile -> $tileString -> $parsedTile")

            // 测试序列化得到的字符串与toString得到的字符串相同
            val serializedTile = Json.encodeToString(tile)
            assertEquals("\"${tileString}\"", serializedTile, "序列化测试失败: $tile -> $serializedTile")

            // 测试反序列化得到的牌与parse得到的牌相同
            val deserializedTile = Json.decodeFromString<Tile>(serializedTile)
            assertEquals(tile, deserializedTile, "反序列化测试失败: $tile -> $serializedTile -> $deserializedTile")
        }
    }
}

