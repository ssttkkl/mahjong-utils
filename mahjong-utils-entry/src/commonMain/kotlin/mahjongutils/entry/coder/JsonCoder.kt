@file:OptIn(ExperimentalSerializationApi::class)

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
    override fun <DATA : Any> encodeData(result: DATA, dataType: KType): String {
        return json.encodeToString(serializer(dataType), result)
    }

    override fun <DATA : Any> encodeResult(result: Result<DATA>, dataType: KType): String {
        return json.encodeToString(
            serializer(Result::class, listOf(serializer(dataType)), false),
            result
        )
    }
}
