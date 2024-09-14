//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Mentsu](index.md)

# Mentsu

@Serializable(with = MentsuSerializer::class)

sealed interface [Mentsu](index.md)

面子

#### Inheritors

| |
|---|
| [Kotsu](../-kotsu/index.md) |
| [Shuntsu](../-shuntsu/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [tiles](tiles.md) | [common]<br>abstract val [tiles](tiles.md): [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>所含的牌 |

## Functions

| Name | Summary |
|---|---|
| [afterDiscard](after-discard.md) | [common]<br>abstract fun [afterDiscard](after-discard.md)(discard: [Tile](../-tile/index.md)): [Tatsu](../-tatsu/index.md)<br>舍牌后形成的搭子 |
