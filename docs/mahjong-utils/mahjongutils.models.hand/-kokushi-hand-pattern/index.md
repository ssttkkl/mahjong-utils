//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[KokushiHandPattern](index.md)

# KokushiHandPattern

[common]\
@Serializable

@SerialName(value = &quot;KokushiHandPattern&quot;)

data class [KokushiHandPattern](index.md)(val yaochu: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val repeated: [Tile](../../mahjongutils.models/-tile/index.md)?, val remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) : [IKokushiHandPattern](../-i-kokushi-hand-pattern/index.md), [CommonHandPattern](../-common-hand-pattern/index.md)

以国士无双为目标的手牌

## Constructors

| | |
|---|---|
| [KokushiHandPattern](-kokushi-hand-pattern.md) | [common]<br>constructor(yaochu: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, repeated: [Tile](../../mahjongutils.models/-tile/index.md)?, remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [furo](../-i-kokushi-hand-pattern/furo.md) | [common]<br>open override val [furo](../-i-kokushi-hand-pattern/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](remaining.md) | [common]<br>open override val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [repeated](repeated.md) | [common]<br>open override val [repeated](repeated.md): [Tile](../../mahjongutils.models/-tile/index.md)?<br>重复的幺九牌 |
| [tiles](../-i-kokushi-hand-pattern/tiles.md) | [common]<br>open override val [tiles](../-i-kokushi-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [yaochu](yaochu.md) | [common]<br>open override val [yaochu](yaochu.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>幺九牌 |
