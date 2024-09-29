mahjong-utils
========

日麻小工具

已实现功能：

- [x] 获取番符对应和牌点数
- [x] 手牌向听数、进张分析
- [x] 摸牌何切分析
- [x] 鸣牌分析
- [x] 和了分析（役种、番数、符数）

基于该库开发的App：https://github.com/ssttkkl/mahjong-utils-app

## 平台支持

本项目使用Kotlin/Multiplatform编写，支持多平台调用

对于Kotlin、Java：请直接往下阅读

对于Python：我们提供了绑定方便Python侧调用，具体请看 [python-lib/README.md](python-lib/README.md)

对于JavaScript/TypeScript：我们提供了绑定方便JS/TS侧调用，具体请看 [js-lib/README.md](js-lib/README.md)

## 使用（Kotlin/Java）

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils)

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

### 向听分析/牌理

- 向听分析：[shanten](docs/mahjong-utils/mahjongutils.shanten/shanten.md)
- 标准形向听分析：[regularShanten](docs/mahjong-utils/mahjongutils.shanten/regular-shanten.md)
- 七对子向听分析：[chitoiShanten](docs/mahjong-utils/mahjongutils.shanten/chitoi-shanten.md)
- 国士无双向听分析：[kokushiShanten](docs/mahjong-utils/mahjongutils.shanten/kokushi-shanten.md)
- 副露判断向听分析：[furoChanceShanten](docs/mahjong-utils/mahjongutils.shanten/furo-chance-shanten.md)

未摸牌：

```kotlin
val result = shanten(Tile.parseTiles("34568m235p68s"))
val shantenInfo = result.shantenInfo as ShantenWithoutGot
print(shantenInfo.shantenNum)  // 2
print(shantenInfo.advance)  // [7m, 1p, 4p, 5p, 6s, 8s, 7s, 2p, 3p, 6m, 8m, 3m]
```

已摸牌：

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

### 和牌分析

- 和牌分析：[hora](docs/mahjong-utils/mahjongutils.hora/hora.md)

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

### 根据番符获取和牌点数

- 获取子家（闲家）和牌点数：[getChildPointByHanHu](docs/mahjong-utils/mahjongutils.hanhu/get-child-point-by-han-hu.md)
- 获取亲家（庄家）和牌点数：[getParentPointByHanHu](docs/mahjong-utils/mahjongutils.hanhu/get-parent-point-by-han-hu.md)


```kotlin
// 获取亲家X番Y符的点数
val parentPoint = getParentPointByHanHu(3, 40)
print(parentPoint.ron)  // 7700
print(parentPoint.tsumo)  // 2600
print(parentPoint.tsumoTotal) // 7800

// 获取子家X番Y符的点数
val childPoint = getChildPointByHanHu(3, 40)
print(childPoint.ron)  // 5200
print(childPoint.tsumoParent)  // 2600
print(childPoint.tsumoChild)  // 1300
print(childPoint.tsumoTotal) // 5200
```

### API文档

[API文档](docs/index.md)

## 使用（动态库）

为方便其他语言以动态库的形式调用，mahjong-utils-entry项目将mahjong-utils的功能封装，并暴露一个统一入口 `call` 方法供外部调用。
其参数与返回值均为JSON编码的字符串。

### 构建

确保本地安装了JDK11或更高版本，克隆本项目后，执行：

```shell
./gradlew :mahjong-utils-entry:linkReleaseSharedNative
```

会在mahjong-utils-entry/build/bin/native/releaseShared目录下得到动态库产物与头文件。

### 使用

以C语言为例：

```c
#include <stdio.h>
#include "libmahjongutils_api.h"

int main(int argc, char** argv) {
  libmahjongutils_ExportedSymbols* lib = libmahjongutils_symbols();

  const char* params = "{\"tiles\":[\"1s\",\"1s\",\"1s\",\"2s\",\"3s\",\"4s\",\"5s\",\"6s\",\"7s\",\"8s\"]}";
  const char* result = lib->kotlin.root.mahjongutils.entry.call("shanten", params);
  printf("%s", result);

  return 0;
}
```

输出：

```
{"data":{"hand":{"tiles":["1s","1s","1s","2s","3s","4s","5s","6s","7s","8s"],"furo":[],"patterns":[{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","234s","567s"],"furo":[],"tatsu":[],"remaining":["8s"]},{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","234s","678s"],"furo":[],"tatsu":[],"remaining":["5s"]},{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","345s","678s"],"furo":[],"tatsu":[],"remaining":["2s"]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["123s","456s"],"furo":[],"tatsu":["78s"],"remaining":[]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["123s","678s"],"furo":[],"tatsu":["45s"],"remaining":[]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["345s","678s"],"furo":[],"tatsu":["12s"],"remaining":[]}]},"shantenInfo":{"type":"ShantenWithoutGot","shantenNum":0,"advance":["8s","5s","2s","6s","9s","3s"],"advanceNum":19,"goodShapeAdvance":null,"goodShapeAdvanceNum":null,"improvement":{},"improvementNum":0,"goodShapeImprovement":{},"goodShapeImprovementNum":0},"regular":{"hand":{"tiles":["1s","1s","1s","2s","3s","4s","5s","6s","7s","8s"],"furo":[],"patterns":[{"k":3,"jyantou":null,"menzenMentsu":["111s","234s","567s"],"furo":[],"tatsu":[],"remaining":["8s"]},{"k":3,"jyantou":null,"menzenMentsu":["111s","234s","678s"],"furo":[],"tatsu":[],"remaining":["5s"]},{"k":3,"jyantou":null,"menzenMentsu":["111s","345s","678s"],"furo":[],"tatsu":[],"remaining":["2s"]},{"k":3,"jyantou":"1s","menzenMentsu":["123s","456s"],"furo":[],"tatsu":["78s"],"remaining":[]},{"k":3,"jyantou":"1s","menzenMentsu":["123s","678s"],"furo":[],"tatsu":["45s"],"remaining":[]},{"k":3,"jyantou":"1s","menzenMentsu":["345s","678s"],"furo":[],"tatsu":["12s"],"remaining":[]}]},"shantenInfo":{"type":"ShantenWithoutGot","shantenNum":0,"advance":["8s","5s","2s","6s","9s","3s"],"advanceNum":19,"goodShapeAdvance":null,"goodShapeAdvanceNum":null,"improvement":{},"improvementNum":0,"goodShapeImprovement":{},"goodShapeImprovementNum":0}},"chitoi":null,"kokushi":null},"code":200,"msg":""}
```

返回值有三个字段：`code`、`msg`与`data`。类似一般的WebAPI，其code指明执行是否成功，msg当执行失败时指明错误原因，data则为执行结果。

关于Kotlin作为动态库调用的更多信息可参考官方文档： https://kotlinlang.org/docs/native-dynamic-libraries.html#use-generated-headers-from-c
