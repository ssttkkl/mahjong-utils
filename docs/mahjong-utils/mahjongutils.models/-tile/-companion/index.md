//[mahjong-utils](../../../../index.md)/[mahjongutils.models](../../index.md)/[Tile](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [all](all.md) | [common]<br>val [all](all.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../index.md)&gt;<br>所有牌 |
| [allExcludeAkaDora](all-exclude-aka-dora.md) | [common]<br>val [allExcludeAkaDora](all-exclude-aka-dora.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../index.md)&gt;<br>所有牌 |
| [allYaochu](all-yaochu.md) | [common]<br>val [allYaochu](all-yaochu.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../index.md)&gt;<br>所有幺九牌 |
| [MAX_TILE_CODE](-m-a-x_-t-i-l-e_-c-o-d-e.md) | [common]<br>const val [MAX_TILE_CODE](-m-a-x_-t-i-l-e_-c-o-d-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>牌编号最大值 |

## Functions

| Name | Summary |
|---|---|
| [get](get.md) | [common]<br>operator fun [get](get.md)(code: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Tile](../index.md)<br>根据编号获取牌<br>[common]<br>operator fun [get](get.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Tile](../index.md)<br>根据文本获取牌<br>[common]<br>fun [get](get.md)(type: [TileType](../../-tile-type/index.md), num: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Tile](../index.md)<br>根据种类和数字获取牌 |
| [getOrNull](get-or-null.md) | [common]<br>fun [getOrNull](get-or-null.md)(code: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Tile](../index.md)?<br>fun [getOrNull](get-or-null.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Tile](../index.md)?<br>fun [getOrNull](get-or-null.md)(type: [TileType](../../-tile-type/index.md), num: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Tile](../index.md)? |
| [parseTiles](parse-tiles.md) | [common]<br>fun [parseTiles](parse-tiles.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../index.md)&gt;<br>将给定的牌文本转换为牌序列 |
