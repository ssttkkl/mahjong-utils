package mahjongutils.entry.coder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import mahjongutils.entry.Result
import kotlin.reflect.KType


private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
internal object JsonParamsDecoder : ParamsDecoder<String> {
    override fun <PARAMS : Any> decodeParams(rawParams: String, paramsType: KType): PARAMS {
        return json.decodeFromString(serializer(paramsType), rawParams) as PARAMS
    }
}

internal object JsonResultEncoder : ResultEncoder<String> {
    @OptIn(ExperimentalSerializationApi::class)
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): String {
        return json.encodeToString(
            serializer(Result::class, listOf(serializer(resultType)), false),
            result
        )
    }
}
