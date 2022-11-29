package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 麻将牌的种类（万、筒、索、字）
 */
enum class TileType {
    /**
     * 万
     */
    M,

    /**
     * 筒
     */
    P,

    /**
     * 索
     */
    S,

    /**
     * 字
     */
    Z
}

/**
 * 麻将牌
 */
@Serializable(with = TileSerializer::class)
data class Tile private constructor(
    /**
     * 种类
     */
    val type: TileType,
    /**
     * 数字
     */
    val num: Int
) : Comparable<Tile> {
    /**
     * 编号
     */
    val code: Int
        get() = type.ordinal * 10 + num

    /**
     * 真正数字。当num为0时（该牌为红宝牌），realName为5。其余情况下与num相等。
     */
    val realNum: Int
        get() = if (type != TileType.Z && num == 0)
            5
        else
            num

    /**
     * 该牌数字加上指定数字后得到的牌
     * @param step 加减数字
     */
    fun advance(step: Int): Tile {
        return if (num == 0) {
            get(code + 5 + step)
        } else {
            get(code + step)
        }
    }

    /**
     * 计算这张牌与另一张牌的数字之差
     * @param that 另一张牌
     */
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
        /**
         * 牌编号最大值
         */
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

        /**
         * 根据编号获取牌
         */
        fun get(code: Int): Tile {
            if (code !in pool.indices || code == 30) {
                throw IllegalArgumentException("invalid code: $code")
            }
            return pool[code]!!
        }

        /**
         * 根据种类和数字获取牌
         */
        fun get(type: TileType, num: Int): Tile {
            return get(type.ordinal * 10 + num)
        }

        /**
         * 根据文本获取牌
         */
        fun get(text: String): Tile {
            if (text.length != 2) {
                throw IllegalArgumentException("invalid tile text: $text")
            }

            val type = when (text[1].lowercaseChar()) {
                'm' -> TileType.M
                'p' -> TileType.P
                's' -> TileType.S
                'z' -> TileType.Z
                else -> throw IllegalArgumentException("invalid tile text: $text")
            }

            val num = text[0].digitToIntOrNull() ?: throw IllegalArgumentException("invalid tile text: $text")

            return get(type, num)
        }

        /**
         * 将给定的牌文本转换为牌序列
         * @param text 牌的文本
         * @return 牌的序列
         */
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

        /**
         * 所有牌
         */
        val all = pool.filterNotNull().toSet()

        /**
         * 所有牌
         */
        val allExcludeAkaDora = all.filter { it.num != 0 }

        /**
         * 所有幺九牌
         */
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

/**
 * 将牌序列转换为牌文本
 * @param lowercase 牌种类字母是否使用小写字母
 * @return 牌的文本
 */
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

fun Iterable<Tile>.groupByCode(): IntArray {
    val groups = IntArray(Tile.MAX_TILE_CODE + 1)
    this.forEach {
        groups[it.code] += 1
    }
    return groups
}

private class TileSerializer : KSerializer<Tile> {
    override val descriptor = PrimitiveSerialDescriptor("Tile", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Tile) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Tile {
        val text = decoder.decodeString()
        return Tile.get(text)
    }
}

/**
 * 是否为幺九牌
 */
val Tile.isYaochu: Boolean get() = this in Tile.allYaochu

/**
 * 是否为三元牌
 */
val Tile.isSangen: Boolean get() = type == TileType.Z && num in 5..7

/**
 * 是否为风牌
 */
val Tile.isWind: Boolean get() = type == TileType.Z && num in 1..4