//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[HandPattern](index.md)

# HandPattern

interface [HandPattern](index.md) : [IHasFuro](../-i-has-furo/index.md)

手牌形

#### Inheritors

| |
|---|
| [HoraHandPattern](../../mahjongutils.hora/-hora-hand-pattern/index.md) |
| [CommonHandPattern](../-common-hand-pattern/index.md) |
| [IRegularHandPattern](../-i-regular-hand-pattern/index.md) |
| [IChitoiHandPattern](../-i-chitoi-hand-pattern/index.md) |
| [IKokushiHandPattern](../-i-kokushi-hand-pattern/index.md) |

## Properties

| Name | Summary |
|---|---|
| [furo](../-i-has-furo/furo.md) | [common]<br>abstract val [furo](../-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](remaining.md) | [common]<br>abstract val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tiles](tiles.md) | [common]<br>abstract val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
