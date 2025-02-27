//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[HoraArgs](index.md)

# HoraArgs

@Serializable

data class [HoraArgs](index.md)

和牌分析参数

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| furo | 副露 |
| shantenResult | 向听分析结果 |
| agari | 和牌张 |
| tsumo | 是否自摸 |
| dora | 宝牌数目 |
| selfWind | 自风 |
| roundWind | 场风 |
| extraYaku | 额外役种（不会对役种合法性进行检查） |

## Constructors

| | |
|---|---|
| [HoraArgs](-hora-args.md) | [common]<br>constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; = DEFAULT_FURO, agari: [Tile](../../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), dora: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_DORA, selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = DEFAULT_SELF_WIND, roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = DEFAULT_ROUND_WIND, extraYaku: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;@Serializable(with = [DefaultYakuSerializer::class](../../mahjongutils.yaku/-default-yaku-serializer/index.md))[Yaku](../../mahjongutils.yaku/-yaku/index.md)&gt; = DEFAULT_EXTRA_YAKU, options: [HoraOptions](../-hora-options/index.md) = DEFAULT_OPTIONS)constructor(shantenResult: [CommonShantenResult](../../mahjongutils.shanten/-common-shanten-result/index.md)&lt;*&gt;, agari: [Tile](../../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), dora: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_DORA, selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = DEFAULT_SELF_WIND, roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = DEFAULT_ROUND_WIND, extraYaku: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;@Serializable(with = [DefaultYakuSerializer::class](../../mahjongutils.yaku/-default-yaku-serializer/index.md))[Yaku](../../mahjongutils.yaku/-yaku/index.md)&gt; = DEFAULT_EXTRA_YAKU, options: [HoraOptions](../-hora-options/index.md) = DEFAULT_OPTIONS) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [agari](agari.md) | [common]<br>val [agari](agari.md): [Tile](../../mahjongutils.models/-tile/index.md) |
| [dora](dora.md) | [common]<br>val [dora](dora.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [extraYaku](extra-yaku.md) | [common]<br>val [extraYaku](extra-yaku.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;@Serializable(with = [DefaultYakuSerializer::class](../../mahjongutils.yaku/-default-yaku-serializer/index.md))[Yaku](../../mahjongutils.yaku/-yaku/index.md)&gt; |
| [furo](furo.md) | [common]<br>val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; |
| [options](options.md) | [common]<br>val [options](options.md): [HoraOptions](../-hora-options/index.md) |
| [roundWind](round-wind.md) | [common]<br>val [roundWind](round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? |
| [selfWind](self-wind.md) | [common]<br>val [selfWind](self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? |
| [shantenResult](shanten-result.md) | [common]<br>val [shantenResult](shanten-result.md): [CommonShantenResult](../../mahjongutils.shanten/-common-shanten-result/index.md)&lt;*&gt;? = null |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;? = null |
| [tsumo](tsumo.md) | [common]<br>val [tsumo](tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [throwOnValidationError](../throw-on-validation-error.md) | [common]<br>fun [HoraArgs](index.md).[throwOnValidationError](../throw-on-validation-error.md)() |
| [validate](../validate.md) | [common]<br>fun [HoraArgs](index.md).[validate](../validate.md)(): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[HoraArgsErrorInfo](../-hora-args-error-info/index.md)&gt; |
