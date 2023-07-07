//[mahjong-utils](../../../index.md)/[mahjongutils.models.hand](../index.md)/[RegularHandPattern](index.md)

# RegularHandPattern

[common]\
@Serializable

data class [RegularHandPattern](index.md)(val k: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val jyantou: [Tile](../../mahjongutils.models/-tile/index.md)?, val menzenMentsu: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;, val furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;, val tatsu: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md)&gt;, val remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) : [IRegularHandPattern](../-i-regular-hand-pattern/index.md), [CommonHandPattern](../-common-hand-pattern/index.md)

以标准形为目标的手牌

## Constructors

| | |
|---|---|
| [RegularHandPattern](-regular-hand-pattern.md) | [common]<br>constructor(k: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), jyantou: [Tile](../../mahjongutils.models/-tile/index.md)?, menzenMentsu: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;, tatsu: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md)&gt;, remaining: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [anko](../-i-regular-hand-pattern/anko.md) | [common]<br>open val [anko](../-i-regular-hand-pattern/anko.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Kotsu](../../mahjongutils.models/-kotsu/index.md)&gt;<br>暗刻 |
| [furo](furo.md) | [common]<br>open override val [furo](furo.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt;<br>副露 |
| [jyantou](jyantou.md) | [common]<br>open override val [jyantou](jyantou.md): [Tile](../../mahjongutils.models/-tile/index.md)?<br>雀头 |
| [k](k.md) | [common]<br>open override val [k](k.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>目标面子组数（=手牌数/4） |
| [mentsu](../-i-regular-hand-pattern/mentsu.md) | [common]<br>open val [mentsu](../-i-regular-hand-pattern/mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>面子（包括门前与副露） |
| [menzen](../-i-has-furo/menzen.md) | [common]<br>open val [menzen](../-i-has-furo/menzen.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否门清 |
| [menzenMentsu](menzen-mentsu.md) | [common]<br>open override val [menzenMentsu](menzen-mentsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Mentsu](../../mahjongutils.models/-mentsu/index.md)&gt;<br>门前面子 |
| [remaining](remaining.md) | [common]<br>open override val [remaining](remaining.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>浮牌 |
| [tatsu](tatsu.md) | [common]<br>open override val [tatsu](tatsu.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tatsu](../../mahjongutils.models/-tatsu/index.md)&gt;<br>搭子 |
| [tiles](../-i-regular-hand-pattern/tiles.md) | [common]<br>open override val [tiles](../-i-regular-hand-pattern/tiles.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>手牌（包括门前与副露） |
