package mahjongutils

import kotlinx.serialization.Serializable

interface ErrorInfo {
    val message: String
}

@Serializable
data class ValidationError<I : ErrorInfo>(
    val field: String,
    val errorInfo: I
)

open class ValidationException(
    val errors: Collection<ValidationError<*>>
) : RuntimeException()