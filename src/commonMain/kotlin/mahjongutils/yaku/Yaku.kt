package mahjongutils.yaku

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mahjongutils.models.hand.HoraHandPattern

fun interface YakuChecker {
    fun check(pattern: HoraHandPattern): Boolean
}

@Serializable(with = YakuSerializer::class)
class Yaku internal constructor(
    val name: String,
    val han: Int,
    val furoLoss: Int = 0,
    val isYakuman: Boolean = false,
    private val checker: YakuChecker
) : YakuChecker by checker {
    val menzenOnly: Boolean
        get() = han == furoLoss

    override fun toString(): String {
        return name
    }
}

class YakuSerializer : KSerializer<Yaku> {
    override val descriptor = PrimitiveSerialDescriptor("Yaku", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Yaku) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Yaku {
        val text = decoder.decodeString()
        return allYaku[text] ?: throw IllegalArgumentException("$text is not a yaku")
    }
}