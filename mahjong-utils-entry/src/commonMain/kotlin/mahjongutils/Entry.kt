@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException


@Serializable
data class Result<out T : Any>(
    @EncodeDefault val data: T?,
    @EncodeDefault val code: Int = 200,
    @EncodeDefault val msg: String = "",
)

internal fun interface Method<in RAW_PARAMS : Any, out RAW_RESULT : Any> {
    fun call(rawParams: RAW_PARAMS): RAW_RESULT
}

internal fun interface MethodHandler<in P : Any, out R : Any> {
    fun handle(params: P): R
}


// 为了封装kt侧的公共实现，各个平台导出时自行实现IEntry接口并委托到该类实现
internal class Entry<in RAW_PARAMS : Any, out RAW_RESULT : Any> internal constructor(
    private val router: Map<String, Method<RAW_PARAMS, RAW_RESULT>>,
    private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    private val resultEncoder: ResultEncoder<RAW_RESULT>
) {
    fun call(name: String, rawParams: RAW_PARAMS): RAW_RESULT {
        val method = router[name]
        return if (method != null) {
            method.call(rawParams)
        } else {
            val result = Result<Unit>(data = null, code = 404, msg = "method \"$name\" not found")
            resultEncoder.encodeResult(result)
        }
    }
}

internal class EntryBuilder<RAW_PARAMS : Any, RAW_RESULT : Any>(
    private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    private val resultEncoder: ResultEncoder<RAW_RESULT>
) {
    val router = HashMap<String, Method<RAW_PARAMS, RAW_RESULT>>()

    inline fun <reified P : Any, reified R : Any> register(name: String, handle: MethodHandler<P, R>) {
        router[name] = Method { rawParams ->
            try {
                val params: P = paramsDecoder.decodeParams(rawParams)
                val data = handle.handle(params)
                val result = Result(data)
                resultEncoder.encodeResult(result)
            } catch (e: SerializationException) {
                val result = Result<R>(data = null, code = 400, msg = e.message ?: "")
                resultEncoder.encodeResult(result)
            } catch (e: IllegalArgumentException) {
                val result = Result<R>(data = null, code = 400, msg = e.message ?: "")
                resultEncoder.encodeResult(result)
            } catch (e: Exception) {
                e.printStackTrace()
                val result = Result<R>(data = null, code = 500, msg = e.message ?: "")
                resultEncoder.encodeResult(result)
            }
        }
    }

    fun build(): Entry<RAW_PARAMS, RAW_RESULT> {
        return Entry(router, paramsDecoder, resultEncoder)
    }
}
