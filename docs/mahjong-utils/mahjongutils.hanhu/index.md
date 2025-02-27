//[mahjong-utils](../../index.md)/[mahjongutils.hanhu](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ChildPoint](-child-point/index.md) | [common]<br>@Serializable<br>data class [ChildPoint](-child-point/index.md)(val ron: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html), val tsumoParent: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html), val tsumoChild: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)) : [Point](-point/index.md)<br>子家（闲家）和牌点数 |
| [HanHuOptions](-han-hu-options/index.md) | [common]<br>@Serializable<br>data class [HanHuOptions](-han-hu-options/index.md)(val aotenjou: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val hasKiriageMangan: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, val hasKazoeYakuman: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true) |
| [ParentPoint](-parent-point/index.md) | [common]<br>@Serializable<br>data class [ParentPoint](-parent-point/index.md)(val ron: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html), val tsumo: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)) : [Point](-point/index.md)<br>亲家（庄家）和牌点数 |
| [Point](-point/index.md) | [common]<br>interface [Point](-point/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getChildPointByHanHu](get-child-point-by-han-hu.md) | [common]<br>fun [getChildPointByHanHu](get-child-point-by-han-hu.md)(han: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hu: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), options: [HanHuOptions](-han-hu-options/index.md) = HanHuOptions.Default): [ChildPoint](-child-point/index.md)<br>获取子家（闲家）和牌点数 |
| [getParentPointByHanHu](get-parent-point-by-han-hu.md) | [common]<br>fun [getParentPointByHanHu](get-parent-point-by-han-hu.md)(han: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), hu: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), options: [HanHuOptions](-han-hu-options/index.md) = HanHuOptions.Default): [ParentPoint](-parent-point/index.md)<br>获取亲家（庄家）和牌点数 |
