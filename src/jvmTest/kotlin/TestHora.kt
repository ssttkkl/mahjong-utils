import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hora.hora
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.yaku.Yakus
import kotlin.test.Test
import kotlin.test.assertEquals

class TestHora {
    @Test
    fun test1() {
        val hora = hora(
            tiles = Tile.parseTiles("11123456778899p"),
            furo = emptyList(),
            agari = Tile.get("4p"),
            tsumo = true, dora = 4, extraYaku = setOf(Yakus.Richi)
        )
        assertEquals(setOf(Yakus.Richi, Yakus.Ittsu, Yakus.Chinitsu, Yakus.Ipe, Yakus.Tsumo, Yakus.Pinhu), hora.yaku)
        assertEquals(16, hora.han)
        assertEquals(20, hora.hu)
        assertEquals(ParentPoint(48000, 16000), hora.parentPoint)
        assertEquals(ChildPoint(32000, 16000, 8000), hora.childPoint)
    }

    @Test
    fun test2() {
        val hora = hora(
            tiles = Tile.parseTiles("11123456789999p"),
            furo = emptyList(),
            agari = Tile.get("4p"),
            tsumo = true, dora = 4, extraYaku = setOf(Yakus.Richi)
        )
        assertEquals(setOf(Yakus.Churen), hora.yaku)
        assertEquals(13, hora.han)
        assertEquals(ParentPoint(48000, 16000), hora.parentPoint)
        assertEquals(ChildPoint(32000, 16000, 8000), hora.childPoint)
    }

    @Test
    fun test3() {
        val hora = hora(
            tiles = Tile.parseTiles("1345556m111z2m"),
            furo = listOf(Furo("789m")),
            agari = Tile.get("2m"),
            tsumo = true
        )
        assertEquals(setOf(Yakus.Ittsu, Yakus.Honitsu), hora.yaku)
        assertEquals(3, hora.han)
        assertEquals(40, hora.hu)
        assertEquals(ParentPoint(7700, 2600), hora.parentPoint)
        assertEquals(ChildPoint(5200, 2600, 1300), hora.childPoint)
    }

    @Test
    fun test4() {
        val hora = hora(
            tiles = Tile.parseTiles("12323467m11z5m"),
            furo = listOf(Furo("789p")),
            agari = Tile.get("5m"),
            tsumo = true
        )
        assertEquals(setOf(), hora.yaku)
        assertEquals(0, hora.han)
        assertEquals(30, hora.hu)
        assertEquals(ParentPoint(0, 0), hora.parentPoint)
        assertEquals(ChildPoint(0, 0, 0), hora.childPoint)
    }

    @Test
    fun test5() {
        val hora = hora(
            tiles = Tile.parseTiles("11122233344455z"),
            agari = Tile.get("5z"),
            tsumo = true,
            extraYaku = setOf(Yakus.Tenhou)
        )
        assertEquals(setOf(Yakus.Tsuiso, Yakus.Daisushi, Yakus.SuankoTanki, Yakus.Tenhou), hora.yaku)
        assertEquals(13 * 6, hora.han)
        assertEquals(ParentPoint(48000 * 6, 16000 * 6), hora.parentPoint)
        assertEquals(ChildPoint(32000 * 6, 16000 * 6, 8000 * 6), hora.childPoint)
    }

    @Test
    fun test6() {
        val hora = hora(
            tiles = Tile.parseTiles("55z"),
            furo = listOf(Furo("0110z"), Furo("0220z"), Furo("0330z"), Furo("0440z")),
            agari = Tile.get("5z"),
            tsumo = true,
        )
        assertEquals(setOf(Yakus.SuankoTanki, Yakus.Sukantsu, Yakus.Daisushi, Yakus.Tsuiso), hora.yaku)
        assertEquals(13 * 6, hora.han)
        assertEquals(ParentPoint(48000 * 6, 16000 * 6), hora.parentPoint)
        assertEquals(ChildPoint(32000 * 6, 16000 * 6, 8000 * 6), hora.childPoint)
    }

    @Test
    fun test7() {
        val hora = hora(
            tiles = Tile.parseTiles("66z"),
            furo = listOf(Furo("0330s"), Furo("0220s"), Furo("0440s"), Furo("0880s")),
            agari = Tile.get("6z"),
            tsumo = true
        )
        assertEquals(setOf(Yakus.Lyuiso, Yakus.Sukantsu, Yakus.SuankoTanki), hora.yaku)
        assertEquals(13 * 4, hora.han)
        assertEquals(ParentPoint(48000 * 4, 16000 * 4), hora.parentPoint)
        assertEquals(ChildPoint(32000 * 4, 16000 * 4, 8000 * 4), hora.childPoint)
    }

    @Test
    fun test8() {
        val hora = hora(
            tiles = Tile.parseTiles("111333555z222s44p"),
            agari = Tile.get("2s"),
            tsumo = false,
            dora = 4,
            selfWind = Wind.West,
            roundWind = Wind.East,
            extraYaku = setOf(Yakus.Richi, Yakus.Ippatsu)
        )
        assertEquals(
            setOf(
                Yakus.Sananko,
                Yakus.Toitoi,
                Yakus.Richi,
                Yakus.Ippatsu,
                Yakus.SelfWind,
                Yakus.RoundWind,
                Yakus.Haku
            ), hora.yaku
        )
        assertEquals(13, hora.han)
        assertEquals(60, hora.hu)
        assertEquals(ParentPoint(48000, 16000), hora.parentPoint)
        assertEquals(ChildPoint(32000, 16000, 8000), hora.childPoint)
    }
}