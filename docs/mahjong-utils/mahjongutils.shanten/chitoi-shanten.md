//[mahjong-utils](../../index.md)/[mahjongutils.shanten](index.md)/[chitoiShanten](chitoi-shanten.md)

# chitoiShanten

[common]\
fun [chitoiShanten](chitoi-shanten.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [ChitoiShantenResult](-chitoi-shanten-result/index.md)

七对子向听分析

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |

[common]\
fun [chitoiShanten](chitoi-shanten.md)(args: [CommonShantenArgs](-common-shanten-args/index.md)): [ChitoiShantenResult](-chitoi-shanten-result/index.md)

七对子向听分析

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| args | 向听分析参数 |
