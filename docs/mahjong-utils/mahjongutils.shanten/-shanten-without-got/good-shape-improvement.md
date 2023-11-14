//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ShantenWithoutGot](index.md)/[goodShapeImprovement](good-shape-improvement.md)

# goodShapeImprovement

[common]\

@EncodeDefault

val [goodShapeImprovement](good-shape-improvement.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Improvement](../-improvement/index.md)&gt;&gt;?

好型改良张（能让听牌数目增加到大于4张的牌） 对于每种改良张，只计算能让进张最多的打法 仅当听牌时进行计算
