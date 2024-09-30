//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Mentsu](index.md)

# Mentsu

[common]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

@Serializable(with = MentsuSerializer::class)

value class [Mentsu](index.md)

面子

## Constructors

| | |
|---|---|
| [Mentsu](-mentsu.md) | [common]<br>constructor(type: [MentsuType](../-mentsu-type/index.md), tile: [Tile](../-tile/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md) |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>所含的牌 |
| [type](type.md) | [common]<br>val [type](type.md): [MentsuType](../-mentsu-type/index.md) |

## Functions

| Name | Summary |
|---|---|
| [afterDiscard](after-discard.md) | [common]<br>fun [afterDiscard](after-discard.md)(discard: [Tile](../-tile/index.md)): [Tatsu](../-tatsu/index.md)<br>舍牌后形成的搭子 |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
