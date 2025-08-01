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
enum class TileType(val shortName: String) {
    /**
     * 万
     */
    Character("M"),

    /**
     * 筒
     */
    Dot("P"),

    /**
     * 条
     */
    Bamboo("S"),

    /**
     * 字
     */
    Honour("Z");

    companion object {
        fun valueOf(ordinal: Int): TileType {
            return when (ordinal) {
                0 -> Character
                1 -> Dot
                2 -> Bamboo
                3 -> Honour
                else -> throw IllegalArgumentException("invalid ordinal value: $ordinal")
            }
        }

        fun valueOfShortName(shortName: String): TileType {
            return when (shortName.uppercase()) {
                "M" -> Character
                "P" -> Dot
                "S" -> Bamboo
                "Z" -> Honour
                else -> throw IllegalArgumentException("invalid short name: $shortName")
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

    private constructor(type: TileType, num: Int) : this(type.ordinal * 10 + num)

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
        return "${num}${type.shortName.lowercase()}"
    }

    override fun compareTo(other: Tile): Int {
        return when {
            type != other.type -> type.ordinal - other.type.ordinal
            else -> num - other.num
        }
    }

    companion object {
        /**
         * 牌编号最大值
         */
        const val MAX_TILE_CODE = 3 * 10 + 7

        private val pool = buildList<Tile?> {
            add(null) // 0M
            for (i in 1..9) {
                add(Tile(TileType.Character, i))
            }
            add(null) // 0P
            for (i in 1..9) {
                add(Tile(TileType.Dot, i))
            }
            add(null) // 0S
            for (i in 1..9) {
                add(Tile(TileType.Bamboo, i))
            }
            add(null) // 0Z
            for (i in 1..7) {
                add(Tile(TileType.Honour, i))
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
            return pool[code]
        }

        /**
         * 根据种类和数字获取牌
         */
        operator fun get(type: TileType, num: Int): Tile {
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
                'm' -> TileType.Character
                'p' -> TileType.Dot
                's' -> TileType.Bamboo
                'z' -> TileType.Honour
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
            val typeNames = TileType.entries.map { it.shortName }
            return buildList {
                val pending = ArrayList<Int>()
                for (c in text) {
                    if (c.uppercase() in typeNames) {
                        val type = TileType.valueOfShortName(c.uppercase())
                        if (pending.isEmpty()) {
                            throw IllegalArgumentException("invalid text: $text")
                        }
                        addAll(pending.map { Tile[type, it] })
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
         * 所有万子
         */
        val allCharacters = buildSet<Tile> {
            for (num in 1..9) {
                add(get(TileType.Character, num))
            }
        }

        /**
         * 所有筒子
         */
        val allDots = buildSet<Tile> {
            for (num in 1..9) {
                add(get(TileType.Dot, num))
            }
        }

        /**
         * 所有索子
         */
        val allBamboos = buildSet<Tile> {
            for (num in 1..9) {
                add(get(TileType.Bamboo, num))
            }
        }

        /**
         * 所有字牌
         */
        val allHonors = buildSet<Tile> {
            for (num in 1..7) {
                add(get(TileType.Honour, num))
            }
        }

        /**
         * 所有风牌
         */
        val allWinds = buildSet<Tile> {
            for (num in 1..4) {
                add(get(TileType.Honour, num))
            }
        }

        /**
         * 所有箭牌
         */
        val allDragons = buildSet<Tile> {
            for (num in 5..7) {
                add(get(TileType.Honour, num))
            }
        }

        /**
         * 所有幺九牌
         */
        val allTerminalsAndHonors = buildSet<Tile> {
            for (type in listOf(TileType.Character, TileType.Dot, TileType.Bamboo)) {
                add(get(type, 1))
                add(get(type, 9))
            }

            for (num in 1..7) {
                add(get(TileType.Honour, num))
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
                    append(prev.type.shortName.lowercase())
                } else {
                    append(prev.type.shortName)
                }
            }

            append(t.num)
            prev = t
        }

        if (prev != null) {
            if (lowercase) {
                append(prev.type.shortName.lowercase())
            } else {
                append(prev.type.shortName)
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
val Tile.isYaochu: Boolean get() = this in Tile.allTerminalsAndHonors

/**
 * 是否为三元牌
 */
val Tile.isSangen: Boolean get() = type == TileType.Honour && num in 5..7

/**
 * 是否为风牌
 */
val Tile.isWind: Boolean get() = type == TileType.Honour && num in 1..4