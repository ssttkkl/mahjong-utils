//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[KokushiHoraHandPattern](index.md)

# KokushiHoraHandPattern

[common]\
@Serializable

@SerialName(value = &quot;KokushiHoraHandPattern&quot;)

data class [KokushiHoraHandPattern](index.md)(val repeated: [Tile](../../mahjongutils.models/-tile/index.md), val agari: [Tile](../../mahjongutils.models/-tile/index.md), val tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null, val roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null) : [HoraHandPattern](../-hora-hand-pattern/index.md), [IKokushiHandPattern](../../mahjongutils.models.hand/-i-kokushi-hand-pattern/index.md)

国士无双的和牌手牌

## Constructors

| | |
|---|---|
| [KokushiHoraHandPattern](-kokushi-hora-hand-pattern.md) | [common]<br>constructor(repeated: [Tile](../../mahjongutils.models/-tile/index.md), agari: [Tile](../../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), selfWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null, roundWind: [Wind](../../mahjongutils.models/-wind/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [agari](agari.md) | [common]<br>open override val [agari](agari.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>和牌张 |
| [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md) | [common]<br>abstract val [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md) | [common]<br>open val [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](remaining.md) | [common]<br>open override val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [repeated](repeated.md) | [common]<br>open override val [repeated](repeated.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>手牌形 |
| [roundWind](round-wind.md) | [common]<br>@EncodeDefault<br>open override val [roundWind](round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>场风 |
| [selfWind](self-wind.md) | [common]<br>@EncodeDefault<br>open override val [selfWind](self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)? = null<br>自风 |
| [thirteenWaiting](thirteen-waiting.md) | [common]<br>val [thirteenWaiting](thirteen-waiting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否十三面和牌 |
| [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md) | [common]<br>abstract val [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [tsumo](tsumo.md) | [common]<br>open override val [tsumo](tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否自摸 |
| [yaochu](yaochu.md) | [common]<br>open override val [yaochu](yaochu.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>幺九牌 |
