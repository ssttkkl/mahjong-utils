---
name: mahjong-utils-cli
description: Command-line tools for Japanese Mahjong calculations (shanten analysis, hora evaluation, han/hu to points conversion). Use when user needs to calculate mahjong game metrics via CLI with tile code format (e.g., 123m456p789s).
---

# Mahjong Utils CLI

Command-line tools for Japanese Mahjong calculations with user-friendly tile code format.

## Commands

### shanten - Calculate Shanten Number
```bash
java -jar mahjong-utils-cli-0.7.7-executable.jar shanten <tiles> [--furo <furo>]
```

**Tile code format**: `123m456p789s1122z`
- `m` = 万 (1-9), `p` = 筒 (1-9), `s` = 索 (1-9), `z` = 字 (1-7)

**Furo format**: `123m,456p,789s` (comma-separated)

**Output**: Shanten number, advance tiles, improvements (for tenpai)

### hora - Analyze Winning Hand
```bash
java -jar mahjong-utils-cli-0.7.7-executable.jar hora <tiles> [options]
```

**Options**:
- `--agari <tile>`: Winning tile (default: last tile)
- `--tsumo`: Self-draw (default: ron)
- `--dora <n>`: Dora count
- `--self-wind <E|S|W|N>`: Self wind
- `--round-wind <E|S|W|N>`: Round wind
- `--furo <furo>`: Melds

**Output**: Yaku list, han/hu, points (parent/child)

### point - Convert Han/Hu to Points
```bash
java -jar mahjong-utils-cli-0.7.7-executable.jar point <han> <hu> [--tsumo]
```

**Output**: Parent and child points for given han/hu

## Build Paths

**JVM JAR**: `~/.openclaw/workspace/mahjong-utils/mahjong-utils-cli/build/libs/mahjong-utils-cli-0.7.7-executable.jar`

**Native**: `~/.openclaw/workspace/mahjong-utils/mahjong-utils-cli/build/bin/macosArm64/releaseExecutable/mahjong-utils-cli.kexe`

**Build commands**:
```bash
cd ~/.openclaw/workspace/mahjong-utils
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./gradlew :mahjong-utils-cli:executableJar  # JVM
./gradlew :mahjong-utils-cli:linkReleaseExecutableMacosArm64  # Native
```

## Examples

```bash
# Shanten with furo
java -jar mahjong-utils-cli-0.7.7-executable.jar shanten 1122z --furo 123m,456p,789s

# Hora with full parameters
java -jar mahjong-utils-cli-0.7.7-executable.jar hora 123m456p789s1122z --agari 2z --tsumo --dora 2 --self-wind E --round-wind E

# Point calculation
java -jar mahjong-utils-cli-0.7.7-executable.jar point 3 40 --tsumo
```
