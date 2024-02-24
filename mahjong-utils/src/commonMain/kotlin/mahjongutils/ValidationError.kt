package mahjongutils

interface ErrorInfo {
    val message: String
}


open class ValidationException(
    val errors: Collection<ErrorInfo>
) : RuntimeException() {
    override val message: String = errors.joinToString("; ") { it.message }
}