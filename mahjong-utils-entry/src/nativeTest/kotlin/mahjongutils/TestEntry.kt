package mahjongutils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hora.Hora
import mahjongutils.models.Tile
import mahjongutils.shanten.RegularShantenResult
import mahjongutils.shanten.ShantenResult
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEntry {
    @Test
    fun testRegularShanten() {
        val args = ShantenArgs(Tile.parseTiles("11123456789999s"))
        val rawResult = ENTRY.call("regularShanten", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)
        val result = Json.decodeFromString<Result<RegularShantenResult>>(rawResult)
        assertEquals(200, result.code)
    }

    @Test
    fun testPointByHanHu() {
        val args = HanHu(5, 30)
        val rawResult = ENTRY.call("getParentPointByHanHu", Json.encodeToString(args))
        print("rawResult: ")
        print(rawResult)
        val result = Json.decodeFromString<Result<ParentPoint>>(rawResult)
        assertEquals(200, result.code)
    }

    @Test
    fun testPointByHanHu2() {
        val args = HanHu(1, 20)
        val rawResult = ENTRY.call("getParentPointByHanHu", Json.encodeToString(args))
        print("rawResult: ")
        print(rawResult)
        val result = Json.decodeFromString<Result<ParentPoint>>(rawResult)
        assertEquals(400, result.code)
    }

    @Test
    fun testHora() {
        val args = HoraArgs(Tile.parseTiles("11123456778999s"), agari = Tile.get("7s"), tsumo = true)
        val rawResult = ENTRY.call("hora", Json.encodeToString(args))
        print("rawResult: ")
        print(rawResult)
        val result = Json.decodeFromString<Result<Hora>>(rawResult)
        assertEquals(200, result.code)
    }
}