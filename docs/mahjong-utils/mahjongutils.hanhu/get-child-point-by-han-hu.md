//[mahjong-utils](../../index.md)/[mahjongutils.hanhu](index.md)/[getChildPointByHanHu](get-child-point-by-han-hu.md)

# getChildPointByHanHu

[common]\
fun [getChildPointByHanHu](get-child-point-by-han-hu.md)(han: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), hu: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), options: [HanHuOptions](-han-hu-options/index.md) = HanHuOptions.Default): [ChildPoint](-child-point/index.md)

获取子家（闲家）和牌点数

#### Return

子家（闲家）和牌点数

#### Parameters

common

| | |
|---|---|
| han | 番数 |
| hu | 符数 |
| hasKiriageMangan | 是否有切上满贯 |
| hasKazoeYakuman | 是否有累计役满 |
