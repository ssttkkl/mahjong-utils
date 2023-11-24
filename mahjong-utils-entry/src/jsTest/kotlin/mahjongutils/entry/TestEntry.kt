@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.entry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import mahjongutils.entry.models.HanHu
import mahjongutils.entry.models.HoraArgs
import mahjongutils.entry.Result
import mahjongutils.entry.call
import mahjongutils.entry.models.ShantenArgs
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.Hora
import mahjongutils.hora.hora
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.*
import mahjongutils.yaku.Yakus
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEntry {
    @Test
    fun testShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11112345678s"),
            listOf(Furo("999s")),
            bestShantenOnly = true
        )

        val rawResult = call("shanten", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<UnionShantenResult> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = shanten(args.tiles, args.furo, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testRegularShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11112345678s"),
            listOf(Furo("999s")),
            bestShantenOnly = true
        )

        val rawResult = call("regularShanten", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<RegularShantenResult> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult =
            regularShanten(args.tiles, args.furo, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testChitoiShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11223344z556789p"),
            bestShantenOnly = true
        )

        val rawResult = call("chitoiShanten", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<ChitoiShantenResult> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = chitoiShanten(args.tiles, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testKokushiShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11223344556677z"),
            bestShantenOnly = true
        )

        val rawResult = call("kokushiShanten", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<KokushiShantenResult> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = kokushiShanten(args.tiles, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testGetParentPointByHanHu() {
        val args = HanHu(5, 30)

        val rawResult = call("getParentPointByHanHu", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<ParentPoint> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = getParentPointByHanHu(args.han, args.hu)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testGetChildPointByHanHu() {
        val args = HanHu(5, 30)

        val rawResult = call("getChildPointByHanHu", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<ChildPoint> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = getChildPointByHanHu(args.han, args.hu)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testHora() {
        val args = HoraArgs(
            tiles = Tile.parseTiles("11123456s"),
            furo = listOf(Furo("0110z"), Furo("789s")),
            agari = Tile.get("1s"),
            tsumo = true,
            dora = 4,
            selfWind = Wind.East,
            roundWind = Wind.North,
            extraYaku = setOf(Yakus.Rinshan)
        )

        val rawResult = call("hora", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<Hora> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = hora(
            args.tiles!!, args.furo!!, args.agari,
            args.tsumo, args.dora, args.selfWind, args.roundWind, args.extraYaku
        )
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testHora2() {
        val shantenResult = shanten(
            tiles = Tile.parseTiles("11123456s"),
            furo = listOf(Furo("0110z"), Furo("789s")),
            bestShantenOnly = true
        )

        val args = HoraArgs(
            shantenResult = shantenResult,
            agari = Tile.get("1s"),
            tsumo = true,
            dora = 4,
            selfWind = Wind.East,
            roundWind = Wind.North,
            extraYaku = setOf(Yakus.Rinshan)
        )

        val rawResult = call("hora", Json.encodeToDynamic(args))
        print("rawResult: ")
        println(JSON.stringify(rawResult))

        val actualResult: Result<Hora> = Json.decodeFromDynamic(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = hora(
            args.shantenResult!!, args.agari,
            args.tsumo, args.dora, args.selfWind, args.roundWind, args.extraYaku
        )
        assertEquals(exceptResult, actualResult.data)
    }
}