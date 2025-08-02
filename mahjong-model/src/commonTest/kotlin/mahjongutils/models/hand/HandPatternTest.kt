package mahjongutils.models.hand

import mahjongutils.models.GroupType
import mahjongutils.models.Melded
import mahjongutils.models.Pair
import mahjongutils.models.Pung
import mahjongutils.models.Seq
import mahjongutils.models.Tile
import mahjongutils.models.Tri
import mahjongutils.models.TwoSide
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HandPatternTest {
    @Test
    fun testRegularHandPattern() {
        // 测试标准形手牌
        val pair = Tile["1m"] // 1万雀头
        val concealedGroups = listOf(
            Seq(Tile["2m"]), // 234万
            Tri(Tile["2p"])  // 222筒
        )
        val meldeds = listOf(
            Pung(Tile["3s"]) // 333索
        )
        val protoruns = listOf(
            TwoSide(Tile["4m"]) // 45万两面搭子
        )
        val floatings = listOf(Tile["5z"]) // 白浮牌

        val handPattern = RegularHandPattern(
            k = 4,
            pair = pair,
            concealedGroups = concealedGroups,
            meldeds = meldeds,
            protoruns = protoruns,
            floatings = floatings
        )

        // 验证基本属性
        assertEquals(pair, handPattern.pair)
        assertEquals(concealedGroups, handPattern.concealedGroups)
        assertEquals(meldeds, handPattern.meldeds)
        assertEquals(protoruns, handPattern.protoruns)
        assertEquals(floatings, handPattern.floatings)

        // 验证面子
        val groups = handPattern.groups
        assertEquals(3, groups.size)
        assertTrue(groups.any { it.type == GroupType.Seq && it.tile == Tile["2m"] })
        assertTrue(groups.any { it.type == GroupType.Tri && it.tile == Tile["2p"] })
        assertTrue(groups.any { it.type == GroupType.Tri && it.tile == Tile["3s"] })

        // 验证暗刻
        val concealedTris = handPattern.concealedTris
        assertEquals(1, concealedTris.size)
        assertEquals(Tile["2p"], concealedTris[0].tile)

        // 验证是否和了/听牌
        assertFalse(handPattern.isWinningHand)
        assertFalse(handPattern.isWaitingHand)

        // 验证门前牌
        val tiles = handPattern.tiles
        assertEquals(11, tiles.size) // 2张雀头 + 6张门前面子 + 2张搭子 + 1张浮牌
        assertEquals(2, tiles.count { it == Tile["1m"] }) // 雀头
        assertEquals(1, tiles.count { it == Tile["2m"] }) // 顺子
        assertEquals(1, tiles.count { it == Tile["3m"] }) // 顺子
        assertEquals(2, tiles.count { it == Tile["4m"] }) // 顺子+搭子
        assertEquals(1, tiles.count { it == Tile["5m"] }) // 搭子
        assertEquals(3, tiles.count { it == Tile["2p"] }) // 刻子
        assertEquals(1, tiles.count { it == Tile["5z"] }) // 浮牌
    }

    @Test
    fun testRegularHandPatternWinningAndWaiting() {
        // 测试和了型
        val winningPattern = RegularHandPattern(
            k = 4,
            pair = Tile["1m"],
            concealedGroups = listOf(
                Seq(Tile["2m"]),
                Tri(Tile["2p"]),
                Seq(Tile["1s"])
            ),
            meldeds = listOf(
                Pung(Tile["3s"])
            ),
            protoruns = emptyList(),
            floatings = emptyList()
        )
        assertTrue(winningPattern.isWinningHand)
        assertFalse(winningPattern.isWaitingHand)

        // 测试听牌型 - 有搭子无浮牌
        val waitingPattern1 = RegularHandPattern(
            k = 4,
            pair = Tile["1m"],
            concealedGroups = listOf(
                Seq(Tile["2m"]),
                Tri(Tile["2p"])
            ),
            meldeds = listOf(
                Pung(Tile["3s"])
            ),
            protoruns = listOf(
                TwoSide(Tile["4m"])
            ),
            floatings = emptyList()
        )
        assertFalse(waitingPattern1.isWinningHand)
        assertTrue(waitingPattern1.isWaitingHand)

        // 测试听牌型 - 有浮牌无搭子无雀头
        val waitingPattern2 = RegularHandPattern(
            k = 4,
            pair = null,
            concealedGroups = listOf(
                Seq(Tile["2m"]),
                Tri(Tile["2p"]),
                Seq(Tile["1s"])
            ),
            meldeds = listOf(
                Pung(Tile["3s"])
            ),
            protoruns = emptyList(),
            floatings = listOf(Tile["5z"])
        )
        assertFalse(waitingPattern2.isWinningHand)
        assertTrue(waitingPattern2.isWaitingHand)
    }

    @Test
    fun testSevenPairsHandPattern() {
        // 测试七对子手牌
        val pairs = setOf(
            Tile["1m"], // 1万
            Tile["2p"], // 2筒
            Tile["3s"], // 3索
            Tile["5z"], // 白
            Tile["6z"], // 发
            Tile["7z"]  // 中
        )
        val floatings = listOf(Tile["4m"]) // 4万浮牌

        val handPattern = SevenPairsHandPattern(
            pairs = pairs,
            floatings = floatings
        )

        // 验证基本属性
        assertEquals(pairs, handPattern.pairs)
        assertEquals(floatings, handPattern.floatings)
        assertEquals(emptyList<Melded>(), handPattern.meldeds)

        // 验证门前牌
        val tiles = handPattern.tiles
        assertEquals(13, tiles.size) // 6对牌 + 1张浮牌
        assertEquals(2, tiles.count { it == Tile["1m"] })
        assertEquals(2, tiles.count { it == Tile["2p"] })
        assertEquals(2, tiles.count { it == Tile["3s"] })
        assertEquals(2, tiles.count { it == Tile["5z"] })
        assertEquals(2, tiles.count { it == Tile["6z"] })
        assertEquals(2, tiles.count { it == Tile["7z"] })
        assertEquals(1, tiles.count { it == Tile["4m"] })

        // 验证是否和了/听牌
        assertFalse(handPattern.isWinningHand)
        assertTrue(handPattern.isWaitingHand) // 6对1浮牌，应该是听牌
    }

    @Test
    fun testSevenPairsHandPatternWinningAndWaiting() {
        // 测试和了型
        val winningPattern = SevenPairsHandPattern(
            pairs = setOf(
                Tile["1m"], Tile["2p"], Tile["3s"], Tile["5z"],
                Tile["6z"], Tile["7z"], Tile["4m"]
            ),
            floatings = emptyList()
        )
        assertTrue(winningPattern.isWinningHand)
        assertFalse(winningPattern.isWaitingHand)

        // 测试听牌型
        val waitingPattern = SevenPairsHandPattern(
            pairs = setOf(
                Tile["1m"], Tile["2p"], Tile["3s"],
                Tile["5z"], Tile["6z"], Tile["7z"]
            ),
            floatings = listOf(Tile["4m"])
        )
        assertFalse(waitingPattern.isWinningHand)
        assertTrue(waitingPattern.isWaitingHand)
    }

    @Test
    fun testThirteenOrphansHandPattern() {
        // 测试国士无双手牌
        val terminalsAndHonors = setOf(
            Tile["1m"], Tile["9m"], // 1万、9万
            Tile["1p"], Tile["9p"], // 1筒、9筒
            Tile["1s"], Tile["9s"], // 1索、9索
            Tile["1z"], Tile["2z"], Tile["3z"], Tile["4z"], // 东南西北
            Tile["5z"], Tile["6z"], Tile["7z"] // 白发中
        )
        val repeated = Tile["1m"] // 重复的1万
        val floatings = emptyList<Tile>()

        val handPattern = ThirteenOrphansHandPattern(
            terminalsAndHonors = terminalsAndHonors,
            repeated = repeated,
            floatings = floatings
        )

        // 验证基本属性
        assertEquals(terminalsAndHonors, handPattern.terminalsAndHonors)
        assertEquals(repeated, handPattern.repeated)
        assertEquals(floatings, handPattern.floatings)
        assertEquals(emptyList<Melded>(), handPattern.meldeds)

        // 验证门前牌
        val tiles = handPattern.tiles
        assertEquals(14, tiles.size) // 13种幺九牌 + 1张重复
        assertEquals(2, tiles.count { it == Tile["1m"] }) // 重复的1万

        // 验证是否和了/听牌
        assertTrue(handPattern.isWinningHand)
        assertFalse(handPattern.isWaitingHand)
    }

    @Test
    fun testThirteenOrphansHandPatternWaitingAndWinning() {
        // 测试和了型
        val winningPattern = ThirteenOrphansHandPattern(
            terminalsAndHonors = setOf(
                Tile["1m"], Tile["9m"], Tile["1p"], Tile["9p"],
                Tile["1s"], Tile["9s"], Tile["1z"], Tile["2z"],
                Tile["3z"], Tile["4z"], Tile["5z"], Tile["6z"], Tile["7z"]
            ),
            repeated = Tile["1m"],
            floatings = emptyList()
        )
        assertTrue(winningPattern.isWinningHand)
        assertFalse(winningPattern.isWaitingHand)

        // 测试听牌型 - 13种幺九牌无重复
        val waitingPattern1 = ThirteenOrphansHandPattern(
            terminalsAndHonors = setOf(
                Tile["1m"], Tile["9m"], Tile["1p"], Tile["9p"],
                Tile["1s"], Tile["9s"], Tile["1z"], Tile["2z"],
                Tile["3z"], Tile["4z"], Tile["5z"], Tile["6z"], Tile["7z"]
            ),
            repeated = null,
            floatings = emptyList()
        )
        assertFalse(waitingPattern1.isWinningHand)
        assertTrue(waitingPattern1.isWaitingHand)

        // 测试听牌型 - 12种幺九牌有重复
        val waitingPattern2 = ThirteenOrphansHandPattern(
            terminalsAndHonors = setOf(
                Tile["1m"], Tile["9m"], Tile["1p"], Tile["9p"],
                Tile["1s"], Tile["9s"], Tile["1z"], Tile["2z"],
                Tile["3z"], Tile["4z"], Tile["5z"], Tile["6z"]
            ),
            repeated = Tile["1m"],
            floatings = emptyList()
        )
        assertFalse(waitingPattern2.isWinningHand)
        assertTrue(waitingPattern2.isWaitingHand)
    }

    @Test
    fun testHandPatternsNeitherWinningNorWaiting() {
        // 测试14张标准形：多个搭子的情况（既不是和了也不是听牌）
        val regularPatternMultipleProtoruns = RegularHandPattern(
            k = 4,
            pair = Tile["1m"],
            concealedGroups = listOf(
                Seq(Tile["2m"]), // 234万
                Tri(Tile["2p"]),  // 222筒
            ),
            meldeds = emptyList(),
            protoruns = listOf(
                TwoSide(Tile["4m"]), // 45万两面搭子
                Pair(Tile["6s"])  // 6索对子
            ),
            floatings = listOf(Tile["5z"])
        )
        assertFalse(regularPatternMultipleProtoruns.isWinningHand)
        assertFalse(regularPatternMultipleProtoruns.isWaitingHand)

        // 测试13张标准形：既有搭子又有浮牌的情况
        val regularPatternProtorunAndFloating = RegularHandPattern(
            k = 4,
            pair = Tile["1m"],
            concealedGroups = listOf(
                Seq(Tile["2m"]), // 234万
                Tri(Tile["2p"])  // 222筒
            ),
            meldeds = emptyList(),
            protoruns = listOf(
                TwoSide(Tile["4m"]) // 45万两面搭子
            ),
            floatings = listOf(Tile["5s"], Tile["6z"], Tile["7z"]) // 白、发浮牌
        )
        assertFalse(regularPatternProtorunAndFloating.isWinningHand)
        assertFalse(regularPatternProtorunAndFloating.isWaitingHand)

        // 测试14张标准形：多个浮牌无搭子无雀头的情况
        val regularPatternMultipleFloatings = RegularHandPattern(
            k = 4,
            pair = null,
            concealedGroups = listOf(
                Seq(Tile["2m"]), // 234万
                Tri(Tile["2p"]),  // 222筒
                Seq(Tile["1s"])   // 123索
            ),
            meldeds = listOf(
                Pung(Tile["3s"]) // 333索
            ),
            protoruns = emptyList(),
            floatings = listOf(Tile["5z"], Tile["6z"]) // 白、发浮牌
        )
        assertFalse(regularPatternMultipleFloatings.isWinningHand)
        assertFalse(regularPatternMultipleFloatings.isWaitingHand)

        // 测试13张七对子：对子数量不足且有多个浮牌
        val sevenPairsPatternFewPairs = SevenPairsHandPattern(
            pairs = setOf(
                Tile["1m"], // 1万
                Tile["2p"], // 2筒
                Tile["3s"], // 3索
                Tile["5z"]  // 白
            ),
            floatings = listOf(
                Tile["4m"],
                Tile["5m"],
                Tile["4s"],
                Tile["6z"],
                Tile["7z"]
            ) // 4万、5万、4索、中发浮牌
        )
        assertFalse(sevenPairsPatternFewPairs.isWinningHand)
        assertFalse(sevenPairsPatternFewPairs.isWaitingHand)

        // 测试14张七对子：5对4浮牌的情况
        val sevenPairsPatternTwoFloatings = SevenPairsHandPattern(
            pairs = setOf(
                Tile["1m"], Tile["2p"], Tile["3s"],
                Tile["5z"], Tile["6z"]
            ),
            floatings = listOf(Tile["4m"], Tile["6m"], Tile["1z"], Tile["7z"]) // 4万、6万、东中浮牌
        )
        assertFalse(sevenPairsPatternTwoFloatings.isWinningHand)
        assertFalse(sevenPairsPatternTwoFloatings.isWaitingHand)

        // 测试13张国士无双：幺九牌种类不足的情况
        val thirteenOrphansPatternFewTerminals = ThirteenOrphansHandPattern(
            terminalsAndHonors = setOf(
                Tile["1m"], Tile["9m"], // 1万、9万
                Tile["1p"], Tile["9p"], // 1筒、9筒
                Tile["1s"], Tile["9s"], // 1索、9索
                Tile["1z"], Tile["2z"], // 东、南
                Tile["5z"] // 白
            ),
            repeated = null,
            floatings = listOf(Tile["2m"], Tile["3m"], Tile["3p"], Tile["4s"]) // 非幺九牌浮牌
        )
        assertFalse(thirteenOrphansPatternFewTerminals.isWinningHand)
        assertFalse(thirteenOrphansPatternFewTerminals.isWaitingHand)

        // 测试14张国士无双：10种幺九牌有重复但还有浮牌
        val thirteenOrphansPatternWithFloatings = ThirteenOrphansHandPattern(
            terminalsAndHonors = setOf(
                Tile["1m"], Tile["9m"], Tile["1p"], Tile["9p"],
                Tile["1s"], Tile["9s"], Tile["1z"], Tile["2z"],
                Tile["3z"], Tile["4z"]
            ),
            repeated = Tile["1m"],
            floatings = listOf(Tile["2m"], Tile["3m"], Tile["4m"]) // 非幺九牌浮牌
        )
        assertFalse(thirteenOrphansPatternWithFloatings.isWinningHand)
        assertFalse(thirteenOrphansPatternWithFloatings.isWaitingHand)
    }
}

