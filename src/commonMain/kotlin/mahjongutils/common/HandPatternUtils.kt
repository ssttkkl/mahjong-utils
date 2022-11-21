package mahjongutils.common

import mahjongutils.models.Tatsu
import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.hand.ChitoiHandPattern
import mahjongutils.models.hand.KokushiHandPattern
import mahjongutils.models.hand.RegularHandPattern


internal fun RegularHandPattern.calcShanten(): Int {
    var shanten = 2 * (k - menzenMentsu.size - furo.size) - tatsu.size
    if (jyantou != null) {
        shanten -= 1
    }
    return shanten
}

internal fun ChitoiHandPattern.calcShanten(): Int {
    return 6 - pairs.size
}

internal fun KokushiHandPattern.calcShanten(): Int {
    return if (repeated != null) {
        // 非十三面
        12 - yaochu.size
    } else {
        // 十三面
        13 - yaochu.size
    }
}

internal val TILE_CLING = buildMap<Tile, Set<Tile>> {
    repeat(3) {
        val type = TileType.values()[it]
        for (j in 1..9) {
            val t = Tile.get(type, j)
            this[t] = listOf(-2, -1, 0, 1, 2)
                .filter { k -> j + k in 1..9 }
                .map { k -> Tile.get(type, j + k) }
                .toSet()
        }
    }

    for (j in 1..7) {
        val t = Tile.get(TileType.Z, j)
        this[t] = setOf(t)
    }
}

internal fun RegularHandPattern.calcAdvance(): Set<Tile> {
    return buildSet {
        // 搭子的进张
        for (tt in tatsu) {
            addAll(tt.waiting)
        }

        // 浮张的靠张
        if (furo.size + menzenMentsu.size + tatsu.size < k) {
            for (t in remaining) {
                addAll(TILE_CLING[t]!!)
            }
        }

        // 无雀头
        if (jyantou == null) {
            for (t in remaining) {
                add(t)
            }
        }
    }
}

internal fun RegularHandPattern.afterDiscard(discard: Tile): List<RegularHandPattern> {
    return buildList {
        // 扣掉雀头
        if (jyantou == discard) {
            val newPattern = copy(jyantou = null, remaining = remaining + discard)
            add(newPattern)
        }

        // 扣掉面子
        menzenMentsu.forEachIndexed { i, mt ->
            if (discard in mt.tiles) {
                val tt = mt.afterDiscard(discard)
                val newPattern = copy(
                    menzenMentsu = menzenMentsu.slice(0 until i) + menzenMentsu.slice(i + 1 until menzenMentsu.size),
                    tatsu = tatsu + tt
                )
                add(newPattern)
            }
        }

        // 扣掉搭子
        tatsu.forEachIndexed { i, tt ->
            if (discard == tt.first) {
                val newPattern = copy(
                    tatsu = tatsu.slice(0 until i) + tatsu.slice(i + 1 until tatsu.size),
                    remaining = remaining + tt.second
                )
                add(newPattern)
            } else if (discard == tt.second) {
                val newPattern = copy(
                    tatsu = tatsu.slice(0 until i) + tatsu.slice(i + 1 until tatsu.size),
                    remaining = remaining + tt.first
                )
                add(newPattern)
            }
        }

        // 扣掉浮张
        val idx = remaining.indexOf(discard)
        if (idx != -1) {
            val newPattern = copy(
                remaining = remaining.slice(0 until idx) + remaining.slice(idx + 1 until remaining.size),
            )
            add(newPattern)
        }
    }
}

internal fun RegularHandPattern.afterAdvance(advance: Tile): List<RegularHandPattern> {
    return buildList {
        // 搭子的进张
        tatsu.forEachIndexed { i, tt ->
            if (advance in tt.waiting) {
                val newPattern = copy(
                    menzenMentsu = menzenMentsu + tt.withWaiting(advance),
                    tatsu = tatsu.slice(0 until i) + tatsu.slice(i + 1 until tatsu.size)
                )
                add(newPattern)
            }
        }

        // 浮张的靠张
        if (furo.size + menzenMentsu.size + tatsu.size < k) {
            remaining.forEachIndexed { i, t ->
                if (advance in TILE_CLING[t]!!) {
                    val newPattern = copy(
                        tatsu = tatsu + Tatsu(advance, t),
                        remaining = remaining.slice(0 until i) + remaining.slice(i + 1 until remaining.size)
                    )
                    add(newPattern)
                }
            }
        }

        // 无雀头
        if (jyantou == null) {
            val idx = remaining.indexOf(advance)
            if (idx != -1) {
                val newPattern = copy(
                    jyantou = advance,
                    remaining = remaining.slice(0 until idx) + remaining.slice(idx + 1 until remaining.size)
                )
                add(newPattern)
            }
        }
    }
}