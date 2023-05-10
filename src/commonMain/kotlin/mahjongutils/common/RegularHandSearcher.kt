package mahjongutils.common

import mahjongutils.models.*
import mahjongutils.models.hand.RegularHandPattern

// 这里用的编码和Tile.code不一样
private fun encode(t: Tile): Int {
    return t.type.ordinal * 9 + t.realNum - 1
}

private fun decode(code: Int): Tile {
    val type = TileType.valueOf(code / 9)
    val num = code % 9 + 1
    return Tile.get(type, num)
}

private class RegularHandPatternSearcher(
    tiles: List<Tile>,
    private val furo: List<Furo> = emptyList(),
    private val callback: (RegularHandPattern) -> Unit
) {
    var n = tiles.size
    var k = n / 3
    private val cnt = IntArray(3 * 9 + 7 + 1)
    private val mentsu = ArrayList<Mentsu>()
    private val tatsu = ArrayList<Tatsu>()
    private var jyantou: Tile? = null

    init {
        tiles.forEach {
            cnt[encode(it)] += 1
        }
    }

    fun run() {
        dfsKotsu()
    }

    private fun dfsKotsu(begin: Int = 0) {
        // begin用于限制从哪张牌开始枚举（下同）
        // 其目的是避免搜索时按不同顺序取了相同的刻字，优化性能

        if (n >= 3) {
            for (i in begin until 3 * 9 + 7) {
                if (cnt[i] >= 3) {
                    n -= 3
                    cnt[i] -= 3
                    mentsu.add(Kotsu(decode(i)))
                    dfsKotsu(i)
                    n += 3
                    cnt[i] += 3
                    mentsu.removeLast()
                }
            }
        }
        dfsShuntsu()
    }

    private fun dfsShuntsu(begin: Int = 0) {
        // begin用于限制从哪张牌开始枚举（下同）
        // 其目的是避免搜索时按不同顺序取了相同的刻字，优化性能

        if (n >= 3) {
            for (i in begin / 9..2) {
                for (j in 0..6) {
                    val x = i * 9 + j
                    val y = x + 1
                    val z = x + 2

                    if (x < begin) {
                        continue
                    }

                    if (cnt[x] > 0 && cnt[y] > 0 && cnt[z] > 0) {
                        n -= 3
                        cnt[x] -= 1
                        cnt[y] -= 1
                        cnt[z] -= 1
                        mentsu.add(Shuntsu(decode(x)))
                        dfsShuntsu(x)
                        n += 3
                        cnt[x] += 1
                        cnt[y] += 1
                        cnt[z] += 1
                        mentsu.removeLast()
                    }
                }
            }
        }
        dfsJyantou()
    }

    private fun dfsJyantou(begin: Int = 0) {
        if (n >= 2) {
            for (i in begin until 3 * 9 + 7) {
                val t = decode(i)
                if (cnt[i] >= 2) {
                    n -= 2
                    cnt[i] -= 2
                    jyantou = t
                    dfsTatsu()
                    n += 2
                    cnt[i] += 2
                    jyantou = null
                }
            }
        }
        dfsTatsu()
    }

    private fun dfsTatsu(begin: Int = 0, tatsuTypeLimitation: Int = 0) {
        /*
         * tatsu_type_limitation用于限制能够取什么样的以begin为第一张牌的搭子（0可以取所有类型，1不可以取对子、2不可以取对子和坎张、3不可以取对子坎张两面）
         * 其目的是避免搜索时按不同顺序取了相同的搭子，优化性能
         * 故当for循环执行了一趟以后就将tatsu_type_limitation置0
         */

        var taken = false

        if (mentsu.size + tatsu.size < k) {
            var tatsuTypeLimitation = tatsuTypeLimitation
            if (n >= 2) {
                for (i in begin until 3 * 9 + 7) {
                    val t = decode(i)

                    // toitsu
                    if (tatsuTypeLimitation == 0 && cnt[i] >= 2) {
                        taken = true
                        n -= 2
                        cnt[i] -= 2
                        tatsu.add(Toitsu(t))
                        dfsTatsu(i, 0)
                        n += 2
                        cnt[i] += 2
                        tatsu.removeLast()
                    }

                    // kanchan
                    if (tatsuTypeLimitation <= 1 && t.type != TileType.Z && t.num <= 7) {
                        val j = i + 2
                        if (cnt[i] > 0 && cnt[j] > 0) {
                            taken = true
                            n -= 2
                            cnt[i] -= 1
                            cnt[j] -= 1
                            tatsu.add(Kanchan(t))
                            dfsTatsu(i, 1)
                            n += 2
                            cnt[i] += 1
                            cnt[j] += 1
                            tatsu.removeLast()
                        }
                    }

                    // ryanmen
                    if (tatsuTypeLimitation <= 2 && t.type != TileType.Z && t.num in 2..7) {
                        val j = i + 1
                        if (cnt[i] > 0 && cnt[j] > 0) {
                            taken = true
                            n -= 2
                            cnt[i] -= 1
                            cnt[j] -= 1
                            tatsu.add(Ryanmen(t))
                            dfsTatsu(i, 2)
                            n += 2
                            cnt[i] += 1
                            cnt[j] += 1
                            tatsu.removeLast()
                        }
                    }

                    // penchan
                    if (tatsuTypeLimitation <= 3 && t.type != TileType.Z && (t.num == 1 || t.num == 8)) {
                        val j = i + 1
                        if (cnt[i] > 0 && cnt[j] > 0) {
                            taken = true
                            n -= 2
                            cnt[i] -= 1
                            cnt[j] -= 1
                            tatsu.add(Penchan(t))
                            dfsTatsu(i, 3)
                            n += 2
                            cnt[i] += 1
                            cnt[j] += 1
                            tatsu.removeLast()
                        }
                    }

                    tatsuTypeLimitation = 0
                }
            }
        }

        if (!taken) {
            onResult()
        }
    }

    private fun onResult() {
        // 将搜索结果处理为（雀头，面子，搭子，浮牌）的形式
        val remaining = buildList {
            for (i in 0 until 3 * 9 + 7) {
                if (cnt[i] > 0) {
                    val t = decode(i)
                    repeat(cnt[i]) {
                        add(t)
                    }
                }
            }
        }

        val pattern = RegularHandPattern(
            k = k + furo.size,
            jyantou = jyantou,
            menzenMentsu = ArrayList(mentsu),
            furo = ArrayList(furo),
            tatsu = ArrayList(tatsu),
            remaining = remaining
        )
        callback(pattern)
    }
}

internal fun regularHandPatternSearch(tiles: List<Tile>, furo: List<Furo>): Collection<RegularHandPattern> {
    val patterns = ArrayList<RegularHandPattern>()
    val searcher = RegularHandPatternSearcher(tiles, furo) {
        patterns.add(it)
    }
    searcher.run()
    return patterns
}