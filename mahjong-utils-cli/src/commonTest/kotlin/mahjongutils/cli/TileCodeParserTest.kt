package mahjongutils.cli

import mahjongutils.models.Tile
import mahjongutils.models.TileType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TileCodeParserTest {
    @Test
    fun testParseValidCode() {
        val result = TileCodeParser.parse("123m456p789s1122z")
        val expected = listOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 3),
            Tile.get(TileType.P, 4),
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 6),
            Tile.get(TileType.S, 7),
            Tile.get(TileType.S, 8),
            Tile.get(TileType.S, 9),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 2),
            Tile.get(TileType.Z, 2)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testParseEmptyString() {
        val result = TileCodeParser.parse("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseOnlyTypeCharacters() {
        // When there are no numbers before type characters, should result in empty list
        val result = TileCodeParser.parse("mpsz")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseOnlyNumbers() {
        // When there are only numbers without type characters, should result in empty list
        val result = TileCodeParser.parse("123456")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseSingleTile() {
        val result = TileCodeParser.parse("5m")
        assertEquals(listOf(Tile.get(TileType.M, 5)), result)
    }

    @Test
    fun testParseMultipleOfSameType() {
        val result = TileCodeParser.parse("111222333m")
        val expected = listOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 3),
            Tile.get(TileType.M, 3),
            Tile.get(TileType.M, 3)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testParseWithZero() {
        val result = TileCodeParser.parse("50m")
        val expected = listOf(
            Tile.get(TileType.M, 5),
            Tile.get(TileType.M, 0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testParseFuroEmptyString() {
        val result = TileCodeParser.parseFuro("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseFuroSingle() {
        val result = TileCodeParser.parseFuro("123m")
        assertEquals(1, result.size)
    }

    @Test
    fun testParseFuroMultiple() {
        val result = TileCodeParser.parseFuro("123m,456p,789s")
        assertEquals(3, result.size)
    }

    @Test
    fun testParseFuroWithWhitespace() {
        val result = TileCodeParser.parseFuro("123m, 456p , 789s")
        assertEquals(3, result.size)
    }
}
