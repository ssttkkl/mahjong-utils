package mahjongutils.shanten

import mahjongutils.models.Tile
import kotlin.test.Test
import kotlin.test.assertEquals

class TestShanten {
    private fun tester(
        tiles: String, expected: Shanten,
        calcAdvanceNum: Boolean = true,
        bestShantenOnly: Boolean = false,
    ) {
        val result = shanten(Tile.parseTiles(tiles), emptyList(), calcAdvanceNum, bestShantenOnly)
        assertEquals(expected, result.shantenInfo)
    }

    private fun regularTester(
        tiles: String, expected: Shanten,
        calcAdvanceNum: Boolean = true,
        bestShantenOnly: Boolean = false,
    ) {
        val result = regularShanten(Tile.parseTiles(tiles), emptyList(), calcAdvanceNum, bestShantenOnly)
        assertEquals(expected, result.shantenInfo)
    }

    private fun chitoiTester(
        tiles: String, expected: Shanten,
        calcAdvanceNum: Boolean = true,
        bestShantenOnly: Boolean = false,
    ) {
        val result = chitoiShanten(Tile.parseTiles(tiles), calcAdvanceNum, bestShantenOnly)
        assertEquals(expected, result.shantenInfo)
    }

    private fun kokushiTester(
        tiles: String, expected: Shanten,
        calcAdvanceNum: Boolean = true,
        bestShantenOnly: Boolean = false,
    ) {
        val result = kokushiShanten(Tile.parseTiles(tiles), calcAdvanceNum, bestShantenOnly)
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
            "3344z6699p11345s8m", ShantenWithGot(
                shantenNum = 1, discardToAdvance = mapOf(
                    Tile.get("8m") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("345s").toSet(),
                        advanceNum = 9,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("3s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("8m45s").toSet(),
                        advanceNum = 9,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("4s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("8m35s").toSet(),
                        advanceNum = 9,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("5s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("8m34s").toSet(),
                        advanceNum = 9,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("3z") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m69p1345s34z").toSet(),
                        advanceNum = 22,
                    ),
                    Tile.get("4z") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m69p1345s34z").toSet(),
                        advanceNum = 22,
                    ),
                    Tile.get("6p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m69p1345s34z").toSet(),
                        advanceNum = 22,
                    ),
                    Tile.get("9p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m69p1345s34z").toSet(),
                        advanceNum = 22,
                    ),
                    Tile.get("1s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("8m69p1345s34z").toSet(),
                        advanceNum = 22,
                    ),
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
        tester(
            "11123456789999p", ShantenWithGot(
                shantenNum = -1,
                discardToAdvance = emptyMap()
            ),
            bestShantenOnly = true
        )
        tester(
            "11223344556677z", ShantenWithGot(
                shantenNum = -1,
                discardToAdvance = emptyMap()
            ),
            bestShantenOnly = true
        )
        tester(
            "112233p44556s127z", ShantenWithGot(
                shantenNum = 1,
                discardToAdvance = mapOf(
                    Tile.get("1z") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("36s27z").toSet(),
                        advanceNum = 13,
                        goodShapeAdvance = Tile.parseTiles("27z").toSet(),
                        goodShapeAdvanceNum = 6
                    ),
                    Tile.get("2z") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("36s17z").toSet(),
                        advanceNum = 13,
                        goodShapeAdvance = Tile.parseTiles("17z").toSet(),
                        goodShapeAdvanceNum = 6
                    ),
                    Tile.get("7z") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("36s12z").toSet(),
                        advanceNum = 13,
                        goodShapeAdvance = Tile.parseTiles("12z").toSet(),
                        goodShapeAdvanceNum = 6
                    ),
                    Tile.get("6s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("127z").toSet(),
                        advanceNum = 9,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("1p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("14p36s127z").toSet(),
                        advanceNum = 22,
                    ),
                    Tile.get("2p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("2p36s127z").toSet(),
                        advanceNum = 18,
                    ),
                    Tile.get("3p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3p36s127z").toSet(),
                        advanceNum = 18,
                    ),
                    Tile.get("4s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("34567s127z").toSet(),
                        advanceNum = 24,
                    ),
                    Tile.get("5s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("234567s127z").toSet(),
                        advanceNum = 28,
                    ),
                )
            )
        )
        tester(
            "114514p1919810s8p", ShantenWithGot(
                shantenNum = 2,
                discardToAdvance = mapOf(
                    Tile.get("8s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("2p3p4p5p6p7p8p9p3s4s5s6s7s9s").toSet(),
                        advanceNum = 49,
                    ),
                    Tile.get("8p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("2p3p4p5p6p7p3s4s5s6s7s8s9s").toSet(),
                        advanceNum = 45,
                    ),
                    Tile.get("4p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3p6p7p8p9p3s4s5s6s7s8s9s").toSet(),
                        advanceNum = 43,
                    ),
                    Tile.get("9s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("3p4p5p6p7p8p9p3s4s5s6s7s").toSet(),
                        advanceNum = 43,
                    ),
                    Tile.get("5p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("4p6p7p8p9p3s4s5s6s7s8s9s").toSet(),
                        advanceNum = 41,
                    ),
                    Tile.get("5s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("2p3p4p5p6p7p8p9p6s7s8s9s").toSet(),
                        advanceNum = 41,
                    ),
                    Tile.get("1p") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("5p8p5s8s").toSet(),
                        advanceNum = 12,
                    ),
                    Tile.get("1s") to ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("5p8p5s8s").toSet(),
                        advanceNum = 12,
                    ),
                )
            )
        )
        tester(
            "111p456p123s678p23s", ShantenWithGot(
                shantenNum = 0,
                discardToAdvance = mapOf(
                    Tile.get("1p") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("14s").toSet(),
                        advanceNum = 7,
                    ),
                    Tile.get("1s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("23s").toSet(),
                        advanceNum = 4,
                    ),
                    Tile.get("2s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("3s").toSet(),
                        advanceNum = 2,
                    ),
                    Tile.get("3s") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("2s").toSet(),
                        advanceNum = 2,
                    ),
                    Tile.get("6p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3456789p1234s").toSet(),
                        advanceNum = 33,
                        goodShapeAdvance = Tile.parseTiles("3456789p1234s").toSet(),
                        goodShapeAdvanceNum = 33,
                    ),
                    Tile.get("4p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("4p5p6p7p8p9p1s2s3s4s").toSet(),
                        advanceNum = 29,
                        goodShapeAdvance = Tile.parseTiles("4p5p6p7p8p9p1s2s3s4s").toSet(),
                        goodShapeAdvanceNum = 29,
                    ),
                    Tile.get("8p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3p4p5p6p7p8p1s2s3s4s").toSet(),
                        advanceNum = 29,
                        goodShapeAdvance = Tile.parseTiles("3p4p5p6p7p8p1s2s3s4s").toSet(),
                        goodShapeAdvanceNum = 29,
                    ),
                    Tile.get("5p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("4p5p6p9p1s2s3s4s").toSet(),
                        advanceNum = 23,
                        goodShapeAdvance = Tile.parseTiles("4p5p6p9p1s4s").toSet(),
                        goodShapeAdvanceNum = 19,
                    ),
                    Tile.get("7p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3p6p7p8p1s2s3s4s").toSet(),
                        advanceNum = 23,
                        goodShapeAdvance = Tile.parseTiles("3p6p7p8p1s4s").toSet(),
                        goodShapeAdvanceNum = 19,
                    ),
                )
            )
        )
        tester(
            "4456m3334556p345s", ShantenWithGot(
                shantenNum = 0,
                discardToAdvance = mapOf(
                    Tile.get("4m") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("457p").toSet(),
                        advanceNum = 9,
                    ),
                    Tile.get("5p") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("47m").toSet(),
                        advanceNum = 6,
                    ),
                )
            ),
            bestShantenOnly = true
        )
        tester(
            "35m11223399p7799s", ShantenWithGot(
                shantenNum = 0,
                discardToAdvance = mapOf(
                    Tile.get("3m") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("5m").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("5m") to ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("3m").toSet(),
                        advanceNum = 3,
                    ),
                    Tile.get("7s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m4m5m9p7s8s9s").toSet(),
                        advanceNum = 20,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("9s") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m4m5m9p7s8s9s").toSet(),
                        advanceNum = 20,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("9p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m4m5m9p7s9s").toSet(),
                        advanceNum = 16,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("1p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m5m1p").toSet(),
                        advanceNum = 8,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("2p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m5m2p").toSet(),
                        advanceNum = 8,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                    Tile.get("3p") to ShantenWithoutGot(
                        shantenNum = 1,
                        advance = Tile.parseTiles("3m5m3p").toSet(),
                        advanceNum = 8,
                        goodShapeAdvance = emptySet(),
                        goodShapeAdvanceNum = 0
                    ),
                )
            )
        )
    }

    @Test
    fun testKokushiWithGot() {
        kokushiTester(
            "119m19p19266s135z3s",
            ShantenWithGot(
                shantenNum = 3,
                discardToAdvance = Tile.parseTiles("2s6s3s").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 3,
                        advance = Tile.parseTiles("2467z").toSet(),
                        advanceNum = 16,
                    )
                } + Tile.parseTiles("1m").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 4,
                        advance = Tile.allYaochu,
                        advanceNum = 42,
                    )
                } + Tile.parseTiles("9m19p19s135z").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 4,
                        advance = Tile.parseTiles("2467z").toSet() + it,
                        advanceNum = 19,
                    )
                }
            ),
        )
        kokushiTester(
            "19m19p19266s1235z3s",
            ShantenWithGot(
                shantenNum = 3,
                discardToAdvance = Tile.parseTiles("2s6s3s").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 3,
                        advance = Tile.allYaochu,
                        advanceNum = 42,
                    )
                }
            ),
            bestShantenOnly = true
        )
        kokushiTester(
            "1119m19p19s12355z3s",
            ShantenWithGot(
                shantenNum = 2,
                discardToAdvance = Tile.parseTiles("1m5z3s").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 2,
                        advance = Tile.parseTiles("467z").toSet(),
                        advanceNum = 12,
                    )
                }
            ),
            bestShantenOnly = true
        )
        kokushiTester(
            "19m19p19s12345566z",
            ShantenWithGot(
                shantenNum = 0,
                discardToAdvance = Tile.parseTiles("56z").associateWith {
                    ShantenWithoutGot(
                        shantenNum = 0,
                        advance = Tile.parseTiles("7z").toSet(),
                        advanceNum = 4,
                    )
                }
            ),
            bestShantenOnly = true
        )
        kokushiTester(
            "19m19p19s12345667z",
            ShantenWithGot(
                shantenNum = -1,
                discardToAdvance = emptyMap()
            ),
            bestShantenOnly = true
        )
    }
}