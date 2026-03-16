---
name: mahjong-utils-cli
description: Command-line tools for Japanese Mahjong calculations (shanten analysis, hora evaluation, han/hu to points conversion). Use when user needs to calculate mahjong game metrics via CLI with tile code format (e.g., 123m456p789s).
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
mahjong-utils-cli.sh shanten <tiles> [--furo <furo>]
```

**Tile code format**: `123m456p789s1122z`
- `m` = 万 (1-9), `p` = 筒 (1-9), `s` = 索 (1-9), `z` = 字 (1-7)

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
- `--self-wind <E|S|W|N>`: Self wind
- `--round-wind <E|S|W|N>`: Round wind
- `--furo <furo>`: Melds

**Output**: Yaku list, han/hu, points (parent/child)

### point - Convert Han/Hu to Points
```bash
mahjong-utils-cli.sh point <han> <hu> [--tsumo]
```

**Output**: Parent and child points for given han/hu

## How It Works

The wrapper script (`scripts/mahjong-utils-cli.sh`):
1. Checks for existing binary (JVM JAR or Native executable)
2. If not found, downloads latest release from GitHub
3. Prefers JVM version if Java 17+ is available, otherwise Native
4. Caches binary in `scripts/` directory for future use

No Gradle build required for end users.
