package mahjongutils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
            calcAdvanceNum = false,
            bestShantenOnly = true,
            allowAnkan = false
        )

        val rawResult = ENTRY.call("shanten", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<UnionShantenResult> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = shanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly, args.allowAnkan)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testRegularShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11112345678s"),
            listOf(Furo("999s")),
            calcAdvanceNum = false,
            bestShantenOnly = true,
            allowAnkan = false
        )

        val rawResult = ENTRY.call("regularShanten", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<RegularShantenResult> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult =
            regularShanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly, args.allowAnkan)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testChitoiShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11223344z556789p"),
            calcAdvanceNum = false,
            bestShantenOnly = true
        )

        val rawResult = ENTRY.call("chitoiShanten", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<ChitoiShantenResult> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = chitoiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testKokushiShanten() {
        val args = ShantenArgs(
            Tile.parseTiles("11223344556677z"),
            calcAdvanceNum = false,
            bestShantenOnly = true
        )

        val rawResult = ENTRY.call("kokushiShanten", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<KokushiShantenResult> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = kokushiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testGetParentPointByHanHu() {
        val args = HanHu(5, 30)

        val rawResult = ENTRY.call("getParentPointByHanHu", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<ParentPoint> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = getParentPointByHanHu(args.han, args.hu)
        assertEquals(exceptResult, actualResult.data)
    }

    @Test
    fun testGetChildPointByHanHu() {
        val args = HanHu(5, 30)

        val rawResult = ENTRY.call("getChildPointByHanHu", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<ChildPoint> = Json.decodeFromString(rawResult)
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

        val rawResult = ENTRY.call("hora", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<Hora> = Json.decodeFromString(rawResult)
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
            calcAdvanceNum = false,
            bestShantenOnly = true,
            allowAnkan = false
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

        val rawResult = ENTRY.call("hora", Json.encodeToString(args))
        print("rawResult: ")
        println(rawResult)

        val actualResult: Result<Hora> = Json.decodeFromString(rawResult)
        assertEquals(200, actualResult.code)

        val exceptResult = hora(
            args.shantenResult!!, args.agari,
            args.tsumo, args.dora, args.selfWind, args.roundWind, args.extraYaku
        )
        assertEquals(exceptResult, actualResult.data)
    }
}