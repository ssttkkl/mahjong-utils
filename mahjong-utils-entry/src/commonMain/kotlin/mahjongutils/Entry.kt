@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils

import kotlinx.serialization.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
data class Result<T : Any>(
    @EncodeDefault val data: T?,
    @EncodeDefault val code: Int = 200,
    @EncodeDefault val msg: String = "",
)

interface Method<RAW_PARAMS : Any, RAW_RESULT : Any> {
    fun call(rawParams: RAW_PARAMS): RAW_RESULT
}

fun interface MethodHandler<P : Any, R : Any> {
    fun handle(params: P): R
}

interface ResultEncoder<RAW_RESULT : Any> {
    fun <RESULT : Any> encodeResult(result: Result<RESULT>, resultType: KType): RAW_RESULT
}

interface ParamsDecoder<RAW_PARAMS : Any> {
    fun <PARAMS : Any> decodeParams(rawParams: RAW_PARAMS, paramsType: KType): PARAMS
}

class Entry<RAW_PARAMS : Any, RAW_RESULT : Any> private constructor(
    private val router: Map<String, Method<RAW_PARAMS, RAW_RESULT>>,
    private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    private val resultEncoder: ResultEncoder<RAW_RESULT>
) {

    internal class Builder<RAW_PARAMS : Any, RAW_RESULT : Any>(
        private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
        private val resultEncoder: ResultEncoder<RAW_RESULT>
    ) {
        val router = HashMap<String, Method<RAW_PARAMS, RAW_RESULT>>()

        inline fun <reified P : Any, reified R : Any> register(name: String, handle: MethodHandler<P, R>) {
            router[name] = object : Method<RAW_PARAMS, RAW_RESULT> {
                override fun call(rawParams: RAW_PARAMS): RAW_RESULT {
                    val paramsType = typeOf<P>()
                    val resultType = typeOf<Result<R>>()
                    return try {
                        val params = paramsDecoder.decodeParams<P>(rawParams, paramsType)
                        val data = handle.handle(params)
                        val result = Result(data)
                        resultEncoder.encodeResult(result, resultType)
                    } catch (e: SerializationException) {
                        val result = Result<R>(data = null, code = 400, msg = e.message ?: "")
                        resultEncoder.encodeResult(result, resultType)
                    } catch (e: IllegalArgumentException) {
                        val result = Result<R>(data = null, code = 400, msg = e.message ?: "")
                        resultEncoder.encodeResult(result, resultType)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val result = Result<R>(data = null, code = 500, msg = e.message ?: "")
                        resultEncoder.encodeResult(result, resultType)
                    }
                }
            }
        }

        fun build(): Entry<RAW_PARAMS, RAW_RESULT> {
            return Entry(router, paramsDecoder, resultEncoder)
        }
    }

    fun call(name: String, rawParams: RAW_PARAMS): RAW_RESULT {
        val method = router[name]
        return if (method != null) {
            method.call(rawParams)
        } else {
            val result = Result<Unit>(data = null, code = 404, msg = "method \"$name\" not found")
            resultEncoder.encodeResult(result, typeOf<Unit>())
        }
    }
}