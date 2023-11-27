package mahjongutils.entry.coder

import mahjongutils.entry.Result
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