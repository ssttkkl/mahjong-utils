//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Chi](index.md)

# Chi

[common]\
@Serializable

data class [Chi](index.md)(val tile: [Tile](../-tile/index.md)) : [Furo](../-furo/index.md)

吃

## Constructors

| | |
|---|---|
| [Chi](-chi.md) | [common]<br>constructor(tile: [Tile](../-tile/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md)<br>吃成的顺子的第一张牌（如789m，tile应为7m） |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../-tile/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [asMentsu](as-mentsu.md) | [common]<br>open override fun [asMentsu](as-mentsu.md)(): [Shuntsu](../-shuntsu/index.md)<br>获取副露的面子 |
