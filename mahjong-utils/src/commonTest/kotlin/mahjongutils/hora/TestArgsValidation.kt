package mahjongutils.hora

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.shanten.shanten
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestArgsValidation {
    @Test
    fun testBothNull() {
        assertTrue {
            HoraArgs(
                agari = Tile["3m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.bothTilesAndShantenResultAreNull
            )
        }
    }

    @Test
    fun testWithTiles() {
        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("1112345678999m"),
                agari = Tile["3m"],
                tsumo = false
            ).validate().isEmpty()
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("11123345678999m"),
                agari = Tile["3m"],
                tsumo = false
            ).validate().isEmpty()
        }

        assertTrue {
            HoraArgs(
                tiles = emptyList(),
                agari = Tile["3m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.tilesIsEmpty
            )
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("11m"),
                furo = listOf(Furo("123m"), Furo("123m"), Furo("456m"), Furo("456m"), Furo("789m")),
                agari = Tile["1m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.tooManyFuro
            )
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("114567m"),
                furo = listOf(Furo("123m"), Furo("123m"), Furo("456m")),
                agari = Tile["1m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.tilesNumIllegal
            )
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("11m"),
                furo = listOf(Furo("123m"), Furo("123m"), Furo("456m")),
                agari = Tile["1m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.tilesNumIllegal
            )
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("11m"),
                furo = listOf(Furo("123m"), Furo("123m"), Furo("456m"), Furo("456m")),
                agari = Tile["3m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.agariNotInTiles
            )
        }

        assertFalse {
            HoraArgs(
                tiles = Tile.parseTiles("1123m"),
                furo = listOf(Furo("123m"), Furo("456m"), Furo("456m")),
                agari = Tile["4m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.agariNotInTiles
            )
        }

        assertTrue {
            HoraArgs(
                tiles = Tile.parseTiles("1123m"),
                furo = listOf(Furo("123m"), Furo("123m"), Furo("123m")),
                agari = Tile["4m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.anyTileMoreThan4
            )
        }
    }

    @Test
    fun testWithShantenResult() {
        assertTrue {
            HoraArgs(
                shantenResult = shanten(
                    Tile.parseTiles("11123456789999m")
                ),
                agari = Tile["9m"],
                tsumo = false
            ).validate().isEmpty()
        }

        assertTrue {
            HoraArgs(
                shantenResult = shanten(
                    Tile.parseTiles("11123456789m"),
                    listOf(Furo("0110z"))
                ),
                agari = Tile["9m"],
                tsumo = false
            ).validate().isEmpty()
        }

        assertTrue {
            HoraArgs(
                shantenResult = shanten(
                    Tile.parseTiles("11123m345p668999s")
                ),
                agari = Tile["1m"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.shantenNotHora
            )
        }

        assertTrue {
            HoraArgs(
                shantenResult = shanten(
                    Tile.parseTiles("11123456789999m")
                ),
                agari = Tile["1z"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.agariNotInTiles
            )
        }

        assertTrue {
            HoraArgs(
                shantenResult = shanten(
                    Tile.parseTiles("11123456789m")
                ),
                agari = Tile["1z"],
                tsumo = false
            ).validate().contains(
                HoraArgsErrorInfo.tilesNumIllegal
            )
        }
    }
}