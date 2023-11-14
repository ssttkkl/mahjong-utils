package mahjongutils.yaku

import mahjongutils.models.*
import mahjongutils.hora.ChitoiHoraHandPattern
import mahjongutils.models.hand.RegularHandPattern
import mahjongutils.hora.RegularHoraHandPattern
import mahjongutils.yaku.Yakus.Chanta
import mahjongutils.yaku.Yakus.Chinitsu
import mahjongutils.yaku.Yakus.Chitoi
import mahjongutils.yaku.Yakus.Chun
import mahjongutils.yaku.Yakus.Haku
import mahjongutils.yaku.Yakus.Hatsu
import mahjongutils.yaku.Yakus.Honitsu
import mahjongutils.yaku.Yakus.Honroto
import mahjongutils.yaku.Yakus.Ipe
import mahjongutils.yaku.Yakus.Ittsu
import mahjongutils.yaku.Yakus.Junchan
import mahjongutils.yaku.Yakus.Pinhu
import mahjongutils.yaku.Yakus.RoundWind
import mahjongutils.yaku.Yakus.Ryanpe
import mahjongutils.yaku.Yakus.Sananko
import mahjongutils.yaku.Yakus.Sandoko
import mahjongutils.yaku.Yakus.Sankantsu
import mahjongutils.yaku.Yakus.Sanshoku
import mahjongutils.yaku.Yakus.SelfWind
import mahjongutils.yaku.Yakus.Tanyao
import mahjongutils.yaku.Yakus.Toitoi
import mahjongutils.yaku.Yakus.Tsumo
import kotlin.test.Test

internal class TestYaku {
    @Test
    fun testChitoi_Honroto_Tsumo() {
        val pattern = ChitoiHoraHandPattern(
            pairs = Tile.parseTiles("19m19s123z").toSet(),
            agari = Tile.get("3z"),
            tsumo = true
        )

        tester(pattern, setOf(Chitoi, Honroto, Tsumo))
    }

    @Test
    fun testChitoi_Chinitsu_Tsumo_Tanyao() {
        val pattern = ChitoiHoraHandPattern(
            pairs = Tile.parseTiles("2345678s").toSet(),
            agari = Tile.get("2s"),
            tsumo = true
        )

        tester(pattern, setOf(Chitoi, Chinitsu, Tsumo, Tanyao))
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

        tester(pattern, setOf(Toitoi, Honitsu, Honroto))
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

        tester(pattern, setOf(Ittsu, Chinitsu))
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

        tester(pattern, setOf(Tanyao, Ryanpe))
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

        tester(pattern, setOf(Tanyao, Ipe, Pinhu))
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

        tester(pattern, setOf(Tanyao, Sanshoku))
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

        tester(pattern, setOf(SelfWind, RoundWind, Haku))
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

        tester(pattern, setOf(Hatsu, Chun, Yakus.Shosangen))
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

        tester(pattern, setOf(Toitoi, Sananko))
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

        tester(pattern, setOf(Toitoi, Sananko))
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

        tester(pattern, setOf(Chanta))
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

        tester(pattern, setOf(Junchan))
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

        tester(pattern, setOf(Sananko, Sandoko))
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

        tester(pattern, setOf(Sandoko, Sankantsu))
    }


}