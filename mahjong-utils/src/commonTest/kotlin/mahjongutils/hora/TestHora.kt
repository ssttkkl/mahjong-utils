package mahjongutils.hora

import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.chitoiShanten
import mahjongutils.shanten.shanten
import mahjongutils.yaku.Yakus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        assertEquals(ParentPoint(0uL, 16000uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 16000uL, 8000uL), hora.childPoint)
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
        assertEquals(ParentPoint(0uL, 16000uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 16000uL, 8000uL), hora.childPoint)
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
        assertEquals(ParentPoint(0uL, 2600uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 2600uL, 1300uL), hora.childPoint)
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
        assertEquals(ParentPoint(0uL, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 0uL, 0uL), hora.childPoint)
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
        assertEquals(ParentPoint(0uL, 16000uL * 6uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 16000uL * 6uL, 8000uL * 6uL), hora.childPoint)
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
        assertEquals(ParentPoint(0uL, 16000uL * 6uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 16000uL * 6uL, 8000uL * 6uL), hora.childPoint)
    }

    @Test
    fun test7() {
        val hora = hora(
            tiles = Tile.parseTiles("6z"),
            furo = listOf(Furo("0330s"), Furo("0220s"), Furo("0440s"), Furo("0880s")),
            agari = Tile.get("6z"),
            tsumo = true
        )
        assertEquals(setOf(Yakus.Lyuiso, Yakus.Sukantsu, Yakus.SuankoTanki), hora.yaku)
        assertEquals(13 * 4, hora.han)
        assertEquals(ParentPoint(0uL, 16000uL * 4uL), hora.parentPoint)
        assertEquals(ChildPoint(0uL, 16000uL * 4uL, 8000uL * 4uL), hora.childPoint)
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
        assertEquals(ParentPoint(48000uL, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(32000uL, 0uL, 0uL), hora.childPoint)
    }

    @Test
    fun test9() {
        val hora = hora(
            tiles = Tile.parseTiles("111122223333m99s"),
            agari = Tile.get("9s"),
            tsumo = false
        )
        assertEquals(hora.han, 3)
        assertEquals(hora.hu, 40)
        assertEquals(ParentPoint(7700uL, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(5200uL, 0uL, 0uL), hora.childPoint)
    }

    @Test
    fun test10() {
        val hora = hora(
            tiles = Tile.parseTiles("234p11z"),
            furo = listOf(Furo("0110s"), Furo("0110m"), Furo("0990m")),
            agari = Tile.get("1z"),
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East
        )
        assertEquals(hora.han, 4)
        assertEquals(hora.hu, 140)
        assertEquals(ParentPoint(ParentPoint.Mangan.ron, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(ChildPoint.Mangan.ron, 0uL, 0uL), hora.childPoint)
    }

    @Test
    fun test11() {
        assertFailsWith<HoraArgsValidationException> {
            hora(
                tiles = Tile.parseTiles("234p11z"),
                furo = listOf(Furo("0110s"), Furo("0990m")),
                agari = Tile.get("1z"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }

        assertFailsWith<HoraArgsValidationException> {
            hora(
                tiles = Tile.parseTiles("234p11z"),
                furo = listOf(Furo("0110s"), Furo("0990m")),
                agari = Tile.get("2z"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }

        assertFailsWith<HoraArgsValidationException> {
            val shantenResult = shanten(
                tiles = Tile.parseTiles("234p11z"),
                furo = listOf(Furo("0110s"), Furo("0990m"))
            )
            hora(
                shantenResult = shantenResult,
                agari = Tile.get("1z"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }

        assertFailsWith<HoraArgsValidationException> {
            val shantenResult = shanten(
                tiles = Tile.parseTiles("234p11z"),
                furo = listOf(Furo("0110s"), Furo("0110m"), Furo("0990m"))
            )
            hora(
                shantenResult = shantenResult,
                agari = Tile.get("1p"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }

        assertFailsWith<HoraArgsValidationException> {
            val shantenResult = shanten(
                tiles = Tile.parseTiles("235p11z"),
                furo = listOf(Furo("0110s"), Furo("0110m"), Furo("0990m"))
            )
            hora(
                shantenResult = shantenResult,
                agari = Tile.get("1p"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }

        assertFailsWith<HoraArgsValidationException> {
            val shantenResult = shanten(
                tiles = Tile.parseTiles("235p1z"),
                furo = listOf(Furo("0110s"), Furo("0110m"), Furo("0990m"))
            )
            hora(
                shantenResult = shantenResult,
                agari = Tile.get("1p"),
                tsumo = false,
                selfWind = Wind.East,
                roundWind = Wind.East
            )
        }
    }

    @Test
    fun test12() {
        val shantenResult = chitoiShanten(
            Tile.parseTiles("1133557799m1122z")
        )
        val hora = hora(
            shantenResult,
            agari = Tile["1z"],
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East
        )
        assertEquals(hora.han, 5)
        assertEquals(hora.hu, 25)
        assertEquals(hora.yaku, setOf(Yakus.Chitoi, Yakus.Honitsu))
        assertEquals(ParentPoint(ParentPoint.Mangan.ron, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(ChildPoint.Mangan.ron, 0uL, 0uL), hora.childPoint)
    }

    @Test
    fun test13() {
        val hora = hora(
            tiles = Tile.parseTiles("19m19s19p12345677z"),
            agari = Tile.get("1z"),
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East
        )
        assertEquals(hora.han, 13)
        assertEquals(hora.hu, 30)
        assertEquals(hora.yaku, setOf(Yakus.Kokushi))
        assertEquals(ParentPoint(ParentPoint.Yakuman.ron, 0uL), hora.parentPoint)
        assertEquals(ChildPoint(ChildPoint.Yakuman.ron, 0uL, 0uL), hora.childPoint)
    }
}

class TestHoraWithCustomOptions {
    @Test
    fun testNotHaveComplexYakuman() {
        val hora = hora(
            Tile.parseTiles("11122233344455z"),
            agari = Tile["5z"],
            tsumo = true,
            options = HoraOptions.Default.copy(hasComplexYakuman = false)
        )
        assertEquals(13 * 2, hora.han)
    }

    @Test
    fun testNotHaveMultipleYakuman() {
        val hora = hora(
            Tile.parseTiles("11122233344455z"),
            agari = Tile["5z"],
            tsumo = true,
            options = HoraOptions.Default.copy(hasMultipleYakuman = false)
        )
        assertEquals(13 * 3, hora.han)
    }

    @Test
    fun testNotHaveRenpuuJyantouHu() {
        val hora = hora(
            Tile.parseTiles("111456p123456s11z"),
            agari = Tile["1s"],
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East,
            options = HoraOptions.Default.copy(hasRenpuuJyantouHu = false)
        )
        assertEquals(40, hora.hu)
    }

    @Test
    fun testNotAllowKuitan() {
        val hora = hora(
            Tile.parseTiles("2233p"),
            listOf(Furo("234s"), Furo("456s"), Furo("567s")),
            Tile["3p"],
            true,
            options = HoraOptions.Default.copy(allowKuitan = false)
        )
        assertEquals(0, hora.han)
    }

    @Test
    fun testNotHaveKazoeYakuman() {
        val hora = hora(
            Tile.parseTiles("111456p123456s11z"),
            agari = Tile["1s"],
            tsumo = true,
            selfWind = Wind.East,
            roundWind = Wind.East,
            dora = 114514,
            options = HoraOptions.Default.copy(hasKazoeYakuman = false)
        )
        assertEquals(ParentPoint.Sanbaiman.tsumo, hora.parentPoint.tsumo)
    }

    @Test
    fun testHaveKiriageMangan() {
        val hora = hora(
            Tile.parseTiles("123456789p234m44s"),
            agari = Tile["9p"],
            tsumo = false,
            dora = 1,
            selfWind = Wind.East,
            roundWind = Wind.East,
            options = HoraOptions.Default.copy(hasKiriageMangan = true)
        )
        assertEquals(ParentPoint.Mangan.ron, hora.parentPoint.ron)
    }

    @Test
    fun testAotenjou() {
        val hora = hora(
            Tile.parseTiles("111456p123456s11z"),
            agari = Tile["1s"],
            tsumo = true,
            selfWind = Wind.East,
            roundWind = Wind.East,
            dora = 7,
            options = HoraOptions.Default.copy(aotenjou = true)
        )
        assertEquals(82000uL, hora.parentPoint.tsumo)

        val hora2 = hora(
            Tile.parseTiles("111456p123456s11z"),
            agari = Tile["1s"],
            tsumo = true,
            selfWind = Wind.South,
            roundWind = Wind.East,
            dora = 7,
            options = HoraOptions.Default.copy(aotenjou = true)
        )
        assertEquals(82000uL, hora2.childPoint.tsumoParent)
        assertEquals(41000uL, hora2.childPoint.tsumoChild)

        val hora3 = hora(
            Tile.parseTiles("19m19p19s12345677z"),
            agari = Tile["6z"],
            tsumo = true,
            selfWind = Wind.East,
            roundWind = Wind.East,
            dora = 0,
            options = HoraOptions.Default.copy(aotenjou = true)
        )
        assertEquals(setOf(Yakus.Kokushi, Yakus.Tsumo).map { it.name }.toSet(), hora3.yaku.map { it.name }.toSet())
        assertEquals(3932200uL, hora3.parentPoint.tsumo)

        val hora4 = hora(
            Tile.parseTiles("19m19p19s12345677z"),
            agari = Tile["6z"],
            tsumo = false,
            selfWind = Wind.South,
            roundWind = Wind.East,
            dora = 2,
            options = HoraOptions.Default.copy(aotenjou = true)
        )
        assertEquals(setOf(Yakus.Kokushi).map { it.name }.toSet(), hora4.yaku.map { it.name }.toSet())
        assertEquals(15728700uL, hora4.childPoint.ron)
    }
}