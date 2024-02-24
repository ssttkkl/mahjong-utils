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
                it.field == "tiles" && it.errorInfo == CommonShantenArgsErrorInfo.tilesIsEmpty
            }
        }

        // tooManyFuro
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("11z"),
                listOf(Furo("111m"), Furo("222m"), Furo("333m"), Furo("444m"), Furo("555m"))
            ).validate().any {
                it.field == "tiles" && it.errorInfo == CommonShantenArgsErrorInfo.tooManyFuro
            }
        }

        // tooManyTiles
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("112233z"),
                listOf(Furo("111m"), Furo("222m"), Furo("333m"), Furo("444m"))
            ).validate().any {
                it.field == "tiles" && it.errorInfo == CommonShantenArgsErrorInfo.tooManyTiles
            }
        }

        // anyTileMoreThan4
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("11z"),
                listOf(Furo("111m"), Furo("123m"), Furo("123m"), Furo("234m"))
            ).validate().any {
                it.field == "tiles" && it.errorInfo == CommonShantenArgsErrorInfo.anyTileMoreThan4
            }
        }

        // tilesDividedInto3
        assertTrue {
            CommonShantenArgs(
                Tile.parseTiles("112233z"),
                listOf(Furo("111m"), Furo("123m"))
            ).validate().any {
                it.field == "tiles" && it.errorInfo == CommonShantenArgsErrorInfo.tilesDividedInto3
            }
        }
    }
}