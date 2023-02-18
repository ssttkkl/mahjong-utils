package mahjongutils.shanten

import mahjongutils.models.Tile
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class TestFuroChanceShanten {
    @Test
    fun test1() {
        val result = furoChanceShanten(Tile.parseTiles("8999m"), Tile.get("9m"))
        assertEquals(0, result.shantenInfo.shantenNum)

        val shantenInfo = result.shantenInfo as ShantenWithFuroChance
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
                        advanceNum = 0
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
        assertThrows<IllegalArgumentException> {
            furoChanceShanten(Tile.parseTiles("9999m"), Tile.get("9m"))
        }
    }

    @Test
    fun test3(){
        val result = furoChanceShanten(Tile.parseTiles("3456778m123457p"), Tile.get("7m"))
        assertEquals(0,result.shantenInfo.shantenNum)
    }
}