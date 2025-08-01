package mahjongutils.models.hand

import mahjongutils.models.ConcealedKong
import mahjongutils.models.Chow
import mahjongutils.models.MeldedKong
import mahjongutils.models.Pung
import mahjongutils.models.Tile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HandTest {
    @Test
    fun testHandCreation() {
        // 测试创建手牌
        val tiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["2p"], Tile["2p"])
        val meldeds = listOf(Pung(Tile["3s"]))
        val hand = Hand(tiles, meldeds)

        assertEquals(tiles, hand.tiles)
        assertEquals(meldeds, hand.meldeds)
    }

    @Test
    fun testIsAfterDrawn() {
        // 测试摸牌后的手牌
        val tiles1 = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["2p"], Tile["2p"])
        val hand1 = Hand(tiles1, emptyList())
        assertTrue(hand1.isAfterDrawn)

        // 测试非摸牌后的手牌
        val tiles2 = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["2p"])
        val hand2 = Hand(tiles2, emptyList())
        assertFalse(hand2.isAfterDrawn)
    }

    @Test
    fun testIsClosed() {
        // 测试门清手牌
        val tiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["2p"], Tile["2p"])
        val hand1 = Hand(tiles, emptyList())
        assertTrue(hand1.isClosed)

        // 测试含暗杠的门清手牌
        val meldeds1 = listOf(ConcealedKong(Tile["3s"]))
        val hand2 = Hand(tiles, meldeds1)
        assertTrue(hand2.isClosed)

        // 测试非门清手牌
        val meldeds2 = listOf(Pung(Tile["3s"]))
        val hand3 = Hand(tiles, meldeds2)
        assertFalse(hand3.isClosed)

        // 测试混合副露的手牌
        val meldeds3 = listOf(ConcealedKong(Tile["3s"]), Chow(Tile["1m"]))
        val hand4 = Hand(tiles, meldeds3)
        assertFalse(hand4.isClosed)
    }

    @Test
    fun testAllTiles() {
        // 测试获取所有牌（包括门前与副露）
        val tiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"], Tile["2p"], Tile["2p"])
        val meldeds = listOf(
            Pung(Tile["3s"]), // 3索刻子
            MeldedKong(Tile["5z"])  // 白杠子
        )
        val hand = Hand(tiles, meldeds)

        val allTiles = hand.allTiles
        assertEquals(12, allTiles.size) // 5张门前牌 + 3张碰 + 4张杠

        // 验证门前牌
        assertTrue(Tile["1m"] in allTiles)
        assertTrue(Tile["2m"] in allTiles)
        assertTrue(Tile["3m"] in allTiles)
        assertTrue(Tile["2p"] in allTiles)

        // 验证副露牌
        assertTrue(Tile["3s"] in allTiles)
        assertTrue(Tile["5z"] in allTiles)

        // 验证副露牌的数量
        assertEquals(3, allTiles.count { it == Tile["3s"] })
        assertEquals(4, allTiles.count { it == Tile["5z"] })
    }
}

