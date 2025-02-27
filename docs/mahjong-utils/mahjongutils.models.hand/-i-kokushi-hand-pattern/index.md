//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[IKokushiHandPattern](index.md)

# IKokushiHandPattern

interface [IKokushiHandPattern](index.md) : [HandPattern](../-hand-pattern/index.md)

以国士无双为目标的手牌

#### Inheritors

| |
|---|
| [KokushiHoraHandPattern](../../mahjongutils.hora/-kokushi-hora-hand-pattern/index.md) |
| [KokushiHandPattern](../-kokushi-hand-pattern/index.md) |

## Properties

| Name | Summary |
|---|---|
| [furo](furo.md) | [common]<br>open override val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](../-hand-pattern/remaining.md) | [common]<br>abstract val [remaining](../-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [repeated](repeated.md) | [common]<br>abstract val [repeated](repeated.md): [Tile](../../mahjongutils.models/-tile/index.md)?<br>重复的幺九牌 |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
| [yaochu](yaochu.md) | [common]<br>abstract val [yaochu](yaochu.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>幺九牌 |
