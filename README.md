mahjong-utils
========

[中文](README-zh.md)

Japanese Mahjong Utilities

Implemented Features:

- [x] Calculate winning points based on han and fu
- [x] Shanten analysis and waiting tile evaluation
- [x] Tile discard analysis after drawing
- [x] Call (meld) analysis
- [x] Winning hand analysis (yaku types, han count, fu count)

App developed based on this library: https://github.com/ssttkkl/mahjong-utils-app

## DeepWiki

~~Good news! This component has officially partnered with DeepWiki. Now you can ask AI directly instead of wasting time posting Issues!~~

Documentation: https://deepwiki.com/ssttkkl/mahjong-utils

Example question: [How to calculate all yaku in a winning hand?](https://deepwiki.com/search/how-to-calculate-all-yaku-in-a_305e1f5e-acf7-4cc8-9d92-dd0c874b4434)

## Platform Support

This project uses Kotlin/Multiplatform and supports multiple platforms:

For Kotlin/Java: Continue reading below

For Python: We provide Python bindings - see [python-lib/README.md](python-lib/README.md)

For JavaScript/TypeScript: We provide JS/TS bindings - see [js-lib/README.md](js-lib/README.md)

For CLI: We provide command-line tools - see [skills/mahjong-utils-cli/SKILL.md](skills/mahjong-utils-cli/SKILL.md)

## Project Modules

- **mahjong-utils**: Core library with shanten analysis, hora calculation, and point calculation
- **mahjong-utils-cli**: Command-line interface with user-friendly tile code format
- **mahjong-utils-entry**: Dynamic library entry point for cross-language integration
- **mahjong-utils-webapi**: Web API server (Spring Boot)
- **python-lib**: Python bindings
- **js-lib**: JavaScript/TypeScript bindings

## Usage (Kotlin/Java)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ssttkkl/mahjong-utils)

### Dependency

Maven:

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

### Shanten analysis

- Shanten analysis: [shanten](docs/mahjong-utils/mahjongutils.shanten/shanten.md)
- Regular pattern shanten analysis: [regularShanten](docs/mahjong-utils/mahjongutils.shanten/regular-shanten.md)
- Seven Pairs shanten analysis: [chitoiShanten](docs/mahjong-utils/mahjongutils.shanten/chitoi-shanten.md)
- Kokushi Musou shanten analysis: [kokushiShanten](docs/mahjong-utils/mahjongutils.shanten/kokushi-shanten.md)
- Call opportunity shanten analysis: [furoChanceShanten](docs/mahjong-utils/mahjongutils.shanten/furo-chance-shanten.md)

Without drawn tile:

```kotlin
val result = shanten(Tile.parseTiles("34568m235p68s"))
val shantenInfo = result.shantenInfo as ShantenWithoutGot
print(shantenInfo.shantenNum)  // 2
print(shantenInfo.advance)  // [7m, 1p, 4p, 5p, 6s, 8s, 7s, 2p, 3p, 6m, 8m, 3m]
```

With drawn tile:

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

### Winning Hand Analysis

- Winning Hand Analysis: [hora](docs/mahjong-utils/mahjongutils.hora/hora.md)

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

### Calculate Points from Han/Fu

- Get non-dealer points: [getChildPointByHanHu](docs/mahjong-utils/mahjongutils.hanhu/get-child-point-by-han-hu.md)
- Get dealer points: [getParentPointByHanHu](docs/mahjong-utils/mahjongutils.hanhu/get-parent-point-by-han-hu.md)

```kotlin
// Dealer points for 3 han 40 fu
val parentPoint = getParentPointByHanHu(3, 40)
print(parentPoint.ron)  // 7700
print(parentPoint.tsumo)  // 2600
print(parentPoint.tsumoTotal) // 7800

// Non-dealer points for 3 han 40 fu
val childPoint = getChildPointByHanHu(3, 40)
print(childPoint.ron)  // 5200
print(childPoint.tsumoParent)  // 2600
print(childPoint.tsumoChild)  // 1300
print(childPoint.tsumoTotal) // 5200
```

### API Documentation

[API Documentation](docs/index.md)

## Dynamic Library Usage

For cross-language integration, mahjong-utils-entry exposes a unified `call` method with JSON-formatted parameters and responses.

### Build

Ensure JDK11+ is installed. Clone the project and run:

```shell
./gradlew :mahjong-utils-entry:linkReleaseSharedNative
```

Output files will be in mahjong-utils-entry/build/bin/native/releaseShared.

### Usage Example (C)

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

Output contains three fields: code (status code), msg (error message), and data (result payload).

```
{"data":{"hand":{"tiles":["1s","1s","1s","2s","3s","4s","5s","6s","7s","8s"],"furo":[],"patterns":[{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","234s","567s"],"furo":[],"tatsu":[],"remaining":["8s"]},{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","234s","678s"],"furo":[],"tatsu":[],"remaining":["5s"]},{"type":"RegularHandPattern","k":3,"jyantou":null,"menzenMentsu":["111s","345s","678s"],"furo":[],"tatsu":[],"remaining":["2s"]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["123s","456s"],"furo":[],"tatsu":["78s"],"remaining":[]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["123s","678s"],"furo":[],"tatsu":["45s"],"remaining":[]},{"type":"RegularHandPattern","k":3,"jyantou":"1s","menzenMentsu":["345s","678s"],"furo":[],"tatsu":["12s"],"remaining":[]}]},"shantenInfo":{"type":"ShantenWithoutGot","shantenNum":0,"advance":["8s","5s","2s","6s","9s","3s"],"advanceNum":19,"goodShapeAdvance":null,"goodShapeAdvanceNum":null,"improvement":{},"improvementNum":0,"goodShapeImprovement":{},"goodShapeImprovementNum":0},"regular":{"hand":{"tiles":["1s","1s","1s","2s","3s","4s","5s","6s","7s","8s"],"furo":[],"patterns":[{"k":3,"jyantou":null,"menzenMentsu":["111s","234s","567s"],"furo":[],"tatsu":[],"remaining":["8s"]},{"k":3,"jyantou":null,"menzenMentsu":["111s","234s","678s"],"furo":[],"tatsu":[],"remaining":["5s"]},{"k":3,"jyantou":null,"menzenMentsu":["111s","345s","678s"],"furo":[],"tatsu":[],"remaining":["2s"]},{"k":3,"jyantou":"1s","menzenMentsu":["123s","456s"],"furo":[],"tatsu":["78s"],"remaining":[]},{"k":3,"jyantou":"1s","menzenMentsu":["123s","678s"],"furo":[],"tatsu":["45s"],"remaining":[]},{"k":3,"jyantou":"1s","menzenMentsu":["345s","678s"],"furo":[],"tatsu":["12s"],"remaining":[]}]},"shantenInfo":{"type":"ShantenWithoutGot","shantenNum":0,"advance":["8s","5s","2s","6s","9s","3s"],"advanceNum":19,"goodShapeAdvance":null,"goodShapeAdvanceNum":null,"improvement":{},"improvementNum":0,"goodShapeImprovement":{},"goodShapeImprovementNum":0}},"chitoi":null,"kokushi":null},"code":200,"msg":""}
```

For more about Kotlin/Native dynamic libraries: https://kotlinlang.org/docs/native-dynamic-libraries.html#use-generated-headers-from-c
