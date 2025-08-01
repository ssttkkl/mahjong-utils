package mahjongutils.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MeldedTest {
    @Test
    fun testMeldedCreation() {
        // 测试创建吃
        val chow = Chow(Tile["1m"]) // 1万吃
        assertEquals(MeldedType.Chow, chow.type)
        assertEquals(Tile["1m"], chow.tile)

        // 测试创建碰
        val pung = Pung(Tile["2p"]) // 2筒碰
        assertEquals(MeldedType.Pung, pung.type)
        assertEquals(Tile["2p"], pung.tile)

        // 测试创建明杠
        val kong = MeldedKong(Tile["3s"]) // 3索明杠
        assertEquals(MeldedType.MeldedKong, kong.type)
        assertEquals(Tile["3s"], kong.tile)

        // 测试创建暗杠
        val concealedKong = ConcealedKong(Tile["5z"]) // 白暗杠
        assertEquals(MeldedType.ConcealedKong, concealedKong.type)
        assertEquals(Tile["5z"], concealedKong.tile)
    }

    @Test
    fun testMeldedTiles() {
        // 测试吃包含的牌
        val chow = Chow(Tile["1m"]) // 1万吃
        val chowTiles = chow.tiles
        assertEquals(3, chowTiles.size)
        assertEquals(Tile["1m"], chowTiles[0]) // 1万
        assertEquals(Tile["2m"], chowTiles[1]) // 2万
        assertEquals(Tile["3m"], chowTiles[2]) // 3万

        // 测试碰包含的牌
        val pung = Pung(Tile["2p"]) // 2筒碰
        val pungTiles = pung.tiles
        assertEquals(3, pungTiles.size)
        assertEquals(Tile["2p"], pungTiles[0]) // 2筒
        assertEquals(Tile["2p"], pungTiles[1]) // 2筒
        assertEquals(Tile["2p"], pungTiles[2]) // 2筒

        // 测试明杠包含的牌
        val kong = MeldedKong(Tile["3s"]) // 3索明杠
        val kongTiles = kong.tiles
        assertEquals(4, kongTiles.size)
        assertEquals(Tile["3s"], kongTiles[0]) // 3索
        assertEquals(Tile["3s"], kongTiles[1]) // 3索
        assertEquals(Tile["3s"], kongTiles[2]) // 3索
        assertEquals(Tile["3s"], kongTiles[3]) // 3索

        // 测试暗杠包含的牌
        val concealedKong = ConcealedKong(Tile["5z"]) // 白暗杠
        val concealedKongTiles = concealedKong.tiles
        assertEquals(4, concealedKongTiles.size)
        assertEquals(Tile["5z"], concealedKongTiles[0]) // 白
        assertEquals(Tile["5z"], concealedKongTiles[1]) // 白
        assertEquals(Tile["5z"], concealedKongTiles[2]) // 白
        assertEquals(Tile["5z"], concealedKongTiles[3]) // 白
    }

    @Test
    fun testMeldedParse() {
        // 测试从牌列表解析副露
        val chowTiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"])
        val chow = Melded(chowTiles)
        assertEquals(MeldedType.Chow, chow.type)
        assertEquals(Tile["1m"], chow.tile)

        val pungTiles = listOf(Tile["2p"], Tile["2p"], Tile["2p"])
        val pung = Melded(pungTiles)
        assertEquals(MeldedType.Pung, pung.type)
        assertEquals(Tile["2p"], pung.tile)

        val kongTiles = listOf(Tile["3s"], Tile["3s"], Tile["3s"], Tile["3s"])
        val kong = Melded(kongTiles)
        assertEquals(MeldedType.MeldedKong, kong.type)
        assertEquals(Tile["3s"], kong.tile)

        val concealedKongTiles = listOf(Tile["5z"], Tile["5z"], Tile["5z"], Tile["5z"])
        val concealedKong = Melded(concealedKongTiles, true)
        assertEquals(MeldedType.ConcealedKong, concealedKong.type)
        assertEquals(Tile["5z"], concealedKong.tile)

        // 测试从文本解析副露
        val chowFromText = Melded("123m")
        assertEquals(MeldedType.Chow, chowFromText.type)
        assertEquals(Tile["1m"], chowFromText.tile)

        val pungFromText = Melded("222p")
        assertEquals(MeldedType.Pung, pungFromText.type)
        assertEquals(Tile["2p"], pungFromText.tile)

        val kongFromText = Melded("3333s")
        assertEquals(MeldedType.MeldedKong, kongFromText.type)
        assertEquals(Tile["3s"], kongFromText.tile)

        val concealedKongFromText = Melded("0550z")
        assertEquals(MeldedType.ConcealedKong, concealedKongFromText.type)
        assertEquals(Tile["5z"], concealedKongFromText.tile)

        // 测试无效输入
        assertFailsWith<IllegalArgumentException> { Melded("123") }
        assertFailsWith<IllegalArgumentException> { Melded("123z") } // 字牌不能组成顺子
        assertFailsWith<IllegalArgumentException> { Melded("124m") } // 不连续的牌不能组成顺子
    }

    @Test
    fun testMeldedToString() {
        val chow = Chow(Tile["1m"]) // 1万吃
        assertEquals("123m", chow.toString())

        val pung = Pung(Tile["2p"]) // 2筒碰
        assertEquals("222p", pung.toString())

        val kong = MeldedKong(Tile["3s"]) // 3索明杠
        assertEquals("3333s", kong.toString())

        val concealedKong = ConcealedKong(Tile["5z"]) // 白暗杠
        assertEquals("0550z", concealedKong.toString())
    }

    @Test
    fun testAsGroup() {
        // 测试副露转换为面子
        val chow = Chow(Tile["1m"]) // 1万吃
        val chowGroup = chow.asGroup()
        assertEquals(GroupType.Seq, chowGroup.type)
        assertEquals(Tile["1m"], chowGroup.tile)

        val pung = Pung(Tile["2p"]) // 2筒碰
        val pungGroup = pung.asGroup()
        assertEquals(GroupType.Tri, pungGroup.type)
        assertEquals(Tile["2p"], pungGroup.tile)

        val kong = MeldedKong(Tile["3s"]) // 3索明杠
        val kongGroup = kong.asGroup()
        assertEquals(GroupType.Tri, kongGroup.type)
        assertEquals(Tile["3s"], kongGroup.tile)

        val concealedKong = ConcealedKong(Tile["5z"]) // 白暗杠
        val concealedKongGroup = concealedKong.asGroup()
        assertEquals(GroupType.Tri, concealedKongGroup.type)
        assertEquals(Tile["5z"], concealedKongGroup.tile)
    }

    @Test
    fun testToStringAndParse() {
        // 测试toString得到的字符串拿去parse得到相同的结果
        val testMeldeds = listOf(
            // 吃
            Chow(Tile["1m"]), Chow(Tile["2m"]), Chow(Tile["3m"]), Chow(Tile["4m"]),
            Chow(Tile["5m"]), Chow(Tile["6m"]), Chow(Tile["7m"]),
            Chow(Tile["1p"]), Chow(Tile["2p"]), Chow(Tile["3p"]), Chow(Tile["4p"]),
            Chow(Tile["5p"]), Chow(Tile["6p"]), Chow(Tile["7p"]),
            Chow(Tile["1s"]), Chow(Tile["2s"]), Chow(Tile["3s"]), Chow(Tile["4s"]),
            Chow(Tile["5s"]), Chow(Tile["6s"]), Chow(Tile["7s"]),

            // 碰
            Pung(Tile["1m"]), Pung(Tile["2m"]), Pung(Tile["3m"]), Pung(Tile["4m"]), Pung(Tile["5m"]),
            Pung(Tile["6m"]), Pung(Tile["7m"]), Pung(Tile["8m"]), Pung(Tile["9m"]),
            Pung(Tile["1p"]), Pung(Tile["2p"]), Pung(Tile["3p"]), Pung(Tile["4p"]), Pung(Tile["5p"]),
            Pung(Tile["6p"]), Pung(Tile["7p"]), Pung(Tile["8p"]), Pung(Tile["9p"]),
            Pung(Tile["1s"]), Pung(Tile["2s"]), Pung(Tile["3s"]), Pung(Tile["4s"]), Pung(Tile["5s"]),
            Pung(Tile["6s"]), Pung(Tile["7s"]), Pung(Tile["8s"]), Pung(Tile["9s"]),
            Pung(Tile["1z"]), Pung(Tile["2z"]), Pung(Tile["3z"]), Pung(Tile["4z"]),
            Pung(Tile["5z"]), Pung(Tile["6z"]), Pung(Tile["7z"]),

            // 明杠
            MeldedKong(Tile["1m"]), MeldedKong(Tile["2m"]), MeldedKong(Tile["3m"]), MeldedKong(Tile["4m"]), MeldedKong(Tile["5m"]),
            MeldedKong(Tile["6m"]), MeldedKong(Tile["7m"]), MeldedKong(Tile["8m"]), MeldedKong(Tile["9m"]),
            MeldedKong(Tile["1p"]), MeldedKong(Tile["2p"]), MeldedKong(Tile["3p"]), MeldedKong(Tile["4p"]), MeldedKong(Tile["5p"]),
            MeldedKong(Tile["6p"]), MeldedKong(Tile["7p"]), MeldedKong(Tile["8p"]), MeldedKong(Tile["9p"]),
            MeldedKong(Tile["1s"]), MeldedKong(Tile["2s"]), MeldedKong(Tile["3s"]), MeldedKong(Tile["4s"]), MeldedKong(Tile["5s"]),
            MeldedKong(Tile["6s"]), MeldedKong(Tile["7s"]), MeldedKong(Tile["8s"]), MeldedKong(Tile["9s"]),
            MeldedKong(Tile["1z"]), MeldedKong(Tile["2z"]), MeldedKong(Tile["3z"]), MeldedKong(Tile["4z"]),
            MeldedKong(Tile["5z"]), MeldedKong(Tile["6z"]), MeldedKong(Tile["7z"]),

            // 暗杠
            ConcealedKong(Tile["1m"]), ConcealedKong(Tile["2m"]), ConcealedKong(Tile["3m"]), ConcealedKong(Tile["4m"]), ConcealedKong(Tile["5m"]),
            ConcealedKong(Tile["6m"]), ConcealedKong(Tile["7m"]), ConcealedKong(Tile["8m"]), ConcealedKong(Tile["9m"]),
            ConcealedKong(Tile["1p"]), ConcealedKong(Tile["2p"]), ConcealedKong(Tile["3p"]), ConcealedKong(Tile["4p"]), ConcealedKong(Tile["5p"]),
            ConcealedKong(Tile["6p"]), ConcealedKong(Tile["7p"]), ConcealedKong(Tile["8p"]), ConcealedKong(Tile["9p"]),
            ConcealedKong(Tile["1s"]), ConcealedKong(Tile["2s"]), ConcealedKong(Tile["3s"]), ConcealedKong(Tile["4s"]), ConcealedKong(Tile["5s"]),
            ConcealedKong(Tile["6s"]), ConcealedKong(Tile["7s"]), ConcealedKong(Tile["8s"]), ConcealedKong(Tile["9s"]),
            ConcealedKong(Tile["1z"]), ConcealedKong(Tile["2z"]), ConcealedKong(Tile["3z"]), ConcealedKong(Tile["4z"]),
            ConcealedKong(Tile["5z"]), ConcealedKong(Tile["6z"]), ConcealedKong(Tile["7z"])
        )

        for (melded in testMeldeds) {
            val meldedString = melded.toString()
            val parsedMelded = Melded.parse(meldedString)
            assertEquals(melded.type, parsedMelded.type, "toString和parse往返测试失败: $melded -> $meldedString -> $parsedMelded")
            assertEquals(melded.tile, parsedMelded.tile, "toString和parse往返测试失败: $melded -> $meldedString -> $parsedMelded")
        }
    }
}

