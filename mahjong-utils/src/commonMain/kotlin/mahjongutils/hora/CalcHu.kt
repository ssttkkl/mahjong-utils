package mahjongutils.hora

import mahjongutils.models.*

/**
 * 计算标准形和牌的符数
 *
 * @param hasRenpuuJyantouHu 连风是否记4符
 * @return 符数
 */
internal fun RegularHoraHandPattern.calcHu(hasRenpuuJyantouHu: Boolean = true): Int {
    var ans = 20

    // 单骑、边张、坎张听牌
    if (agariTatsu == null || agariTatsu is Penchan || agariTatsu is Kanchan) {
        ans += 2
    }

    // 明刻、杠
    furo.forEach { fr ->
        if (fr is Pon) {
            if (fr.tile.isYaochu) {
                ans += 4
            } else {
                ans += 2
            }
        } else if (fr is Kan) {
            if (!fr.ankan) {
                if (fr.tile.isYaochu) {
                    ans += 16
                } else {
                    ans += 8
                }
            } else {
                if (fr.tile.isYaochu) {
                    ans += 32
                } else {
                    ans += 16
                }
            }
        }
    }

    // 暗刻（不含暗杠）
    menzenMentsu.forEach { mt ->
        if (mt is Kotsu) {
            if (mt.tile.isYaochu) {
                ans += 8
            } else {
                ans += 4
            }
        }
    }

    // 对碰荣和时该刻子计为明刻
    if (agariTatsu is Toitsu && !tsumo) {
        ans -= 2
    }

    // 役牌雀头
    if (hasRenpuuJyantouHu) {
        if (selfWind != null && jyantou == selfWind.tile) {
            ans += 2
        }
        if (roundWind != null && jyantou == roundWind.tile) {
            ans += 2
        }
    } else {
        if (selfWind != null && jyantou == selfWind.tile || roundWind != null && jyantou == roundWind.tile) {
            ans += 2
        }
    }

    if (jyantou.isSangen) {
        ans += 2
    }

    // 门清荣和
    if (menzen && !tsumo) {
        ans += 10
    }

    // 非平和自摸
    if (ans != 20 && tsumo) {
        ans += 2
    }

    // 非门清最低30符
    if (!menzen && ans < 30) {
        ans = 30
    }

    // 切上
    if (ans % 10 > 0) {
        ans += (10 - ans % 10)
    }

    return ans
}