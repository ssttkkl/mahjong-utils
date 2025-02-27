//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[FuroChanceShantenArgs](index.md)/[FuroChanceShantenArgs](-furo-chance-shanten-args.md)

# FuroChanceShantenArgs

[common]\
constructor(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../../mahjongutils.models/-tile/index.md)&gt;, chanceTile: [Tile](../../mahjongutils.models/-tile/index.md), allowChi: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true, bestShantenOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, allowKuikae: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| chanceTile | 副露机会牌（能够吃、碰的牌） |
| allowChi | 是否允许吃 |
| bestShantenOnly | 仅计算最优向听数的打法（不计算退向打法） |
| allowKuikae | 是否允许食替 |
