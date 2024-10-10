package mahjongutils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mahjongutils.hora.HoraArgsErrorInfo
import mahjongutils.shanten.CommonShantenArgsErrorInfo
import mahjongutils.shanten.FuroChanceShantenArgsErrorInfo

@Serializable(ErrorInfoSerializer::class)
interface ErrorInfo {
    val name: String
    val message: String
}

object ErrorInfoSerializer : KSerializer<ErrorInfo> {
    @Serializable
    private data class Structure(
        val type: String,
        val name: String
    )

    override val descriptor: SerialDescriptor
        get() = Structure.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ErrorInfo) {
        Structure.serializer().serialize(encoder, Structure(value::class.simpleName!!, value.name))
    }

    override fun deserialize(decoder: Decoder): ErrorInfo {
        return Structure.serializer().deserialize(decoder).let { structure ->
            when (structure.type) {
                "CommonShantenArgsErrorInfo" -> CommonShantenArgsErrorInfo.entries
                "FuroChanceShantenArgsErrorInfo" -> FuroChanceShantenArgsErrorInfo.entries
                "HoraArgsErrorInfo" -> HoraArgsErrorInfo.entries
                else -> error("unknown type: ${structure.type}")
            }.first { it.name == structure.name }
        }
    }
}


open class ValidationException(
    val errors: Collection<ErrorInfo>
) : IllegalArgumentException() {
    override val message: String = errors.joinToString("; ") { it.message }
}