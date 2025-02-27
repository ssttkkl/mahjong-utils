//[mahjong-utils](../../../index.md)/[mahjongutils.hanhu](../index.md)/[ParentPoint](index.md)

# ParentPoint

[common]\
@Serializable

data class [ParentPoint](index.md)(val ron: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html), val tsumo: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)) : [Point](../-point/index.md)

亲家（庄家）和牌点数

## Constructors

| | |
|---|---|
| [ParentPoint](-parent-point.md) | [common]<br>constructor(ron: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html), tsumo: [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [ron](ron.md) | [common]<br>open override val [ron](ron.md): [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)<br>荣和点数 |
| [tsumo](tsumo.md) | [common]<br>val [tsumo](tsumo.md): [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)<br>自摸各家点数 |
| [tsumoTotal](tsumo-total.md) | [common]<br>open override val [tsumoTotal](tsumo-total.md): [ULong](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-u-long/index.html)<br>自摸总点数 |
