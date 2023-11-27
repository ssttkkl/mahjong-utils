package mahjongutils.entry.coder

import kotlin.reflect.KType
import kotlin.reflect.typeOf


internal interface ParamsDecoder<in RAW_PARAMS : Any> {
    fun <PARAMS : Any> decodeParams(rawParams: RAW_PARAMS, paramsType: KType): PARAMS
}
