@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.models.hand

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.common.afterDiscardForHoraHand
import mahjongutils.hora.HoraInfo
import mahjongutils.models.*

/**
 * 和牌手牌
 */
@Serializable
sealed interface HoraHandPattern : HoraInfo, HandPattern {
    companion object {
        /**
         * 构建和牌手牌
         *
         * @param pattern 手牌形
         * @param agari 和牌张
         * @param tsumo 是否自摸
         * @param selfWind 自风
         * @param roundWind 场风
         * @return 所有可能的和牌手牌
         */
        fun build(
            pattern: HandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ): Collection<HoraHandPattern> = when (pattern) {
            is RegularHandPattern -> RegularHoraHandPattern.build(pattern, agari, tsumo, selfWind, roundWind)
            is ChitoiHandPattern -> {
                require(pattern.pairs.size == 7 && pattern.remaining.isEmpty()) {
                    "invalid ChitoiHandPattern"
                }
                listOf(ChitoiHoraHandPattern(pattern.pairs, agari, tsumo, selfWind, roundWind))
            }

            is KokushiHandPattern -> {
                require(pattern.yaochu == Tile.allYaochu && pattern.remaining.isEmpty() && pattern.repeated != null) {
                    "invalid KokushiHandPattern"
                }
                listOf(KokushiHoraHandPattern(pattern.repeated, agari, tsumo, selfWind, roundWind))
            }

            is HoraHandPattern -> listOf(pattern)
        }
    }
}

/**
 * 标准形的和牌手牌
 */
@Serializable
@SerialName("RegularHoraHandPattern")
data class RegularHoraHandPattern internal constructor(
    /**
     * 标准形手牌
     */
    val pattern: RegularHandPattern,
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind? = null,
    @EncodeDefault override val roundWind: Wind? = null,
    /**
     * 和牌搭子（为空表示单骑和牌）
     */
    val agariTatsu: Tatsu?,
) : HoraHandPattern, IRegularHandPattern by pattern {
    init {
        require(k == 4)
        require(pattern.menzenMentsu.size + pattern.furo.size == 4)
        require(pattern.jyantou != null)
        require(pattern.remaining.isEmpty())
    }

    override val jyantou: Tile
        get() = pattern.jyantou!!

    @EncodeDefault
    override val hu: Int = run {
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
        if (agariTatsu is Toitsu) {
            ans -= 2
        }

        // 役牌雀头（连风算4符）
        if (selfWind != null && jyantou == selfWind.tile ||
            roundWind != null && jyantou == roundWind.tile ||
            jyantou.isSangen
        ) {
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
            ans + (10 - ans % 10)
        } else {
            ans
        }
    }

    companion object {
        private fun buildWithoutGot(
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null,
            pattern: RegularHandPattern,
        ): RegularHoraHandPattern {
            if (pattern.jyantou != null) {
                return RegularHoraHandPattern(
                    agari = agari,
                    tsumo = tsumo,
                    selfWind = selfWind,
                    roundWind = roundWind,
                    agariTatsu = pattern.tatsu[0],
                    pattern = pattern.copy(
                        menzenMentsu = pattern.menzenMentsu + (pattern.tatsu[0].withWaiting(agari)),
                        tatsu = emptyList()
                    ),
                )
            } else {
                return RegularHoraHandPattern(
                    agari = agari,
                    tsumo = tsumo,
                    selfWind = selfWind,
                    roundWind = roundWind,
                    agariTatsu = null,
                    pattern = pattern.copy(
                        jyantou = agari,
                        remaining = emptyList()
                    ),
                )
            }
        }

        /**
         * 构建标准形和牌手牌
         *
         * @param pattern 手牌形
         * @param agari 和牌张
         * @param tsumo 是否自摸
         * @param selfWind 自风
         * @param roundWind 场风
         * @return 所有可能的和牌手牌
         */
        fun build(
            pattern: RegularHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ): Collection<RegularHoraHandPattern> {
            return pattern.afterDiscardForHoraHand(agari).map {
                buildWithoutGot(agari, tsumo, selfWind, roundWind, it)
            }
        }
    }
}

/**
 * 七对子的和牌手牌
 */
@Serializable
@SerialName("ChitoiHoraHandPattern")
data class ChitoiHoraHandPattern(
    /**
     * 手牌形
     */
    override val pairs: Set<Tile>,
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind? = null,
    @EncodeDefault override val roundWind: Wind? = null,
) : HoraHandPattern, IChitoiHandPattern {
    override val hu: Int
        get() = 25
    override val remaining: List<Tile>
        get() = emptyList()
}

/**
 * 国士无双的和牌手牌
 */
@Serializable
@SerialName("KokushiHoraHandPattern")
data class KokushiHoraHandPattern(
    /**
     * 手牌形
     */
    override val repeated: Tile,
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind? = null,
    @EncodeDefault override val roundWind: Wind? = null,
) : HoraHandPattern, IKokushiHandPattern {
    override val hu: Int
        get() = 20
    override val yaochu: Set<Tile>
        get() = Tile.allYaochu
    override val remaining: List<Tile>
        get() = emptyList()

    /**
     * 是否十三面和牌
     */
    val thirteenWaiting: Boolean
        get() = repeated == agari
}
