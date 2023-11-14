//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Penchan](index.md)

# Penchan

[common]\
@Serializable

@SerialName(value = &quot;Penchan&quot;)

data class [Penchan](index.md)(val first: [Tile](../-tile/index.md)) : [Tatsu](../-tatsu/index.md)

边张

## Constructors

| | |
|---|---|
| [Penchan](-penchan.md) | [common]<br>constructor(first: [Tile](../-tile/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [first](first.md) | [common]<br>open override val [first](first.md): [Tile](../-tile/index.md)<br>第一张牌 |
| [second](second.md) | [common]<br>open override val [second](second.md): [Tile](../-tile/index.md)<br>第二张牌 |
| [waiting](waiting.md) | [common]<br>open override val [waiting](waiting.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../-tile/index.md)&gt;<br>进张 |

## Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [withWaiting](with-waiting.md) | [common]<br>open override fun [withWaiting](with-waiting.md)(tile: [Tile](../-tile/index.md)): [Shuntsu](../-shuntsu/index.md)<br>进张后形成的面子 |
