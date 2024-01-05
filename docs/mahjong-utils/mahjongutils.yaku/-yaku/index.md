//[mahjong-utils](../../../index.md)/[mahjongutils.yaku](../index.md)/[Yaku](index.md)

# Yaku

[common]\
class [Yaku](index.md) : YakuChecker

役种

## Properties

| Name | Summary |
|---|---|
| [furoLoss](furo-loss.md) | [common]<br>val [furoLoss](furo-loss.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>副露降低多少番数 |
| [han](han.md) | [common]<br>val [han](han.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>番数 |
| [isYakuman](is-yakuman.md) | [common]<br>val [isYakuman](is-yakuman.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>是否为役满役种 |
| [menzenOnly](menzen-only.md) | [common]<br>val [menzenOnly](menzen-only.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>是否为门清限定 |
| [name](name.md) | [common]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>役种名 |

## Functions

| Name | Summary |
|---|---|
| [check](index.md#254715025%2FFunctions%2F1581026887) | [common]<br>open override fun [check](index.md#254715025%2FFunctions%2F1581026887)(pattern: [HoraHandPattern](../../mahjongutils.hora/-hora-hand-pattern/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>检测和了手牌是否具有役种 |
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
