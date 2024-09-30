//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Furo](index.md)

# Furo

[common]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

@Serializable(with = [FuroSerializer::class](../-furo-serializer/index.md))

value class [Furo](index.md)

## Constructors

| | |
|---|---|
| [Furo](-furo.md) | [common]<br>constructor(type: [FuroType](../-furo-type/index.md), tile: [Tile](../-tile/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md) |
| [tiles](tiles.md) | [common]<br>val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../-tile/index.md)&gt; |
| [type](type.md) | [common]<br>val [type](type.md): [FuroType](../-furo-type/index.md) |

## Functions

| Name | Summary |
|---|---|
| [asMentsu](as-mentsu.md) | [common]<br>fun [asMentsu](as-mentsu.md)(): [Mentsu](../-mentsu/index.md)<br>获取副露的面子 |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
