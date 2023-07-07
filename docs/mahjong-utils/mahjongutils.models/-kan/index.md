//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Kan](index.md)

# Kan

[common]\
@Serializable

data class [Kan](index.md)(val tile: [Tile](../-tile/index.md), val ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) : [Furo](../-furo/index.md)

杠

## Constructors

| | |
|---|---|
| [Kan](-kan.md) | [common]<br>constructor(tile: [Tile](../-tile/index.md), ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [ankan](ankan.md) | [common]<br>val [ankan](ankan.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>是否为暗杠 |
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md)<br>杠成的刻子的牌（如7777s，tile应为7s） |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../-tile/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [asMentsu](as-mentsu.md) | [common]<br>open override fun [asMentsu](as-mentsu.md)(): [Kotsu](../-kotsu/index.md)<br>获取副露的面子 |
