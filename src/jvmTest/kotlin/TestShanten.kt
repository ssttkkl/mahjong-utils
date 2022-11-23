import mahjongutils.models.Shanten
import mahjongutils.models.ShantenWithGot
import mahjongutils.models.ShantenWithoutGot
import mahjongutils.models.Tile
import mahjongutils.shanten.regularShanten
import mahjongutils.shanten.shanten
import kotlin.test.Test
import kotlin.test.assertEquals

class TestShanten {
    private fun tester(tiles: String, expected: Shanten) {
        val result = shanten(Tile.parseTiles(tiles))
        assertEquals(expected, result.shantenInfo)
    }

    private fun regularTester(tiles: String, expected: Shanten) {
        val result = regularShanten(Tile.parseTiles(tiles))
        assertEquals(expected, result.shantenInfo)
    }

    @Test
    fun testRegularWithoutGot() {
        regularTester(
            "34568m235p68s", ShantenWithoutGot(
                shantenNum = 2,
                advance = Tile.parseTiles("3678m12345p678s").toSet(),
                advanceNum = 40
            )
        )
        regularTester(
            "112233p44556s12z", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("36s12z").toSet(),
                advanceNum = 13,
                wellShapeAdvance = Tile.parseTiles("12z").toSet(),
                wellShapeAdvanceNum = 6
            )
        )
        regularTester(
            "112233p44556s12z", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("36s12z").toSet(),
                advanceNum = 13,
                wellShapeAdvance = Tile.parseTiles("12z").toSet(),
                wellShapeAdvanceNum = 6
            )
        )
        regularTester(
            "114514p1919810s", ShantenWithoutGot(
                shantenNum = 2,
                advance = Tile.parseTiles("234567p3456789s").toSet(),
                advanceNum = 45,
            )
        )
    }

    @Test
    fun testWithoutGot() {
        tester(
            "34568m235p68s", ShantenWithoutGot(
                shantenNum = 2,
                advance = Tile.parseTiles("3678m12345p678s").toSet(),
                advanceNum = 40
            )
        )
        tester(
            "3344z6699p11345s", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("345s").toSet(),
                advanceNum = 9,
                wellShapeAdvance = emptySet(),
                wellShapeAdvanceNum = 0
            )
        )
        tester(
            "112233p44556s12z", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("36s12z").toSet(),
                advanceNum = 13,
                wellShapeAdvance = Tile.parseTiles("12z").toSet(),
                wellShapeAdvanceNum = 6
            )
        )
        tester(
            "1112345678999p", ShantenWithoutGot(
                shantenNum = 0,
                advance = Tile.parseTiles("123456789p").toSet(),
                advanceNum = 23
            )
        )
        tester(
            "114514p1919810s", ShantenWithoutGot(
                shantenNum = 2,
                advance = Tile.parseTiles("234567p3456789s").toSet(),
                advanceNum = 45,
            )
        )
        tester(
            "119m19p19266s135z", ShantenWithoutGot(
                shantenNum = 3,
                advance = Tile.parseTiles("2467z").toSet(),
                advanceNum = 16,
            )
        )
        tester(
            "19m19p19266s1235z", ShantenWithoutGot(
                shantenNum = 3,
                advance = Tile.allYaochu,
                advanceNum = 42,
            )
        )
        tester(
            "1119m19p19s12355z", ShantenWithoutGot(
                shantenNum = 2,
                advance = Tile.parseTiles("467z").toSet(),
                advanceNum = 12,
            )
        )
        tester(
            "119m19p19s123456z", ShantenWithoutGot(
                shantenNum = 0,
                advance = Tile.parseTiles("7z").toSet(),
                advanceNum = 4,
            )
        )
    }

    @Test
    fun testWithGot() {
//        tester(
//            "1112345678999s1z", ShantenWithGot(
//                shantenNum = 0, discardToAdvance = mapOf(
//                    Tile.get("1s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("123469s1z").toSet(),
//                        wellShapeAdvanceNum = 17
//                    ),
//                    Tile.get("2s") to ShantenWithoutGot(
//                        shantenNum = 0,
//                        advance = Tile.parseTiles("1z").toSet(),
//                        advanceNum = 3,
//                    ),
//                    Tile.get("3s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("23s1z").toSet(),
//                        wellShapeAdvanceNum = 9
//                    ),
//                    Tile.get("4s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("1456789s1z").toSet(),
//                        wellShapeAdvanceNum = 20
//                    ),
//                    Tile.get("5s") to ShantenWithoutGot(
//                        shantenNum = 0,
//                        advance = Tile.parseTiles("1z").toSet(),
//                        advanceNum = 3,
//                    ),
//                    Tile.get("6s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("1234569s1z").toSet(),
//                        wellShapeAdvanceNum = 20
//                    ),
//                    Tile.get("7s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("478s1z").toSet(),
//                        wellShapeAdvanceNum = 12
//                    ),
//                    Tile.get("8s") to ShantenWithoutGot(
//                        shantenNum = 0,
//                        advance = Tile.parseTiles("1z").toSet(),
//                        advanceNum = 3,
//                    ),
//                    Tile.get("9s") to ShantenWithoutGot(
//                        shantenNum = 1,
//                        advance = Tile.parseTiles("123456789s1z").toSet(),
//                        advanceNum = 26,
//                        wellShapeAdvance = Tile.parseTiles("146789s1z").toSet(),
//                        wellShapeAdvanceNum = 17
//                    ),
//                    Tile.get("1z") to ShantenWithoutGot(
//                        shantenNum = 0,
//                        advance = Tile.parseTiles("123456789s").toSet(),
//                        advanceNum = 23,
//                    ),
//                )
//            )
//        )
        tester(
            "114514p1919810s8p", ShantenWithGot(
                shantenNum = 2, discardToAdvance = mapOf(
                    Tile.get("1s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("123469s1z").toSet(),
                        wellShapeAdvanceNum = 17
                    ),
                    Tile.get("2s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("3s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("23s1z").toSet(),
                        wellShapeAdvanceNum = 9
                    ),
                    Tile.get("4s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("1456789s1z").toSet(),
                        wellShapeAdvanceNum = 20
                    ),
                    Tile.get("5s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("6s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("1234569s1z").toSet(),
                        wellShapeAdvanceNum = 20
                    ),
                    Tile.get("7s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("478s1z").toSet(),
                        wellShapeAdvanceNum = 12
                    ),
                    Tile.get("8s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("9s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        wellShapeAdvance = Tile.parseTiles("146789s1z").toSet(),
                        wellShapeAdvanceNum = 17
                    ),
                    Tile.get("1z") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("123456789s").toSet(),
                        advanceNum = 23,
                    ),
                )
            )
        )
    }
}