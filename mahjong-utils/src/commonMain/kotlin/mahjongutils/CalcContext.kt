package mahjongutils

class CalcContext {
    @PublishedApi
    internal val cache = HashMap<Any?, Any?>()

    inline fun <reified T> memo(key: Any?, calc: () -> T): T {
        if (cache.containsKey(key)) {
            return cache[key] as T
        }
        return calc().also {
            cache[key] = it
        }
    }
}