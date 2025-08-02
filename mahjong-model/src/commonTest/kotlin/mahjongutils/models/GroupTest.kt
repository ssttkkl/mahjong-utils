package mahjongutils.models

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GroupTest {
    @Test
    fun testGroupCreation() {
        // 测试创建顺子
        for (tile in Tile.all.filter { i -> i.num in 1..7 && i.type != TileType.Honour }) {
            val seq = Seq(tile)
            assertEquals(GroupType.Seq, seq.type)
            assertEquals(tile, seq.tile)
        }

        // 测试创建刻子
        for (tile in Tile.all) {
            val tri = Tri(tile)
            assertEquals(GroupType.Tri, tri.type)
            assertEquals(tile, tri.tile)
        }
    }

    @Test
    fun testGroupTiles() {
        // 测试顺子包含的牌
        val seq = Seq(Tile["1m"]) // 1万顺子
        val seqTiles = seq.tiles.toList()
        assertEquals(3, seqTiles.size)
        assertEquals(Tile["1m"], seqTiles[0]) // 1万
        assertEquals(Tile["2m"], seqTiles[1]) // 2万
        assertEquals(Tile["3m"], seqTiles[2]) // 3万

        // 测试刻子包含的牌
        val tri = Tri(Tile["2p"]) // 2筒刻子
        val triTiles = tri.tiles.toList()
        assertEquals(3, triTiles.size)
        assertEquals(Tile["2p"], triTiles[0]) // 2筒
        assertEquals(Tile["2p"], triTiles[1]) // 2筒
        assertEquals(Tile["2p"], triTiles[2]) // 2筒
    }

    @Test
    fun testGroupParse() {
        // 测试从牌列表解析面子
        val seqTiles = listOf(Tile["1m"], Tile["2m"], Tile["3m"])
        val seq = Group(seqTiles)
        assertEquals(GroupType.Seq, seq.type)
        assertEquals(Tile["1m"], seq.tile)

        val triTiles = listOf(Tile["2p"], Tile["2p"], Tile["2p"])
        val tri = Group(triTiles)
        assertEquals(GroupType.Tri, tri.type)
        assertEquals(Tile["2p"], tri.tile)

        // 测试从文本解析面子
        val seqFromText = Group("123m")
        assertEquals(GroupType.Seq, seqFromText.type)
        assertEquals(Tile["1m"], seqFromText.tile)

        val triFromText = Group("222p")
        assertEquals(GroupType.Tri, triFromText.type)
        assertEquals(Tile["2p"], triFromText.tile)

        // 测试无效输入
        assertFailsWith<IllegalArgumentException> { Group("123") }
        assertFailsWith<IllegalArgumentException> { Group("123z") } // 字牌不能组成顺子
        assertFailsWith<IllegalArgumentException> { Group("124m") } // 不连续的牌不能组成顺子
        assertFailsWith<IllegalArgumentException> { Group("1234m") } // 多于3张牌不能组成顺子
    }

    @Test
    fun testGroupToString() {
        val seq = Seq(Tile["1m"]) // 1万顺子
        assertEquals("123m", seq.toString())

        val tri = Tri(Tile["2p"]) // 2筒刻子
        assertEquals("222p", tri.toString())
    }

    @Test
    fun testAfterDiscard() {
        // 测试顺子舍牌后形成的搭子
        val seq = Seq(Tile["1m"]) // 1万顺子

        // 舍掉1万，应该形成两面搭子
        val protorun1 = seq.afterDiscard(Tile["1m"])
        assertEquals(ProtorunType.TwoSide, protorun1.type)
        assertEquals(Tile["2m"], protorun1.first)

        // 舍掉2万，应该形成嵌张搭子
        val protorun2 = seq.afterDiscard(Tile["2m"])
        assertEquals(ProtorunType.Closed, protorun2.type)
        assertEquals(Tile["1m"], protorun2.first)

        // 舍掉3万，应该形成两面搭子
        val protorun3 = seq.afterDiscard(Tile["3m"])
        assertEquals(ProtorunType.Edge, protorun3.type)
        assertEquals(Tile["1m"], protorun3.first)

        // 测试刻子舍牌后形成的搭子
        val tri = Tri(Tile["2p"]) // 2筒刻子
        val protorun4 = tri.afterDiscard(Tile["2p"])
        assertEquals(ProtorunType.Pair, protorun4.type)
        assertEquals(Tile["2p"], protorun4.first)

        // 测试无效舍牌
        assertFailsWith<IllegalArgumentException> { seq.afterDiscard(Tile["4m"]) }
        assertFailsWith<IllegalArgumentException> { tri.afterDiscard(Tile["3p"]) }

        // 789万顺子舍掉7万的情况
        val seq7 = Seq(Tile["7m"]) // 7万顺子 (789m)
        val protorun5 = seq7.afterDiscard(Tile["7m"])
        assertEquals(ProtorunType.Edge, protorun5.type)
        assertEquals(Tile["8m"], protorun5.first)

        // 789万顺子舍掉9万的情况
        val protorun6 = seq7.afterDiscard(Tile["9m"])
        assertEquals(ProtorunType.TwoSide, protorun6.type)
        assertEquals(Tile["7m"], protorun6.first)

        // 123万顺子舍掉3万的情况
        val seq1 = Seq(Tile["1m"]) // 1万顺子 (123m)
        val protorun7 = seq1.afterDiscard(Tile["3m"])
        assertEquals(ProtorunType.Edge, protorun7.type)
        assertEquals(Tile["1m"], protorun7.first)

        // 123万顺子舍掉1万的情况
        val protorun8 = seq1.afterDiscard(Tile["1m"])
        assertEquals(ProtorunType.TwoSide, protorun8.type)
        assertEquals(Tile["2m"], protorun8.first)
    }

    @Test
    fun testToStringAndParse() {
        // 测试toString得到的字符串拿去parse得到相同的结果
        val testGroups = listOf(
            // 顺子
            Seq(Tile["1m"]), Seq(Tile["2m"]), Seq(Tile["3m"]), Seq(Tile["4m"]),
            Seq(Tile["5m"]), Seq(Tile["6m"]), Seq(Tile["7m"]),
            Seq(Tile["1p"]), Seq(Tile["2p"]), Seq(Tile["3p"]), Seq(Tile["4p"]),
            Seq(Tile["5p"]), Seq(Tile["6p"]), Seq(Tile["7p"]),
            Seq(Tile["1s"]), Seq(Tile["2s"]), Seq(Tile["3s"]), Seq(Tile["4s"]),
            Seq(Tile["5s"]), Seq(Tile["6s"]), Seq(Tile["7s"]),

            // 刻子
            Tri(Tile["1m"]), Tri(Tile["2m"]), Tri(Tile["3m"]), Tri(Tile["4m"]), Tri(Tile["5m"]),
            Tri(Tile["6m"]), Tri(Tile["7m"]), Tri(Tile["8m"]), Tri(Tile["9m"]),
            Tri(Tile["1p"]), Tri(Tile["2p"]), Tri(Tile["3p"]), Tri(Tile["4p"]), Tri(Tile["5p"]),
            Tri(Tile["6p"]), Tri(Tile["7p"]), Tri(Tile["8p"]), Tri(Tile["9p"]),
            Tri(Tile["1s"]), Tri(Tile["2s"]), Tri(Tile["3s"]), Tri(Tile["4s"]), Tri(Tile["5s"]),
            Tri(Tile["6s"]), Tri(Tile["7s"]), Tri(Tile["8s"]), Tri(Tile["9s"]),
            Tri(Tile["1z"]), Tri(Tile["2z"]), Tri(Tile["3z"]), Tri(Tile["4z"]),
            Tri(Tile["5z"]), Tri(Tile["6z"]), Tri(Tile["7z"])
        )

        for (group in testGroups) {
            // 测试toString得到的字符串拿去parse得到相同的结果
            val groupString = group.toString()
            val parsedGroup = Group.parse(groupString)
            assertEquals(
                group.type,
                parsedGroup.type,
                "toString和parse往返测试失败: $group -> $groupString -> $parsedGroup"
            )
            assertEquals(
                group.tile,
                parsedGroup.tile,
                "toString和parse往返测试失败: $group -> $groupString -> $parsedGroup"
            )

            // 测试序列化得到的字符串与toString得到的字符串相同
            val serializedGroup = Json.encodeToString(group)
            assertEquals(
                "\"${groupString}\"",
                serializedGroup,
                "序列化测试失败: $group -> $serializedGroup"
            )

            // 测试反序列化得到的面子与parse得到的面子相同
            val deserializedGroup = Json.decodeFromString<Group>(serializedGroup)
            assertEquals(
                group.type,
                deserializedGroup.type,
                "反序列化测试失败: $group -> $serializedGroup -> $deserializedGroup"
            )
            assertEquals(
                group.tile,
                deserializedGroup.tile,
                "反序列化测试失败: $group -> $serializedGroup -> $deserializedGroup"
            )
        }
    }
}

