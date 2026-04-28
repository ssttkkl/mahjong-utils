package mahjongutils.cli

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MessagesTest {

    @Test
    fun testLanguageDetectReturnsValidValue() {
        // Language.detect() should return one of the enum values
        val detected = Language.detect()
        assertTrue(
            detected == Language.ZH || detected == Language.EN || detected == Language.JA,
            "Detected language should be a valid Language enum value"
        )
    }

    @Test
    fun testLanguageEnumValues() {
        // Verify all language enum values exist
        val values = Language.entries
        assertEquals(3, values.size)
        assertTrue(values.contains(Language.ZH))
        assertTrue(values.contains(Language.EN))
        assertTrue(values.contains(Language.JA))
    }

    @Test
    fun testMessagesGet() {
        assertEquals(ChineseMessages, Messages.get(Language.ZH))
        assertEquals(EnglishMessages, Messages.get(Language.EN))
        assertEquals(JapaneseMessages, Messages.get(Language.JA))
    }

    @Test
    fun testChineseMessages() {
        val msgs = ChineseMessages
        assertEquals("和牌", msgs.agari)
        assertEquals("听牌", msgs.tenpai)
        assertEquals("3向听", msgs.shanten(3))
        assertEquals("0向听", msgs.shanten(0))
        assertEquals("进张", msgs.advance)
        assertEquals("进张牌", msgs.advanceTiles)
        assertEquals("弃牌选择", msgs.discardChoice)
        assertEquals("打", msgs.discard)
        assertEquals("役种", msgs.yaku)
        assertEquals("番数", msgs.han)
        assertEquals("符数", msgs.hu)
        assertEquals("亲家自摸", msgs.parentTsumo)
        assertEquals("子家自摸", msgs.childTsumo)
        assertEquals("亲家荣和", msgs.parentRon)
        assertEquals("子家荣和", msgs.childRon)
        assertEquals("亲", msgs.parent)
        assertEquals("子", msgs.child)
        assertEquals("种", msgs.types)
        assertEquals("张", msgs.tiles)
        assertEquals("点", msgs.points)
        assertEquals("和牌张", msgs.winningTile)
        assertEquals("自摸", msgs.tsumo)
        assertEquals("荣和", msgs.ron)
        assertEquals("宝牌", msgs.dora)
    }

    @Test
    fun testEnglishMessages() {
        val msgs = EnglishMessages
        assertEquals("Winning Hand", msgs.agari)
        assertEquals("Tenpai", msgs.tenpai)
        assertEquals("2-shanten", msgs.shanten(2))
        assertEquals("0-shanten", msgs.shanten(0))
        assertEquals("Advance", msgs.advance)
        assertEquals("Advance tiles", msgs.advanceTiles)
        assertEquals("Discard Options", msgs.discardChoice)
        assertEquals("Discard", msgs.discard)
        assertEquals("Yaku", msgs.yaku)
        assertEquals("Han", msgs.han)
        assertEquals("Fu", msgs.hu)
        assertEquals("Parent Tsumo", msgs.parentTsumo)
        assertEquals("Child Tsumo", msgs.childTsumo)
        assertEquals("Parent Ron", msgs.parentRon)
        assertEquals("Child Ron", msgs.childRon)
        assertEquals("Parent", msgs.parent)
        assertEquals("Child", msgs.child)
        assertEquals("types", msgs.types)
        assertEquals("tiles", msgs.tiles)
        assertEquals("points", msgs.points)
        assertEquals("Winning tile", msgs.winningTile)
        assertEquals("Tsumo", msgs.tsumo)
        assertEquals("Ron", msgs.ron)
        assertEquals("Dora", msgs.dora)
    }

    @Test
    fun testJapaneseMessages() {
        val msgs = JapaneseMessages
        assertEquals("和了", msgs.agari)
        assertEquals("聴牌", msgs.tenpai)
        assertEquals("1向聴", msgs.shanten(1))
        assertEquals("0向聴", msgs.shanten(0))
        assertEquals("有効牌", msgs.advance)
        assertEquals("有効牌", msgs.advanceTiles)
        assertEquals("打牌選択", msgs.discardChoice)
        assertEquals("打", msgs.discard)
        assertEquals("役", msgs.yaku)
        assertEquals("翻数", msgs.han)
        assertEquals("符数", msgs.hu)
        assertEquals("親のツモ", msgs.parentTsumo)
        assertEquals("子のツモ", msgs.childTsumo)
        assertEquals("親のロン", msgs.parentRon)
        assertEquals("子のロン", msgs.childRon)
        assertEquals("親", msgs.parent)
        assertEquals("子", msgs.child)
        assertEquals("種", msgs.types)
        assertEquals("枚", msgs.tiles)
        assertEquals("点", msgs.points)
        assertEquals("和了牌", msgs.winningTile)
        assertEquals("ツモ", msgs.tsumo)
        assertEquals("ロン", msgs.ron)
        assertEquals("ドラ", msgs.dora)
    }
}
