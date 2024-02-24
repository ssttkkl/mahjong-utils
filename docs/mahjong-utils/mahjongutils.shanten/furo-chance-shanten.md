//[mahjong-utils](../../index.md)/[mahjongutils.shanten](index.md)/[furoChanceShanten](furo-chance-shanten.md)

# furoChanceShanten

[common]\
fun [furoChanceShanten](furo-chance-shanten.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, chanceTile: [Tile](../mahjongutils.models/-tile/index.md), allowChi: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, allowKuikae: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [FuroChanceShantenResult](-furo-chance-shanten-result/index.md)

副露判断向听分析

#### Return

向听分析结果（其中shantenInfo必定为ShantenWithFuroChance类型）

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| chanceTile | 副露机会牌（能够吃、碰的牌） |
| allowChi | 是否允许吃 |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
| allowKuikae | 是否允许食替 |

[common]\
fun [furoChanceShanten](furo-chance-shanten.md)(args: [FuroChanceShantenArgs](-furo-chance-shanten-args/index.md)): [FuroChanceShantenResult](-furo-chance-shanten-result/index.md)
