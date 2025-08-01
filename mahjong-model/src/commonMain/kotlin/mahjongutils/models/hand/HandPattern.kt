package mahjongutils.models.hand

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.Melded
import mahjongutils.models.MeldedType
import mahjongutils.models.Group
import mahjongutils.models.GroupType
import mahjongutils.models.Protorun
import mahjongutils.models.Tile

/**
 * 手牌形。将一个手牌按照目标形（标准形、七对子、国士无双）拆分的结果
 */
sealed interface HandPattern : IHand {
    /**
     * 浮牌
     */
    val floatings: List<Tile>

    /**
     * 是否听牌牌型
     */
    val isWaitingHand: Boolean

    /**
     * 是否和了牌型
     */
    val isWinningHand: Boolean
}

/**
 * 以标准形为目标的手牌
 */
@Serializable
@SerialName("RegularHandPattern")
data class RegularHandPattern(
    /**
     * 目标面子组数（=手牌数/4）
     */
    val k: Int,

    /**
     * 雀头
     */
    val pair: Tile?,

    /**
     * 门前面子，即从手牌中解析出来的面子（非副露）
     */
    val concealedGroups: List<Group>,
    override val meldeds: List<Melded>,
    /**
     * 搭子
     */
    val protoruns: List<Protorun>,
    override val floatings: List<Tile>,
) : HandPattern {
    override val tiles: List<Tile>
        get() = buildList {
            pair?.let {
                add(it)
                add(it)
            }
            for (mt in concealedGroups) {
                addAll(mt.tiles)
            }
            for (tt in protoruns) {
                add(tt.first)
                add(tt.second)
            }
            addAll(floatings)
        }

    /**
     * 面子（包括门前与副露）
     */
    val groups: List<Group>
        get() = concealedGroups + meldeds.map { it.asGroup() }

    /**
     * 暗刻
     */
    val concealedTris: List<Group>
        get() = concealedGroups.filter { it.type == GroupType.Tri } +
                meldeds.filter { it.type == MeldedType.ConcealedKong }.map { it.asGroup() }

    override val isWinningHand: Boolean
        get() = floatings.isEmpty() && protoruns.isEmpty()

    override val isWaitingHand: Boolean
        get() = floatings.isEmpty() && protoruns.size == 1 && pair != null
                || floatings.size == 1 && protoruns.isEmpty() && pair == null
}

/**
 * 以七对子为目标的手牌
 */
@Serializable
@SerialName("SevenPairsHandPattern")
data class SevenPairsHandPattern(
    /**
     * 已有对子
     */
    val pairs: Set<Tile>,
    override val floatings: List<Tile>
) : HandPattern {
    override val meldeds: List<Melded>
        get() = emptyList()

    override val tiles: List<Tile>
        get() = buildList {
            addAll(pairs)
            addAll(pairs)
            addAll(floatings)
        }

    override val isWinningHand: Boolean
        get() = floatings.isEmpty() && pairs.size == 7

    override val isWaitingHand: Boolean
        get() = floatings.size == 1 && pairs.size == 6
}

/**
 * 以国士无双为目标的手牌
 */
@Serializable
@SerialName("ThirteenOrphansHandPattern")
data class ThirteenOrphansHandPattern(
    /**
     * 幺九牌
     */
    val terminalsAndHonors: Set<Tile>,
    /**
     * 重复的幺九牌
     */
    val repeated: Tile?,
    override val floatings: List<Tile>
) : HandPattern {

    override val meldeds: List<Melded>
        get() = emptyList()

    override val tiles: List<Tile>
        get() = buildList {
            addAll(terminalsAndHonors)
            repeated?.let { add(it) }
            addAll(floatings)
        }

    override val isWinningHand: Boolean
        get() = floatings.isEmpty() && terminalsAndHonors.size == 13 && repeated != null

    override val isWaitingHand: Boolean
        get() = floatings.isEmpty() && terminalsAndHonors.size == 13 && repeated == null
                || floatings.isEmpty() && terminalsAndHonors.size == 12 && repeated != null
}