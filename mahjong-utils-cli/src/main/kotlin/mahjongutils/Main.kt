package mahjongutils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import mahjongutils.entry.Result
import mahjongutils.entry.call
import java.io.EOFException


fun main() {
    while (true) {
        var resultPrinted = false
        try {
            val rawPayload = readln()
            val obj = Json.decodeFromString<JsonObject>(rawPayload)
            val method = obj["method"]?.jsonPrimitive?.contentOrNull
                ?: throw IllegalArgumentException("method is missing")
            val params = obj["params"]
                ?: throw IllegalArgumentException("params is missing")
            val rawParams = params.toString()

            val rawResult = call(method, rawParams)
            println(rawResult)
            resultPrinted = true
        } catch (e: EOFException) {
            return
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()

            if (!resultPrinted) {
                val result = Result<Unit>(data = null, code = 400, msg = e.message ?: "")
                println(Json.encodeToString(result))
            }
        } catch (e: Exception) {
            e.printStackTrace()

            if (!resultPrinted) {
                val result = Result<Unit>(data = null, code = 500, msg = e.message ?: "")
                println(Json.encodeToString(result))
            }
        }
    }
}