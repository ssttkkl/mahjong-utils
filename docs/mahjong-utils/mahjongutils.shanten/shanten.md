//[mahjong-utils](../../index.md)/[mahjongutils.shanten](index.md)/[shanten](shanten.md)

# shanten

[common]\

@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)

fun [shanten](shanten.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../mahjongutils.models/-furo/index.md)&gt; = emptyList(), bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [UnionShantenResult](-union-shanten-result/index.md)

向听分析

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
fun [shanten](shanten.md)(args: [ShantenArgs](-shanten-args/index.md)): [UnionShantenResult](-union-shanten-result/index.md)

向听分析

#### Return

向听分析结果

#### Parameters

common

| | |
|---|---|
| args | 向听分析参数 |
