//[mahjong-utils](../../index.md)/[mahjongutils.models](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Chi](-chi/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Chi&quot;)<br>data class [Chi](-chi/index.md)(val tile: [Tile](-tile/index.md)) : [Furo](-furo/index.md)<br>吃 |
| [Furo](-furo/index.md) | [common]<br>@Serializable<br>interface [Furo](-furo/index.md)<br>副露 |
| [Kan](-kan/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Kan&quot;)<br>data class [Kan](-kan/index.md)(val tile: [Tile](-tile/index.md), val ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) : [Furo](-furo/index.md)<br>杠 |
| [Kanchan](-kanchan/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Kanchan&quot;)<br>data class [Kanchan](-kanchan/index.md)(val first: [Tile](-tile/index.md)) : [Tatsu](-tatsu/index.md)<br>坎张 |
| [Kotsu](-kotsu/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Kotsu&quot;)<br>data class [Kotsu](-kotsu/index.md)(val tile: [Tile](-tile/index.md)) : [Mentsu](-mentsu/index.md)<br>刻子 |
| [Mentsu](-mentsu/index.md) | [common]<br>@Serializable(with = MentsuSerializer::class)<br>interface [Mentsu](-mentsu/index.md)<br>面子 |
| [Penchan](-penchan/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Penchan&quot;)<br>data class [Penchan](-penchan/index.md)(val first: [Tile](-tile/index.md)) : [Tatsu](-tatsu/index.md)<br>边张 |
| [Pon](-pon/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Pon&quot;)<br>data class [Pon](-pon/index.md)(val tile: [Tile](-tile/index.md)) : [Furo](-furo/index.md)<br>碰 |
| [Ryanmen](-ryanmen/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Ryanmen&quot;)<br>data class [Ryanmen](-ryanmen/index.md)(val first: [Tile](-tile/index.md)) : [Tatsu](-tatsu/index.md)<br>两面 |
| [Shuntsu](-shuntsu/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Shuntsu&quot;)<br>data class [Shuntsu](-shuntsu/index.md)(val tile: [Tile](-tile/index.md)) : [Mentsu](-mentsu/index.md)<br>顺子 |
| [Tatsu](-tatsu/index.md) | [common]<br>@Serializable(with = TatsuSerializer::class)<br>interface [Tatsu](-tatsu/index.md)<br>搭子 |
| [Tile](-tile/index.md) | [common]<br>@Serializable(with = TileSerializer::class)<br>data class [Tile](-tile/index.md) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Tile](-tile/index.md)&gt; <br>麻将牌 |
| [TileType](-tile-type/index.md) | [common]<br>enum [TileType](-tile-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[TileType](-tile-type/index.md)&gt; <br>麻将牌的种类（万、筒、索、字） |
| [Toitsu](-toitsu/index.md) | [common]<br>@Serializable<br>@SerialName(value = &quot;Toitsu&quot;)<br>data class [Toitsu](-toitsu/index.md)(val first: [Tile](-tile/index.md)) : [Tatsu](-tatsu/index.md)<br>对子 |
| [Wind](-wind/index.md) | [common]<br>enum [Wind](-wind/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Wind](-wind/index.md)&gt; <br>风（东、南、西、北） |

## Properties

| Name | Summary |
|---|---|
| [isSangen](is-sangen.md) | [common]<br>val [Tile](-tile/index.md).[isSangen](is-sangen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为三元牌 |
| [isWind](is-wind.md) | [common]<br>val [Tile](-tile/index.md).[isWind](is-wind.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为风牌 |
| [isYaochu](is-yaochu.md) | [common]<br>val [Tile](-tile/index.md).[isYaochu](is-yaochu.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为幺九牌 |

## Functions

| Name | Summary |
|---|---|
| [countAsCodeArray](count-as-code-array.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[countAsCodeArray](count-as-code-array.md)(): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [countAsMap](count-as-map.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[countAsMap](count-as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](-tile/index.md), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
| [Furo](-furo.md) | [common]<br>fun [Furo](-furo.md)(vararg tiles: [Tile](-tile/index.md), ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [Furo](-furo/index.md)<br>fun [Furo](-furo.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [Furo](-furo/index.md)<br>fun [Furo](-furo.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](-tile/index.md)&gt;, ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [Furo](-furo/index.md) |
| [Mentsu](-mentsu.md) | [common]<br>fun [Mentsu](-mentsu.md)(vararg tiles: [Tile](-tile/index.md)): [Mentsu](-mentsu/index.md)<br>fun [Mentsu](-mentsu.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Mentsu](-mentsu/index.md)<br>fun [Mentsu](-mentsu.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](-tile/index.md)&gt;): [Mentsu](-mentsu/index.md) |
| [Tatsu](-tatsu.md) | [common]<br>fun [Tatsu](-tatsu.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Tatsu](-tatsu/index.md)<br>fun [Tatsu](-tatsu.md)(first: [Tile](-tile/index.md), second: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [toTilesString](to-tiles-string.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[toTilesString](to-tiles-string.md)(lowercase: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>将牌序列转换为牌文本 |
