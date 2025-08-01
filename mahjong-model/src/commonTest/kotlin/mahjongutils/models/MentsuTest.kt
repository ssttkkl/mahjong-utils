package mahjongutils.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MentsuTest {

    @Test
    fun testMentsuCreation() {
        // 测试顺子创建
        val shuntsu = Shuntsu(Tile.get(TileType.M, 1))
        assertEquals(MentsuType.Shuntsu, shuntsu.type)
        assertEquals(Tile.get(TileType.M, 1), shuntsu.tile)

        // 测试刻子创建
        val kotsu = Kotsu(Tile.get(TileType.P, 5))
        assertEquals(MentsuType.Kotsu, kotsu.type)
        assertEquals(Tile.get(TileType.P, 5), kotsu.tile)

        // 测试通过牌列表创建
        val mentsu1 = Mentsu(listOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 3)
        ))
        assertEquals(MentsuType.Shuntsu, mentsu1.type)
        assertEquals(Tile.get(TileType.M, 1), mentsu1.tile)

        val mentsu2 = Mentsu(listOf(
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 5)
        ))
        assertEquals(MentsuType.Kotsu, mentsu2.type)
        assertEquals(Tile.get(TileType.P, 5), mentsu2.tile)

        // 测试通过文本创建
        val mentsu3 = Mentsu("123m")
        assertEquals(MentsuType.Shuntsu, mentsu3.type)
        assertEquals(Tile.get(TileType.M, 1), mentsu3.tile)

        val mentsu4 = Mentsu("555p")
        assertEquals(MentsuType.Kotsu, mentsu4.type)
        assertEquals(Tile.get(TileType.P, 5), mentsu4.tile)
    }

    @Test
    fun testMentsuTiles() {
        // 测试顺子的牌
        val shuntsu = Shuntsu(Tile.get(TileType.M, 1))
        val shuntsuTiles = shuntsu.tiles.toList()
        assertEquals(3, shuntsuTiles.size)
        assertEquals(Tile.get(TileType.M, 1), shuntsuTiles[0])
        assertEquals(Tile.get(TileType.M, 2), shuntsuTiles[1])
        assertEquals(Tile.get(TileType.M, 3), shuntsuTiles[2])

        // 测试刻子的牌
        val kotsu = Kotsu(Tile.get(TileType.P, 5))
        val kotsuTiles = kotsu.tiles.toList()
        assertEquals(3, kotsuTiles.size)
        assertEquals(Tile.get(TileType.P, 5), kotsuTiles[0])
        assertEquals(Tile.get(TileType.P, 5), kotsuTiles[1])
        assertEquals(Tile.get(TileType.P, 5), kotsuTiles[2])

        // 测试红宝牌的顺子
        val redDoraShuntsu = Shuntsu(Tile.get(TileType.M, 0))
        val redDoraShuntsuTiles = redDoraShuntsu.tiles.toList()
        assertEquals(3, redDoraShuntsuTiles.size)
        assertEquals(Tile.get(TileType.M, 0), redDoraShuntsuTiles[0])
        assertEquals(Tile.get(TileType.M, 6), redDoraShuntsuTiles[1])
        assertEquals(Tile.get(TileType.M, 7), redDoraShuntsuTiles[2])
    }

    @Test
    fun testAfterDiscard() {
        // 测试顺子舍牌后的搭子
        val shuntsu = Shuntsu(Tile.get(TileType.M, 1))

        // 舍弃第一张牌
        val tatsu1 = shuntsu.afterDiscard(Tile.get(TileType.M, 1))
        assertEquals(TatsuType.Ryanmen, tatsu1.type)
        assertEquals(Tile.get(TileType.M, 2), tatsu1.first)

        // 舍弃中间牌
        val tatsu2 = shuntsu.afterDiscard(Tile.get(TileType.M, 2))
        assertEquals(TatsuType.Kanchan, tatsu2.type)
        assertEquals(Tile.get(TileType.M, 1), tatsu2.first)

        // 舍弃最后一张牌
        val tatsu3 = shuntsu.afterDiscard(Tile.get(TileType.M, 3))
        assertEquals(TatsuType.Penchan, tatsu3.type)
        assertEquals(Tile.get(TileType.M, 1), tatsu3.first)

        // 测试边张搭子
        val shuntsu2 = Shuntsu(Tile.get(TileType.M, 7))
        val tatsu4 = shuntsu2.afterDiscard(Tile.get(TileType.M, 7))
        assertEquals(TatsuType.Penchan, tatsu4.type)
        assertEquals(Tile.get(TileType.M, 8), tatsu4.first)

        // 测试刻子舍牌后的搭子
        val kotsu = Kotsu(Tile.get(TileType.P, 5))
        val tatsu5 = kotsu.afterDiscard(Tile.get(TileType.P, 5))
        assertEquals(TatsuType.Toitsu, tatsu5.type)
        assertEquals(Tile.get(TileType.P, 5), tatsu5.first)
    }

    @Test
    fun testToString() {
        // 测试顺子的字符串表示
        val shuntsu = Shuntsu(Tile.get(TileType.M, 1))
        assertEquals("123m", shuntsu.toString())

        // 测试刻子的字符串表示
        val kotsu = Kotsu(Tile.get(TileType.P, 5))
        assertEquals("555p", kotsu.toString())

        // 测试红宝牌的顺子
        val redDoraShuntsu = Shuntsu(Tile.get(TileType.M, 0))
        assertEquals("067m", redDoraShuntsu.toString())
    }

    @Test
    fun testInvalidMentsuCreation() {
        // 测试无效的顺子（字牌不能组成顺子）
        assertFailsWith(IllegalArgumentException::class) {
            Mentsu(listOf(
                Tile.get(TileType.Z, 1),
                Tile.get(TileType.Z, 2),
                Tile.get(TileType.Z, 3)
            ))
        }

        // 测试无效的顺子（不连续的牌）
        assertFailsWith(IllegalArgumentException::class) {
            Mentsu(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.M, 2),
                Tile.get(TileType.M, 4)
            ))
        }

        // 测试无效的面子（牌数不对）
        assertFailsWith(IllegalArgumentException::class) {
            Mentsu(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.M, 2)
            ))
        }

        // 测试无效的面子（混合类型）
        assertFailsWith(IllegalArgumentException::class) {
            Mentsu(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.P, 2),
                Tile.get(TileType.S, 3)
            ))
        }
    }

    @Test
    fun testInvalidAfterDiscard() {
        val shuntsu = Shuntsu(Tile.get(TileType.M, 1))

        // 测试舍弃不属于面子的牌
        assertFailsWith(IllegalArgumentException::class) {
            shuntsu.afterDiscard(Tile.get(TileType.M, 4))
        }

        val kotsu = Kotsu(Tile.get(TileType.P, 5))

        // 测试舍弃不属于面子的牌
        assertFailsWith(IllegalArgumentException::class) {
            kotsu.afterDiscard(Tile.get(TileType.P, 6))
        }
    }
}

