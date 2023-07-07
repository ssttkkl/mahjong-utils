//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ShantenWithGot](index.md)

# ShantenWithGot

[common]\
@Serializable

data class [ShantenWithGot](index.md)(val shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val discardToAdvance: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt;, val ankanToAdvance: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt; = emptyMap()) : [CommonShanten](../-common-shanten/index.md)

摸牌的手牌的向听信息

## Constructors

| | |
|---|---|
| [ShantenWithGot](-shanten-with-got.md) | [common]<br>constructor(shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), discardToAdvance: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt;, ankanToAdvance: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt; = emptyMap()) |

## Properties

| Name | Summary |
|---|---|
| [ankanToAdvance](ankan-to-advance.md) | [common]<br>val [ankanToAdvance](ankan-to-advance.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt;<br>每种暗杠后的向听信息 |
| [discardToAdvance](discard-to-advance.md) | [common]<br>val [discardToAdvance](discard-to-advance.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [ShantenWithoutGot](../-shanten-without-got/index.md)&gt;<br>每种弃牌后的向听信息 |
| [shantenNum](shanten-num.md) | [common]<br>open override val [shantenNum](shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>向听数 |
