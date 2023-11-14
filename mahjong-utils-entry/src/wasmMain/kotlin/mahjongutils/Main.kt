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

private val ENTRY = buildEntry(ParamsDecoderImpl, ResultEncoderImpl)


fun call(name: String, rawParams: String): String {
    return ENTRY.call(name, rawParams)
}