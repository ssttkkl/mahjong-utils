package mahjongutils.models.hand

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.*

/**
 * 手牌形
 */
interface HandPattern : IHasFuro {
    /**
     * 手牌（包括门前与副露）
     */
    val tiles: List<Tile>

    /**
     * 浮牌
     */
    val remaining: List<Tile>
}

// 单纯是为了sealed
@Serializable
sealed interface CommonHandPattern : HandPattern

/**
 * 以标准形为目标的手牌
 */
interface IRegularHandPattern : HandPattern {
    /**
     * 目标面子组数（=手牌数/4）
     */
    val k: Int

    /**
     * 雀头
     */
    val jyantou: Tile?

    /**
     * 门前面子
     */
    val menzenMentsu: List<Mentsu>

    /**
     * 搭子
     */
    val tatsu: List<Tatsu>

    override val tiles: List<Tile>
        get() = buildList {
            jyantou?.let {
                add(it)
                add(it)
            }
            for (mt in menzenMentsu) {
                addAll(mt.tiles)
            }
            for (fr in furo) {
                addAll(fr.asMentsu().tiles)
            }
            for (tt in tatsu) {
                add(tt.first)
                add(tt.second)
            }
            addAll(remaining)
        }

    /**
     * 面子（包括门前与副露）
     */
    val mentsu: List<Mentsu>
        get() = menzenMentsu + furo.map { it.asMentsu() }

    /**
     * 暗刻
     */
    val anko: List<Kotsu>
        get() = menzenMentsu.filterIsInstance<Kotsu>() +
                furo.filterIsInstance<Kan>().filter { it.ankan }.map { it.asMentsu() }
}

/**
 * 以标准形为目标的手牌
 */
@Serializable
@SerialName("RegularHandPattern")
data class RegularHandPattern(
    override val k: Int,
    override val jyantou: Tile?,
    override val menzenMentsu: List<Mentsu>,
    override val furo: List<Furo>,
    override val tatsu: List<Tatsu>,
    override val remaining: List<Tile>,
) : IRegularHandPattern, CommonHandPattern

/**
 * 以七对子为目标的手牌
 */
interface IChitoiHandPattern : HandPattern {
    /**
     * 已有对子
     */
    val pairs: Set<Tile>

    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: List<Tile>
        get() = buildList {
            addAll(pairs)
            addAll(pairs)
            addAll(remaining)
        }
}

/**
 * 以七对子为目标的手牌
 */
@Serializable
@SerialName("ChitoiHandPattern")
data class ChitoiHandPattern(
    override val pairs: Set<Tile>,
    override val remaining: List<Tile>
) : IChitoiHandPattern, CommonHandPattern

/**
 * 以国士无双为目标的手牌
 */
interface IKokushiHandPattern : HandPattern {
    /**
     * 幺九牌
     */
    val yaochu: Set<Tile>

    /**
     * 重复的幺九牌
     */
    val repeated: Tile?

    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: List<Tile>
        get() = buildList {
            addAll(yaochu)
            repeated?.let { add(it) }
            addAll(remaining)
        }
}

/**
 * 以国士无双为目标的手牌
 */
@Serializable
@SerialName("KokushiHandPattern")
data class KokushiHandPattern(
    override val yaochu: Set<Tile>,
    override val repeated: Tile?,
    override val remaining: List<Tile>
) : IKokushiHandPattern, CommonHandPattern