package mahjongutils.entry.coder

import kotlin.reflect.KType
import kotlin.reflect.typeOf


internal interface ParamsDecoder<in RAW_PARAMS : Any> {
    fun <PARAMS : Any> decodeParams(rawParams: RAW_PARAMS, paramsType: KType): PARAMS
}

internal inline fun <RAW_PARAMS : Any, reified PARAMS : Any> ParamsDecoder<RAW_PARAMS>.decodeParams(
    rawParams: RAW_PARAMS
): PARAMS {
    return decodeParams(rawParams, typeOf<PARAMS>())
}
