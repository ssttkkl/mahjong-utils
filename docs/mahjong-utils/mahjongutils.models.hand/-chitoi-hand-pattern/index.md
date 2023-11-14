//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[ChitoiHandPattern](index.md)

# ChitoiHandPattern

[common]\
@Serializable

@SerialName(value = &quot;ChitoiHandPattern&quot;)

data class [ChitoiHandPattern](index.md)(val pairs: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) : [IChitoiHandPattern](../-i-chitoi-hand-pattern/index.md), [CommonHandPattern](../-common-hand-pattern/index.md)

以七对子为目标的手牌

## Constructors

| | |
|---|---|
| [ChitoiHandPattern](-chitoi-hand-pattern.md) | [common]<br>constructor(pairs: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [furo](../-i-chitoi-hand-pattern/furo.md) | [common]<br>open override val [furo](../-i-chitoi-hand-pattern/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [pairs](pairs.md) | [common]<br>open override val [pairs](pairs.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>已有对子 |
| [remaining](remaining.md) | [common]<br>open override val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tiles](../-i-chitoi-hand-pattern/tiles.md) | [common]<br>open override val [tiles](../-i-chitoi-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
