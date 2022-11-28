package mahjongutils.yaku

import mahjongutils.models.hand.HoraHandPattern
import kotlin.test.assertEquals


internal fun tester(pattern: HoraHandPattern, expected: Set<Yaku>) {
    val actual = Yakus.allYaku.filter {
        it.check(pattern)
    }.toSet()

    // 如果有役满就只比较役满
    val expectedYakuman = expected.filter { it.isYakuman }.toSet()
    val actualYakuman = actual.filter { it.isYakuman }.toSet()
    assertEquals(expectedYakuman, actualYakuman)

    if (actualYakuman.isEmpty()) {
        assertEquals(expected, actual)
    }
}