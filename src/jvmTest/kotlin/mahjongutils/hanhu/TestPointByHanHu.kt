package mahjongutils.hanhu

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class TestPointByHanHu {
    @Test
    fun testGetParentPointByHanHu() {
        assert(getParentPointByHanHu(2, 25) == ParentPoint(2400, 0))
        assert(getParentPointByHanHu(2, 30) == ParentPoint(2900, 1000))
        assert(getParentPointByHanHu(3, 20) == ParentPoint(0, 1300))
        assert(getParentPointByHanHu(4, 40) == ParentPoint(12000, 4000))
        assert(getParentPointByHanHu(5, 60) == ParentPoint(12000, 4000))
        assert(getParentPointByHanHu(16, 25) == ParentPoint(48000, 16000))
        assert(getParentPointByHanHu(16, 30) == ParentPoint(48000, 16000))

        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(1, 20)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(1, 25)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(3, 34)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(3, 120)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(6, 34)
        }
        assertThrows<IllegalArgumentException> {
            getParentPointByHanHu(6, 120)
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
        assert(getChildPointByHanHu(2, 25) == ChildPoint(1600, 0, 0))
        assert(getChildPointByHanHu(2, 30) == ChildPoint(2000, 1000, 500))
        assert(getChildPointByHanHu(3, 20) == ChildPoint(0, 1300, 700))
        assert(getChildPointByHanHu(4, 40) == ChildPoint(8000, 4000, 2000))
        assert(getChildPointByHanHu(5, 60) == ChildPoint(8000, 4000, 2000))
        assert(getChildPointByHanHu(16, 25) == ChildPoint(32000, 16000, 8000))
        assert(getChildPointByHanHu(16, 30) == ChildPoint(32000, 16000, 8000))

        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(1, 20)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(1, 25)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(3, 34)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(3, 120)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(6, 34)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(6, 120)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(-1, 30)
        }
        assertThrows<IllegalArgumentException> {
            getChildPointByHanHu(114, 514)
        }
    }
}