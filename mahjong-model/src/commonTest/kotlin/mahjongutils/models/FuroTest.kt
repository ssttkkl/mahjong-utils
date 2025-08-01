package mahjongutils.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FuroTest {

    @Test
    fun testFuroCreation() {
        // 测试吃创建
        val chi = Chi(Tile.get(TileType.M, 1))
        assertEquals(FuroType.Chi, chi.type)
        assertEquals(Tile.get(TileType.M, 1), chi.tile)

        // 测试碰创建
        val pon = Pon(Tile.get(TileType.P, 5))
        assertEquals(FuroType.Pon, pon.type)
        assertEquals(Tile.get(TileType.P, 5), pon.tile)

        // 测试明杠创建
        val kan = Kan(Tile.get(TileType.S, 9))
        assertEquals(FuroType.Kan, kan.type)
        assertEquals(Tile.get(TileType.S, 9), kan.tile)

        // 测试暗杠创建
        val ankan = Ankan(Tile.get(TileType.Z, 1))
        assertEquals(FuroType.Ankan, ankan.type)
        assertEquals(Tile.get(TileType.Z, 1), ankan.tile)

        // 测试通过牌列表创建
        val furo1 = Furo(listOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 2),
            Tile.get(TileType.M, 3)
        ))
        assertEquals(FuroType.Chi, furo1.type)
        assertEquals(Tile.get(TileType.M, 1), furo1.tile)

        val furo2 = Furo(listOf(
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 5),
            Tile.get(TileType.P, 5)
        ))
        assertEquals(FuroType.Pon, furo2.type)
        assertEquals(Tile.get(TileType.P, 5), furo2.tile)

        val furo3 = Furo(listOf(
            Tile.get(TileType.S, 9),
            Tile.get(TileType.S, 9),
            Tile.get(TileType.S, 9),
            Tile.get(TileType.S, 9)
        ))
        assertEquals(FuroType.Kan, furo3.type)
        assertEquals(Tile.get(TileType.S, 9), furo3.tile)

        val furo4 = Furo(listOf(
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 1)
        ), true)
        assertEquals(FuroType.Ankan, furo4.type)
        assertEquals(Tile.get(TileType.Z, 1), furo4.tile)

        // 测试通过文本创建
        val furo5 = Furo("123m")
        assertEquals(FuroType.Chi, furo5.type)
        assertEquals(Tile.get(TileType.M, 1), furo5.tile)

        val furo6 = Furo("555p")
        assertEquals(FuroType.Pon, furo6.type)
        assertEquals(Tile.get(TileType.P, 5), furo6.tile)

        val furo7 = Furo("9999s")
        assertEquals(FuroType.Kan, furo7.type)
        assertEquals(Tile.get(TileType.S, 9), furo7.tile)

        val furo8 = Furo("0110z", true)
        assertEquals(FuroType.Ankan, furo8.type)
        assertEquals(Tile.get(TileType.Z, 1), furo8.tile)
    }

    @Test
    fun testFuroTiles() {
        // 测试吃的牌
        val chi = Chi(Tile.get(TileType.M, 1))
        val chiTiles = chi.tiles
        assertEquals(3, chiTiles.size)
        assertEquals(Tile.get(TileType.M, 1), chiTiles[0])
        assertEquals(Tile.get(TileType.M, 2), chiTiles[1])
        assertEquals(Tile.get(TileType.M, 3), chiTiles[2])

        // 测试碰的牌
        val pon = Pon(Tile.get(TileType.P, 5))
        val ponTiles = pon.tiles
        assertEquals(3, ponTiles.size)
        assertEquals(Tile.get(TileType.P, 5), ponTiles[0])
        assertEquals(Tile.get(TileType.P, 5), ponTiles[1])
        assertEquals(Tile.get(TileType.P, 5), ponTiles[2])

        // 测试明杠的牌
        val kan = Kan(Tile.get(TileType.S, 9))
        val kanTiles = kan.tiles
        assertEquals(4, kanTiles.size)
        assertEquals(Tile.get(TileType.S, 9), kanTiles[0])
        assertEquals(Tile.get(TileType.S, 9), kanTiles[1])
        assertEquals(Tile.get(TileType.S, 9), kanTiles[2])
        assertEquals(Tile.get(TileType.S, 9), kanTiles[3])

        // 测试暗杠的牌
        val ankan = Ankan(Tile.get(TileType.Z, 1))
        val ankanTiles = ankan.tiles
        assertEquals(4, ankanTiles.size)
        assertEquals(Tile.get(TileType.Z, 1), ankanTiles[0])
        assertEquals(Tile.get(TileType.Z, 1), ankanTiles[1])
        assertEquals(Tile.get(TileType.Z, 1), ankanTiles[2])
        assertEquals(Tile.get(TileType.Z, 1), ankanTiles[3])

        // 测试红宝牌的吃
        val redDoraChi = Chi(Tile.get(TileType.M, 0))
        val redDoraChiTiles = redDoraChi.tiles
        assertEquals(3, redDoraChiTiles.size)
        assertEquals(Tile.get(TileType.M, 0), redDoraChiTiles[0])
        assertEquals(Tile.get(TileType.M, 6), redDoraChiTiles[1])
        assertEquals(Tile.get(TileType.M, 7), redDoraChiTiles[2])
    }

    @Test
    fun testAsMentsu() {
        // 测试吃转换为面子
        val chi = Chi(Tile.get(TileType.M, 1))
        val chiMentsu = chi.asMentsu()
        assertEquals(MentsuType.Shuntsu, chiMentsu.type)
        assertEquals(Tile.get(TileType.M, 1), chiMentsu.tile)

        // 测试碰转换为面子
        val pon = Pon(Tile.get(TileType.P, 5))
        val ponMentsu = pon.asMentsu()
        assertEquals(MentsuType.Kotsu, ponMentsu.type)
        assertEquals(Tile.get(TileType.P, 5), ponMentsu.tile)

        // 测试明杠转换为面子
        val kan = Kan(Tile.get(TileType.S, 9))
        val kanMentsu = kan.asMentsu()
        assertEquals(MentsuType.Kotsu, kanMentsu.type)
        assertEquals(Tile.get(TileType.S, 9), kanMentsu.tile)

        // 测试暗杠转换为面子
        val ankan = Ankan(Tile.get(TileType.Z, 1))
        val ankanMentsu = ankan.asMentsu()
        assertEquals(MentsuType.Kotsu, ankanMentsu.type)
        assertEquals(Tile.get(TileType.Z, 1), ankanMentsu.tile)
    }

    @Test
    fun testToString() {
        // 测试吃的字符串表示
        val chi = Chi(Tile.get(TileType.M, 1))
        assertEquals("123m", chi.toString())

        // 测试碰的字符串表示
        val pon = Pon(Tile.get(TileType.P, 5))
        assertEquals("555p", pon.toString())

        // 测试明杠的字符串表示
        val kan = Kan(Tile.get(TileType.S, 9))
        assertEquals("9999s", kan.toString())

        // 测试暗杠的字符串表示
        val ankan = Ankan(Tile.get(TileType.Z, 1))
        assertEquals("0110z", ankan.toString())

        // 测试红宝牌的吃
        val redDoraChi = Chi(Tile.get(TileType.M, 0))
        assertEquals("067m", redDoraChi.toString())
    }

    @Test
    fun testInvalidFuroCreation() {
        // 测试无效的吃（字牌不能组成吃）
        assertFailsWith(IllegalArgumentException::class) {
            Furo(listOf(
                Tile.get(TileType.Z, 1),
                Tile.get(TileType.Z, 2),
                Tile.get(TileType.Z, 3)
            ))
        }

        // 测试无效的吃（不连续的牌）
        assertFailsWith(IllegalArgumentException::class) {
            Furo(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.M, 2),
                Tile.get(TileType.M, 4)
            ))
        }

        // 测试无效的副露（牌数不对）
        assertFailsWith(IllegalArgumentException::class) {
            Furo(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.M, 2)
            ))
        }

        // 测试无效的副露（混合类型）
        assertFailsWith(IllegalArgumentException::class) {
            Furo(listOf(
                Tile.get(TileType.M, 1),
                Tile.get(TileType.P, 2),
                Tile.get(TileType.S, 3)
            ))
        }
    }
}

