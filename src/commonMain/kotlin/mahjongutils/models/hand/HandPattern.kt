package mahjongutils.models.hand

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.*

@Serializable
sealed interface HandPattern : IHasFuro {
    val tiles: Collection<Tile>
}

sealed interface IRegularHandPattern : HandPattern {
    val k: Int
    val jyantou: Tile?
    val menzenMentsu: List<Mentsu>
    val tatsu: List<Tatsu>
    val remaining: List<Tile>

    override val tiles: Collection<Tile>
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

    val mentsu: Collection<Mentsu>
        get() = menzenMentsu + furo.map { it.asMentsu() }

    val anko: Collection<Kotsu>
        get() = menzenMentsu.filterIsInstance<Kotsu>() +
                furo.filterIsInstance<Kan>().filter { it.ankan }.map { it.asMentsu() }
}

@Serializable
@SerialName("RegularHandPattern")
data class RegularHandPattern(
    override val k: Int,
    override val jyantou: Tile?,
    override val menzenMentsu: List<Mentsu>,
    override val furo: List<Furo>,
    override val tatsu: List<Tatsu>,
    override val remaining: List<Tile>,
) : IRegularHandPattern

sealed interface IChitoiHandPattern : HandPattern {
    val pairs: Set<Tile>
    val remaining: List<Tile>

    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: Collection<Tile>
        get() = buildList {
            addAll(pairs)
            addAll(pairs)
            addAll(remaining)
        }
}

@Serializable
@SerialName("ChitoiHandPattern")
data class ChitoiHandPattern(
    override val pairs: Set<Tile>,
    override val remaining: List<Tile>
) : IChitoiHandPattern

sealed interface IKokushiHandPattern : HandPattern {
    val yaochu: Set<Tile>
    val repeated: Tile?
    val remaining: List<Tile>

    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: Collection<Tile>
        get() = buildList {
            addAll(yaochu)
            repeated?.let { add(it) }
            addAll(remaining)
        }
}

@Serializable
@SerialName("KokushiHandPattern")
data class KokushiHandPattern(
    override val yaochu: Set<Tile>,
    override val repeated: Tile?,
    override val remaining: List<Tile>
) : IKokushiHandPattern