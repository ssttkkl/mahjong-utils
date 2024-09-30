package mahjongutils.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

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
    Z;

    companion object {
        fun valueOf(ordinal: Int): TileType {
            return when (ordinal) {
                0 -> M
                1 -> P
                2 -> S
                3 -> Z
                else -> throw IllegalArgumentException("invalid ordinal value: $ordinal")
            }
        }
    }
}

/**
 * 麻将牌
 */
@JvmInline
@Serializable(with = TileSerializer::class)
value class Tile private constructor(
    val code: Int
) : Comparable<Tile> {

    /**
     * 种类
     */
    val type: TileType
        get() = TileType.valueOf(code / 10)

    /**
     * 数字
     */
    val num: Int
        get() = code % 10

    constructor(type: TileType, num: Int) : this(type.ordinal * 10 + num)

    /**
     * 真正数字。当num为0时（该牌为红宝牌），realNum为5。其余情况下与num相等。
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
                if (num <= 5) {
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
        operator fun get(code: Int): Tile {
            return getOrNull(code) ?: throw IllegalArgumentException("invalid code: $code")
        }

        fun getOrNull(code: Int): Tile? {
            if (code !in pool.indices) {
                return null
            }
            return pool[code]!!
        }

        /**
         * 根据种类和数字获取牌
         */
        fun get(type: TileType, num: Int): Tile {
            return get(type.ordinal * 10 + num)
        }

        fun getOrNull(type: TileType, num: Int): Tile? {
            return getOrNull(type.ordinal * 10 + num)
        }

        /**
         * 根据文本获取牌
         */
        operator fun get(text: String): Tile {
            return getOrNull(text) ?: throw IllegalArgumentException("invalid tile text: $text")
        }

        fun getOrNull(text: String): Tile? {
            if (text.length != 2) {
                return null
            }

            val type = when (text[1].lowercaseChar()) {
                'm' -> TileType.M
                'p' -> TileType.P
                's' -> TileType.S
                'z' -> TileType.Z
                else -> return null
            }

            val num = text[0].digitToIntOrNull() ?: return null

            return getOrNull(type, num)
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
                        if (pending.isEmpty()) {
                            throw IllegalArgumentException("invalid text: $text")
                        }
                        addAll(pending.map { Tile.get(type, it) })
                        pending.clear()
                    } else if (c.isDigit()) {
                        pending.add(c.digitToInt())
                    } else {
                        throw IllegalArgumentException("invalid text: $text")
                    }
                }

                if (pending.size > 0) {
                    throw IllegalArgumentException("invalid text: $text")
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
        val allExcludeAkaDora = all.filter { it.num != 0 }.toSet()

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

fun Iterable<Tile>.countAsMap(): Map<Tile, Int> {
    return buildMap {
        this@countAsMap.forEach {
            this[it] = (this[it] ?: 0) + 1
        }
    }
}

fun Iterable<Tile>.countAsCodeArray(): IntArray {
    val groups = IntArray(Tile.MAX_TILE_CODE + 1)
    this.forEach {
        groups[it.code] += 1
    }
    return groups
}

internal class TileSerializer : KSerializer<Tile> {
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