package mahjongutils.entry

import mahjongutils.entry.coder.JsonParamsDecoder
import mahjongutils.entry.coder.JsonResultEncoder

private val ENTRY = buildEntry(JsonParamsDecoder, JsonResultEncoder)


fun call(name: String, rawParams: String): String {
    return ENTRY.call(name, rawParams)
}
