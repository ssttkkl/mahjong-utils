@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.shanten.helpers.afterDiscardForHoraHand
import mahjongutils.models.*
import mahjongutils.models.hand.*

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
            pattern: CommonHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ): Collection<HoraHandPattern> = when (pattern) {
            is RegularHandPattern -> RegularHoraHandPattern.build(pattern, agari, tsumo, selfWind, roundWind)
            is ChitoiHandPattern -> {
                require(pattern.pairs.size == 7 && pattern.remaining.isEmpty()) {
                    "this hand pattern is not hora yet"
                }
                listOf(ChitoiHoraHandPattern(pattern.pairs, agari, tsumo, selfWind, roundWind))
            }

            is KokushiHandPattern -> {
                require(pattern.yaochu == Tile.allYaochu && pattern.remaining.isEmpty() && pattern.repeated != null) {
                    "this hand pattern is not hora yet"
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
        require(k == 4 && pattern.menzenMentsu.size + pattern.furo.size == 4 && pattern.jyantou != null && pattern.remaining.isEmpty()) {
            "this hand pattern is not hora yet"
        }
    }

    override val jyantou: Tile
        get() = pattern.jyantou!!

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
