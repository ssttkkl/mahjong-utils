//[mahjong-utils](../../index.md)/[mahjongutils.hanhu](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ChildPoint](-child-point/index.md) | [common]<br>@Serializable<br>data class [ChildPoint](-child-point/index.md)(val ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumoParent: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumoChild: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>子家（闲家）和牌点数 |
| [ParentPoint](-parent-point/index.md) | [common]<br>@Serializable<br>data class [ParentPoint](-parent-point/index.md)(val ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumo: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>亲家（庄家）和牌点数 |

## Functions

| Name | Summary |
|---|---|
| [getChildPointByHanHu](get-child-point-by-han-hu.md) | [common]<br>fun [getChildPointByHanHu](get-child-point-by-han-hu.md)(han: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), hu: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ChildPoint](-child-point/index.md)<br>获取子家（闲家）和牌点数 |
| [getParentPointByHanHu](get-parent-point-by-han-hu.md) | [common]<br>fun [getParentPointByHanHu](get-parent-point-by-han-hu.md)(han: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), hu: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ParentPoint](-parent-point/index.md)<br>获取亲家（庄家）和牌点数 |
