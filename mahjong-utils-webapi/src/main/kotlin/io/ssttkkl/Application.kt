package io.ssttkkl

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*
import mahjongutils.entry.call as callMahjongUtils

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        post("/{method}") {
            call.response.header(HttpHeaders.ContentType, "application/json")

            val method = call.parameters["method"]!!
            val paramsText = call.receiveText()
            val resultText = callMahjongUtils(method, paramsText)

            val result = Json.decodeFromString<JsonObject>(resultText)
            val code = result["code"]!!.jsonPrimitive.int
            val msg = result["msg"]!!.jsonPrimitive.content

            if (code == 200) {
                val data = result["data"]!!.jsonObject
                call.respond(data.toString())
            } else {
                call.respond(HttpStatusCode.fromValue(code),
                    buildJsonObject { this.put("msg", msg) }.toString())
            }
        }
    }
}
