package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

enum class FuroType {
    Chi, Pon, Kan, Ankan
}

@JvmInline
@Serializable(FuroSerializer::class)
value class Furo private constructor(private val value: Int) {
    constructor(type: FuroType, tile: Tile) : this(type.ordinal * 100 + tile.code)

    val type: FuroType
        get() = FuroType.entries[value / 100]

    val tile: Tile
        get() = Tile[value % 100]

    /**
     * 获取副露的面子
     */
    fun asMentsu(): Mentsu {
        return when (type) {
            FuroType.Chi -> Shuntsu(tile)
            FuroType.Pon, FuroType.Kan, FuroType.Ankan -> Kotsu(tile)
        }
    }

    val tiles: List<Tile>
        get() = when (type) {
            FuroType.Chi -> listOf(tile, tile.advance(1), tile.advance(2))
            FuroType.Pon -> listOf(tile, tile, tile)
            FuroType.Kan, FuroType.Ankan -> listOf(tile, tile, tile, tile)
        }

    override fun toString(): String {
        return when (type) {
            FuroType.Chi -> "${tile.num}${tile.realNum + 1}${tile.realNum + 2}${tile.type.name.lowercase()}"
            FuroType.Pon -> "${tile.num}${tile.num}${tile.num}${tile.type.name.lowercase()}"
            FuroType.Kan -> "${tile.num}${tile.num}${tile.num}${tile.num}${tile.type.name.lowercase()}"
            FuroType.Ankan -> "0${tile.num}${tile.num}0${tile.type.name.lowercase()}"
        }
    }

    companion object {
        /**
         * 根据给定牌构造副露
         * @param tiles 牌
         * @param ankan 是否为暗杠
         * @return 副露
         */
        fun parse(tiles: List<Tile>, ankan: Boolean = false): Furo {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Pon(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Z }) {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Chi(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }
                }
            } else if (tiles.size == 4) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2] && tiles[2] == tiles[3]) {
                    if (!ankan) {
                        return Kan(tiles[0])
                    } else {
                        return Ankan(tiles[0])
                    }
                }
            }

            throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
        }

        /**
         * 根据给定牌构造副露
         * @param text 牌的文本
         * @param ankan 是否为暗杠
         * @return 副露
         */
        fun parse(text: String, ankan: Boolean = false): Furo {
            var ankan_ = ankan

            val tiles = if (text.length == 5 && text[0] == text[3] && text[0] == '0' && text[1] == text[2]) {
                ankan_ = true
                Tile.parseTiles("${text[1]}${text[1]}${text[1]}${text[1]}${text[4]}")
            } else {
                Tile.parseTiles(text)
            }

            return parse(tiles, ankan_)
        }
    }
}

fun Furo(tiles: List<Tile>, ankan: Boolean = false): Furo {
    return Furo.parse(tiles, ankan)
}

fun Furo(text: String, ankan: Boolean = false): Furo {
    return Furo.parse(text, ankan)
}

fun Chi(tile: Tile) = Furo(FuroType.Chi, tile)
fun Pon(tile: Tile) = Furo(FuroType.Pon, tile)
fun Kan(tile: Tile) = Furo(FuroType.Kan, tile)
fun Ankan(tile: Tile) = Furo(FuroType.Ankan, tile)

object FuroSerializer : KSerializer<Furo> {
    override val descriptor = PrimitiveSerialDescriptor("Furo", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Furo) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Furo {
        val text = decoder.decodeString()
        return Furo(text)
    }
}
