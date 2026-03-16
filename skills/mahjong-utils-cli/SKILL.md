# Mahjong Utils CLI

日本麻将工具 CLI，提供向听数计算、和牌分析、番符算点等功能。

## 功能

- **shanten**: 向听数计算（显示进张、好型进张、改良张等）
- **hora**: 和牌分析（显示役种、番符、点数）
- **point**: 番符算点（根据番数和符数计算点数）

## 构建

```bash
cd ~/.openclaw/workspace/mahjong-utils
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# JVM 可执行 JAR
./gradlew :mahjong-utils-cli:executableJar

# Native 可执行文件
./gradlew :mahjong-utils-cli:linkReleaseExecutableMacosArm64
```

## 使用方法

### 基本语法

```bash
mahjong-utils-cli <command> <tiles> [options]
```

### 牌代码格式

使用简洁的牌代码格式（如 `123m456p789s1122z`）：
- `m` = 万子（1-9）
- `p` = 筒子（1-9）
- `s` = 索子（1-9）
- `z` = 字牌（1-7，东南西北白发中）

示例：
- `123m` = 一二三万
- `456p` = 四五六筒
- `789s` = 七八九索
- `1122z` = 东东南南

### 副露格式

副露使用逗号分隔（如 `123m,456p,789s`）：
- 吃：`123m`（顺子）
- 碰：`111m`（刻子）
- 明杠：`1111m`（四张）
- 暗杠：`01110m`（0 表示暗）

## 命令详解

### 1. Shanten 命令

计算向听数和进张信息。

```bash
# 基本用法
java -jar mahjong-utils-cli-0.7.7-executable.jar shanten 123m456p789s1122z

# 带副露
java -jar mahjong-utils-cli-0.7.7-executable.jar shanten 1122z --furo 123m,456p,789s
```

**输出信息**：
- 向听数
- 进张（种类和张数）
- 一向听时：好型进张
- 听牌时：改良张、好型改良张
- 摸牌后：弃牌选择

### 2. Hora 命令

分析和牌结果。

```bash
# 基本用法
java -jar mahjong-utils-cli-0.7.7-executable.jar hora 123m456p789s1122z

# 完整参数
java -jar mahjong-utils-cli-0.7.7-executable.jar hora 123m456p789s1122z \
  --agari 2z \
  --tsumo \
  --dora 2 \
  --self-wind E \
  --round-wind E

# 带副露
java -jar mahjong-utils-cli-0.7.7-executable.jar hora 1122z \
  --furo 123m,456p,789s \
  --agari 2z
```

**参数说明**：
- `--agari <tile>`: 和牌张（默认为最后一张）
- `--tsumo`: 自摸（不加则为荣和）
- `--dora <n>`: 宝牌数量
- `--self-wind <E|S|W|N>`: 自风（东南西北）
- `--round-wind <E|S|W|N>`: 场风（东南西北）
- `--furo <furo>`: 副露（逗号分隔）

**输出信息**：
- 和牌张
- 自摸/荣和
- 宝牌数
- 役种列表
- 番数和符数
- 亲家/子家点数

### 3. Point 命令

根据番数和符数计算点数。

```bash
# 荣和点数
java -jar mahjong-utils-cli-0.7.7-executable.jar point 3 40

# 自摸点数
java -jar mahjong-utils-cli-0.7.7-executable.jar point 3 40 --tsumo
```

**参数说明**：
- `<han>`: 番数
- `<hu>`: 符数
- `--tsumo`: 自摸（不加则为荣和）

**输出信息**：
- 亲家点数
- 子家点数

## 文件路径

- **JVM JAR**: `~/.openclaw/workspace/mahjong-utils/mahjong-utils-cli/build/libs/mahjong-utils-cli-0.7.7-executable.jar`
- **Native 可执行文件**: `~/.openclaw/workspace/mahjong-utils/mahjong-utils-cli/build/bin/macosArm64/releaseExecutable/mahjong-utils-cli.kexe`

## 示例输出

### Shanten 示例

```
$ java -jar mahjong-utils-cli-0.7.7-executable.jar shanten 123m456p789s1122z

1m2m3m4p5p6p7s8s9s1z1z2z2z

听牌

进张 (4种16张):
1z2z
```

### Hora 示例

```
$ java -jar mahjong-utils-cli-0.7.7-executable.jar hora 123m456p789s1122z --agari 2z --tsumo --dora 2

1m2m3m4p5p6p7s8s9s1z1z2z2z

和牌张: 2z
自摸
宝牌: 2枚

役种:
  - Tsumo

番数: 3番
符数: 40符

亲家自摸: 2600点
子家自摸: 亲2600点 子1300点
```

### Point 示例

```
$ java -jar mahjong-utils-cli-0.7.7-executable.jar point 3 40 --tsumo

番数: 3番
符数: 40符

亲家自摸: 2600点
子家自摸: 亲2600点 子1300点
```

## 副露示例

```
$ java -jar mahjong-utils-cli-0.7.7-executable.jar hora 1122z --furo 123m,456p,789s --agari 2z

1z1z2z2z 123m 456p 789s

和牌张: 2z
荣和

役种:

番数: 0番
符数: 30符

亲家荣和: 0点
子家荣和: 0点
```
