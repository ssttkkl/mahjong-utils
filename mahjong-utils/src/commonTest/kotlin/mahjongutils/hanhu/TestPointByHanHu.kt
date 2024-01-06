package mahjongutils.hanhu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestPointByHanHu {
    @Test
    fun testGetParentPointByHanHu() {
        assertEquals(ParentPoint(2900, 1000), getParentPointByHanHu(2, 30))
        assertEquals(ParentPoint.Mangan, getParentPointByHanHu(3, 70))
        assertEquals(ParentPoint(9600, 3200), getParentPointByHanHu(4, 25))
        assertEquals(ParentPoint.Mangan, getParentPointByHanHu(4, 40))
        assertEquals(ParentPoint.Mangan, getParentPointByHanHu(5, 60))
        assertEquals(ParentPoint.Haneman, getParentPointByHanHu(6, 30))
        assertEquals(ParentPoint.Haneman, getParentPointByHanHu(7, 30))
        assertEquals(ParentPoint.Baiman, getParentPointByHanHu(8, 30))
        assertEquals(ParentPoint.Baiman, getParentPointByHanHu(9, 30))
        assertEquals(ParentPoint.Baiman, getParentPointByHanHu(10, 30))
        assertEquals(ParentPoint.Sanbaiman, getParentPointByHanHu(11, 30))
        assertEquals(ParentPoint.Sanbaiman, getParentPointByHanHu(12, 30))
        assertEquals(ParentPoint.Yakuman, getParentPointByHanHu(13, 30))
        assertEquals(ParentPoint.Yakuman, getParentPointByHanHu(16, 25))
        assertEquals(ParentPoint.Yakuman, getParentPointByHanHu(16, 30))

        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(3, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(6, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(-1, 30)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(114, 514)
        }
    }

    @Test
    fun testGetChildPointByHanHu() {
        assertEquals(ChildPoint(2000, 1000, 500), getChildPointByHanHu(2, 30))
        assertEquals(ChildPoint.Mangan, getChildPointByHanHu(3, 70))
        assertEquals(ChildPoint(6400, 3200, 1600), getChildPointByHanHu(4, 25))
        assertEquals(ChildPoint.Mangan, getChildPointByHanHu(4, 40))
        assertEquals(ChildPoint.Mangan, getChildPointByHanHu(5, 60))
        assertEquals(ChildPoint.Haneman, getChildPointByHanHu(6, 30))
        assertEquals(ChildPoint.Haneman, getChildPointByHanHu(7, 30))
        assertEquals(ChildPoint.Baiman, getChildPointByHanHu(8, 30))
        assertEquals(ChildPoint.Baiman, getChildPointByHanHu(9, 30))
        assertEquals(ChildPoint.Baiman, getChildPointByHanHu(10, 30))
        assertEquals(ChildPoint.Sanbaiman, getChildPointByHanHu(11, 30))
        assertEquals(ChildPoint.Sanbaiman, getChildPointByHanHu(12, 30))
        assertEquals(ChildPoint.Yakuman, getChildPointByHanHu(13, 30))
        assertEquals(ChildPoint.Yakuman, getChildPointByHanHu(16, 25))
        assertEquals(ChildPoint.Yakuman, getChildPointByHanHu(16, 30))

        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(3, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(6, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(-1, 30)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(114, 514)
        }
    }

    @Test
    fun testNotHaveKazoeYakuman() {
        assertEquals(
            ChildPoint.Sanbaiman,
            getChildPointByHanHu(18, 30, HanHuOptions.Default.copy(hasKazoeYakuman = false))
        )

        assertEquals(
            ParentPoint.Sanbaiman,
            getParentPointByHanHu(18, 30, HanHuOptions.Default.copy(hasKazoeYakuman = false))
        )
    }

    @Test
    fun testHasKiriageMangan() {
        assertEquals(
            ChildPoint.Mangan,
            getChildPointByHanHu(4, 30, HanHuOptions.Default.copy(hasKiriageMangan = true))
        )

        assertEquals(
            ParentPoint.Mangan,
            getParentPointByHanHu(4, 30, HanHuOptions.Default.copy(hasKiriageMangan = true))
        )
    }

    @Test
    fun testAotenjou() {
        assertEquals(
            ChildPoint(15400, 7700, 3900),
            getChildPointByHanHu(5, 30, HanHuOptions.Default.copy(aotenjou = true))
        )

        assertEquals(
            ParentPoint(23100, 7700),
            getParentPointByHanHu(5, 30, HanHuOptions.Default.copy(aotenjou = true))
        )
    }
}