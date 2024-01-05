//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[HoraOptions](index.md)

# HoraOptions

[common]\
@Serializable

data class [HoraOptions](index.md)(val allowKuitan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val hasRenpuuJyantouHu: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val hasKiriageMangan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val hasKazoeYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val hasMultipleYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val hasComplexYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

和牌分析选项

## Constructors

| | |
|---|---|
| [HoraOptions](-hora-options.md) | [common]<br>constructor(allowKuitan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), hasRenpuuJyantouHu: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), hasKiriageMangan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), hasKazoeYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), hasMultipleYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), hasComplexYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [allowKuitan](allow-kuitan.md) | [common]<br>@EncodeDefault<br>val [allowKuitan](allow-kuitan.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否允许食断 |
| [hanHuOptions](han-hu-options.md) | [common]<br>val [hanHuOptions](han-hu-options.md): [HanHuOptions](../../mahjongutils.hanhu/-han-hu-options/index.md) |
| [hasComplexYakuman](has-complex-yakuman.md) | [common]<br>@EncodeDefault<br>val [hasComplexYakuman](has-complex-yakuman.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否有复合役满 |
| [hasKazoeYakuman](has-kazoe-yakuman.md) | [common]<br>@EncodeDefault<br>val [hasKazoeYakuman](has-kazoe-yakuman.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否有累计役满（false则除役满牌型外最高三倍满） |
| [hasKiriageMangan](has-kiriage-mangan.md) | [common]<br>@EncodeDefault<br>val [hasKiriageMangan](has-kiriage-mangan.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否有切上满贯 |
| [hasMultipleYakuman](has-multiple-yakuman.md) | [common]<br>@EncodeDefault<br>val [hasMultipleYakuman](has-multiple-yakuman.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否有多倍役满（false则大四喜、国士无双十三面、纯正九莲宝灯只记单倍役满） |
| [hasRenpuuJyantouHu](has-renpuu-jyantou-hu.md) | [common]<br>@EncodeDefault<br>val [hasRenpuuJyantouHu](has-renpuu-jyantou-hu.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>连风雀头是否记4符（true则记4符，false则记2符） |
