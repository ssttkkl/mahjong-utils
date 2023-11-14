package mahjongutils

import kotlin.reflect.KType
import kotlin.reflect.typeOf


internal interface ResultEncoder<out RAW_RESULT : Any> {
    fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): RAW_RESULT
}

internal inline fun <RAW_RESULT : Any, reified RESULT : Any> ResultEncoder<RAW_RESULT>.encodeResult(
    result: Result<RESULT>
): RAW_RESULT {
    return encodeResult(result, typeOf<Result<RESULT>>())
}

internal interface ParamsDecoder<in RAW_PARAMS : Any> {
    fun <PARAMS : Any> decodeParams(rawParams: RAW_PARAMS, paramsType: KType): PARAMS
}

internal inline fun <RAW_PARAMS : Any, reified PARAMS : Any> ParamsDecoder<RAW_PARAMS>.decodeParams(
    rawParams: RAW_PARAMS
): PARAMS {
    return decodeParams(rawParams, typeOf<PARAMS>())
}
