mahjong-utils
========

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils)

日麻小工具

已实现功能：

- [x] 获取番符对应和牌点数
- [x] 向听数、进张分析
- [x] 和了分析（役种、番数、符数）

## 引用

```
implementation("io.github.ssttkkl:mahjong-utils:${mahjongUtilsVersion}")
```

## 使用

### 获取番符对应和牌点数

```kotlin
import mahjongutils.hanhu.*

// 获取亲家X番Y符的点数，返回(荣和点数, 自摸各家点数)
val parentPoint = getParentPointByHanHu(3, 40)  // ParentPoint(ron=7700, tsumo=2600)

// 获取子家X番Y符的点数，返回(荣和点数, 自摸庄家点数, 自摸闲家点数)
val childPoint = getChildPointByHanHu(3, 40)  // ChildPoint(ron=5200, tsumoParent=2600, tsumoChild=1300)
```

### 向听数、进张分析

未摸牌：

```kotlin
import mahjongutils.shanten.*
import mahjongutils.models.Tile

// 向听数、进张分析
val result = shanten(Tile.parseTiles("34568m235p68s"))
val shantenInfo = result.shantenInfo as ShantenWithoutGot
shantenInfo.shantenNum  // 1
shantenInfo.advance  // [3m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6s, 7s, 8s]
```

已摸牌：

```kotlin
import mahjongutils.shanten.*
import mahjongutils.models.Tile

// 向听数、进张分析（已摸牌状态）
val result = shanten(Tile.parseTiles("112233p44556s127z"))
val shantenInfo = result.shantenInfo as ShantenWithGot
shantenInfo.shantenNum  // 1
shantenInfo.discardToAdvance
/*
1z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 2z, 7z], advanceNum=13, goodShapeAdvance=[2z, 7z], goodShapeAdvanceNum=6), 
2z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 7z], advanceNum=13, goodShapeAdvance=[1z, 7z], goodShapeAdvanceNum=6), 
6s=ShantenWithoutGot(shantenNum=1, advance=[1z, 2z, 7z], advanceNum=9, goodShapeAdvance=[], goodShapeAdvanceNum=0),
7z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 2z], advanceNum=13, goodShapeAdvance=[1z, 2z], goodShapeAdvanceNum=6)},
{5s=ShantenWithoutGot(shantenNum=2, advance=[2s, 3s, 4s, 5s, 6s, 1z, 2z, 7z, 7s], advanceNum=28, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
3p=ShantenWithoutGot(shantenNum=2, advance=[3p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
4s=ShantenWithoutGot(shantenNum=2, advance=[3s, 4s, 5s, 6s, 7s, 1z, 2z, 7z], advanceNum=24, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
2p=ShantenWithoutGot(shantenNum=2, advance=[2p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
1p=ShantenWithoutGot(shantenNum=2, advance=[1p, 4p, 3s, 6s, 1z, 2z, 7z], advanceNum=22, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
*/
```

### 和了分析

```kotlin
import mahjongutils.hora.*
import mahjongutils.models.*
import mahjongutils.yaku.Yakus

val result = hora(
    tiles = Tile.parseTiles("12233466m111z"),
    furo = listOf(Furo("789p")),
    agari = Tile.get("1z"),
    tsumo = true,
    dora = 4,
    selfWind = Wind.East,
    roundWind = Wind.East
)

result.yaku  // {Yakus.SelfWind, Yakus.RoundWind}
result.han  // 6
result.hu  // 30
result.parentPoint  // ParentPoint(ron=18000, tsumo=6000)
result.childPoint  // ChildPoint(ron=12000, tsumoParent=6000, tsumoChild=3000)
```