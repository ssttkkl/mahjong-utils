//[mahjong-utils](../../index.md)/[mahjongutils.models](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Furo](-furo/index.md) | [common]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-inline/index.html)<br>@Serializable(with = [FuroSerializer::class](-furo-serializer/index.md))<br>value class [Furo](-furo/index.md) |
| [FuroSerializer](-furo-serializer/index.md) | [common]<br>object [FuroSerializer](-furo-serializer/index.md) : KSerializer&lt;[Furo](-furo/index.md)&gt; |
| [FuroType](-furo-type/index.md) | [common]<br>enum [FuroType](-furo-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[FuroType](-furo-type/index.md)&gt; |
| [Mentsu](-mentsu/index.md) | [common]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-inline/index.html)<br>@Serializable(with = MentsuSerializer::class)<br>value class [Mentsu](-mentsu/index.md)<br>面子 |
| [MentsuType](-mentsu-type/index.md) | [common]<br>enum [MentsuType](-mentsu-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[MentsuType](-mentsu-type/index.md)&gt; |
| [Tatsu](-tatsu/index.md) | [common]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-inline/index.html)<br>@Serializable(with = TatsuSerializer::class)<br>value class [Tatsu](-tatsu/index.md)<br>搭子 |
| [TatsuType](-tatsu-type/index.md) | [common]<br>enum [TatsuType](-tatsu-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[TatsuType](-tatsu-type/index.md)&gt; |
| [Tile](-tile/index.md) | [common]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-inline/index.html)<br>@Serializable(with = TileSerializer::class)<br>value class [Tile](-tile/index.md) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-comparable/index.html)&lt;[Tile](-tile/index.md)&gt; <br>麻将牌 |
| [TileType](-tile-type/index.md) | [common]<br>enum [TileType](-tile-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[TileType](-tile-type/index.md)&gt; <br>麻将牌的种类（万、筒、索、字） |
| [Wind](-wind/index.md) | [common]<br>enum [Wind](-wind/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Wind](-wind/index.md)&gt; <br>风（东、南、西、北） |

## Properties

| Name | Summary |
|---|---|
| [isSangen](is-sangen.md) | [common]<br>val [Tile](-tile/index.md).[isSangen](is-sangen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否为三元牌 |
| [isWind](is-wind.md) | [common]<br>val [Tile](-tile/index.md).[isWind](is-wind.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否为风牌 |
| [isYaochu](is-yaochu.md) | [common]<br>val [Tile](-tile/index.md).[isYaochu](is-yaochu.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否为幺九牌 |

## Functions

| Name | Summary |
|---|---|
| [Ankan](-ankan.md) | [common]<br>fun [Ankan](-ankan.md)(tile: [Tile](-tile/index.md)): [Furo](-furo/index.md) |
| [Chi](-chi.md) | [common]<br>fun [Chi](-chi.md)(tile: [Tile](-tile/index.md)): [Furo](-furo/index.md) |
| [countAsCodeArray](count-as-code-array.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[countAsCodeArray](count-as-code-array.md)(): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int-array/index.html) |
| [countAsMap](count-as-map.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[countAsMap](count-as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[Tile](-tile/index.md), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)&gt; |
| [Furo](-furo.md) | [common]<br>fun [Furo](-furo.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [Furo](-furo/index.md)<br>fun [Furo](-furo.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](-tile/index.md)&gt;, ankan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [Furo](-furo/index.md) |
| [Kan](-kan.md) | [common]<br>fun [Kan](-kan.md)(tile: [Tile](-tile/index.md)): [Furo](-furo/index.md) |
| [Kanchan](-kanchan.md) | [common]<br>fun [Kanchan](-kanchan.md)(tile: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [Kotsu](-kotsu.md) | [common]<br>fun [Kotsu](-kotsu.md)(tile: [Tile](-tile/index.md)): [Mentsu](-mentsu/index.md) |
| [Mentsu](-mentsu.md) | [common]<br>fun [Mentsu](-mentsu.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Mentsu](-mentsu/index.md)<br>fun [Mentsu](-mentsu.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](-tile/index.md)&gt;): [Mentsu](-mentsu/index.md) |
| [Penchan](-penchan.md) | [common]<br>fun [Penchan](-penchan.md)(tile: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [Pon](-pon.md) | [common]<br>fun [Pon](-pon.md)(tile: [Tile](-tile/index.md)): [Furo](-furo/index.md) |
| [Ryanmen](-ryanmen.md) | [common]<br>fun [Ryanmen](-ryanmen.md)(tile: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [Shuntsu](-shuntsu.md) | [common]<br>fun [Shuntsu](-shuntsu.md)(tile: [Tile](-tile/index.md)): [Mentsu](-mentsu/index.md) |
| [Tatsu](-tatsu.md) | [common]<br>fun [Tatsu](-tatsu.md)(text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Tatsu](-tatsu/index.md)<br>fun [Tatsu](-tatsu.md)(first: [Tile](-tile/index.md), second: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [Toitsu](-toitsu.md) | [common]<br>fun [Toitsu](-toitsu.md)(tile: [Tile](-tile/index.md)): [Tatsu](-tatsu/index.md) |
| [toTilesString](to-tiles-string.md) | [common]<br>fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Tile](-tile/index.md)&gt;.[toTilesString](to-tiles-string.md)(lowercase: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>将牌序列转换为牌文本 |
