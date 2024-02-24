package mahjongutils.shanten

import mahjongutils.models.Tatsu
import mahjongutils.models.Tile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestFuroChanceShanten {
    @Test
    fun test1() {
        val result = furoChanceShanten(Tile.parseTiles("8999m"), Tile.get("9m"))
        assertEquals(0, result.shantenInfo.shantenNum)

        val shantenInfo = result.shantenInfo
        assertEquals(
            ShantenWithoutGot(
                shantenNum = 0,
                advance = Tile.parseTiles("78m").toSet(),
                advanceNum = 7
            ), shantenInfo.pass
        )
        assertEquals(
            ShantenWithGot(
                shantenNum = 0,
                discardToAdvance = mapOf(
                    Tile.get("8m") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = setOf(),
                        advanceNum = 0,
                        improvement = Tile.allExcludeAkaDora.filter { it !== Tile.get("9m") }.associateWith {
                            listOf(
                                Improvement(
                                    discard = Tile.get("9m"),
                                    advance = setOf(it),
                                    advanceNum = if (it == Tile.get("8m")) 2 else 3
                                )
                            )
                        },
                        improvementNum = 131,
                    ),
                    Tile.get("9m") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = setOf(Tile.get("8m")),
                        advanceNum = 3
                    )
                )
            ), shantenInfo.pon
        )
        assertEquals(
            ShantenWithoutGot(
                shantenNum = 0,
                advance = setOf(Tile.get("8m")),
                advanceNum = 3
            ), shantenInfo.minkan
        )
    }

    @Test
    fun test2() {
        assertFailsWith(FuroChanceShantenArgsValidationException::class) {
            furoChanceShanten(Tile.parseTiles("9999m"), Tile.get("9m"))
        }
    }

    @Test
    fun test3() {
        val result = furoChanceShanten(Tile.parseTiles("3456778m123457p"), Tile.get("7m"))
        assertEquals(0, result.shantenInfo.shantenNum)
    }

    @Test
    fun test4() {
        val result = furoChanceShanten(Tile.parseTiles("3467m119p"), Tile.get("5m"))
        assertEquals(7, result.shantenInfo.chi[Tatsu.parse("67m")]!!.discardToAdvance[Tile.get("9p")]!!.advanceNum)
    }

    @Test
    fun test5() {
        val result = furoChanceShanten(Tile.parseTiles("1112345678999m"), Tile["9m"])
        assertEquals(-1, result.shantenInfo.shantenNum)
    }
}