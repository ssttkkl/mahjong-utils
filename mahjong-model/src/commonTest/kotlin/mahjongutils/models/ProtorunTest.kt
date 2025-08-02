package mahjongutils.models

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ProtorunTest {
    @Test
    fun testProtorunCreation() {
        // 测试创建两面搭子
        for (tile in Tile.all.filter { i -> i.num in 2..7 && i.type != TileType.Honour }) {
            val twoSide = TwoSide(tile)
            assertEquals(ProtorunType.TwoSide, twoSide.type)
            assertEquals(tile, twoSide.first)
            assertEquals(tile.advance(1), twoSide.second)
        }

        // 测试创建嵌张搭子
        for (tile in Tile.all.filter { i -> i.num in 1..7 && i.type != TileType.Honour }) {
            val closed = Closed(tile)
            assertEquals(ProtorunType.Closed, closed.type)
            assertEquals(tile, closed.first)
            assertEquals(tile.advance(2), closed.second)
        }

        // 测试创建边张搭子
        for (tile in Tile.all.filter { i -> i.num in 1..8 && i.type != TileType.Honour }) {
            val edge = Edge(tile)
            assertEquals(ProtorunType.Edge, edge.type)
            assertEquals(tile, edge.first)
            assertEquals(tile.advance(1), edge.second)
        }

        // 测试创建对子
        for (tile in Tile.all) {
            val pair = Pair(tile)
            assertEquals(ProtorunType.Pair, pair.type)
            assertEquals(tile, pair.first)
            assertEquals(tile, pair.second)
        }
    }

    @Test
    fun testProtorunWaiting() {
        // 测试两面搭子的进张
        val twoSide = TwoSide(Tile["2m"]) // 2万两面搭子
        val twoSideWaiting = twoSide.waiting
        assertEquals(2, twoSideWaiting.size)
        assertTrue(Tile["1m"] in twoSideWaiting) // 1万
        assertTrue(Tile["4m"] in twoSideWaiting) // 4万

        // 测试嵌张搭子的进张
        val closed = Closed(Tile["1m"]) // 1-3万嵌张搭子
        val closedWaiting = closed.waiting
        assertEquals(1, closedWaiting.size)
        assertTrue(Tile["2m"] in closedWaiting) // 2万

        // 测试边张12搭子的进张
        val edge12 = Edge(Tile["1m"]) // 1-2万边张搭子
        val edgeWaiting = edge12.waiting
        assertEquals(1, edgeWaiting.size)
        assertTrue(Tile["3m"] in edgeWaiting) // 3万

        // 测试边张89搭子的进张
        val edge89 = Edge(Tile["8m"]) // 8-9万边张搭子
        val edge89Waiting = edge89.waiting
        assertEquals(1, edge89Waiting.size)
        assertTrue(Tile["7m"] in edge89Waiting) // 7万

        // 测试对子的进张
        val pair = Pair(Tile["2p"]) // 2筒对子
        val pairWaiting = pair.waiting
        assertEquals(1, pairWaiting.size)
        assertTrue(Tile["2p"] in pairWaiting) // 2筒
    }

    @Test
    fun testProtorunParse() {
        // 测试从牌解析搭子
        val twoSide = Protorun(Tile["2m"], Tile["3m"]) // 2-3万两面搭子
        assertEquals(ProtorunType.TwoSide, twoSide.type)
        assertEquals(Tile["2m"], twoSide.first)

        val closed = Protorun(Tile["1m"], Tile["3m"]) // 1-3万嵌张搭子
        assertEquals(ProtorunType.Closed, closed.type)
        assertEquals(Tile["1m"], closed.first)

        val edge = Protorun(Tile["1m"], Tile["2m"]) // 1-2万边张搭子
        assertEquals(ProtorunType.Edge, edge.type)
        assertEquals(Tile["1m"], edge.first)

        val pair = Protorun(Tile["2p"], Tile["2p"]) // 2筒对子
        assertEquals(ProtorunType.Pair, pair.type)
        assertEquals(Tile["2p"], pair.first)

        // 测试从文本解析搭子
        val twoSideFromText = Protorun("23m")
        assertEquals(ProtorunType.TwoSide, twoSideFromText.type)
        assertEquals(Tile["2m"], twoSideFromText.first)

        val closedFromText = Protorun("13m")
        assertEquals(ProtorunType.Closed, closedFromText.type)
        assertEquals(Tile["1m"], closedFromText.first)

        val edgeFromText = Protorun("12m")
        assertEquals(ProtorunType.Edge, edgeFromText.type)
        assertEquals(Tile["1m"], edgeFromText.first)

        val pairFromText = Protorun("22p")
        assertEquals(ProtorunType.Pair, pairFromText.type)
        assertEquals(Tile["2p"], pairFromText.first)

        // 测试倒序的两张牌
        assertEquals(Protorun("32m"), Protorun("23m"))

        // 测试无效输入
        assertFailsWith<IllegalArgumentException> { Protorun("12") }
        assertFailsWith<IllegalArgumentException> { Protorun("12z") } // 字牌不能组成搭子
        assertFailsWith<IllegalArgumentException> { Protorun("14m") } // 间隔太大的牌不能组成搭子
        assertFailsWith<IllegalArgumentException> { Protorun("123m") } // 多于2张的牌不能组成搭子
    }

    @Test
    fun testProtorunToString() {
        val twoSide = TwoSide(Tile["2m"]) // 2万两面搭子
        assertEquals("23m", twoSide.toString())

        val closed = Closed(Tile["1m"]) // 1-3万嵌张搭子
        assertEquals("13m", closed.toString())

        val edge = Edge(Tile["1m"]) // 1-2万边张搭子
        assertEquals("12m", edge.toString())

        val pair = Pair(Tile["2p"]) // 2筒对子
        assertEquals("22p", pair.toString())
    }

    @Test
    fun testWithWaiting() {
        // 测试两面搭子的进张后形成的面子
        val twoSide = TwoSide(Tile["2m"]) // 2万两面搭子

        // 进1万，应该形成123万顺子
        val group1 = twoSide.withWaiting(Tile["1m"])
        assertEquals(GroupType.Seq, group1.type)
        assertEquals(Tile["1m"], group1.tile)

        // 进4万，应该形成234万顺子
        val group2 = twoSide.withWaiting(Tile["4m"])
        assertEquals(GroupType.Seq, group2.type)
        assertEquals(Tile["2m"], group2.tile)

        // 测试嵌张搭子的进张后形成的面子
        val closed = Closed(Tile["1m"]) // 1-3万嵌张搭子

        // 进2万，应该形成123万顺子
        val group3 = closed.withWaiting(Tile["2m"])
        assertEquals(GroupType.Seq, group3.type)
        assertEquals(Tile["1m"], group3.tile)

        // 测试边张12搭子的进张后形成的面子
        val edge12 = Edge(Tile["1m"]) // 1-2万边张搭子

        // 进3万，应该形成123万顺子
        val group4 = edge12.withWaiting(Tile["3m"])
        assertEquals(GroupType.Seq, group4.type)
        assertEquals(Tile["1m"], group4.tile)

        // 测试边张89搭子的进张后形成的面子
        val edge89 = Edge(Tile["8m"]) // 8-9万边张搭子

        // 进7万，应该形成89万顺子
        val group6 = edge89.withWaiting(Tile["7m"])
        assertEquals(GroupType.Seq, group6.type)
        assertEquals(Tile["7m"], group6.tile)

        // 测试对子的进张后形成的面子
        val pair = Pair(Tile["2p"]) // 2筒对子

        // 进2筒，应该形成222筒刻子
        val group5 = pair.withWaiting(Tile["2p"])
        assertEquals(GroupType.Tri, group5.type)
        assertEquals(Tile["2p"], group5.tile)

        // 测试无效进张
        assertFailsWith<IllegalArgumentException> { twoSide.withWaiting(Tile["5m"]) }
        assertFailsWith<IllegalArgumentException> { closed.withWaiting(Tile["4m"]) }
        assertFailsWith<IllegalArgumentException> { edge12.withWaiting(Tile["4m"]) }
        assertFailsWith<IllegalArgumentException> { edge89.withWaiting(Tile["6m"]) }
        assertFailsWith<IllegalArgumentException> { pair.withWaiting(Tile["3p"]) }
    }

    @Test
    fun testToStringAndParse() {
        val testProtoruns = listOf(
            // 两面搭子
            TwoSide(Tile["2m"]),
            TwoSide(Tile["3m"]),
            TwoSide(Tile["4m"]),
            TwoSide(Tile["5m"]),
            TwoSide(Tile["6m"]),
            TwoSide(Tile["7m"]),
            TwoSide(Tile["2p"]),
            TwoSide(Tile["3p"]),
            TwoSide(Tile["4p"]),
            TwoSide(Tile["5p"]),
            TwoSide(Tile["6p"]),
            TwoSide(Tile["7p"]),
            TwoSide(Tile["2s"]),
            TwoSide(Tile["3s"]),
            TwoSide(Tile["4s"]),
            TwoSide(Tile["5s"]),
            TwoSide(Tile["6s"]),
            TwoSide(Tile["7s"]),

            // 嵌张搭子
            Closed(Tile["1m"]),
            Closed(Tile["2m"]),
            Closed(Tile["3m"]),
            Closed(Tile["4m"]),
            Closed(Tile["5m"]),
            Closed(Tile["6m"]),
            Closed(Tile["7m"]),
            Closed(Tile["1p"]),
            Closed(Tile["2p"]),
            Closed(Tile["3p"]),
            Closed(Tile["4p"]),
            Closed(Tile["5p"]),
            Closed(Tile["6p"]),
            Closed(Tile["7p"]),
            Closed(Tile["1s"]),
            Closed(Tile["2s"]),
            Closed(Tile["3s"]),
            Closed(Tile["4s"]),
            Closed(Tile["5s"]),
            Closed(Tile["6s"]),
            Closed(Tile["7s"]),

            // 边张搭子
            Edge(Tile["1m"]),
            Edge(Tile["8m"]),
            Edge(Tile["1p"]),
            Edge(Tile["8p"]),
            Edge(Tile["1s"]),
            Edge(Tile["8s"]),

            // 对子
            Pair(Tile["1m"]),
            Pair(Tile["2m"]),
            Pair(Tile["3m"]),
            Pair(Tile["4m"]),
            Pair(Tile["5m"]),
            Pair(Tile["6m"]),
            Pair(Tile["7m"]),
            Pair(Tile["8m"]),
            Pair(Tile["9m"]),
            Pair(Tile["1p"]),
            Pair(Tile["2p"]),
            Pair(Tile["3p"]),
            Pair(Tile["4p"]),
            Pair(Tile["5p"]),
            Pair(Tile["6p"]),
            Pair(Tile["7p"]),
            Pair(Tile["8p"]),
            Pair(Tile["9p"]),
            Pair(Tile["1s"]),
            Pair(Tile["2s"]),
            Pair(Tile["3s"]),
            Pair(Tile["4s"]),
            Pair(Tile["5s"]),
            Pair(Tile["6s"]),
            Pair(Tile["7s"]),
            Pair(Tile["8s"]),
            Pair(Tile["9s"]),
            Pair(Tile["1z"]),
            Pair(Tile["2z"]),
            Pair(Tile["3z"]),
            Pair(Tile["4z"]),
            Pair(Tile["5z"]),
            Pair(Tile["6z"]),
            Pair(Tile["7z"])
        )

        for (protorun in testProtoruns) {
            // 测试toString得到的字符串拿去parse得到相同的结果
            val protorunString = protorun.toString()
            val parsedProtorun = Protorun.parse(protorunString)
            assertEquals(
                protorun,
                parsedProtorun,
                "toString和parse往返测试失败: $protorun -> $protorunString -> $parsedProtorun"
            )

            // 测试序列化得到的字符串与toString得到的字符串相同
            val serializedProtorun = Json.encodeToString(protorun)
            assertEquals(
                "\"${protorunString}\"",
                serializedProtorun,
                "序列化测试失败: $protorun -> $serializedProtorun"
            )

            // 测试反序列化得到的牌与parse得到的牌相同
            val deserializedProtorun = Json.decodeFromString<Protorun>(serializedProtorun)
            assertEquals(
                protorun,
                deserializedProtorun,
                "反序列化测试失败: $protorun -> $serializedProtorun -> $deserializedProtorun"
            )
        }
    }
}

