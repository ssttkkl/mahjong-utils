package mahjongutils.entry

internal fun interface IMethodEntry<in RAW_PARAMS : Any, out RAW_RESULT : Any> {
    fun call(rawParams: RAW_PARAMS): RAW_RESULT
}