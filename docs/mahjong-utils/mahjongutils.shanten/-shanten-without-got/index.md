//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ShantenWithoutGot](index.md)

# ShantenWithoutGot

[common]\
@Serializable

@SerialName(value = &quot;ShantenWithoutGot&quot;)

data class [ShantenWithoutGot](index.md)(val shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val advance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, val advanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val goodShapeAdvance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;? = if (shantenNum == 1) emptySet() else null, val goodShapeAdvanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 1) 0 else null, val improvement: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;? = if (shantenNum == 0) emptyMap() else null, val improvementNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 0) 0 else null, val goodShapeImprovement: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;? = if (shantenNum == 0) emptyMap() else null, val goodShapeImprovementNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 0) 0 else null) : [CommonShanten](../-common-shanten/index.md)

未摸牌的手牌的向听信息

## Constructors

| | |
|---|---|
| [ShantenWithoutGot](-shanten-without-got.md) | [common]<br>constructor(shantenNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), advance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, advanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, goodShapeAdvance: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;? = if (shantenNum == 1) emptySet() else null, goodShapeAdvanceNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 1) 0 else null, improvement: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;? = if (shantenNum == 0) emptyMap() else null, improvementNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 0) 0 else null, goodShapeImprovement: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;? = if (shantenNum == 0) emptyMap() else null, goodShapeImprovementNum: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = if (shantenNum == 0) 0 else null) |

## Properties

| Name | Summary |
|---|---|
| [advance](advance.md) | [common]<br>val [advance](advance.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;<br>进张 |
| [advanceNum](advance-num.md) | [common]<br>@EncodeDefault<br>val [advanceNum](advance-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>进张数 |
| [asWithGot](../as-with-got.md) | [common]<br>val [CommonShanten](../-common-shanten/index.md).[asWithGot](../as-with-got.md): [ShantenWithGot](../-shanten-with-got/index.md) |
| [asWithoutGot](../as-without-got.md) | [common]<br>val [CommonShanten](../-common-shanten/index.md).[asWithoutGot](../as-without-got.md): [ShantenWithoutGot](index.md) |
| [goodShapeAdvance](good-shape-advance.md) | [common]<br>@EncodeDefault<br>val [goodShapeAdvance](good-shape-advance.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;?<br>好型进张 仅当一向听时进行计算 |
| [goodShapeAdvanceNum](good-shape-advance-num.md) | [common]<br>@EncodeDefault<br>val [goodShapeAdvanceNum](good-shape-advance-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>好型进张数 仅当一向听时进行计算 |
| [goodShapeImprovement](good-shape-improvement.md) | [common]<br>@EncodeDefault<br>val [goodShapeImprovement](good-shape-improvement.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;?<br>好型改良张（能让听牌数目增加到大于4张的牌） 对于每种改良张，只计算能让进张最多的打法 仅当听牌时进行计算 |
| [goodShapeImprovementNum](good-shape-improvement-num.md) | [common]<br>@EncodeDefault<br>val [goodShapeImprovementNum](good-shape-improvement-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>好型改良张数（能让听牌数目增加到大于4张的牌） 仅当听牌时进行计算 |
| [improvement](improvement.md) | [common]<br>@EncodeDefault<br>val [improvement](improvement.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;?<br>改良张（能让听牌数目增加的牌） 对于每种改良张，只计算能让进张最多的打法 仅当听牌时进行计算 |
| [improvementNum](improvement-num.md) | [common]<br>@EncodeDefault<br>val [improvementNum](improvement-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>改良张数（能让听牌数目增加的牌） 仅当听牌时进行计算 |
| [shantenNum](shanten-num.md) | [common]<br>open override val [shantenNum](shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>向听数 |
