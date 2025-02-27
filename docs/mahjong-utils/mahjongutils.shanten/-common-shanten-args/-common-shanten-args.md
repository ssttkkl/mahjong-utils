//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[CommonShantenArgs](index.md)/[CommonShantenArgs](-common-shanten-args.md)

# CommonShantenArgs

[common]\
constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../../mahjongutils.models/-furo/index.md)&gt; = emptyList(), bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| furo | 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入） |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
| mode | 向听分析模式 |
