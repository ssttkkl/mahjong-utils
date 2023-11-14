//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Shuntsu](index.md)

# Shuntsu

[common]\
@Serializable

@SerialName(value = &quot;Shuntsu&quot;)

data class [Shuntsu](index.md)(val tile: [Tile](../-tile/index.md)) : [Mentsu](../-mentsu/index.md)

顺子

## Constructors

| | |
|---|---|
| [Shuntsu](-shuntsu.md) | [common]<br>constructor(tile: [Tile](../-tile/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md)<br>刻子的牌（如777s，tile应为7s） |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>所含的牌 |

## Functions

| Name | Summary |
|---|---|
| [afterDiscard](after-discard.md) | [common]<br>open override fun [afterDiscard](after-discard.md)(discard: [Tile](../-tile/index.md)): [Tatsu](../-tatsu/index.md)<br>舍牌后形成的搭子 |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
