package mahjongutils.hanhu

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class TestPointByHanHu {
    @Test
    fun testGetParentPointByHanHu() {
        assert(getParentPointByHanHu(2, 30) == ParentPoint(2900, 1000))
        assert(getParentPointByHanHu(4, 25) == ParentPoint(9600, 3200))
        assert(getParentPointByHanHu(4, 40) == ParentPoint(12000, 4000))
        assert(getParentPointByHanHu(5, 60) == ParentPoint(12000, 4000))
        assert(getParentPointByHanHu(16, 25) == ParentPoint(48000, 16000))
        assert(getParentPointByHanHu(16, 30) == ParentPoint(48000, 16000))

        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(3, 34)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(3, 180)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(6, 34)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(-1, 30)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(114, 514)
        }
    }

    @Test
    fun testGetChildPointByHanHu() {
        assert(getChildPointByHanHu(2, 30) == ChildPoint(2000, 1000, 500))
        assert(getChildPointByHanHu(4, 25) == ChildPoint(6400, 3200, 1600))
        assert(getChildPointByHanHu(4, 40) == ChildPoint(8000, 4000, 2000))
        assert(getChildPointByHanHu(5, 60) == ChildPoint(8000, 4000, 2000))
        assert(getChildPointByHanHu(16, 25) == ChildPoint(32000, 16000, 8000))
        assert(getChildPointByHanHu(16, 30) == ChildPoint(32000, 16000, 8000))

        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(3, 34)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(3, 180)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(6, 34)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(-1, 30)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(114, 514)
        }
    }
}