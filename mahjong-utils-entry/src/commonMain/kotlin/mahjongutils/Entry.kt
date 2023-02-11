@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

package mahjongutils

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
interface IEntry<in RAW_PARAMS : Any, out RAW_RESULT : Any> {
    fun call(name: String, rawParams: RAW_PARAMS): RAW_RESULT
}

@Serializable
data class Result<out T : Any>(
    @EncodeDefault val data: T?,
    @EncodeDefault val code: Int = 200,
    @EncodeDefault val msg: String = "",
)

interface Method<in RAW_PARAMS : Any,out RAW_RESULT : Any> {
    fun call(rawParams: RAW_PARAMS): RAW_RESULT
}

fun interface MethodHandler<in P : Any, out R : Any> {
    fun handle(params: P): R
}

interface EntryFactory<RAW_PARAMS : Any, RAW_RESULT : Any, out E : IEntry<RAW_PARAMS, RAW_RESULT>> {
    val paramsDecoder: ParamsDecoder<RAW_PARAMS>

    val resultEncoder: ResultEncoder<RAW_RESULT>

    fun create(
        router: Map<String, Method<RAW_PARAMS, RAW_RESULT>>,
    ): E
}

internal class EntryBuilder<RAW_PARAMS : Any, RAW_RESULT : Any, E : IEntry<RAW_PARAMS, RAW_RESULT>>(
    private val factory: EntryFactory<RAW_PARAMS, RAW_RESULT, E>
) {
    val paramsDecoder = factory.paramsDecoder
    val resultEncoder = factory.resultEncoder
    val router = HashMap<String, Method<RAW_PARAMS, RAW_RESULT>>()

    inline fun <reified P : Any, reified R : Any> register(name: String, handle: MethodHandler<P, R>) {
        router[name] = object : Method<RAW_PARAMS, RAW_RESULT> {
            override fun call(rawParams: RAW_PARAMS): RAW_RESULT {
                return try {
                    val params:P = paramsDecoder.decodeParams(rawParams)
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
    }

    fun build(): E {
        return factory.create(router)
    }
}

// 为了封装kt侧的公共实现，各个平台导出时自行实现IEntry接口并委托到该类实现
internal class EntryImpl<in RAW_PARAMS : Any, out RAW_RESULT : Any> internal constructor(
    private val router: Map<String, Method<RAW_PARAMS, RAW_RESULT>>,
    private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    private val resultEncoder: ResultEncoder<RAW_RESULT>
) : IEntry<RAW_PARAMS, RAW_RESULT> {
    override fun call(name: String, rawParams: RAW_PARAMS): RAW_RESULT {
        val method = router[name]
        return if (method != null) {
            method.call(rawParams)
        } else {
            val result = Result<Unit>(data = null, code = 404, msg = "method \"$name\" not found")
            resultEncoder.encodeResult(result)
        }
    }
}
