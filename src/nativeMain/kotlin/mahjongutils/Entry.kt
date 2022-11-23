@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
data class Result<T : Any>(
    @EncodeDefault val data: T?,
    @EncodeDefault val code: Int = 200,
    @EncodeDefault val msg: String = "",
)

class Entry private constructor(private val router: Map<String, Method<*, *>>) {
    class Method<P : Any, R : Any> internal constructor(
        val handle: (P) -> R,
        private val paramsType: KType,
        private val resultType: KType
    ) {
        companion object {
            internal inline operator fun <reified P : Any, reified R : Any> invoke(noinline handle: (P) -> R): Method<P, R> {
                return Method(handle, typeOf<P>(), typeOf<Result<R>>())
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun call(rawParams: String): String {
            return try {
                val params = Json.decodeFromString(serializer(paramsType), rawParams) as P
                val data = handle(params)
                val result = Result(data)
                Json.encodeToString(serializer(resultType), result)
            } catch (e: SerializationException) {
                val result = Result<Unit>(data = null, code = 400, msg = e.message ?: "")
                Json.encodeToString(result)
            } catch (e: IllegalArgumentException) {
                val result = Result<Unit>(data = null, code = 400, msg = e.message ?: "")
                Json.encodeToString(result)
            } catch (e: Exception) {
                e.printStackTrace()
                val result = Result<Unit>(data = null, code = 500, msg = e.message ?: "")
                Json.encodeToString(result)
            }
        }
    }

    internal class Builder {
        private val router = HashMap<String, Method<*, *>>()

        inline fun <reified P : Any, reified R : Any> register(name: String, noinline handle: (P) -> R) {
            router[name] = Method(handle)
        }

        fun build(): Entry {
            return Entry(router)
        }
    }

    fun call(name: String, rawParams: String): String {
        val method = router[name]
        return if (method != null) {
            method.call(rawParams)
        } else {
            val result = Result<Unit>(data = null, code = 404, msg = "method \"$name\" not found")
            Json.encodeToString(result)
        }
    }
}

@Serializable
data class ShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = listOf(),
    val calcAdvanceNum: Boolean = true
)

@Serializable
data class HanHu(
    val han: Int,
    val hu: Int
)

val ENTRY = Entry.Builder().apply {
    register<ShantenArgs, ShantenResult>("shanten") { args ->
        shanten(args.tiles, args.furo, args.calcAdvanceNum)
    }
    register<ShantenArgs, ShantenResult>("regularShanten") { args ->
        regularShanten(args.tiles, args.furo, args.calcAdvanceNum)
    }
    register<ShantenArgs, ShantenResult>("chitoiShanten") { args ->
        chitoiShanten(args.tiles, args.calcAdvanceNum)
    }
    register<ShantenArgs, ShantenResult>("kokushiShanten") { args ->
        kokushiShanten(args.tiles, args.calcAdvanceNum)
    }

    register<HanHu, ParentPoint>("getParentPointByHanHu") { args ->
        getParentPointByHanHu(args.han, args.hu)
    }
    register<HanHu, ChildPoint>("getChildPointByHanHu") { args ->
        getChildPointByHanHu(args.han, args.hu)
    }
}.build()