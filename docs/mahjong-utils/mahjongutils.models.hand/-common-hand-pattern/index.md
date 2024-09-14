//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[CommonHandPattern](index.md)

# CommonHandPattern

@Serializable

sealed interface [CommonHandPattern](index.md) : [HandPattern](../-hand-pattern/index.md)

#### Inheritors

| |
|---|
| [RegularHandPattern](../-regular-hand-pattern/index.md) |
| [ChitoiHandPattern](../-chitoi-hand-pattern/index.md) |
| [KokushiHandPattern](../-kokushi-hand-pattern/index.md) |

## Properties

| Name | Summary |
|---|---|
| [furo](../-i-has-furo/furo.md) | [common]<br>abstract val [furo](../-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [remaining](../-hand-pattern/remaining.md) | [common]<br>abstract val [remaining](../-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tiles](../-hand-pattern/tiles.md) | [common]<br>abstract val [tiles](../-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
