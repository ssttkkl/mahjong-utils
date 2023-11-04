package mahjongutils.hanhu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestPointByHanHu {
    @Test
    fun testGetParentPointByHanHu() {
        assertEquals(ParentPoint(2900, 1000), getParentPointByHanHu(2, 30))
        assertEquals(ParentPoint(9600, 3200), getParentPointByHanHu(4, 25))
        assertEquals(ParentPoint(12000, 4000), getParentPointByHanHu(4, 40))
        assertEquals(ParentPoint(12000, 4000), getParentPointByHanHu(5, 60))
        assertEquals(ParentPoint(48000, 16000), getParentPointByHanHu(16, 25))
        assertEquals(ParentPoint(48000, 16000), getParentPointByHanHu(16, 30))

        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(3, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getParentPointByHanHu(3, 180)
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
        assertEquals(ChildPoint(6400, 3200, 1600), getChildPointByHanHu(4, 25))
        assertEquals(ChildPoint(8000, 4000, 2000), getChildPointByHanHu(4, 40))
        assertEquals(ChildPoint(8000, 4000, 2000), getChildPointByHanHu(5, 60))
        assertEquals(ChildPoint(32000, 16000, 8000), getChildPointByHanHu(16, 25))
        assertEquals(ChildPoint(32000, 16000, 8000), getChildPointByHanHu(16, 30))

        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(3, 34)
        }
        assertFailsWith(IllegalArgumentException::class) {
            getChildPointByHanHu(3, 180)
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
}