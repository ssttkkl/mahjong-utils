//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[HoraHandPattern](index.md)

# HoraHandPattern

@Serializable

interface [HoraHandPattern](index.md) : [HoraInfo](../-hora-info/index.md), [HandPattern](../../mahjongutils.models.hand/-hand-pattern/index.md)

和牌手牌

#### Inheritors

| |
|---|
| [RegularHoraHandPattern](../-regular-hora-hand-pattern/index.md) |
| [ChitoiHoraHandPattern](../-chitoi-hora-hand-pattern/index.md) |
| [KokushiHoraHandPattern](../-kokushi-hora-hand-pattern/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [agari](../-hora-info/agari.md) | [common]<br>abstract val [agari](../-hora-info/agari.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>和牌张 |
| [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md) | [common]<br>abstract val [furo](../../mahjongutils.models.hand/-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md) | [common]<br>open val [menzen](../../mahjongutils.models.hand/-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](../../mahjongutils.models.hand/-hand-pattern/remaining.md) | [common]<br>abstract val [remaining](../../mahjongutils.models.hand/-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [roundWind](../-hora-info/round-wind.md) | [common]<br>abstract val [roundWind](../-hora-info/round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)?<br>场风 |
| [selfWind](../-hora-info/self-wind.md) | [common]<br>abstract val [selfWind](../-hora-info/self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)?<br>自风 |
| [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md) | [common]<br>abstract val [tiles](../../mahjongutils.models.hand/-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [tsumo](../-hora-info/tsumo.md) | [common]<br>abstract val [tsumo](../-hora-info/tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否自摸 |
