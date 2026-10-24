//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[CommonShanten](index.md)

# CommonShanten

@Serializable

sealed interface [CommonShanten](index.md) : [Shanten](../-shanten/index.md)

#### Inheritors

| |
|---|
| [ShantenWithoutGot](../-shanten-without-got/index.md) |
| [ShantenWithGot](../-shanten-with-got/index.md) |

## Properties

| Name | Summary |
|---|---|
| [asWithGot](../as-with-got.md) | [common]<br>val [CommonShanten](index.md).[asWithGot](../as-with-got.md): [ShantenWithGot](../-shanten-with-got/index.md) |
| [asWithoutGot](../as-without-got.md) | [common]<br>val [CommonShanten](index.md).[asWithoutGot](../as-without-got.md): [ShantenWithoutGot](../-shanten-without-got/index.md) |
| [shantenNum](../-shanten/shanten-num.md) | [common]<br>abstract val [shantenNum](../-shanten/shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>向听数，考虑自己手牌和副露中已用满的牌。 |
| [structuralShantenNum](../-shanten/structural-shanten-num.md) | [common]<br>abstract val [structuralShantenNum](../-shanten/structural-shanten-num.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>结构向听数，仅按面子、搭子和雀头结构计算，不检查和牌张是否已被自己用满。 |
