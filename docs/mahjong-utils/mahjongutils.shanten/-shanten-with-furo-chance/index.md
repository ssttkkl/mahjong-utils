//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ShantenWithFuroChance](index.md)

# ShantenWithFuroChance

[common]\
@Serializable

@SerialName(value = &quot;ShantenWithFuroChance&quot;)

data class [ShantenWithFuroChance](index.md)(val shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val pass: [ShantenWithoutGot](../-shanten-without-got/index.md)?, val chi: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md), [ShantenWithGot](../-shanten-with-got/index.md)&gt;, val pon: [ShantenWithGot](../-shanten-with-got/index.md)?, val minkan: [ShantenWithoutGot](../-shanten-without-got/index.md)?) : [Shanten](../-shanten/index.md)

有副露机会的手牌的向听信息

## Constructors

| | |
|---|---|
| [ShantenWithFuroChance](-shanten-with-furo-chance.md) | [common]<br>constructor(shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), pass: [ShantenWithoutGot](../-shanten-without-got/index.md)?, chi: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md), [ShantenWithGot](../-shanten-with-got/index.md)&gt;, pon: [ShantenWithGot](../-shanten-with-got/index.md)?, minkan: [ShantenWithoutGot](../-shanten-without-got/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [chi](chi.md) | [common]<br>val [chi](chi.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md), [ShantenWithGot](../-shanten-with-got/index.md)&gt;<br>每种搭子吃后的向听信息 |
| [minkan](minkan.md) | [common]<br>val [minkan](minkan.md): [ShantenWithoutGot](../-shanten-without-got/index.md)?<br>明杠后的向听信息（若无法明杠则为null） |
| [pass](pass.md) | [common]<br>val [pass](pass.md): [ShantenWithoutGot](../-shanten-without-got/index.md)?<br>Pass后的向听信息 |
| [pon](pon.md) | [common]<br>val [pon](pon.md): [ShantenWithGot](../-shanten-with-got/index.md)?<br>碰后的向听信息（若无法碰则为null） |
| [shantenNum](shanten-num.md) | [common]<br>open override val [shantenNum](shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>向听数 |
