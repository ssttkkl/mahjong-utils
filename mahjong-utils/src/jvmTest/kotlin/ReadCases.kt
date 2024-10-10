import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified M : Any> readCases(folder: String) = sequence<M> {
    val dir = File("../test-cases").resolve(folder).canonicalFile
    println("case folder: ${dir}")
    dir.walkTopDown().filter { it.isFile && it.extension == "json" }
        .forEach {
            it.inputStream().use { stream ->
                yieldAll(Json.decodeFromStream<List<M>>(stream))
            }
        }
}