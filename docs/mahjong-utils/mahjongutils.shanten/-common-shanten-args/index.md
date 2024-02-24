//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[CommonShantenArgs](index.md)

# CommonShantenArgs

@Serializable

data class [CommonShantenArgs](index.md)(val tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; = emptyList(), val bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)

向听分析参数

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| furo | 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入） |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
| mode | 向听分析模式 |

#### See also

| |
|---|
| ShantenMode |

## Constructors

| | |
|---|---|
| [CommonShantenArgs](-common-shanten-args.md) | [common]<br>constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; = emptyList(), bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [bestShantenOnly](best-shanten-only.md) | [common]<br>val [bestShantenOnly](best-shanten-only.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [furo](furo.md) | [common]<br>val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [throwOnValidationError](../throw-on-validation-error.md) | [common]<br>fun [CommonShantenArgs](index.md).[throwOnValidationError](../throw-on-validation-error.md)() |
| [validate](../validate.md) | [common]<br>fun [CommonShantenArgs](index.md).[validate](../validate.md)(): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[CommonShantenArgsErrorInfo](../-common-shanten-args-error-info/index.md)&gt; |
