package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 搭子
 */
@Serializable(with = TatsuSerializer::class)
sealed interface Tatsu {
    /**
     * 第一张牌
     */
    val first: Tile

    /**
     * 第二张牌
     */
    val second: Tile

    /**
     * 进张
     */
    val waiting: Set<Tile>

    /**
     * 进张后形成的面子
     */
    fun withWaiting(tile: Tile): Mentsu

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

        operator fun invoke(first: Tile, second: Tile): Tatsu {
            return parse(first, second)
        }

        operator fun invoke(text: String): Tatsu {
            return parse(text)
        }
    }
}

/**
 * 两面
 */
@Serializable
@SerialName("Ryanmen")
data class Ryanmen(override val first: Tile) : Tatsu {
    init {
        require(first.num in 2..7)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(1)

    override val waiting: Set<Tile>
        get() = setOf(first.advance(-1), first.advance(2))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (tile == first.advance(-1))
            return Shuntsu(tile)
        else if (tile == second.advance(1))
            return Shuntsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

/**
 * 坎张
 */
@Serializable
@SerialName("Kanchan")
data class Kanchan(override val first: Tile) : Tatsu {
    init {
        require(first.num in 1..7)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(2)

    override val waiting: Set<Tile>
        get() = setOf(first.advance(1))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (tile == first.advance(1))
            return Shuntsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

/**
 * 边张
 */
@Serializable
@SerialName("Penchan")
data class Penchan(override val first: Tile) : Tatsu {
    init {
        require(first.num == 1 || first.num == 8)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(1)

    override val waiting: Set<Tile>
        get() = if (first.num == 1)
            setOf(first.advance(2))
        else
            setOf(first.advance(-1))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (first.num == 1 && tile == first.advance(2))
            return Shuntsu(first)
        if (first.num == 8 && tile == first.advance(-1))
            return Shuntsu(tile)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

/**
 * 对子
 */
@Serializable
@SerialName("Toitsu")
data class Toitsu(override val first: Tile) : Tatsu {
    override val second: Tile
        get() = first

    override val waiting: Set<Tile>
        get() = setOf(first)

    override fun withWaiting(tile: Tile): Kotsu {
        if (tile == first)
            return Kotsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

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
