package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

/**
 * 搭子类型
 */
enum class ProtorunType {
    TwoSide, Edge, Closed, Pair
}

/**
 * 搭子
 */
@JvmInline
@Serializable(with = ProtorunSerializer::class)
value class Protorun private constructor(private val value: Int) {
    /**
     * 搭子类型
     */
    val type: ProtorunType
        get() = ProtorunType.entries[value / 100]

    constructor(type: ProtorunType, first: Tile) : this(type.ordinal * 100 + first.code)

    /**
     * 搭子的第一张牌
     */
    val first: Tile
        get() = Tile[value % 100]

    /**
     * 搭子的第二张牌
     */
    val second: Tile
        get() = when (type) {
            ProtorunType.TwoSide -> first.advance(1)
            ProtorunType.Closed -> first.advance(2)
            ProtorunType.Edge -> first.advance(1)
            ProtorunType.Pair -> first
        }

    /**
     * 进张
     */
    val waiting: Set<Tile>
        get() = when (type) {
            ProtorunType.TwoSide -> setOf(first.advance(-1), first.advance(2))
            ProtorunType.Closed -> setOf(first.advance(1))
            ProtorunType.Edge -> if (first.num == 1)
                setOf(first.advance(2))
            else
                setOf(first.advance(-1))

            ProtorunType.Pair -> setOf(first)
        }

    /**
     * 进张后形成的面子
     */
    fun withWaiting(tile: Tile): Group {
        when (type) {
            ProtorunType.TwoSide -> {
                if (tile == first.advance(-1))
                    return Seq(tile)
                else if (tile == second.advance(1))
                    return Seq(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            ProtorunType.Closed -> {
                if (tile == first.advance(1))
                    return Seq(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            ProtorunType.Edge -> {
                if (first.num == 1 && tile == first.advance(2))
                    return Seq(first)
                if (first.num == 8 && tile == first.advance(-1))
                    return Seq(tile)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }

            ProtorunType.Pair -> {
                if (tile == first)
                    return Tri(first)
                else
                    throw IllegalArgumentException("tile $tile is not waiting")
            }
        }
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.shortName.lowercase()}"
    }

    companion object {
        /**
         * 根据给定牌构造搭子
         * @param first 第一张牌
         * @param second 第二张牌
         * @return 搭子
         */
        fun parse(first: Tile, second: Tile): Protorun {
            if (first > second)
                return parse(second, first)

            if (first == second) {
                return Pair(first)
            } else {
                if (first.type == TileType.Honour || second.type == TileType.Honour) {
                    throw IllegalArgumentException("invalid allTiles: $first $second")
                }

                when (second.distance(first)) {
                    1 -> {
                        if (first.num == 1 || first.num == 8) {
                            return Edge(first)
                        } else {
                            return TwoSide(first)
                        }
                    }

                    2 -> {
                        return Closed(first)
                    }

                    else -> {
                        throw IllegalArgumentException("invalid allTiles: $first $second")
                    }
                }
            }
        }

        /**
         * 根据给定牌的文本构造搭子
         * @param text 牌的文本
         * @return 搭子
         */
        fun parse(text: String): Protorun {
            val tiles = Tile.parseTiles(text)
            if (tiles.size != 2) {
                throw IllegalArgumentException("invalid allTiles: $text")
            }
            return parse(tiles[0], tiles[1])
        }
    }
}

fun Protorun(first: Tile, second: Tile): Protorun {
    return Protorun.parse(first, second)
}

fun Protorun(text: String): Protorun {
    return Protorun.parse(text)
}

fun TwoSide(tile: Tile) = Protorun(ProtorunType.TwoSide, tile)
fun Closed(tile: Tile) = Protorun(ProtorunType.Closed, tile)
fun Edge(tile: Tile) = Protorun(ProtorunType.Edge, tile)
fun Pair(tile: Tile) = Protorun(ProtorunType.Pair, tile)

internal class ProtorunSerializer : KSerializer<Protorun> {
    override val descriptor = PrimitiveSerialDescriptor("Protorun", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Protorun) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Protorun {
        val text = decoder.decodeString()
        return Protorun(text)
    }
}
