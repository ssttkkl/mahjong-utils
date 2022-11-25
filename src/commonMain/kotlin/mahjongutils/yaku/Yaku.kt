package mahjongutils.yaku

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mahjongutils.models.hand.HoraHandPattern

/**
 * 用于检测和了手牌是否具有役种
 */
internal fun interface YakuChecker {
    /**
     * 检测和了手牌是否具有役种
     * @param pattern 和了手牌
     * @return 是否具有役种
     */
    fun check(pattern: HoraHandPattern): Boolean
}

/**
 * 役种
 */
@Serializable(with = YakuSerializer::class)
class Yaku internal constructor(
    /**
     * 役种名
     */
    val name: String,
    /**
     * 番数
     */
    val han: Int,
    /**
     * 副露降低多少番数
     */
    val furoLoss: Int = 0,
    /**
     * 是否为役满役种
     */
    val isYakuman: Boolean = false,
    private val checker: YakuChecker
) : YakuChecker by checker {
    /**
     * 是否为门清限定
     */
    val menzenOnly: Boolean
        get() = han == furoLoss

    override fun toString(): String {
        return name
    }
}

private class YakuSerializer : KSerializer<Yaku> {
    override val descriptor = PrimitiveSerialDescriptor("Yaku", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Yaku) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Yaku {
        val text = decoder.decodeString()
        return Yakus.getYaku(text)
    }
}