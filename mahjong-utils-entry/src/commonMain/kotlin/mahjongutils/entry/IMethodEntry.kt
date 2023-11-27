package mahjongutils.entry

internal interface IMethodEntry<in RAW_PARAMS : Any, out RAW_RESULT : Any> {
    fun call(rawParams: RAW_PARAMS): RAW_RESULT

    @Throws(MethodExecutionException::class)
    fun callReceivingData(rawParams: RAW_PARAMS): RAW_RESULT
}