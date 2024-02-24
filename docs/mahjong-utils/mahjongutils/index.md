//[mahjong-utils](../../index.md)/[mahjongutils](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [CalcContext](-calc-context/index.md) | [common]<br>class [CalcContext](-calc-context/index.md) |
| [ErrorInfo](-error-info/index.md) | [common]<br>interface [ErrorInfo](-error-info/index.md) |
| [ValidationError](-validation-error/index.md) | [common]<br>@Serializable<br>data class [ValidationError](-validation-error/index.md)&lt;[I](-validation-error/index.md) : [ErrorInfo](-error-info/index.md)&gt;(val field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val errorInfo: [I](-validation-error/index.md)) |
| [ValidationException](-validation-exception/index.md) | [common]<br>open class [ValidationException](-validation-exception/index.md)(val errors: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[ValidationError](-validation-error/index.md)&lt;*&gt;&gt;) : [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) |
