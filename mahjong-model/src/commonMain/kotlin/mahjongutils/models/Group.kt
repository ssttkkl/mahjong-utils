package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

/**
 * 面子类型
 */
enum class GroupType {
    /**
     * 顺子
     */
    Seq,
    /**
     * 刻子
     */
    Tri
}

/**
 * 面子
 */
@JvmInline
@Serializable(with = GroupSerializer::class)
value class Group private constructor(private val value: Int) {
    /**
     * 面子类型
     */
    val type: GroupType
        get() = GroupType.entries[value / 100]

    /**
     * 面子的第一张牌
     */
    val tile: Tile
        get() = Tile[value % 100]

    constructor(type: GroupType, tile: Tile) : this(type.ordinal * 100 + tile.code)

    /**
     * 所含的牌
     */
    val tiles: Iterable<Tile>
        get() = when (type) {
            GroupType.Seq -> listOf(tile, tile.advance(1), tile.advance(2))
            GroupType.Tri -> listOf(tile, tile, tile)
        }

    /**
     * 舍牌后形成的搭子
     */
    fun afterDiscard(discard: Tile): Protorun {
        return when (type) {
            GroupType.Seq -> {
                if (discard == tile) {
                    if (discard.num == 7)
                        return Edge(discard.advance(1))
                    else
                        return TwoSide(discard.advance(1))
                } else if (discard == tile.advance(1))
                    return Closed(tile)
                else if (discard == tile.advance(2)) {
                    if (tile.num == 1)
                        return Edge(tile)
                    else
                        return TwoSide(tile)
                }
                throw IllegalArgumentException("invalid discard: $discard")
            }

            GroupType.Tri -> {
                if (discard == tile) {
                    return Pair(discard)
                }
                throw IllegalArgumentException("invalid discard: $discard")

            }
        }
    }

    override fun toString(): String {
        return when (type) {
            GroupType.Seq -> "${tile.num}${tile.num + 1}${tile.num + 2}${tile.type.shortName.lowercase()}"
            GroupType.Tri -> "${tile.num}${tile.num}${tile.num}${tile.type.shortName.lowercase()}"
        }
    }

    companion object {
        /**
         * 根据给定牌构造面子
         * @param tiles 牌
         * @return 面子
         */
        fun parse(tiles: List<Tile>): Group {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Tri(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Honour }) {
                        throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Seq(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
                    }
                }
            }

            throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
        }

        /**
         * 根据给定牌构造面子
         * @param text 牌的文本
         * @return 面子
         */
        fun parse(text: String): Group {
            return parse(Tile.parseTiles(text))
        }
    }
}

fun Group(tiles: List<Tile>): Group {
    return Group.parse(tiles)
}

fun Group(text: String): Group {
    return Group.parse(text)
}

fun Seq(tile: Tile) = Group(GroupType.Seq, tile)

fun Tri(tile: Tile) = Group(GroupType.Tri, tile)

internal class GroupSerializer : KSerializer<Group> {
    override val descriptor = PrimitiveSerialDescriptor("Group", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Group) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Group {
        val text = decoder.decodeString()
        return Group(text)
    }
}
