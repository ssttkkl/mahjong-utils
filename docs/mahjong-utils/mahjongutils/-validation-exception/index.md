//[mahjong-utils](../../../index.md)/[mahjongutils](../index.md)/[ValidationException](index.md)

# ValidationException

open class [ValidationException](index.md)(val errors: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[ErrorInfo](../-error-info/index.md)&gt;) : [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html)

#### Inheritors

| |
|---|
| [HoraArgsValidationException](../../mahjongutils.hora/-hora-args-validation-exception/index.md) |
| [CommonShantenArgsValidationException](../../mahjongutils.shanten/-common-shanten-args-validation-exception/index.md) |
| [FuroChanceShantenArgsValidationException](../../mahjongutils.shanten/-furo-chance-shanten-args-validation-exception/index.md) |

## Constructors

| | |
|---|---|
| [ValidationException](-validation-exception.md) | [common]<br>constructor(errors: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[ErrorInfo](../-error-info/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [cause](../../mahjongutils.shanten/-furo-chance-shanten-args-validation-exception/index.md#-654012527%2FProperties%2F1581026887) | [common]<br>open val [cause](../../mahjongutils.shanten/-furo-chance-shanten-args-validation-exception/index.md#-654012527%2FProperties%2F1581026887): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [errors](errors.md) | [common]<br>val [errors](errors.md): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[ErrorInfo](../-error-info/index.md)&gt; |
| [message](message.md) | [common]<br>open override val [message](message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
