package mahjongutils.hora.helpers

import mahjongutils.hora.ChitoiHoraHandPattern
import mahjongutils.hora.HoraHandPattern
import mahjongutils.hora.KokushiHoraHandPattern
import mahjongutils.hora.RegularHoraHandPattern
import mahjongutils.models.*

internal fun calcHu(pattern: HoraHandPattern, hasRenpuuJyantouHu: Boolean = true): Int {
    return when (pattern) {
        is RegularHoraHandPattern -> calcHuForRegular(pattern, hasRenpuuJyantouHu)
        is ChitoiHoraHandPattern -> 25
        is KokushiHoraHandPattern -> 30
    }
}

/**
 * 计算标准形和牌的符数
 *
 * @param hasRenpuuJyantouHu 连风是否记4符
 * @return 符数
 */
internal fun calcHuForRegular(regularPattern: RegularHoraHandPattern, hasRenpuuJyantouHu: Boolean = true): Int =
    with(regularPattern) {
        var ans = 20

        // 单骑、边张、坎张听牌
        if (agariTatsu == null || agariTatsu.type == TatsuType.Penchan || agariTatsu.type == TatsuType.Kanchan) {
            ans += 2
        }

        // 明刻、杠
        furo.forEach { fr ->
            if (fr.type == FuroType.Pon) {
                if (fr.tile.isYaochu) {
                    ans += 4
                } else {
                    ans += 2
                }
            } else if (fr.type == FuroType.Ankan) {
                if (fr.tile.isYaochu) {
                    ans += 32
                } else {
                    ans += 16
                }
            } else if (fr.type == FuroType.Kan) {
                if (fr.tile.isYaochu) {
                    ans += 16
                } else {
                    ans += 8
                }
            }
        }

        // 暗刻（不含暗杠）
        menzenMentsu.forEach { mt ->
            if (mt.type == MentsuType.Kotsu) {
                if (mt.tile.isYaochu) {
                    ans += 8
                } else {
                    ans += 4
                }
            }
        }

        // 对碰荣和时该刻子计为明刻
        if (agariTatsu?.type == TatsuType.Toitsu && !tsumo) {
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