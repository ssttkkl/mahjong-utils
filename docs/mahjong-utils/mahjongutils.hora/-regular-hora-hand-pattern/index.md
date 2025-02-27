//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[RegularHoraHandPattern](index.md)

# RegularHoraHandPattern

[common]\
@Serializable

@SerialName(value = &quot;RegularHoraHandPattern&quot;)

data class [RegularHoraHandPattern](index.md) : [HoraHandPattern](../-hora-hand-pattern/index.md), [IRegularHandPattern](../../mahjongutils.models.hand/-i-regular-hand-pattern/index.md)

标准形的和牌手牌

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [agari](agari.md) | [common]<br>open override val [agari](agari.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>和牌张 |
| [agariTatsu](agari-tatsu.md) | [common]<br>val [agariTatsu](agari-tatsu.md): [Tatsu](../../mahjongutils.models/-tatsu/index.md)?<br>和牌搭子（为空表示单骑和牌） |
| [anko](../../mahjongutils.models.hand/-i-regular-hand-pattern/anko.md) | [common]<br>open override val [anko](../../mahjongutils.models.hand/-i-regular-hand-pattern/anko.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>暗刻 |
| [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md) | [common]<br>open override val [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [jyantou](jyantou.md) | [common]<br>open override val [jyantou](jyantou.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>雀头 |
| [k](../../mahjongutils.models.hand/-i-regular-hand-pattern/k.md) | [common]<br>open override val [k](../../mahjongutils.models.hand/-i-regular-hand-pattern/k.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>目标面子组数（=手牌数/4） |
| [mentsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/mentsu.md) | [common]<br>open override val [mentsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>面子（包括门前与副露） |
| [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md) | [common]<br>open override val [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [menzenMentsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/menzen-mentsu.md) | [common]<br>open override val [menzenMentsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/menzen-mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>门前面子 |
| [pattern](pattern.md) | [common]<br>val [pattern](pattern.md): [RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)<br>标准形手牌 |
| [remaining](../../mahjongutils.models.hand/-hand-pattern/remaining.md) | [common]<br>open override val [remaining](../../mahjongutils.models.hand/-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [roundWind](round-wind.md) | [common]<br>@EncodeDefault<br>open override val [roundWind](round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>场风 |
| [selfWind](self-wind.md) | [common]<br>@EncodeDefault<br>open override val [selfWind](self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>自风 |
| [tatsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/tatsu.md) | [common]<br>open override val [tatsu](../../mahjongutils.models.hand/-i-regular-hand-pattern/tatsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md)&gt;<br>搭子 |
| [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md) | [common]<br>open override val [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [tsumo](tsumo.md) | [common]<br>open override val [tsumo](tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否自摸 |
