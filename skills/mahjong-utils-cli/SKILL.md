---
name: mahjong-utils-cli
description: Command-line tools for Japanese Mahjong calculations including shanten analysis (with advance tiles and improvements), hora evaluation (yaku recognition, han/hu calculation), furo (meld) analysis, and han/hu to points conversion. Supports multi-step推演 (simulation) for deeper analysis - after calculating current shanten, you can simulate "discard X then draw Y" scenarios to explore optimal strategies. Note that local optimal choices (e.g., max advance tiles) may not be globally optimal due to tile flow, opponent discards, and hand development potential.
---

# Mahjong Utils CLI

Command-line tools for Japanese Mahjong calculations with user-friendly tile code format.

## Multi-Step Analysis (推演) Capability

This tool supports **iterative multi-step analysis**. After getting initial results, you can:

1. **Simulate discarding**: Take the current hand, remove a tile (打X)
2. **Simulate drawing**: Add a tile from advance tiles (摸Y)
3. **Re-calculate**: Run shanten again on the new hand to see the evolved shape

**Important Principle**: 局部最优 ≠ 综合最优 (Local optimal ≠ Global optimal)
- A discard with maximum advance tiles may lead to awkward shapes later
- Consider: tile flow (壁牌), opponent discard patterns, hand development potential
- Use multi-step simulation to evaluate 2-3 moves ahead for critical decisions

### Example Workflow

```
Step 1: Initial calculation
  $ shanten 1135799m2246p579s
  → 3向听, 打6p has 55种68张进张

Step 2: Simulate 打6p摸1m (from advance tiles)
  → Remove 6p, add 1m to hand
  → New hand: 11135799m224p579s
  → Run shanten again to see evolved shape

Step 3: Compare with alternative 打1m摸2m
  → Evaluate which path leads to better development
```

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
- `m` = 万/Man/萬(マンズ) (1-9)
- `p` = 筒/Pin/筒(ピンズ) (1-9)
- `s` = 索/Sou/索(ソーズ) (1-9)
- `z` = 字/Honor/字牌(ジハイ) (1-7: 东南西北白发中 / E-S-W-N-White-Green-Red / 東南西北白發中(トンナンシャーペーパイハクハツチュン))

**Furo format**: `123m,456p,789s` (comma-separated)
- Chi (吃/Chii/チー): `123m` (sequence)
- Pon (碰/Pon/ポン): `111m` (triplet)
- Minkan (明杠/Open Kan/明カン): `1111m` (open quad)
- Kakan (加杠/Added Kan/加カン): `1111m` (added kan, same format as minkan)
- Ankan (暗杠/Closed Kan/暗カン): `1111m` (closed quad, same format)

**Output**: 
- Shanten number
- Advance tiles (进张牌)
- **Advance discard analysis (进张后弃牌)**: For each possible advance tile, shows the optimal discard and resulting advance count after drawing it
- Improvements (for tenpai)

**Example**: For hand `113579m2246p579s` (13 tiles, without got):
```
3向听

进张 (36种40张):
1m2m4m6m8m2p3p5p6s8s

进张后弃牌:
  摸1m打2p: 61种
  摸2m打4p: 74种
  摸3p打9m: 82种  ← 摸3p后打9m最优，82种进张
```

**For Multi-Step Analysis**: The "Advance discard analysis" shows you the future state after drawing each possible tile. Use this to:
- Identify which advance tiles lead to the best shapes
- Discover hidden optimizations (e.g., a tile with fewer initial advances may lead to better development)
- Plan 2-3 moves ahead without manual simulation

### furo-chance - Analyze Furo (Meld) Opportunity
```bash
mahjong-utils-cli.sh furo-chance <tiles> <chance-tile> [--no-chi] [--lang <zh|en|ja>]
```

**Use when**: Opponent discards a tile you might call (chi/pon/kan)

**Output**: 
- Pass: Shanten if you skip the opportunity
- Chi/Pon/Minkan: Shanten after melding (includes discard options and advance tiles)

**For Multi-Step Analysis**: Compare Pass vs Meld paths by simulating subsequent draws

### hora - Analyze Winning Hand
```bash
mahjong-utils-cli.sh hora <tiles> [options]
```

**Options**:
- `--agari <tile>`: Winning tile (default: last tile)
- `--tsumo`: Self-draw (default: ron)
- `--dora <n>`: Dora count
- `--self-wind <E|S|W|N>`: Self wind (东/East/東(トン), 南/South/南(ナン), 西/West/西(シャー), 北/North/北(ペー))
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

## Strategic Analysis Tips

When using multi-step推演:

1. **Don't just look at advance count** - A 55种进张 may include many difficult tiles
2. **Evaluate shape quality** - Good shapes (好型) are worth more than raw numbers
3. **Consider tile flow** - Tiles near dora or in your discard pool may be harder to draw
4. **Think about opponent discards** - Tiles that opponents are likely to discard change the value
5. **Plan 2-3 moves ahead** - The best discard now may not lead to the fastest tenpai