package mahjongutils.yaku

import mahjongutils.models.Furo
import mahjongutils.models.Mentsu
import mahjongutils.models.Tatsu
import mahjongutils.models.Tile
import mahjongutils.hora.KokushiHoraHandPattern
import mahjongutils.models.hand.RegularHandPattern
import mahjongutils.hora.RegularHoraHandPattern
import mahjongutils.yaku.Yakus.Chinroto
import mahjongutils.yaku.Yakus.Churen
import mahjongutils.yaku.Yakus.ChurenNineWaiting
import mahjongutils.yaku.Yakus.Daisangen
import mahjongutils.yaku.Yakus.Daisushi
import mahjongutils.yaku.Yakus.Kokushi
import mahjongutils.yaku.Yakus.KokushiThirteenWaiting
import mahjongutils.yaku.Yakus.Lyuiso
import mahjongutils.yaku.Yakus.Shousushi
import mahjongutils.yaku.Yakus.Suanko
import mahjongutils.yaku.Yakus.SuankoTanki
import mahjongutils.yaku.Yakus.Sukantsu
import mahjongutils.yaku.Yakus.Tsuiso
import kotlin.test.Test

internal class TestYakuman {
    @Test
    fun testKokushi() {
        val pattern = KokushiHoraHandPattern(
            repeated = Tile.get("1z"),
            agari = Tile.get("5z"),
            tsumo = true
        )

        tester(pattern, setOf(Kokushi))
    }

    @Test
    fun testKokushiThirteenWaiting() {
        val pattern = KokushiHoraHandPattern(
            repeated = Tile.get("1z"),
            agari = Tile.get("1z"),
            tsumo = true
        )

        tester(pattern, setOf(KokushiThirteenWaiting))
    }

    @Test
    fun testSuankoTanki_Lyuiso() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("6z"),
                menzenMentsu = listOf(Mentsu("222s"), Mentsu("333s"), Mentsu("444s"), Mentsu("666s")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("6z"),
            agariTatsu = null,
            tsumo = false
        )

        tester(pattern, setOf(SuankoTanki, Lyuiso))
    }

    @Test
    fun testSuanko_Lyuiso() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("8s"),
                menzenMentsu = listOf(Mentsu("222s"), Mentsu("333s"), Mentsu("444s"), Mentsu("666s")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("6s"),
            agariTatsu = Tatsu("66s"),
            tsumo = true
        )

        tester(pattern, setOf(Suanko, Lyuiso))
    }

    @Test
    fun testSuankoTanki_Sukantsu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("8s"),
                menzenMentsu = emptyList(),
                furo = listOf(Furo("2222m", true), Furo("3333p", true), Furo("4444s", true), Furo("6666s", true)),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("8s"),
            agariTatsu = null,
            tsumo = true
        )

        tester(pattern, setOf(SuankoTanki, Sukantsu))
    }

    @Test
    fun testChuren() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("1s"),
                menzenMentsu = listOf(Mentsu("123s"), Mentsu("456s"), Mentsu("789s"), Mentsu("999s")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("7s"),
            agariTatsu = Tatsu("89s"),
            tsumo = false
        )

        tester(pattern, setOf(Churen))
    }

    @Test
    fun testChurenNineWaiting() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("1s"),
                menzenMentsu = listOf(Mentsu("123s"), Mentsu("456s"), Mentsu("789s"), Mentsu("999s")),
                furo = emptyList(),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("9s"),
            agariTatsu = Tatsu("99s"),
            tsumo = false
        )

        tester(pattern, setOf(ChurenNineWaiting))
    }

    @Test
    fun testDaisushi_Tsuiso() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("5z"),
                menzenMentsu = listOf(Mentsu("333z"), Mentsu("444z")),
                furo = listOf(Furo("111z"), Furo("222z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4z"),
            agariTatsu = Tatsu("44z"),
            tsumo = false
        )

        tester(pattern, setOf(Daisushi, Tsuiso))
    }

    @Test
    fun testDaisangen() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("1s"),
                menzenMentsu = listOf(Mentsu("777z"), Mentsu("123s")),
                furo = listOf(Furo("555z"), Furo("666z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("7z"),
            agariTatsu = Tatsu("77z"),
            tsumo = false
        )

        tester(pattern, setOf(Daisangen))
    }

    @Test
    fun testShousushi() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("2z"),
                menzenMentsu = listOf(Mentsu("444z"), Mentsu("123s")),
                furo = listOf(Furo("111z"), Furo("333z")),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("4z"),
            agariTatsu = Tatsu("44z"),
            tsumo = false
        )

        tester(pattern, setOf(Shousushi))
    }

    @Test
    fun testChinroto_Sukantsu() {
        val pattern = RegularHoraHandPattern(
            RegularHandPattern(
                k = 4,
                jyantou = Tile.get("1s"),
                menzenMentsu = emptyList(),
                furo = listOf(
                    Furo("1111p", false), Furo("9999p", true),
                    Furo("1111m", true), Furo("9999m", false),
                ),
                tatsu = emptyList(),
                remaining = emptyList()
            ),
            agari = Tile.get("1s"),
            agariTatsu = null,
            tsumo = false
        )

        tester(pattern, setOf(Chinroto, Sukantsu))
    }
}