@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.hora.Hora
import mahjongutils.hora.hora
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.shanten.*
import mahjongutils.yaku.Yaku
import kotlin.reflect.KType
import kotlin.reflect.typeOf

private val json = Json { ignoreUnknownKeys = true }

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
                val params = json.decodeFromString(serializer(paramsType), rawParams) as P
                val data = handle(params)
                val result = Result(data)
                json.encodeToString(serializer(resultType), result)
            } catch (e: SerializationException) {
                val result = Result<Unit>(data = null, code = 400, msg = e.message ?: "")
                json.encodeToString(result)
            } catch (e: IllegalArgumentException) {
                val result = Result<Unit>(data = null, code = 400, msg = e.message ?: "")
                json.encodeToString(result)
            } catch (e: Exception) {
                e.printStackTrace()
                val result = Result<Unit>(data = null, code = 500, msg = e.message ?: "")
                json.encodeToString(result)
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
            json.encodeToString(result)
        }
    }
}

@Serializable
data class ShantenArgs(
    val tiles: List<Tile>,
    val furo: List<Furo> = listOf(),
    val calcAdvanceNum: Boolean = true,
    val bestShantenOnly: Boolean = false,
)

@Serializable
data class FuroChanceShantenArgs(
    val tiles: List<Tile>,
    val chanceTile: Tile,
    val allowChi: Boolean = true,
    val calcAdvanceNum: Boolean = true,
    val bestShantenOnly: Boolean = false,
)

@Serializable
data class HanHu(
    val han: Int,
    val hu: Int
)


@Serializable
data class HoraArgs(
    val tiles: List<Tile>? = null,
    val furo: List<Furo>? = null,
    val shantenResult: ShantenResult? = null,
    val agari: Tile,
    val tsumo: Boolean,
    val dora: Int = 0,
    val selfWind: Wind? = null,
    val roundWind: Wind? = null,
    val extraYaku: Set<Yaku> = emptySet()
)

val ENTRY = Entry.Builder().apply {
    register<ShantenArgs, ShantenResult>("shanten") { args ->
        shanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly)
    }
    register<ShantenArgs, ShantenResult>("regularShanten") { args ->
        regularShanten(args.tiles, args.furo, args.calcAdvanceNum, args.bestShantenOnly)
    }
    register<ShantenArgs, ShantenResult>("chitoiShanten") { args ->
        chitoiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
    }
    register<ShantenArgs, ShantenResult>("kokushiShanten") { args ->
        kokushiShanten(args.tiles, args.calcAdvanceNum, args.bestShantenOnly)
    }
    register<FuroChanceShantenArgs, ShantenResult>("furoChanceShanten") { args ->
        furoChanceShanten(args.tiles, args.chanceTile, args.allowChi, args.calcAdvanceNum, args.bestShantenOnly)
    }

    register<HanHu, ParentPoint>("getParentPointByHanHu") { args ->
        getParentPointByHanHu(args.han, args.hu)
    }
    register<HanHu, ChildPoint>("getChildPointByHanHu") { args ->
        getChildPointByHanHu(args.han, args.hu)
    }

    register<HoraArgs, Hora>("hora") { args ->
        if (args.shantenResult != null) {
            hora(
                args.shantenResult, args.agari, args.tsumo,
                args.dora, args.selfWind, args.roundWind, args.extraYaku
            )
        } else if (args.tiles != null) {
            hora(
                args.tiles, args.furo ?: emptyList(), args.agari, args.tsumo,
                args.dora, args.selfWind, args.roundWind, args.extraYaku
            )
        } else {
            throw IllegalArgumentException("either shantenResult or tiles/furo muse be set")
        }
    }
}.build()