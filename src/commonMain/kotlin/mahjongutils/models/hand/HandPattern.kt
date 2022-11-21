package mahjongutils.models.hand

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.Furo
import mahjongutils.models.Mentsu
import mahjongutils.models.Tatsu
import mahjongutils.models.Tile

@Serializable
sealed interface HandPattern : IHasFuro {
    val tiles: Iterable<Tile>
}

@Serializable
@SerialName("RegularHandPattern")
data class RegularHandPattern(
    val k: Int,
    val jyantou: Tile?,
    val menzenMentsu: List<Mentsu>,
    override val furo: List<Furo>,
    val tatsu: List<Tatsu>,
    val remaining: List<Tile>,
) : HandPattern {
    override val tiles: Iterable<Tile>
        get() = buildList {
            if (jyantou != null) {
                add(jyantou)
                add(jyantou)
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
}

@Serializable
@SerialName("ChitoiHandPattern")
data class ChitoiHandPattern(
    val pairs: Set<Tile>,
    val remaining: List<Tile>
) : HandPattern {
    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: Iterable<Tile>
        get() = buildList {
            addAll(pairs)
            addAll(pairs)
            addAll(remaining)
        }
}

@Serializable
@SerialName("KokushiHandPattern")
data class KokushiHandPattern(
    val yaochu: Set<Tile>,
    val repeated: Tile?,
    val remaining: List<Tile>
) : HandPattern {
    override val furo: List<Furo>
        get() = emptyList()

    override val tiles: Iterable<Tile>
        get() = buildList {
            addAll(yaochu)
            if (repeated != null)
                add(repeated)
            addAll(remaining)
        }
}