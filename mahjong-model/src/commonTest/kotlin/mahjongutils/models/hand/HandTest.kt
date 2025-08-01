package mahjongutils.models.hand

import mahjongutils.models.Ankan
import mahjongutils.models.Chi
import mahjongutils.models.Pon
import mahjongutils.models.Tile
import mahjongutils.models.TileType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HandTest {

    @Test
    fun testHandCreation() {
        // 测试创建空手牌
        val emptyHand = Hand(emptyList(), emptyList())
        assertEquals(0, emptyHand.tilesInHand.size)
        assertEquals(0, emptyHand.furo.size)
        assertEquals(0, emptyHand.tiles.size)

        // 测试创建只有门前牌的手牌
        val tilesInHand = Tile.parseTiles("123456789m11p")
        val hand1 = Hand(tilesInHand, emptyList())
        assertEquals(tilesInHand.size, hand1.tilesInHand.size)
        assertEquals(0, hand1.furo.size)
        assertEquals(tilesInHand.size, hand1.tiles.size)
        assertTrue(hand1.menzen)

        // 测试创建有副露的手牌
        val furo = listOf(
            Chi(Tile.get(TileType.M, 1)),
            Pon(Tile.get(TileType.P, 5))
        )
        val hand2 = Hand(tilesInHand, furo)
        assertEquals(tilesInHand.size, hand2.tilesInHand.size)
        assertEquals(2, hand2.furo.size)
        assertEquals(tilesInHand.size + 6, hand2.tiles.size) // 门前牌 + 副露牌(3+3)
        assertFalse(hand2.menzen)

        // 测试创建只有暗杠的手牌
        val ankanFuro = listOf(
            Ankan(Tile.get(TileType.Z, 1))
        )
        val hand3 = Hand(tilesInHand, ankanFuro)
        assertEquals(tilesInHand.size, hand3.tilesInHand.size)
        assertEquals(1, hand3.furo.size)
        assertEquals(tilesInHand.size + 4, hand3.tiles.size) // 门前牌 + 暗杠牌(4)
        assertTrue(hand3.menzen) // 暗杠不影响门清状态
    }

    @Test
    fun testIsWithDraw() {
        // 测试摸有摸牌的手牌（手牌数为3n+1）
        val tilesInHand2 = Tile.parseTiles("123456789m1234p")
        val hand2 = Hand(tilesInHand2, emptyList())
        assertFalse(hand2.isWithDraw)

        // 测试有副露且没有摸牌的手牌（手牌数为3n+1）
        val tilesInHand3 = Tile.parseTiles("123456m123p4s")
        val furo = listOf(Chi(Tile.get(TileType.M, 7)))
        val hand3 = Hand(tilesInHand3, furo)
        assertFalse(hand3.isWithDraw)

        // 测试有副露且有摸牌的手牌（手牌数为3n+2）
        val tilesInHand4 = Tile.parseTiles("123456m123p44p")
        val hand4 = Hand(tilesInHand4, furo)
        assertTrue(hand4.isWithDraw)
    }
}

