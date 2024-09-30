//[mahjong-utils](../../../index.md)/[mahjongutils.models](../index.md)/[Tile](index.md)

# Tile

[common]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

@Serializable(with = TileSerializer::class)

value class [Tile](index.md) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Tile](index.md)&gt; 

麻将牌

## Constructors

| | |
|---|---|
| [Tile](-tile.md) | [common]<br>constructor(type: [TileType](../-tile-type/index.md), num: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [code](code.md) | [common]<br>val [code](code.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isSangen](../is-sangen.md) | [common]<br>val [Tile](index.md).[isSangen](../is-sangen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为三元牌 |
| [isWind](../is-wind.md) | [common]<br>val [Tile](index.md).[isWind](../is-wind.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为风牌 |
| [isYaochu](../is-yaochu.md) | [common]<br>val [Tile](index.md).[isYaochu](../is-yaochu.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为幺九牌 |
| [num](num.md) | [common]<br>val [num](num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>数字 |
| [realNum](real-num.md) | [common]<br>val [realNum](real-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>真正数字。当num为0时（该牌为红宝牌），realNum为5。其余情况下与num相等。 |
| [type](type.md) | [common]<br>val [type](type.md): [TileType](../-tile-type/index.md)<br>种类 |

## Functions

| Name | Summary |
|---|---|
| [advance](advance.md) | [common]<br>fun [advance](advance.md)(step: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Tile](index.md)<br>该牌数字加上指定数字后得到的牌 |
| [compareTo](compare-to.md) | [common]<br>open operator override fun [compareTo](compare-to.md)(other: [Tile](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [distance](distance.md) | [common]<br>fun [distance](distance.md)(that: [Tile](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>计算这张牌与另一张牌的数字之差 |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
