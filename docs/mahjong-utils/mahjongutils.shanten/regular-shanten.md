//[mahjong-utils](../../index.md)/[mahjongutils.shanten](index.md)/[regularShanten](regular-shanten.md)

# regularShanten

[common]\
fun [regularShanten](regular-shanten.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../mahjongutils.models/-furo/index.md)&gt; = listOf(), bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [RegularShantenResult](-regular-shanten-result/index.md)

标准形向听分析（只考虑4面子+1雀头和牌的形状）

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| furo | 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入） |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |

[common]\
fun [regularShanten](regular-shanten.md)(args: [CommonShantenArgs](-common-shanten-args/index.md)): [RegularShantenResult](-regular-shanten-result/index.md)

标准形向听分析（只考虑4面子+1雀头和牌的形状）

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| args | 向听分析参数 |
