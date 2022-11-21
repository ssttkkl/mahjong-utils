import mahjongutils.models.ShantenWithGot
import mahjongutils.models.ShantenWithoutGot
import mahjongutils.models.Tile
import mahjongutils.shanten
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TestShanten {
    @Test
    fun testShanten() {
        val result = shanten(Tile.parseTiles("1112345678999s"))
        assertIs<ShantenWithoutGot>(result.shantenInfo)
        assertEquals(0, result.shantenInfo.shantenNum)
    }

    @Test
    fun testShanten2() {
        val result = shanten(Tile.parseTiles("11123456788999s"))
        assertIs<ShantenWithGot>(result.shantenInfo)
        assertEquals(-1, result.shantenInfo.shantenNum)
    }
}