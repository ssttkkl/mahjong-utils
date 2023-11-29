package io.ssttkkl

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import mahjongutils.entry.MethodExecutionException
import mahjongutils.entry.callReceivingData

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        post("/{method}") {
            call.response.header(HttpHeaders.ContentType, "application/json")

            val method = call.parameters["method"]!!
            val paramsText = call.receiveText()

            try {
                val data = withContext(Dispatchers.Default) {
                    callReceivingData(method, paramsText)
                }
                call.respond(data)
            } catch (e: MethodExecutionException) {
                call.respond(
                    HttpStatusCode.fromValue(e.code),
                    buildJsonObject { this.put("msg", e.message) }.toString()
                )
            }
        }
    }
}
