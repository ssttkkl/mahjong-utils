package mahjongutils.models


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TatsuTest {

    @Test
    fun testTatsuCreation() {
        // 测试两面搭子创建
        val ryanmen = Ryanmen(Tile.get(TileType.M, 3))
        assertEquals(TatsuType.Ryanmen, ryanmen.type)
        assertEquals(Tile.get(TileType.M, 3), ryanmen.first)
        assertEquals(Tile.get(TileType.M, 4), ryanmen.second)

        // 测试嵌张搭子创建
        val kanchan = Kanchan(Tile.get(TileType.P, 2))
        assertEquals(TatsuType.Kanchan, kanchan.type)
        assertEquals(Tile.get(TileType.P, 2), kanchan.first)
        assertEquals(Tile.get(TileType.P, 4), kanchan.second)

        // 测试边张搭子创建
        val penchan = Penchan(Tile.get(TileType.S, 1))
        assertEquals(TatsuType.Penchan, penchan.type)
        assertEquals(Tile.get(TileType.S, 1), penchan.first)
        assertEquals(Tile.get(TileType.S, 2), penchan.second)

        // 测试对子创建
        val toitsu = Toitsu(Tile.get(TileType.Z, 1))
        assertEquals(TatsuType.Toitsu, toitsu.type)
        assertEquals(Tile.get(TileType.Z, 1), toitsu.first)
        assertEquals(Tile.get(TileType.Z, 1), toitsu.second)

        // 测试通过两张牌创建
        val tatsu1 = Tatsu(Tile.get(TileType.M, 3), Tile.get(TileType.M, 4))
        assertEquals(TatsuType.Ryanmen, tatsu1.type)
        assertEquals(Tile.get(TileType.M, 3), tatsu1.first)

        val tatsu2 = Tatsu(Tile.get(TileType.P, 2), Tile.get(TileType.P, 4))
        assertEquals(TatsuType.Kanchan, tatsu2.type)
        assertEquals(Tile.get(TileType.P, 2), tatsu2.first)

        val tatsu3 = Tatsu(Tile.get(TileType.S, 1), Tile.get(TileType.S, 2))
        assertEquals(TatsuType.Penchan, tatsu3.type)
        assertEquals(Tile.get(TileType.S, 1), tatsu3.first)

        val tatsu4 = Tatsu(Tile.get(TileType.Z, 1), Tile.get(TileType.Z, 1))
        assertEquals(TatsuType.Toitsu, tatsu4.type)
        assertEquals(Tile.get(TileType.Z, 1), tatsu4.first)

        // 测试通过文本创建
        val tatsu5 = Tatsu("34m")
        assertEquals(TatsuType.Ryanmen, tatsu5.type)
        assertEquals(Tile.get(TileType.M, 3), tatsu5.first)

        val tatsu6 = Tatsu("24p")
        assertEquals(TatsuType.Kanchan, tatsu6.type)
        assertEquals(Tile.get(TileType.P, 2), tatsu6.first)

        val tatsu7 = Tatsu("12s")
        assertEquals(TatsuType.Penchan, tatsu7.type)
        assertEquals(Tile.get(TileType.S, 1), tatsu7.first)

        val tatsu8 = Tatsu("11z")
        assertEquals(TatsuType.Toitsu, tatsu8.type)
        assertEquals(Tile.get(TileType.Z, 1), tatsu8.first)
    }

    @Test
    fun testWaiting() {
        // 测试两面搭子的进张
        val ryanmen = Ryanmen(Tile.get(TileType.M, 3))
        val ryanmenWaiting = ryanmen.waiting
        assertEquals(2, ryanmenWaiting.size)
        assertTrue(Tile.get(TileType.M, 2) in ryanmenWaiting)
        assertTrue(Tile.get(TileType.M, 5) in ryanmenWaiting)

        // 测试嵌张搭子的进张
        val kanchan = Kanchan(Tile.get(TileType.P, 2))
        val kanchanWaiting = kanchan.waiting
        assertEquals(1, kanchanWaiting.size)
        assertTrue(Tile.get(TileType.P, 3) in kanchanWaiting)

        // 测试边张搭子的进张
        val penchan1 = Penchan(Tile.get(TileType.S, 1))
        val penchan1Waiting = penchan1.waiting
        assertEquals(1, penchan1Waiting.size)
        assertTrue(Tile.get(TileType.S, 3) in penchan1Waiting)

        val penchan2 = Penchan(Tile.get(TileType.S, 8))
        val penchan2Waiting = penchan2.waiting
        assertEquals(1, penchan2Waiting.size)
        assertTrue(Tile.get(TileType.S, 7) in penchan2Waiting)

        // 测试对子的进张
        val toitsu = Toitsu(Tile.get(TileType.Z, 1))
        val toitsuWaiting = toitsu.waiting
        assertEquals(1, toitsuWaiting.size)
        assertTrue(Tile.get(TileType.Z, 1) in toitsuWaiting)
    }

    @Test
    fun testWithWaiting() {
        // 测试两面搭子的进张形成面子
        val ryanmen = Ryanmen(Tile.get(TileType.M, 3))

        val mentsu1 = ryanmen.withWaiting(Tile.get(TileType.M, 2))
        assertEquals(MentsuType.Shuntsu, mentsu1.type)
        assertEquals(Tile.get(TileType.M, 2), mentsu1.tile)

        val mentsu2 = ryanmen.withWaiting(Tile.get(TileType.M, 5))
        assertEquals(MentsuType.Shuntsu, mentsu2.type)
        assertEquals(Tile.get(TileType.M, 3), mentsu2.tile)

        // 测试嵌张搭子的进张形成面子
        val kanchan = Kanchan(Tile.get(TileType.P, 2))
        val mentsu3 = kanchan.withWaiting(Tile.get(TileType.P, 3))
        assertEquals(MentsuType.Shuntsu, mentsu3.type)
        assertEquals(Tile.get(TileType.P, 2), mentsu3.tile)

        // 测试边张搭子的进张形成面子
        val penchan1 = Penchan(Tile.get(TileType.S, 1))
        val mentsu4 = penchan1.withWaiting(Tile.get(TileType.S, 3))
        assertEquals(MentsuType.Shuntsu, mentsu4.type)
        assertEquals(Tile.get(TileType.S, 1), mentsu4.tile)

        val penchan2 = Penchan(Tile.get(TileType.S, 8))
        val mentsu5 = penchan2.withWaiting(Tile.get(TileType.S, 7))
        assertEquals(MentsuType.Shuntsu, mentsu5.type)
        assertEquals(Tile.get(TileType.S, 7), mentsu5.tile)

        // 测试对子的进张形成面子
        val toitsu = Toitsu(Tile.get(TileType.Z, 1))
        val mentsu6 = toitsu.withWaiting(Tile.get(TileType.Z, 1))
        assertEquals(MentsuType.Kotsu, mentsu6.type)
        assertEquals(Tile.get(TileType.Z, 1), mentsu6.tile)
    }

    @Test
    fun testToString() {
        // 测试两面搭子的字符串表示
        val ryanmen = Ryanmen(Tile.get(TileType.M, 3))
        assertEquals("34m", ryanmen.toString())

        // 测试嵌张搭子的字符串表示
        val kanchan = Kanchan(Tile.get(TileType.P, 2))
        assertEquals("24p", kanchan.toString())

        // 测试边张搭子的字符串表示
        val penchan = Penchan(Tile.get(TileType.S, 1))
        assertEquals("12s", penchan.toString())

        // 测试对子的字符串表示
        val toitsu = Toitsu(Tile.get(TileType.Z, 1))
        assertEquals("11z", toitsu.toString())

        // 测试红宝牌的搭子
        val redDoraTatsu = Tatsu(Tile.get(TileType.M, 0), Tile.get(TileType.M, 6))
        assertEquals("06m", redDoraTatsu.toString())
    }

    @Test
    fun testInvalidTatsuCreation() {
        // 测试无效的搭子（字牌不能组成顺子相关的搭子）
        assertFailsWith(IllegalArgumentException::class) {
            Tatsu(Tile.get(TileType.Z, 1), Tile.get(TileType.Z, 2))
        }

        // 测试无效的搭子（不符合搭子间隔的牌）
        assertFailsWith(IllegalArgumentException::class) {
            Tatsu(Tile.get(TileType.M, 1), Tile.get(TileType.M, 4))
        }

        // 测试无效的搭子（混合类型）
        assertFailsWith(IllegalArgumentException::class) {
            Tatsu(Tile.get(TileType.M, 1), Tile.get(TileType.P, 2))
        }

        // 测试无效的文本
        assertFailsWith(IllegalArgumentException::class) {
            Tatsu("123m")
        }
    }

    @Test
    fun testInvalidWithWaiting() {
        val ryanmen = Ryanmen(Tile.get(TileType.M, 3))

        // 测试不是进张的牌
        assertFailsWith(IllegalArgumentException::class) {
            ryanmen.withWaiting(Tile.get(TileType.M, 1))
        }

        val kanchan = Kanchan(Tile.get(TileType.P, 2))

        // 测试不是进张的牌
        assertFailsWith(IllegalArgumentException::class) {
            kanchan.withWaiting(Tile.get(TileType.P, 5))
        }

        val penchan = Penchan(Tile.get(TileType.S, 1))

        // 测试不是进张的牌
        assertFailsWith(IllegalArgumentException::class) {
            penchan.withWaiting(Tile.get(TileType.S, 4))
        }

        val toitsu = Toitsu(Tile.get(TileType.Z, 1))

        // 测试不是进张的牌
        assertFailsWith(IllegalArgumentException::class) {
            toitsu.withWaiting(Tile.get(TileType.Z, 2))
        }
    }
}

