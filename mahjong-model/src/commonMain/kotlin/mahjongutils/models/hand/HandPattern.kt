package mahjongutils.models.hand

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.FuroType
import mahjongutils.models.Mentsu
import mahjongutils.models.MentsuType
import mahjongutils.models.Tatsu
import mahjongutils.models.Tile

/**
 * 手牌形。将一个手牌按照目标形（标准形、七对子、国士无双）拆分的结果
 */
sealed interface HandPattern : IHand {
    /**
     * 浮牌
     */
    val remaining: List<Tile>
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
    val jyantou: Tile?,

    /**
     * 门前面子，即从手牌中解析出来的面子（非副露）
     */
    val menzenMentsu: List<Mentsu>,
    override val furo: List<Furo>,
    /**
     * 搭子
     */
    val tatsu: List<Tatsu>,
    override val remaining: List<Tile>,
) : HandPattern {
    override val tilesInHand: List<Tile>
        get() = buildList {
            jyantou?.let {
                add(it)
                add(it)
            }
            for (mt in menzenMentsu) {
                addAll(mt.tiles)
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
    val anko: List<Mentsu>
        get() = menzenMentsu.filter { it.type == MentsuType.Kotsu } +
                furo.filter { it.type == FuroType.Ankan }.map { it.asMentsu() }
}

/**
 * 以七对子为目标的手牌
 */
@Serializable
@SerialName("ChitoiHandPattern")
data class ChitoiHandPattern(
    /**
     * 已有对子
     */
    val pairs: Set<Tile>,
    override val remaining: List<Tile>
) : HandPattern {
    override val furo: List<Furo>
        get() = emptyList()

    override val tilesInHand: List<Tile>
        get() = buildList {
            addAll(pairs)
            addAll(pairs)
            addAll(remaining)
        }
}

/**
 * 以国士无双为目标的手牌
 */
@Serializable
@SerialName("KokushiHandPattern")
data class KokushiHandPattern(
    /**
     * 幺九牌
     */
    val yaochu: Set<Tile>,
    /**
     * 重复的幺九牌
     */
    val repeated: Tile?,
    override val remaining: List<Tile>
) : HandPattern {

    override val furo: List<Furo>
        get() = emptyList()

    override val tilesInHand: List<Tile>
        get() = buildList {
            addAll(yaochu)
            repeated?.let { add(it) }
            addAll(remaining)
        }
}