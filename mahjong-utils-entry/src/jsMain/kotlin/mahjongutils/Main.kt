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

@JsExport
class Entry internal constructor(
    router: Map<String, Method<dynamic, dynamic>>
) : IEntry<dynamic, dynamic> by EntryImpl(router, ParamsDecoderImpl, ResultEncoderImpl)


@JsExport
val ENTRY = buildEntry(object : EntryFactory<dynamic, dynamic, Entry> {
    override val paramsDecoder: ParamsDecoder<dynamic> = ParamsDecoderImpl
    override val resultEncoder: ResultEncoder<dynamic> = ResultEncoderImpl
    override fun create(router: Map<String, Method<dynamic, dynamic>>): Entry {
        return Entry(router)
    }
})