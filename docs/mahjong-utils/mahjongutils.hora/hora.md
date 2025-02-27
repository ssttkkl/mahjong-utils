//[mahjong-utils](../../index.md)/[mahjongutils.hora](index.md)/[hora](hora.md)

# hora

[common]\
fun [hora](hora.md)(tiles: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Tile](../mahjongutils.models/-tile/index.md)&gt;, furo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Furo](../mahjongutils.models/-furo/index.md)&gt; = DEFAULT_FURO, agari: [Tile](../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), dora: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_DORA, selfWind: [Wind](../mahjongutils.models/-wind/index.md)? = DEFAULT_SELF_WIND, roundWind: [Wind](../mahjongutils.models/-wind/index.md)? = DEFAULT_ROUND_WIND, extraYaku: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Yaku](../mahjongutils.yaku/-yaku/index.md)&gt; = DEFAULT_EXTRA_YAKU, options: [HoraOptions](-hora-options/index.md) = DEFAULT_OPTIONS): [Hora](-hora/index.md)

和牌分析

#### Return

和牌分析结果

#### Parameters

common

| | |
|---|---|
| tiles | 门前的牌 |
| furo | 副露 |
| agari | 和牌张 |
| tsumo | 是否自摸 |
| dora | 宝牌数目 |
| selfWind | 自风 |
| roundWind | 场风 |
| extraYaku | 额外役种 |

[common]\
fun [hora](hora.md)(shantenResult: [CommonShantenResult](../mahjongutils.shanten/-common-shanten-result/index.md)&lt;*&gt;, agari: [Tile](../mahjongutils.models/-tile/index.md), tsumo: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), dora: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = DEFAULT_DORA, selfWind: [Wind](../mahjongutils.models/-wind/index.md)? = DEFAULT_SELF_WIND, roundWind: [Wind](../mahjongutils.models/-wind/index.md)? = DEFAULT_ROUND_WIND, extraYaku: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[Yaku](../mahjongutils.yaku/-yaku/index.md)&gt; = DEFAULT_EXTRA_YAKU, options: [HoraOptions](-hora-options/index.md) = DEFAULT_OPTIONS): [Hora](-hora/index.md)

和牌分析

#### Return

和牌分析结果

#### Parameters

common

| | |
|---|---|
| shantenResult | 向听分析结果 |
| agari | 和牌张 |
| tsumo | 是否自摸 |
| dora | 宝牌数目 |
| selfWind | 自风 |
| roundWind | 场风 |
| extraYaku | 额外役种（不会对役种合法性进行检查） |

[common]\
fun [hora](hora.md)(args: [HoraArgs](-hora-args/index.md)): [Hora](-hora/index.md)
