package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

/**
 * 副露类型
 */
enum class MeldedType {
    /**
     * 吃
     */
    Chow,
    /**
     * 碰
     */
    Pung,
    /**
     * 明杠（包括大明杠和加杠）
     */
    MeldedKong,
    /**
     * 暗杠
     */
    ConcealedKong
}

/**
 * 副露
 */
@JvmInline
@Serializable(MeldedSerializer::class)
value class Melded private constructor(private val value: Int) {
    constructor(type: MeldedType, tile: Tile) : this(type.ordinal * 100 + tile.code)

    /**
     * 副露类型
     */
    val type: MeldedType
        get() = MeldedType.entries[value / 100]

    /**
     * 副露的第一张牌
     */
    val tile: Tile
        get() = Tile[value % 100]

    /**
     * 获取副露的面子
     */
    fun asGroup(): Group {
        return when (type) {
            MeldedType.Chow -> Seq(tile)
            MeldedType.Pung, MeldedType.MeldedKong, MeldedType.ConcealedKong -> Tri(tile)
        }
    }

    val tiles: List<Tile>
        get() = when (type) {
            MeldedType.Chow -> listOf(tile, tile.advance(1), tile.advance(2))
            MeldedType.Pung -> listOf(tile, tile, tile)
            MeldedType.MeldedKong, MeldedType.ConcealedKong -> listOf(tile, tile, tile, tile)
        }

    override fun toString(): String {
        return when (type) {
            MeldedType.Chow -> "${tile.num}${tile.num + 1}${tile.num + 2}${tile.type.shortName.lowercase()}"
            MeldedType.Pung -> "${tile.num}${tile.num}${tile.num}${tile.type.shortName.lowercase()}"
            MeldedType.MeldedKong -> "${tile.num}${tile.num}${tile.num}${tile.num}${tile.type.shortName.lowercase()}"
            MeldedType.ConcealedKong -> "0${tile.num}${tile.num}0${tile.type.shortName.lowercase()}"
        }
    }

    companion object {
        /**
         * 根据给定牌构造副露
         * @param tiles 牌
         * @param ankan 是否为暗杠
         * @return 副露
         */
        fun parse(tiles: List<Tile>, ankan: Boolean = false): Melded {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Pung(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Honour }) {
                        throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Chow(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
                    }
                }
            } else if (tiles.size == 4) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2] && tiles[2] == tiles[3]) {
                    if (!ankan) {
                        return MeldedKong(tiles[0])
                    } else {
                        return ConcealedKong(tiles[0])
                    }
                }
            }

            throw IllegalArgumentException("invalid allTiles: ${tiles.toTilesString()}")
        }

        /**
         * 根据给定牌构造副露
         * @param text 牌的文本
         * @param concealedKong 是否为暗杠
         * @return 副露
         */
        fun parse(text: String, concealedKong: Boolean = false): Melded {
            var concealedKong_ = concealedKong

            val tiles = if (text.length == 5 && text[0] == text[3] && text[0] == '0' && text[1] == text[2]) {
                concealedKong_ = true
                Tile.parseTiles("${text[1]}${text[1]}${text[1]}${text[1]}${text[4]}")
            } else {
                Tile.parseTiles(text)
            }

            return parse(tiles, concealedKong_)
        }
    }
}

fun Melded(tiles: List<Tile>, concealedKong: Boolean = false): Melded {
    return Melded.parse(tiles, concealedKong)
}

fun Melded(text: String, concealedKong: Boolean = false): Melded {
    return Melded.parse(text, concealedKong)
}

fun Chow(tile: Tile) = Melded(MeldedType.Chow, tile)
fun Pung(tile: Tile) = Melded(MeldedType.Pung, tile)
fun MeldedKong(tile: Tile) = Melded(MeldedType.MeldedKong, tile)
fun ConcealedKong(tile: Tile) = Melded(MeldedType.ConcealedKong, tile)

object MeldedSerializer : KSerializer<Melded> {
    override val descriptor = PrimitiveSerialDescriptor("Melded", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Melded) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Melded {
        val text = decoder.decodeString()
        return Melded(text)
    }
}
