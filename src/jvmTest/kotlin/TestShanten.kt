import mahjongutils.models.Tile
import mahjongutils.shanten.*
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

    private fun chitoiTester(tiles: String, expected: Shanten) {
        val result = chitoiShanten(Tile.parseTiles(tiles))
        assertEquals(expected, result.shantenInfo)
    }

    private fun kokushiTester(tiles: String, expected: Shanten) {
        val result = chitoiShanten(Tile.parseTiles(tiles))
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
                goodShapeAdvance = Tile.parseTiles("12z").toSet(),
                goodShapeAdvanceNum = 6
            )
        )
        regularTester(
            "112233p44556s12z", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("36s12z").toSet(),
                advanceNum = 13,
                goodShapeAdvance = Tile.parseTiles("12z").toSet(),
                goodShapeAdvanceNum = 6
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
                goodShapeAdvance = emptySet(),
                goodShapeAdvanceNum = 0
            )
        )
        tester(
            "112233p44556s12z", ShantenWithoutGot(
                shantenNum = 1,
                advance = Tile.parseTiles("36s12z").toSet(),
                advanceNum = 13,
                goodShapeAdvance = Tile.parseTiles("12z").toSet(),
                goodShapeAdvanceNum = 6
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
        kokushiTester(
            "119m19p19s123456z", ShantenWithoutGot(
                shantenNum = 0,
                advance = Tile.parseTiles("7z").toSet(),
                advanceNum = 4,
            )
        )
        chitoiTester(
            "1111223344556z", ShantenWithoutGot(
                shantenNum = 2,
                advance = (Tile.all.filter { it.num != 0 } - Tile.parseTiles("12345z").toSet()).toSet(),
                advanceNum = 115
            )
        )
    }

    @Test
    fun testWithGot() {
        tester(
            "34568m235p368s", ShantenWithGot(
                shantenNum = 2, discardToAdvance = mapOf(
                    Tile.get("5p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3678m1234p3678s").toSet(),
                        advanceNum = 40
                    ),
                    Tile.get("3s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3678m12345p678s").toSet(),
                        advanceNum = 40
                    ),
                    Tile.get("2p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3678m345p3678s").toSet(),
                        advanceNum = 36
                    ),
                    Tile.get("8m") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("36m1245p37s").toSet(),
                        advanceNum = 27
                    ),
                    Tile.get("3m") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m1245p37s").toSet(),
                        advanceNum = 24
                    ),
                    Tile.get("6m") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m1245p37s").toSet(),
                        advanceNum = 24
                    ),
                    Tile.get("6s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("7m1245p38s").toSet(),
                        advanceNum = 24
                    ),
                    Tile.get("8s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("7m1245p36s").toSet(),
                        advanceNum = 24
                    ),
                    Tile.get("3p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("7m25p37s").toSet(),
                        advanceNum = 17
                    ),
                    Tile.get("4m") to ShantenWithoutGot(
                        shantenNum = 3,
                        advance = Tile.parseTiles("345678m12345p3678s").toSet(),
                        advanceNum = 49
                    ),
                    Tile.get("5m") to ShantenWithoutGot(
                        shantenNum = 3,
                        advance = Tile.parseTiles("2345678m12345p3678s").toSet(),
                        advanceNum = 53
                    )
                )
            )
        )
        tester(
            "1112345678999s1z", ShantenWithGot(
                shantenNum = 0, discardToAdvance = mapOf(
                    Tile.get("2s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("5s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("8s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("1z").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("1z") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("123456789s").toSet(),
                        advanceNum = 23,
                    ),
                    Tile.get("1s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("1234679s1z").toSet(),
                        goodShapeAdvanceNum = 20
                    ),
                    Tile.get("3s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("2369s1z").toSet(),
                        goodShapeAdvanceNum = 13
                    ),
                    Tile.get("4s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("1456789s1z").toSet(),
                        goodShapeAdvanceNum = 20
                    ),
                    Tile.get("6s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("1234569s1z").toSet(),
                        goodShapeAdvanceNum = 20
                    ),
                    Tile.get("7s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("1478s1z").toSet(),
                        goodShapeAdvanceNum = 13
                    ),
                    Tile.get("9s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("123456789s1z").toSet(),
                        advanceNum = 26,
                        goodShapeAdvance = Tile.parseTiles("1346789s1z").toSet(),
                        goodShapeAdvanceNum = 20
                    ),
                )
            )
        )
    }
}