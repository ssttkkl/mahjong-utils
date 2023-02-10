package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import mahjongutils.shanten.*
import kotlin.reflect.KType

private val json = Json { ignoreUnknownKeys = true }

@Suppress("UNCHECKED_CAST")
private object ParamsDecoderImpl : ParamsDecoder<String> {
    override fun <PARAMS : Any> decodeParams(rawParams: String, paramsType: KType): PARAMS {
        return json.decodeFromString(serializer(paramsType), rawParams) as PARAMS
    }
}

private object ResultEncoderImpl : ResultEncoder<String> {
    override fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): String {
        return json.encodeToString(serializer(resultType), result)
    }
}

class Entry internal constructor(
    router: Map<String, Method<String, String>>
) : IEntry<String, String> by EntryImpl(router, ParamsDecoderImpl, ResultEncoderImpl)

val ENTRY = buildEntry(object : EntryFactory<String, String, Entry> {
    override val paramsDecoder: ParamsDecoder<String> = ParamsDecoderImpl
    override val resultEncoder: ResultEncoder<String> = ResultEncoderImpl
    override fun create(router: Map<String, Method<String, String>>): Entry {
        return Entry(router)
    }
})