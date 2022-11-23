package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class TileType {
    M, P, S, Z
}

@Serializable(with = TileSerializer::class)
data class Tile private constructor(val type: TileType, val num: Int) : Comparable<Tile> {
    val code: Int
        get() = type.ordinal * 10 + num

    val realNum: Int
        get() = if (type != TileType.Z && num == 0)
            5
        else
            num

    fun advance(step: Int): Tile {
        return if (num == 0) {
            decode(code + 5 + step)
        } else {
            decode(code + step)
        }
    }

    fun distance(that: Tile): Int {
        val this_ = if (this.num == 0) Tile(type, 5) else this
        val that_ = if (that.num == 0) Tile(that.type, 5) else that
        return this_.code - that_.code
    }

    override fun toString(): String {
        return "${num}${type.name.lowercase()}"
    }

    override fun compareTo(other: Tile): Int {
        return when {
            type != other.type -> type.ordinal - other.type.ordinal
            num != 0 && other.num != 0 -> num - other.num
            num == 0 && other.num == 0 -> 0
            num == 0 -> {
                if (other.num > 5) {
                    -1
                } else {
                    1
                }
            }

            else -> {
                if (other.num <= 5) {
                    -1
                } else {
                    1
                }
            }
        }
    }

    companion object {
        const val MAX_TILE_CODE = 3 * 10 + 7

        private val pool = buildList<Tile?> {
            for (i in 0..9) {
                add(Tile(TileType.M, i))
            }
            for (i in 0..9) {
                add(Tile(TileType.P, i))
            }
            for (i in 0..9) {
                add(Tile(TileType.S, i))
            }
            add(null) // 0Z
            for (i in 1..7) {
                add(Tile(TileType.Z, i))
            }
        }.toTypedArray()

        fun get(code: Int): Tile {
            if (code !in pool.indices || code == 30) {
                throw IllegalArgumentException("invalid code: $code")
            }
            return pool[code]!!
        }

        fun get(type: TileType, num: Int): Tile {
            return get(type.ordinal * 10 + num)
        }

        fun get(text: String): Tile {
            if (text.length != 2) {
                throw IllegalArgumentException("invalid text: $text")
            }

            val type = when (text[1].lowercaseChar()) {
                'm' -> TileType.M
                'p' -> TileType.P
                's' -> TileType.S
                'z' -> TileType.Z
                else -> throw IllegalArgumentException("invalid text: $text")
            }

            val num = text[0].digitToIntOrNull() ?: throw IllegalArgumentException("invalid text: $text")

            return get(type, num)
        }

        fun parseTiles(text: String): List<Tile> {
            val typeNames = TileType.values().map { it.name }
            return buildList {
                val pending = ArrayList<Int>()
                for (c in text) {
                    if (c.uppercase() in typeNames) {
                        val type = TileType.valueOf(c.uppercase())
                        addAll(pending.map { Tile.get(type, it) })
                        pending.clear()
                    } else if (c.isDigit()) {
                        pending.add(c.digitToInt())
                    } else {
                        throw IllegalArgumentException("invalid character: $c")
                    }
                }

                if (pending.size > 0) {
                    throw IllegalArgumentException("missing tile type at the end of your given text")
                }
            }
        }

        fun decode(code: Int): Tile {
            if (code < 0 || code > MAX_TILE_CODE) {
                error("invalid code")
            }

            val type = TileType.values()[code / 10];
            val num = code % 10;
            return get(type, num)
        }

        val all = pool.filterNotNull().toSet()

        val allYaochu = buildSet<Tile> {
            for (type in listOf(TileType.M, TileType.P, TileType.S)) {
                add(get(type, 1))
                add(get(type, 9))
            }

            for (num in 1..7) {
                add(get(TileType.Z, num))
            }
        }
    }
}

fun Iterable<Tile>.toTilesString(lowercase: Boolean = true): String {
    return buildString {
        var prev: Tile? = null
        for (t in this@toTilesString) {
            if (prev != null && prev.type != t.type) {
                if (lowercase) {
                    append(prev.type.name.lowercase())
                } else {
                    append(prev.type.name)
                }
            }

            append(t.num)
            prev = t
        }

        if (prev != null) {
            if (lowercase) {
                append(prev.type.name.lowercase())
            } else {
                append(prev.type.name)
            }
        }
    }
}

class TileSerializer : KSerializer<Tile> {
    override val descriptor = PrimitiveSerialDescriptor("Tile", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Tile) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Tile {
        val text = decoder.decodeString()
        return Tile.get(text)
    }
}


val Tile.isYaochu: Boolean get() = this in Tile.allYaochu

val Tile.isSangen: Boolean get() = type == TileType.Z && num in 5..7

val Tile.isWind: Boolean get() = type == TileType.Z && num in 1..4