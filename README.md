mahjong-utils
========

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils)

日麻小工具

（Python版：[ssttkkl/mahjong-utils-py](https://github.com/ssttkkl/mahjong-utils-py)）

已实现功能：

- [x] 获取番符对应和牌点数
- [x] 手牌向听数、进张分析
- [x] 摸牌何切分析
- [x] 鸣牌分析
- [x] 和了分析（役种、番数、符数）

## 使用（Kotlin）

### 引用

```groovy
implementation "io.github.ssttkkl:mahjong-utils:{mahjongUtilsVersion}"
```

### 获取番符对应和牌点数

```kotlin
// 获取亲家X番Y符的点数
val parentPoint = getParentPointByHanHu(3, 40)
print(parentPoint.ron)  // 7700
print(parentPoint.tsumo)  // 2600

// 获取子家X番Y符的点数
val childPoint = getChildPointByHanHu(3, 40)
print(childPoint.ron)  // 5200
print(childPoint.tsumoParent)  // 2600
print(childPoint.tsumoChild)  // 1300
```

### 手牌向听数、进张分析

```kotlin
val result = shanten(Tile.parseTiles("34568m235p68s"))
val shantenInfo = result.shantenInfo as ShantenWithoutGot
print(shantenInfo.shantenNum)  // 2
print(shantenInfo.advance)  // [7m, 1p, 4p, 5p, 6s, 8s, 7s, 2p, 3p, 6m, 8m, 3m]
```

### 摸牌何切分析

```kotlin
val result = shanten(Tile.parseTiles("112233p44556s127z"))
val shantenInfo = result.shantenInfo as ShantenWithGot
print(shantenInfo.shantenNum)  // 1
print(shantenInfo.discardToAdvance)
/*
{1z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 2z, 7z], advanceNum=13, goodShapeAdvance=[2z, 7z], goodShapeAdvanceNum=6), 
2z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 7z], advanceNum=13, goodShapeAdvance=[1z, 7z], goodShapeAdvanceNum=6), 
6s=ShantenWithoutGot(shantenNum=1, advance=[1z, 2z, 7z], advanceNum=9, goodShapeAdvance=[], goodShapeAdvanceNum=0),
7z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 2z], advanceNum=13, goodShapeAdvance=[1z, 2z], goodShapeAdvanceNum=6),
5s=ShantenWithoutGot(shantenNum=2, advance=[2s, 3s, 4s, 5s, 6s, 1z, 2z, 7z, 7s], advanceNum=28, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
3p=ShantenWithoutGot(shantenNum=2, advance=[3p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
4s=ShantenWithoutGot(shantenNum=2, advance=[3s, 4s, 5s, 6s, 7s, 1z, 2z, 7z], advanceNum=24, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
2p=ShantenWithoutGot(shantenNum=2, advance=[2p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
1p=ShantenWithoutGot(shantenNum=2, advance=[1p, 4p, 3s, 6s, 1z, 2z, 7z], advanceNum=22, goodShapeAdvance=null, goodShapeAdvanceNum=null)}
*/
```

### 和了分析

```kotlin
val result = hora(
    tiles = Tile.parseTiles("12233466m111z"),
    furo = listOf(Furo("789p")),
    agari = Tile.get("1z"),
    tsumo = true,
    dora = 4,
    selfWind = Wind.East,
    roundWind = Wind.East
)

print(result.yaku)  // {SelfWind, RoundWind}
print(result.han)  // 6
print(result.hu)  // 30
print(result.parentPoint)  // ParentPoint(ron=18000, tsumo=6000)
print(result.childPoint)  // ChildPoint(ron=12000, tsumoParent=6000, tsumoChild=3000)
```

## 使用（Java）

### 引用

Maven：

```xml
<dependency>
  <groupId>io.github.ssttkkl</groupId>
  <artifactId>mahjong-utils</artifactId>
  <version>${mahjongUtilsVersion}</version>
</dependency>
```

Gradle：

```groovy
implementation "io.github.ssttkkl:mahjong-utils:{mahjongUtilsVersion}"
```

### 获取番符对应和牌点数

```java
// 获取亲家X番Y符的点数
ParentPoint parentPoint = PointByHanHuKt.getParentPointByHanHu(3, 40);
System.out.println(parentPoint.getRon());  // 7700
System.out.println(parentPoint.getTsumo());  // 2600

// 获取子家X番Y符的点数
ChildPoint childPoint = PointByHanHuKt.getChildPointByHanHu(3, 40);
System.out.println(childPoint.getRon());  // 5200
System.out.println(childPoint.getTsumoParent());  // 2600
System.out.println(childPoint.getTsumoChild());  // 1300
```

### 手牌向听数、进张分析

```java
ShantenResult result = ShantenKt.shanten(
        Tile.Companion.parseTiles("34568m235p68s"),
        Collections.emptyList(),  // furo
        true,  // calcAdvanceNum
        false  // bestShantenOnly
);
ShantenWithoutGot shantenInfo = (ShantenWithoutGot) result.getShantenInfo();
System.out.println(shantenInfo.getShantenNum());  // 2
System.out.println(shantenInfo.getAdvance());  // [7m, 1p, 4p, 5p, 6s, 8s, 7s, 2p, 3p, 6m, 8m, 3m]
```

### 摸牌何切分析

```java
ShantenResult result = ShantenKt.shanten(
        Tile.Companion.parseTiles("112233p44556s127z"),
        Collections.emptyList(),  // furo
        true,  // calcAdvanceNum
        false  // bestShantenOnly
);
ShantenWithGot shantenInfo = (ShantenWithGot) result.getShantenInfo();
System.out.println(shantenInfo.getShantenNum());  // 1
System.out.println(shantenInfo.getDiscardToAdvance());
/*
{1z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 2z, 7z], advanceNum=13, goodShapeAdvance=[2z, 7z], goodShapeAdvanceNum=6), 
2z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 7z], advanceNum=13, goodShapeAdvance=[1z, 7z], goodShapeAdvanceNum=6), 
6s=ShantenWithoutGot(shantenNum=1, advance=[1z, 2z, 7z], advanceNum=9, goodShapeAdvance=[], goodShapeAdvanceNum=0),
7z=ShantenWithoutGot(shantenNum=1, advance=[3s, 6s, 1z, 2z], advanceNum=13, goodShapeAdvance=[1z, 2z], goodShapeAdvanceNum=6),
5s=ShantenWithoutGot(shantenNum=2, advance=[2s, 3s, 4s, 5s, 6s, 1z, 2z, 7z, 7s], advanceNum=28, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
3p=ShantenWithoutGot(shantenNum=2, advance=[3p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
4s=ShantenWithoutGot(shantenNum=2, advance=[3s, 4s, 5s, 6s, 7s, 1z, 2z, 7z], advanceNum=24, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
2p=ShantenWithoutGot(shantenNum=2, advance=[2p, 3s, 6s, 1z, 2z, 7z], advanceNum=18, goodShapeAdvance=null, goodShapeAdvanceNum=null), 
1p=ShantenWithoutGot(shantenNum=2, advance=[1p, 4p, 3s, 6s, 1z, 2z, 7z], advanceNum=22, goodShapeAdvance=null, goodShapeAdvanceNum=null)}
*/
```

### 和了分析

```java
Hora result = HoraKt.hora(
        Tile.Companion.parseTiles("12233466m111z"),  // tiles
        List.of(Furo.Companion.invoke("789p", false)),  // furo
        Tile.Companion.get("1z"),  // agari
        true,  // tsumo
        4,  // dora
        Wind.East,  // selfWind
        Wind.East,  // roundWind
        Collections.emptySet()  // extraYaku
);
System.out.println(result.getYaku());  // {SelfWind, RoundWind}
System.out.println(result.getHan());  // 6
System.out.println(result.getHu());  // 30
System.out.println(result.getParentPoint());  // ParentPoint(ron=18000, tsumo=6000)
System.out.println(result.getChildPoint());  // ChildPoint(ron=12000, tsumoParent=6000, tsumoChild=3000)
```

## 使用（动态库、JS）

