package mahjongutils.entry

internal fun interface Method<in P : Any, out R : Any> {
    fun handle(params: P): R
}