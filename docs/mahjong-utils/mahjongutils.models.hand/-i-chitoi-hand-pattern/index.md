//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[IChitoiHandPattern](index.md)

# IChitoiHandPattern

interface [IChitoiHandPattern](index.md) : [HandPattern](../-hand-pattern/index.md)

以七对子为目标的手牌

#### Inheritors

| |
|---|
| [ChitoiHoraHandPattern](../../mahjongutils.hora/-chitoi-hora-hand-pattern/index.md) |
| [ChitoiHandPattern](../-chitoi-hand-pattern/index.md) |

## Properties

| Name | Summary |
|---|---|
| [furo](furo.md) | [common]<br>open override val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [pairs](pairs.md) | [common]<br>abstract val [pairs](pairs.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>已有对子 |
| [remaining](../-hand-pattern/remaining.md) | [common]<br>abstract val [remaining](../-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
