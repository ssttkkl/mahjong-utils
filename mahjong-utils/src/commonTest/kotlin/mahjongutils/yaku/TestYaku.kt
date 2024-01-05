package mahjongutils.yaku

import mahjongutils.models.*
import mahjongutils.hora.ChitoiHoraHandPattern
import mahjongutils.models.hand.RegularHandPattern
import mahjongutils.hora.RegularHoraHandPattern
import mahjongutils.yaku.Yakus
import kotlin.test.Test

internal class TestYaku {
    @Test
    fun testChitoi_Honroto_Tsumo() {
        val pattern = ChitoiHoraHandPattern(
            pairs = Tile.parseTiles("19m19s123z").toSet(),
            agari = Tile.get("3z"),
            tsumo = true
        )

        tester(pattern, setOf(Yakus.Chitoi, Yakus.Honroto, Yakus.Tsumo))
    }

    @Test
    fun testChitoi_Chinitsu_Tsumo_Tanyao() {
        val pattern = ChitoiHoraHandPattern(
            pairs = Tile.parseTiles("2345678s").toSet(),
            agari = Tile.get("2s"),
            tsumo = true
        )

        tester(pattern, setOf(Yakus.Chitoi, Yakus.Chinitsu, Yakus.Tsumo, Yakus.Tanyao))
    }

    @Test
    fun testToitoi_Honroto_Honitsu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("4z"),
                menzenMentsu = listOf(Mentsu("111s"), Mentsu("999s")),
                furo = listOf(Furo("111z"), Furo("222z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4z"),
            agariTatsu = null,
            tsumo = false
        )

        tester(pattern, setOf(Yakus.Toitoi, Yakus.Honitsu, Yakus.Honroto))
    }

    @Test
    fun testIttsu_Chinitsu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("3s"),
                menzenMentsu = listOf(Mentsu("789s"), Mentsu("999s")),
                furo = listOf(Furo("123s"), Furo("456s")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("3s"),
            agariTatsu = null,
            tsumo = false
        )

        tester(pattern, setOf(Yakus.Ittsu, Yakus.Chinitsu))
    }

    @Test
    fun testTanyao_Ryanpe() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("345s"), Mentsu("345s"), Mentsu("345p"), Mentsu("345p")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4s"),
            agariTatsu = Tatsu("35s"),
            tsumo = false
        )

        tester(pattern, setOf(Yakus.Tanyao, Yakus.Ryanpe))
    }

    @Test
    fun testTanyao_Ipe_Pinhu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("345s"), Mentsu("345s"), Mentsu("567p"), Mentsu("678m")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("5s"),
            agariTatsu = Tatsu("34s"),
            tsumo = false
        )

        tester(pattern, setOf(Yakus.Tanyao, Yakus.Ipe, Yakus.Pinhu))
    }

    @Test
    fun testTanyao_Sanshoku() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("345m"), Mentsu("345p"), Mentsu("678m")),
                furo = listOf(Furo("345s")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("5m"),
            agariTatsu = Tatsu("34m"),
            tsumo = false
        )

        tester(pattern, setOf(Yakus.Tanyao, Yakus.Sanshoku))
    }

    @Test
    fun testSelfWind_RoundWind_Haku() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("345p"), Mentsu("678m")),
                furo = listOf(Furo("111z"), Furo("555z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("5p"),
            agariTatsu = Tatsu("34p"),
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East
        )

        tester(pattern, setOf(Yakus.SelfWind, Yakus.RoundWind, Yakus.Haku))
    }

    @Test
    fun testHatsu_Chun_Shosangen() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("5z"),
                menzenMentsu = listOf(Mentsu("345p"), Mentsu("678m")),
                furo = listOf(Furo("666z"), Furo("777z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("5p"),
            agariTatsu = Tatsu("34p"),
            tsumo = false,
            selfWind = Wind.East,
            roundWind = Wind.East
        )

        tester(pattern, setOf(Yakus.Hatsu, Yakus.Chun, Yakus.Shosangen))
    }

    @Test
    fun testToitoi_Sananko() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("555s"), Mentsu("111m"), Mentsu("222m")),
                furo = listOf(Furo("333s")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("2m"),
            agariTatsu = Tatsu("22m"),
            tsumo = true,
        )

        tester(pattern, setOf(Yakus.Toitoi, Yakus.Sananko))
    }

    @Test
    fun testToitoi_Sananko_2() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2s"),
                menzenMentsu = listOf(Mentsu("333s"), Mentsu("555s"), Mentsu("111m"), Mentsu("222m")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("2m"),
            agariTatsu = Tatsu("22m"),
            tsumo = false,
        )

        tester(pattern, setOf(Yakus.Toitoi, Yakus.Sananko))
    }

    @Test
    fun testChanta() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("9p"),
                menzenMentsu = listOf(Mentsu("789s"), Mentsu("111m"), Mentsu("123m")),
                furo = listOf(Furo("111z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("1m"),
            agariTatsu = Tatsu("23m"),
            tsumo = false,
        )

        tester(pattern, setOf(Yakus.Chanta))
    }

    @Test
    fun testJunchan() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("9p"),
                menzenMentsu = listOf(Mentsu("789s"), Mentsu("111m"), Mentsu("123m")),
                furo = listOf(Furo("111p")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("1m"),
            agariTatsu = Tatsu("23m"),
            tsumo = false,
        )

        tester(pattern, setOf(Yakus.Junchan))
    }


    @Test
    fun testSananko_Sandoko() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("9p"),
                menzenMentsu = listOf(Mentsu("456m"), Mentsu("111p")),
                furo = listOf(Furo("1111m", true), Furo("1111s", true)),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4m"),
            agariTatsu = Tatsu("56m"),
            tsumo = false,
        )

        tester(pattern, setOf(Yakus.Sananko, Yakus.Sandoko))
    }


    @Test
    fun testSandoko_Sankantsu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("9p"),
                menzenMentsu = listOf(Mentsu("456m")),
                furo = listOf(Furo("1111p", false), Furo("1111m", true), Furo("1111s", true)),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4m"),
            agariTatsu = Tatsu("56m"),
            tsumo = false,
        )

        tester(pattern, setOf(Yakus.Sandoko, Yakus.Sankantsu))
    }


}