package mahjongutils.entry.coder

import kotlin.reflect.KType
import mahjongutils.entry.Result

interface ResultEncoder<out RAW_RESULT : Any> {
    fun <DATA : Any> encodeData(result: DATA, dataType: KType): RAW_RESULT
    fun <DATA : Any> encodeResult(result: Result<DATA>, dataType: KType): RAW_RESULT
}
