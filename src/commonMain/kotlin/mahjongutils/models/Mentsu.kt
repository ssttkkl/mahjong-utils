package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 面子
 */
@Serializable(with = MentsuSerializer::class)
sealed interface Mentsu {
    /**
     * 所含的牌
     */
    val tiles: Iterable<Tile>

    /**
     * 舍牌后形成的搭子
     */
    fun afterDiscard(discard: Tile): Tatsu

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

        operator fun invoke(tiles: List<Tile>): Mentsu {
            return parse(tiles)
        }

        operator fun invoke(text: String): Mentsu {
            return parse(text)
        }
    }
}

/**
 * 刻子
 */
@Serializable
@SerialName("Kotsu")
data class Kotsu(
    /**
     * 顺子的第一张牌（如789m，tile应为7m）
     */
    val tile: Tile
) : Mentsu {
    override val tiles: Iterable<Tile>
        get() = listOf(tile, tile, tile)

    override fun afterDiscard(discard: Tile): Tatsu {
        if (discard == tile) {
            return Toitsu(discard)
        }
        throw IllegalArgumentException("invalid discard: $discard")
    }

    override fun toString(): String {
        return "${tile.num}${tile.num}${tile.num}${tile.type.name.lowercase()}"
    }
}

/**
 * 顺子
 */
@Serializable
@SerialName("Shuntsu")
data class Shuntsu(
    /**
     * 刻子的牌（如777s，tile应为7s）
     */
    val tile: Tile
) : Mentsu {
    init {
        require(tile.num in 1..7)
        require(tile.type != TileType.Z)
    }

    override val tiles: Iterable<Tile>
        get() = listOf(tile, tile.advance(1), tile.advance(2))

    override fun afterDiscard(discard: Tile): Tatsu {
        if (discard == tile) {
            if (discard.num == 7)
                return Penchan(discard.advance(1))
            else if (discard.num == 3)
                return Penchan(discard.advance(-2))
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

    override fun toString(): String {
        return "${tile.num}${tile.num + 1}${tile.num + 2}${tile.type.name.lowercase()}"
    }
}

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
