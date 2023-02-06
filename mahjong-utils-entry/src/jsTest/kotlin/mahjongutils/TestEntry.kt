@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hora.Hora
import mahjongutils.models.Tile
import mahjongutils.shanten.ShantenResult
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEntry {
    @Test
    fun testRegularShanten() {
        val args = ShantenArgs(Tile.parseTiles("11123456789999s"))
        val rawResult = ENTRY.call("regularShanten", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))
        val result = Json.decodeFromDynamic<Result<ShantenResult>>(rawResult)
        assertEquals(200, result.code)
    }

    @Test
    fun testPointByHanHu() {
        val args = HanHu(5, 30)
        val rawResult = ENTRY.call("getParentPointByHanHu", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))
        val result = Json.decodeFromDynamic<Result<ParentPoint>>(rawResult)
        assertEquals(200, result.code)
    }

    @Test
    fun testPointByHanHu2() {
        val args = HanHu(1, 20)
        val rawResult = ENTRY.call("getParentPointByHanHu", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))
        val result = Json.decodeFromDynamic<Result<ParentPoint>>(rawResult)
        assertEquals(400, result.code)
    }

    @Test
    fun testHora() {
        val args = HoraArgs(Tile.parseTiles("11123456778999s"), agari = Tile.get("7s"), tsumo = true)
        val rawResult = ENTRY.call("hora", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))
        val result = Json.decodeFromDynamic<Result<Hora>>(rawResult)
        assertEquals(200, result.code)
    }
}