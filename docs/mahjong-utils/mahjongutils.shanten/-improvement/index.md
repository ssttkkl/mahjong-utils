//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[Improvement](index.md)

# Improvement

[common]\
@Serializable

data class [Improvement](index.md)(val discard: [Tile](../../mahjongutils.models/-tile/index.md), val advance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val advanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0)

## Constructors

| | |
|---|---|
| [Improvement](-improvement.md) | [common]<br>constructor(discard: [Tile](../../mahjongutils.models/-tile/index.md), advance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, advanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0) |

## Properties

| Name | Summary |
|---|---|
| [advance](advance.md) | [common]<br>val [advance](advance.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>摸上改良张且弃牌后的进张 |
| [advanceNum](advance-num.md) | [common]<br>@EncodeDefault<br>val [advanceNum](advance-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0<br>摸上改良张且弃牌后的进张数 |
| [discard](discard.md) | [common]<br>val [discard](discard.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>摸上改良张后的弃牌 |
