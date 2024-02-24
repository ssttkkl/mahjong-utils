//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[FuroChanceShantenArgs](index.md)

# FuroChanceShantenArgs

data class [FuroChanceShantenArgs](index.md)(val tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val chanceTile: [Tile](../../mahjongutils.models/-tile/index.md), val allowChi: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, val bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val allowKuikae: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)

副露判断向听分析参数

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| chanceTile | 副露机会牌（能够吃、碰的牌） |
| allowChi | 是否允许吃 |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
| allowKuikae | 是否允许食替 |

## Constructors

| | |
|---|---|
| [FuroChanceShantenArgs](-furo-chance-shanten-args.md) | [common]<br>constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, chanceTile: [Tile](../../mahjongutils.models/-tile/index.md), allowChi: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, allowKuikae: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [allowChi](allow-chi.md) | [common]<br>val [allowChi](allow-chi.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true |
| [allowKuikae](allow-kuikae.md) | [common]<br>val [allowKuikae](allow-kuikae.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [bestShantenOnly](best-shanten-only.md) | [common]<br>val [bestShantenOnly](best-shanten-only.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [chanceTile](chance-tile.md) | [common]<br>val [chanceTile](chance-tile.md): [Tile](../../mahjongutils.models/-tile/index.md) |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [throwOnValidationError](../throw-on-validation-error.md) | [common]<br>fun [FuroChanceShantenArgs](index.md).[throwOnValidationError](../throw-on-validation-error.md)() |
| [validate](../validate.md) | [common]<br>fun [FuroChanceShantenArgs](index.md).[validate](../validate.md)(): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[FuroChanceShantenArgsValidationError](../-furo-chance-shanten-args-validation-error/index.md)&gt; |
