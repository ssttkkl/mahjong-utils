@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import mahjongutils.shanten.*
import kotlin.reflect.KType

private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
private object DynamicParamsDecoder : ParamsDecoder<dynamic> {
    override fun <PARAMS : Any> decodeParams(rawParams: dynamic, paramsType: KType): PARAMS {
        return json.decodeFromDynamic(serializer(paramsType), rawParams) as PARAMS
    }
}

private object DynamicResultEncoder : ResultEncoder<dynamic> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): dynamic {
        return json.encodeToDynamic(serializer(resultType), result)
    }
}

@JsExport
class Entry internal constructor(
    router: Map<String, Method<dynamic, dynamic>>
) : IEntry<dynamic, dynamic> by EntryImpl(router, DynamicParamsDecoder, DynamicResultEncoder)

@JsExport
val ENTRY = buildEntry(object : EntryFactory<dynamic, dynamic, Entry> {
    override val paramsDecoder: ParamsDecoder<dynamic> = DynamicParamsDecoder
    override val resultEncoder: ResultEncoder<dynamic> = DynamicResultEncoder
    override fun create(router: Map<String, Method<dynamic, dynamic>>): Entry {
        return Entry(router)
    }
})