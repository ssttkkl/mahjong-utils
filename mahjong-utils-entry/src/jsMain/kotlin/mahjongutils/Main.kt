@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

package mahjongutils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import kotlinx.serialization.serializer
import kotlin.reflect.KType

private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
private object ParamsDecoderImpl : ParamsDecoder<dynamic> {
    override fun <PARAMS : Any> decodeParams(rawParams: dynamic, paramsType: KType): PARAMS {
        return json.decodeFromDynamic(serializer(paramsType), rawParams) as PARAMS
    }
}

private object ResultEncoderImpl : ResultEncoder<dynamic> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): dynamic {
        return json.encodeToDynamic(serializer(resultType), result)
    }
}


private val ENTRY = buildEntry(ParamsDecoderImpl, ResultEncoderImpl)

@JsExport
fun call(name: String, rawParams: dynamic): dynamic {
    return ENTRY.call(name, rawParams)
}