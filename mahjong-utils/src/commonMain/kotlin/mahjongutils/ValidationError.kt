package mahjongutils

interface ErrorInfo {
    val message: String
}


open class ValidationException(
    val errors: Collection<ErrorInfo>
) : IllegalArgumentException() {
    override val message: String = errors.joinToString("; ") { it.message }
}