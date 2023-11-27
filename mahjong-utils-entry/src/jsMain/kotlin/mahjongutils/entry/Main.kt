@file:OptIn(ExperimentalJsExport::class)

package mahjongutils.entry


internal val ENTRY = buildEntry(DynamicParamsDecoder, DynamicResultEncoder)

@JsExport
fun call(name: String, rawParams: dynamic): dynamic {
    return ENTRY.call(name, rawParams)
}

@JsExport
fun callReceivingData(name: String, rawParams: dynamic): dynamic {
    return ENTRY.callReceivingData(name, rawParams)
}
