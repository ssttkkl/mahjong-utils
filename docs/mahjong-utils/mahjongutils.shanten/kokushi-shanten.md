//[mahjong-utils](../../index.md)/[mahjongutils.shanten](index.md)/[kokushiShanten](kokushi-shanten.md)

# kokushiShanten

[common]\
fun [kokushiShanten](kokushi-shanten.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [KokushiShantenResult](-kokushi-shanten-result/index.md)

国士无双向听分析

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
