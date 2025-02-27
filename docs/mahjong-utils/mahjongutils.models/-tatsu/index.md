//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Tatsu](index.md)

# Tatsu

[common]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-inline/index.html)

@Serializable(with = TatsuSerializer::class)

value class [Tatsu](index.md)

搭子

## Constructors

| | |
|---|---|
| [Tatsu](-tatsu.md) | [common]<br>constructor(type: [TatsuType](../-tatsu-type/index.md), first: [Tile](../-tile/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [first](first.md) | [common]<br>val [first](first.md): [Tile](../-tile/index.md)<br>第一张牌 |
| [second](second.md) | [common]<br>val [second](second.md): [Tile](../-tile/index.md)<br>第二张牌 |
| [type](type.md) | [common]<br>val [type](type.md): [TatsuType](../-tatsu-type/index.md) |
| [waiting](waiting.md) | [common]<br>val [waiting](waiting.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>进张 |

## Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [withWaiting](with-waiting.md) | [common]<br>fun [withWaiting](with-waiting.md)(tile: [Tile](../-tile/index.md)): [Mentsu](../-mentsu/index.md)<br>进张后形成的面子 |
