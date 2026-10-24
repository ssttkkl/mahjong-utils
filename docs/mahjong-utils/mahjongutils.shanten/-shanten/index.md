//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[Shanten](index.md)

# Shanten

@Serializable

sealed interface [Shanten](index.md)

向听信息

#### Inheritors

| |
|---|
| [CommonShanten](../-common-shanten/index.md) |
| [ShantenWithFuroChance](../-shanten-with-furo-chance/index.md) |

## Properties

| Name | Summary |
|---|---|
| [shantenNum](shanten-num.md) | [common]<br>abstract val [shantenNum](shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>向听数，考虑自己手牌和副露中已用满的牌。 |
| [structuralShantenNum](structural-shanten-num.md) | [common]<br>abstract val [structuralShantenNum](structural-shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>结构向听数，仅按面子、搭子和雀头结构计算，不检查和牌张是否已被自己用满。 |
