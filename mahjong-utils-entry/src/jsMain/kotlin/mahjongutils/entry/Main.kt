@file:OptIn(ExperimentalJsExport::class)

package mahjongutils.entry


private val ENTRY = buildEntry(DynamicParamsDecoder, DynamicResultEncoder)

@JsExport
fun call(name: String, rawParams: dynamic): dynamic {
    return ENTRY.call(name, rawParams)
}