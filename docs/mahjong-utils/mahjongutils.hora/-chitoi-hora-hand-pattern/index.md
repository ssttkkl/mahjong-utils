//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[ChitoiHoraHandPattern](index.md)

# ChitoiHoraHandPattern

[common]\
@Serializable

@SerialName(value = &quot;ChitoiHoraHandPattern&quot;)

data class [ChitoiHoraHandPattern](index.md)(val pairs: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val agari: [Tile](../../mahjongutils.models/-tile/index.md), val tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null, val roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null) : [HoraHandPattern](../-hora-hand-pattern/index.md), [IChitoiHandPattern](../../mahjongutils.models.hand/-i-chitoi-hand-pattern/index.md)

七对子的和牌手牌

## Constructors

| | |
|---|---|
| [ChitoiHoraHandPattern](-chitoi-hora-hand-pattern.md) | [common]<br>constructor(pairs: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, agari: [Tile](../../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null, roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [agari](agari.md) | [common]<br>open override val [agari](agari.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>和牌张 |
| [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md) | [common]<br>abstract val [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md) | [common]<br>open val [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [pairs](pairs.md) | [common]<br>open override val [pairs](pairs.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌形 |
| [remaining](remaining.md) | [common]<br>open override val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [roundWind](round-wind.md) | [common]<br>@EncodeDefault<br>open override val [roundWind](round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>场风 |
| [selfWind](self-wind.md) | [common]<br>@EncodeDefault<br>open override val [selfWind](self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>自风 |
| [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md) | [common]<br>abstract val [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [tsumo](tsumo.md) | [common]<br>open override val [tsumo](tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否自摸 |
