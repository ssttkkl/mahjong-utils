package mahjongutils.models.hand

import mahjongutils.models.Ankan
import mahjongutils.models.Chi
import mahjongutils.models.Kotsu
import mahjongutils.models.MentsuType
import mahjongutils.models.Ryanmen
import mahjongutils.models.Shuntsu
import mahjongutils.models.Tatsu
import mahjongutils.models.Tile
import mahjongutils.models.TileType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HandPatternTest {

    @Test
    fun testRegularHandPattern() {
        // 测试创建标准形手牌
        val jyantou = Tile.get(TileType.M, 1)
        val menzenMentsu = listOf(
            Shuntsu(Tile.get(TileType.M, 2)),
            Kotsu(Tile.get(TileType.P, 5))
        )
        val furo = listOf(
            Chi(Tile.get(TileType.S, 3))
        )
        val tatsu = listOf(
            Ryanmen(Tile.get(TileType.M, 6))
        )
        val remaining = listOf(Tile.get(TileType.Z, 1))

        val regularHand = RegularHandPattern(
            k = 4,
            jyantou = jyantou,
            menzenMentsu = menzenMentsu,
            furo = furo,
            tatsu = tatsu,
            remaining = remaining
        )

        // 测试基本属性
        assertEquals(4, regularHand.k)
        assertEquals(jyantou, regularHand.jyantou)
        assertEquals(menzenMentsu, regularHand.menzenMentsu)
        assertEquals(furo, regularHand.furo)
        assertEquals(tatsu, regularHand.tatsu)
        assertEquals(remaining, regularHand.remaining)

        // 测试门前牌
        val tilesInHand = regularHand.tilesInHand
        assertEquals(11, tilesInHand.size) // 雀头(2) + 门前面子(6) + 搭子(2) + 浮牌(1)

        // 测试所有面子
        val allMentsu = regularHand.mentsu
        assertEquals(3, allMentsu.size) // 门前面子(2) + 副露面子(1)

        // 测试暗刻
        val anko = regularHand.anko
        assertEquals(1, anko.size)
        assertEquals(MentsuType.Kotsu, anko[0].type)
        assertEquals(Tile.get(TileType.P, 5), anko[0].tile)

        // 测试门清状态
        assertFalse(regularHand.menzen)
    }

    @Test
    fun testRegularHandPatternWithAnkan() {
        // 测试创建带暗杠的标准形手牌
        val jyantou = Tile.get(TileType.M, 1)
        val menzenMentsu = listOf(
            Shuntsu(Tile.get(TileType.M, 2)),
            Kotsu(Tile.get(TileType.P, 5))
        )
        val furo = listOf(
            Ankan(Tile.get(TileType.Z, 1))
        )
        val tatsu = listOf<Tatsu>()
        val remaining = listOf<Tile>()

        val regularHand = RegularHandPattern(
            k = 4,
            jyantou = jyantou,
            menzenMentsu = menzenMentsu,
            furo = furo,
            tatsu = tatsu,
            remaining = remaining
        )

        // 测试暗刻（包括暗杠）
        val anko = regularHand.anko
        assertEquals(2, anko.size) // 门前刻子(1) + 暗杠(1)

        // 测试门清状态（暗杠不影响门清）
        assertTrue(regularHand.menzen)
    }

    @Test
    fun testChitoiHandPattern() {
        // 测试创建七对子手牌
        val pairs = setOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 3),
            Tile.get(TileType.M, 5),
            Tile.get(TileType.P, 2),
            Tile.get(TileType.P, 4),
            Tile.get(TileType.S, 6),
            Tile.get(TileType.Z, 1)
        )
        val remaining = emptyList<Tile>()

        val chitoiHand = ChitoiHandPattern(
            pairs = pairs,
            remaining = remaining
        )

        // 测试基本属性
        assertEquals(pairs, chitoiHand.pairs)
        assertEquals(remaining, chitoiHand.remaining)

        // 测试门前牌
        val tilesInHand = chitoiHand.tilesInHand
        assertEquals(14, tilesInHand.size) // 7对牌 = 14张

        // 测试副露（七对子不能有副露）
        val furo = chitoiHand.furo
        assertEquals(0, furo.size)

        // 测试门清状态（七对子必然门清）
        assertTrue(chitoiHand.menzen)
    }

    @Test
    fun testKokushiHandPattern() {
        // 测试创建国士无双手牌
        val yaochu = setOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 9),
            Tile.get(TileType.P, 1),
            Tile.get(TileType.P, 9),
            Tile.get(TileType.S, 1),
            Tile.get(TileType.S, 9),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 2),
            Tile.get(TileType.Z, 3),
            Tile.get(TileType.Z, 4),
            Tile.get(TileType.Z, 5),
            Tile.get(TileType.Z, 6),
            Tile.get(TileType.Z, 7)
        )
        val repeated = Tile.get(TileType.M, 1)
        val remaining = emptyList<Tile>()

        val kokushiHand = KokushiHandPattern(
            yaochu = yaochu,
            repeated = repeated,
            remaining = remaining
        )

        // 测试基本属性
        assertEquals(yaochu, kokushiHand.yaochu)
        assertEquals(repeated, kokushiHand.repeated)
        assertEquals(remaining, kokushiHand.remaining)

        // 测试门前牌
        val tilesInHand = kokushiHand.tilesInHand
        assertEquals(14, tilesInHand.size) // 13种幺九牌 + 1张重复的幺九牌

        // 测试副露（国士无双不能有副露）
        val furo = kokushiHand.furo
        assertEquals(0, furo.size)

        // 测试门清状态（国士无双必然门清）
        assertTrue(kokushiHand.menzen)
    }

    @Test
    fun testKokushiHandPatternWithoutRepeated() {
        // 测试创建没有重复牌的国士无双手牌（听牌状态）
        val yaochu = setOf(
            Tile.get(TileType.M, 1),
            Tile.get(TileType.M, 9),
            Tile.get(TileType.P, 1),
            Tile.get(TileType.P, 9),
            Tile.get(TileType.S, 1),
            Tile.get(TileType.S, 9),
            Tile.get(TileType.Z, 1),
            Tile.get(TileType.Z, 2),
            Tile.get(TileType.Z, 3),
            Tile.get(TileType.Z, 4),
            Tile.get(TileType.Z, 5),
            Tile.get(TileType.Z, 6),
            Tile.get(TileType.Z, 7)
        )
        val repeated = null
        val remaining = emptyList<Tile>()

        val kokushiHand = KokushiHandPattern(
            yaochu = yaochu,
            repeated = repeated,
            remaining = remaining
        )

        // 测试门前牌
        val tilesInHand = kokushiHand.tilesInHand
        assertEquals(13, tilesInHand.size) // 13种幺九牌，没有重复
    }
}

