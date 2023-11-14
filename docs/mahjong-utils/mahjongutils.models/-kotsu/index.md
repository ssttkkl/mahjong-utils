//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Kotsu](index.md)

# Kotsu

[common]\
@Serializable

@SerialName(value = &quot;Kotsu&quot;)

data class [Kotsu](index.md)(val tile: [Tile](../-tile/index.md)) : [Mentsu](../-mentsu/index.md)

刻子

## Constructors

| | |
|---|---|
| [Kotsu](-kotsu.md) | [common]<br>constructor(tile: [Tile](../-tile/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md)<br>顺子的第一张牌（如789m，tile应为7m） |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>所含的牌 |

## Functions

| Name | Summary |
|---|---|
| [afterDiscard](after-discard.md) | [common]<br>open override fun [afterDiscard](after-discard.md)(discard: [Tile](../-tile/index.md)): [Tatsu](../-tatsu/index.md)<br>舍牌后形成的搭子 |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
