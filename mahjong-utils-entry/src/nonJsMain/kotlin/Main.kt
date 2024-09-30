@file:OptIn(ExperimentalJsExport::class)

import mahjongutils.entry.MethodExecutionException
import mahjongutils.entry.buildEntry
import mahjongutils.entry.coder.JsonParamsDecoder
import mahjongutils.entry.coder.JsonResultEncoder
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

internal val ENTRY = buildEntry(JsonParamsDecoder, JsonResultEncoder)


@JsExport
fun call(name: String, rawParams: String): String {
    return ENTRY.call(name, rawParams)
}

@JsExport
@Throws(MethodExecutionException::class)
fun callReceivingData(name: String, rawParams: String): String {
    return ENTRY.callReceivingData(name, rawParams)
}
