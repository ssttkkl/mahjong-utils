package mahjongutils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mahjongutils.models.Tile
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEntry {
    @Test
    fun testEntry() {
        val args = ShantenArgs(Tile.parseTiles("11123456789999s"))
        val rawResult = ENTRY.call("regularShanten", Json.encodeToString(args))
        print(rawResult)
        val result = Json.decodeFromString<Result<ShantenResult>>(rawResult)
        assertEquals(200, result.code)
    }
}