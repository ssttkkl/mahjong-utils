---
name: mahjong-utils-cli
description: Command-line tools for Japanese Mahjong calculations including shanten analysis (with advance tiles and improvements), hora evaluation (yaku recognition, han/hu calculation), furo (meld) analysis, and han/hu to points conversion. Use when user needs to calculate mahjong game metrics via CLI with tile code format (e.g., 123m456p789s1122z).
---

# Mahjong Utils CLI

Command-line tools for Japanese Mahjong calculations with user-friendly tile code format.

## Usage

Use the wrapper script that auto-downloads the binary:

```bash
SKILL_DIR/scripts/mahjong-utils-cli.sh <command> [options]
```

Replace `SKILL_DIR` with the actual skill directory path.

## Commands

### shanten - Calculate Shanten Number
```bash
mahjong-utils-cli.sh shanten <tiles> [--furo <furo>] [--lang <zh|en|ja>]
```

**Tile code format**: `123m456p789s1122z`
- `m` = 万/Man/萬 (1-9)
- `p` = 筒/Pin/筒 (1-9)
- `s` = 索/Sou/索 (1-9)
- `z` = 字/Honor/字 (1-7: 东南西北白发中 / E-S-W-N-White-Green-Red / 東南西北白發中)

**Furo format**: `123m,456p,789s` (comma-separated)

**Output**: Shanten number, advance tiles, improvements (for tenpai)

### hora - Analyze Winning Hand
```bash
mahjong-utils-cli.sh hora <tiles> [options]
```

**Options**:
- `--agari <tile>`: Winning tile (default: last tile)
- `--tsumo`: Self-draw (default: ron)
- `--dora <n>`: Dora count
- `--self-wind <E|S|W|N>`: Self wind (东/East/東, 南/South/南, 西/West/西, 北/North/北)
- `--round-wind <E|S|W|N>`: Round wind
- `--furo <furo>`: Melds
- `--lang <zh|en|ja>`: Output language (auto-detected from LANG env if not specified)

**Output**: Yaku list, han/hu, points (parent/child)

### point - Convert Han/Hu to Points
```bash
mahjong-utils-cli.sh point <han> <hu> [--tsumo] [--lang <zh|en|ja>]
```

**Han/Hu**: 番数/Han/翻数, 符数/Fu/符数

**Output**: Parent and child points for given han/hu

## How It Works

The wrapper script (`scripts/mahjong-utils-cli.sh`):
1. Checks for existing binary (JVM JAR or Native executable)
2. If not found, downloads latest release from GitHub
3. Prefers JVM version if Java 17+ is available, otherwise Native
4. Caches binary in `scripts/` directory for future use

No Gradle build required for end users.
