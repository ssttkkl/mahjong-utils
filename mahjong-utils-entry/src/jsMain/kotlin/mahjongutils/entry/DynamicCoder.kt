@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.entry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import kotlinx.serialization.serializer
import mahjongutils.entry.coder.ParamsDecoder
import mahjongutils.entry.coder.ResultEncoder
import kotlin.reflect.KType


private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
internal object DynamicParamsDecoder : ParamsDecoder<dynamic> {
    override fun <PARAMS : Any> decodeParams(rawParams: dynamic, paramsType: KType): PARAMS {
        return json.decodeFromDynamic(serializer(paramsType), rawParams) as PARAMS
    }
}

internal object DynamicResultEncoder : ResultEncoder<dynamic> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): dynamic {
        return json.encodeToDynamic(
            serializer(Result::class, listOf(serializer(resultType)), false),
            result
        )
    }
}