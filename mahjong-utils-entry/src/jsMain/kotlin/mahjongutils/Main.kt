package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import mahjongutils.shanten.*
import kotlin.reflect.KType

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
@Suppress("UNCHECKED_CAST")
private object DynamicParamsDecoder : ParamsDecoder<dynamic> {
    override fun <PARAMS : Any> decodeParams(rawParams: dynamic, paramsType: KType): PARAMS {
        return json.decodeFromDynamic(serializer(paramsType), rawParams) as PARAMS
    }
}

@OptIn(ExperimentalSerializationApi::class)
private object DynamicResultEncoder : ResultEncoder<dynamic> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): dynamic {
        return json.encodeToDynamic(serializer(resultType), result)
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
val ENTRY = buildEntry(DynamicParamsDecoder, DynamicResultEncoder)