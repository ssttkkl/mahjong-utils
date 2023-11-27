package mahjongutils.entry

class MethodExecutionException(
    val code: Int,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    constructor(code: Int, cause: Throwable) : this(code, cause.message ?: "", cause)
}