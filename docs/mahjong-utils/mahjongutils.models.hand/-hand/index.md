//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[Hand](index.md)

# Hand

[common]\
@Serializable

data class [Hand](index.md)&lt;out [P](index.md) : [CommonHandPattern](../-common-hand-pattern/index.md)&gt;(val tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;, val patterns: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[P](index.md)&gt;) : [IHasFuro](../-i-has-furo/index.md)

手牌

## Constructors

| | |
|---|---|
| [Hand](-hand.md) | [common]<br>constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;, patterns: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[P](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [furo](furo.md) | [common]<br>open override val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [patterns](patterns.md) | [common]<br>val [patterns](patterns.md): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[P](index.md)&gt;<br>手牌形 |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>门前的牌 |
