package mahjongutils.shanten

import mahjongutils.models.Furo
import mahjongutils.models.Tile
import kotlin.test.Test
import kotlin.test.assertTrue

class TestArgsValidation {
    @Test
    fun testCommonShantenArgsValidation() {
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("1123456789m"),
                listOf(Furo("123m"))
            ).validate().isEmpty()
        }

        // tilesIsEmpty
        assertTrue {
            CommonShantenArgs(
                emptyList()
            ).validate().any {
                it == CommonShantenArgsErrorInfo.tilesIsEmpty
            }
        }

        // tooManyFuro
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("11z"),
                listOf(Furo("111m"), Furo("222m"), Furo("333m"), Furo("444m"), Furo("555m"))
            ).validate().any {
                it == CommonShantenArgsErrorInfo.tooManyFuro
            }
        }

        // tooManyTiles
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("112233z"),
                listOf(Furo("111m"), Furo("222m"), Furo("333m"), Furo("444m"))
            ).validate().any {
                it == CommonShantenArgsErrorInfo.tooManyTiles
            }
        }

        // anyTileMoreThan4
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("11z"),
                listOf(Furo("111m"), Furo("123m"), Furo("123m"), Furo("234m"))
            ).validate().any {
                it == CommonShantenArgsErrorInfo.anyTileMoreThan4
            }
        }

        // tilesNumIllegal
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("112233z"),
                listOf(Furo("111m"), Furo("123m"))
            ).validate().any {
                it == CommonShantenArgsErrorInfo.tilesNumIllegal
            }
        }
    }
    
    @Test
    fun testFuroChanceShantenArgsValidation() {
        assertTrue {
            FuroChanceShantenArgs(
                Tile.parseTiles("1123456789m"),
                Tile["3m"]
            ).validate().isEmpty()
        }

        // tilesIsEmpty
        assertTrue {
            FuroChanceShantenArgs(
                emptyList(),
                Tile["3m"]
            ).validate().any {
                it == FuroChanceShantenArgsErrorInfo.tilesIsEmpty
            }
        }

        // tooManyTiles
        assertTrue {
            FuroChanceShantenArgs(
                Tile.parseTiles("1112345678999m111z"),
                Tile["3m"]
            ).validate().any {
                it == FuroChanceShantenArgsErrorInfo.tooManyTiles
            }
        }

        // anyTileMoreThan4
        assertTrue {
            FuroChanceShantenArgs(
                Tile.parseTiles("3333456m"),
                Tile["3m"]
            ).validate().any {
                it == FuroChanceShantenArgsErrorInfo.anyTileMoreThan4
            }
        }

        // tilesNumIllegal
        assertTrue {
            FuroChanceShantenArgs(
                Tile.parseTiles("334455m"),
                Tile["3m"]
            ).validate().any {
                it == FuroChanceShantenArgsErrorInfo.tilesNumIllegal
            }
        }
    }
}