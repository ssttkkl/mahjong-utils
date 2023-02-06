package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import mahjongutils.shanten.*
import kotlin.reflect.KType

private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
private object JsonParamsDecoder : ParamsDecoder<String> {
    override fun <PARAMS : Any> decodeParams(rawParams: String, paramsType: KType): PARAMS {
        return json.decodeFromString(serializer(paramsType), rawParams) as PARAMS
    }
}

private object JsonResultEncoder : ResultEncoder<String> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): String {
        return json.encodeToString(serializer(resultType), result)
    }
}

class Entry(
    router: Map<String, Method<String, String>>
) : IEntry<String, String> by EntryImpl(router, JsonParamsDecoder, JsonResultEncoder)

val ENTRY = buildEntry(object : EntryFactory<String, String, Entry> {
    override val paramsDecoder: ParamsDecoder<String> = JsonParamsDecoder
    override val resultEncoder: ResultEncoder<String> = JsonResultEncoder
    override fun create(router: Map<String, Method<String, String>>): Entry {
        return Entry(router)
    }
})