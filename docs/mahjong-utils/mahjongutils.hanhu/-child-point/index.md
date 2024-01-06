//[mahjong-utils](../../../index.md)/[mahjongutils.hanhu](../index.md)/[ChildPoint](index.md)

# ChildPoint

[common]\
@Serializable

data class [ChildPoint](index.md)(val ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumoParent: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumoChild: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Point](../-point/index.md)

子家（闲家）和牌点数

## Constructors

| | |
|---|---|
| [ChildPoint](-child-point.md) | [common]<br>constructor(ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), tsumoParent: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), tsumoChild: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [ron](ron.md) | [common]<br>open override val [ron](ron.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>荣和点数 |
| [tsumoChild](tsumo-child.md) | [common]<br>val [tsumoChild](tsumo-child.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>自摸子家（闲家）点数 |
| [tsumoParent](tsumo-parent.md) | [common]<br>val [tsumoParent](tsumo-parent.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>自摸亲家（庄家）点数 |
| [tsumoTotal](tsumo-total.md) | [common]<br>open override val [tsumoTotal](tsumo-total.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>自摸总点数 |
