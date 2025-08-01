package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

enum class TatsuType {
    Ryanmen, Kanchan, Penchan, Toitsu
}

/**
 * 搭子
 */
@JvmInline
@Serializable(with = TatsuSerializer::class)
value class Tatsu private constructor(private val value: Int) {
    val type: TatsuType
        get() = TatsuType.entries[value / 100]

    constructor(type: TatsuType, first: Tile) : this(type.ordinal * 100 + first.code)

    /**
     * 第一张牌
     */
    val first: Tile
        get() = Tile[value % 100]

    /**
     * 第二张牌
     */
    val second: Tile
        get() = when (type) {
            TatsuType.Ryanmen -> first.advance(1)
            TatsuType.Kanchan -> first.advance(2)
            TatsuType.Penchan -> first.advance(1)
            TatsuType.Toitsu -> first
        }

    /**
     * 进张
     */
    val waiting: Set<Tile>
        get() = when (type) {
            TatsuType.Ryanmen -> setOf(first.advance(-1), first.advance(2))
            TatsuType.Kanchan -> setOf(first.advance(1))
            TatsuType.Penchan -> if (first.num == 1)
                setOf(first.advance(2))
            else
                setOf(first.advance(-1))

            TatsuType.Toitsu -> setOf(first)
        }

    /**
     * 进张后形成的面子
     */
    fun withWaiting(tile: Tile): Mentsu {
        when (type) {
            TatsuType.Ryanmen -> {
                if (tile == first.advance(-1))
                    return Shuntsu(tile)
                else if (tile == second.advance(1))
                    return Shuntsu(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            TatsuType.Kanchan -> {
                if (tile == first.advance(1))
                    return Shuntsu(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            TatsuType.Penchan -> {
                if (first.num == 1 && tile == first.advance(2))
                    return Shuntsu(first)
                if (first.num == 8 && tile == first.advance(-1))
                    return Shuntsu(tile)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            TatsuType.Toitsu -> {
                if (tile == first)
                    return Kotsu(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }
        }
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }

    companion object {
        /**
         * 根据给定牌构造搭子
         * @param first 第一张牌
         * @param second 第二张牌
         * @return 搭子
         */
        fun parse(first: Tile, second: Tile): Tatsu {
            if (first > second)
                return parse(second, first)

            if (first == second) {
                return Toitsu(first)
            } else {
                if (first.type == TileType.Z || second.type == TileType.Z) {
                    throw IllegalArgumentException("invalid tiles: $first $second")
                }

                when (second.distance(first)) {
                    1 -> {
                        if (first.num == 1 || first.num == 8) {
                            return Penchan(first)
                        } else {
                            return Ryanmen(first)
                        }
                    }

                    2 -> {
                        return Kanchan(first)
                    }

                    else -> {
                        throw IllegalArgumentException("invalid tiles: $first $second")
                    }
                }
            }
        }

        /**
         * 根据给定牌的文本构造搭子
         * @param text 牌的文本
         * @return 搭子
         */
        fun parse(text: String): Tatsu {
            val tiles = Tile.parseTiles(text)
            if (tiles.size != 2) {
                throw IllegalArgumentException("invalid tiles: $text")
            }
            return parse(tiles[0], tiles[1])
        }
    }
}

fun Tatsu(first: Tile, second: Tile): Tatsu {
    return Tatsu.parse(first, second)
}

fun Tatsu(text: String): Tatsu {
    return Tatsu.parse(text)
}

fun Ryanmen(tile: Tile) = Tatsu(TatsuType.Ryanmen, tile)
fun Kanchan(tile: Tile) = Tatsu(TatsuType.Kanchan, tile)
fun Penchan(tile: Tile) = Tatsu(TatsuType.Penchan, tile)
fun Toitsu(tile: Tile) = Tatsu(TatsuType.Toitsu, tile)

internal class TatsuSerializer : KSerializer<Tatsu> {
    override val descriptor = PrimitiveSerialDescriptor("Tatsu", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Tatsu) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Tatsu {
        val text = decoder.decodeString()
        return Tatsu(text)
    }
}
