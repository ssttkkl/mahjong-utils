//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[IRegularHandPattern](index.md)

# IRegularHandPattern

interface [IRegularHandPattern](index.md) : [HandPattern](../-hand-pattern/index.md)

以标准形为目标的手牌

#### Inheritors

| |
|---|
| [RegularHoraHandPattern](../../mahjongutils.hora/-regular-hora-hand-pattern/index.md) |
| [RegularHandPattern](../-regular-hand-pattern/index.md) |

## Properties

| Name | Summary |
|---|---|
| [anko](anko.md) | [common]<br>open val [anko](anko.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Kotsu](../../mahjongutils.models/-kotsu/index.md)&gt;<br>暗刻 |
| [furo](../-i-has-furo/furo.md) | [common]<br>abstract val [furo](../-i-has-furo/furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [jyantou](jyantou.md) | [common]<br>abstract val [jyantou](jyantou.md): [Tile](../../mahjongutils.models/-tile/index.md)?<br>雀头 |
| [k](k.md) | [common]<br>abstract val [k](k.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>目标面子组数（=手牌数/4） |
| [mentsu](mentsu.md) | [common]<br>open val [mentsu](mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>面子（包括门前与副露） |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [menzenMentsu](menzen-mentsu.md) | [common]<br>abstract val [menzenMentsu](menzen-mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>门前面子 |
| [remaining](../-hand-pattern/remaining.md) | [common]<br>abstract val [remaining](../-hand-pattern/remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tatsu](tatsu.md) | [common]<br>abstract val [tatsu](tatsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md)&gt;<br>搭子 |
| [tiles](tiles.md) | [common]<br>open override val [tiles](tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
