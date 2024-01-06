//[mahjong-utils](../../../index.md)/[mahjongutils.hanhu](../index.md)/[ParentPoint](index.md)

# ParentPoint

[common]\
@Serializable

data class [ParentPoint](index.md)(val ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val tsumo: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Point](../-point/index.md)

亲家（庄家）和牌点数

## Constructors

| | |
|---|---|
| [ParentPoint](-parent-point.md) | [common]<br>constructor(ron: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), tsumo: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [ron](ron.md) | [common]<br>open override val [ron](ron.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>荣和点数 |
| [tsumo](tsumo.md) | [common]<br>val [tsumo](tsumo.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>自摸各家点数 |
| [tsumoTotal](tsumo-total.md) | [common]<br>open override val [tsumoTotal](tsumo-total.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>自摸总点数 |
