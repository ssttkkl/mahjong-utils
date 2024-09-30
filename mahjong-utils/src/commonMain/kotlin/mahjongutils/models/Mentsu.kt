package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

enum class MentsuType {
    Shuntsu, Kotsu
}

/**
 * 面子
 */
@JvmInline
@Serializable(with = MentsuSerializer::class)
value class Mentsu private constructor(private val value: Int) {
    val type: MentsuType
        get() = MentsuType.entries[value / 100]

    val tile: Tile
        get() = Tile[value % 100]

    constructor(type: MentsuType, tile: Tile) : this(type.ordinal * 100 + tile.code)

    /**
     * 所含的牌
     */
    val tiles: Iterable<Tile>
        get() = when (type) {
            MentsuType.Shuntsu -> listOf(tile, tile.advance(1), tile.advance(2))
            MentsuType.Kotsu -> listOf(tile, tile, tile)
        }

    /**
     * 舍牌后形成的搭子
     */
    fun afterDiscard(discard: Tile): Tatsu {
        return when (type) {
            MentsuType.Shuntsu -> {
                if (discard == tile) {
                    if (discard.num == 7)
                        return Penchan(discard.advance(1))
                    else
                        return Ryanmen(discard.advance(1))
                } else if (discard == tile.advance(1))
                    return Kanchan(tile)
                else if (discard == tile.advance(2)) {
                    if (tile.num == 1)
                        return Penchan(tile)
                    else
                        return Ryanmen(tile)
                }
                throw IllegalArgumentException("invalid discard: $discard")
            }

            MentsuType.Kotsu -> {
                if (discard == tile) {
                    return Toitsu(discard)
                }
                throw IllegalArgumentException("invalid discard: $discard")

            }
        }
    }

    override fun toString(): String {
        return when (type) {
            MentsuType.Shuntsu -> "${tile.num}${tile.realNum + 1}${tile.realNum + 2}${tile.type.name.lowercase()}"
            MentsuType.Kotsu -> "${tile.num}${tile.num}${tile.num}${tile.type.name.lowercase()}"
        }
    }

    companion object {
        /**
         * 根据给定牌构造面子
         * @param tiles 牌
         * @return 面子
         */
        fun parse(tiles: List<Tile>): Mentsu {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Kotsu(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Z }) {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Shuntsu(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }
                }
            }

            throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
        }

        /**
         * 根据给定牌构造面子
         * @param text 牌的文本
         * @return 面子
         */
        fun parse(text: String): Mentsu {
            return parse(Tile.parseTiles(text))
        }
    }
}

fun Mentsu(tiles: List<Tile>): Mentsu {
    return Mentsu.parse(tiles)
}

fun Mentsu(text: String): Mentsu {
    return Mentsu.parse(text)
}

fun Shuntsu(tile: Tile) = Mentsu(MentsuType.Shuntsu, tile)

fun Kotsu(tile: Tile) = Mentsu(MentsuType.Kotsu, tile)

internal class MentsuSerializer : KSerializer<Mentsu> {
    override val descriptor = PrimitiveSerialDescriptor("Mentsu", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Mentsu) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Mentsu {
        val text = decoder.decodeString()
        return Mentsu(text)
    }
}
