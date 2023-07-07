//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Tatsu](index.md)

# Tatsu

@Serializable(with = TatsuSerializer::class)

interface [Tatsu](index.md)

搭子

#### Inheritors

| |
|---|
| [Ryanmen](../-ryanmen/index.md) |
| [Kanchan](../-kanchan/index.md) |
| [Penchan](../-penchan/index.md) |
| [Toitsu](../-toitsu/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [first](first.md) | [common]<br>abstract val [first](first.md): [Tile](../-tile/index.md)<br>第一张牌 |
| [second](second.md) | [common]<br>abstract val [second](second.md): [Tile](../-tile/index.md)<br>第二张牌 |
| [waiting](waiting.md) | [common]<br>abstract val [waiting](waiting.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>进张 |

## Functions

| Name | Summary |
|---|---|
| [withWaiting](with-waiting.md) | [common]<br>abstract fun [withWaiting](with-waiting.md)(tile: [Tile](../-tile/index.md)): [Mentsu](../-mentsu/index.md)<br>进张后形成的面子 |
