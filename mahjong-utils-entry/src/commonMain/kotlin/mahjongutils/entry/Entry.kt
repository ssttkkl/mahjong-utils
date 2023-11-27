package mahjongutils.entry

import kotlinx.serialization.SerializationException
import mahjongutils.entry.coder.ParamsDecoder
import mahjongutils.entry.coder.ResultEncoder
import mahjongutils.entry.coder.encodeResult
import kotlin.concurrent.Volatile
import kotlin.reflect.KType
import kotlin.reflect.typeOf


// 为了封装kt侧的公共实现，各个平台导出时自行实现IEntry接口并委托到该类实现
internal class Entry<in RAW_PARAMS : Any, out RAW_RESULT : Any> internal constructor(
    private val paramsDecoder: ParamsDecoder<RAW_PARAMS>,
    private val resultEncoder: ResultEncoder<RAW_RESULT>
) {
    @Volatile
    private var router: Map<String, IMethodEntry<RAW_PARAMS, RAW_RESULT>> = emptyMap()

    private inner class MethodEntry<in P : Any, out R : Any>(
        val paramsType: KType,
        val resultType: KType,
        val handler: Method<P, R>
    ) : IMethodEntry<RAW_PARAMS, RAW_RESULT> {
        override fun call(rawParams: RAW_PARAMS): RAW_RESULT {
            return try {
                val params: P = paramsDecoder.decodeParams(rawParams, paramsType)
                val data = handler.handle(params)
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

    fun <P : Any, R : Any> register(
        name: String,
        paramsType: KType,
        resultType: KType,
        handle: Method<P, R>
    ) {
        val method = MethodEntry(paramsType, resultType, handle)
        router = router + (name to method)
    }

    inline fun <reified P : Any, reified R : Any> register(name: String, handle: Method<P, R>) {
        register(name, typeOf<P>(), typeOf<R>(), handle)
    }

    fun call(name: String, rawParams: RAW_PARAMS): RAW_RESULT {
        val method = router[name]
        return if (method != null) {
            method.call(rawParams)
        } else {
            val result = Result<Unit>(data = null, code = 404, msg = "method '$name' not found")
            resultEncoder.encodeResult(result)
        }
    }
}

