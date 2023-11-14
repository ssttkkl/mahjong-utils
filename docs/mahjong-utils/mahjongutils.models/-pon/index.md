//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Pon](index.md)

# Pon

[common]\
@Serializable

@SerialName(value = &quot;Pon&quot;)

data class [Pon](index.md)(val tile: [Tile](../-tile/index.md)) : [Furo](../-furo/index.md)

碰

## Constructors

| | |
|---|---|
| [Pon](-pon.md) | [common]<br>constructor(tile: [Tile](../-tile/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [tile](tile.md) | [common]<br>val [tile](tile.md): [Tile](../-tile/index.md)<br>碰成的刻子的牌（如777s，tile应为7s） |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../-tile/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [asMentsu](as-mentsu.md) | [common]<br>open override fun [asMentsu](as-mentsu.md)(): [Kotsu](../-kotsu/index.md)<br>获取副露的面子 |
