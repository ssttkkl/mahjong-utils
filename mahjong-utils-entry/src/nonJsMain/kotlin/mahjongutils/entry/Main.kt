package mahjongutils.entry

import mahjongutils.entry.coder.JsonParamsDecoder
import mahjongutils.entry.coder.JsonResultEncoder

internal val ENTRY = buildEntry(JsonParamsDecoder, JsonResultEncoder)


fun call(name: String, rawParams: String): String {
    return ENTRY.call(name, rawParams)
}

@Throws(MethodExecutionException::class)
fun callReceivingData(name: String, rawParams: String): String {
    return ENTRY.callReceivingData(name, rawParams)
}
