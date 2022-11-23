@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.models.hand

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.common.afterDiscard
import mahjongutils.hora.HoraInfo
import mahjongutils.models.*

@Serializable
sealed interface HoraHandPattern : HoraInfo, HandPattern {
    companion object {
        fun build(
            pattern: HandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ): Collection<HoraHandPattern> = when (pattern) {
            is RegularHandPattern -> RegularHoraHandPattern.build(pattern, agari, tsumo, selfWind, roundWind)
            is ChitoiHandPattern -> listOf(ChitoiHoraHandPattern.build(pattern, agari, tsumo, selfWind, roundWind))
            is KokushiHandPattern -> listOf(KokushiHoraHandPattern.build(pattern, agari, tsumo, selfWind, roundWind))
            is HoraHandPattern -> listOf(pattern)
        }
    }
}

@Serializable
@SerialName("RegularHoraHandPattern")
data class RegularHoraHandPattern(
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind? = null,
    @EncodeDefault override val roundWind: Wind? = null,
    val agariTatsu: Tatsu?,
    val pattern: RegularHandPattern,
) : HoraHandPattern, IRegularHandPattern by pattern {
    init {
        require(k == 4)
        require(pattern.menzenMentsu.size + pattern.furo.size == 4)
        require(pattern.jyantou != null)
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
            pattern: RegularHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
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
                        jyantou = agari
                    ),
                )
            }
        }

        fun build(
            pattern: RegularHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ): Collection<RegularHoraHandPattern> {
            return pattern.afterDiscard(agari).map {
                buildWithoutGot(it, agari, tsumo, selfWind, roundWind)
            }
        }
    }
}

@Serializable
@SerialName("ChitoiHoraHandPattern")
data class ChitoiHoraHandPattern(
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind?,
    @EncodeDefault override val roundWind: Wind?,
    val pattern: ChitoiHandPattern,
) : HoraHandPattern, IChitoiHandPattern by pattern {
    init {
        require(pattern.remaining.isEmpty())
    }

    override val hu: Int
        get() = 25

    companion object {
        fun build(
            pattern: ChitoiHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ) = ChitoiHoraHandPattern(
            agari = agari,
            tsumo = tsumo,
            selfWind = selfWind,
            roundWind = roundWind,
            pattern = pattern
        )
    }
}

@Serializable
@SerialName("KokushiHoraHandPattern")
data class KokushiHoraHandPattern(
    override val agari: Tile,
    override val tsumo: Boolean,
    @EncodeDefault override val selfWind: Wind?,
    @EncodeDefault override val roundWind: Wind?,
    val pattern: KokushiHandPattern,
) : HoraHandPattern, IKokushiHandPattern by pattern {
    init {
        require(pattern.repeated != null)
        require(pattern.remaining.isEmpty())
    }

    override val hu: Int
        get() = 20

    override val repeated: Tile
        get() = pattern.repeated!!

    val thirteenWaiting: Boolean
        get() = pattern.repeated == agari

    companion object {
        fun build(
            pattern: KokushiHandPattern,
            agari: Tile,
            tsumo: Boolean,
            selfWind: Wind? = null,
            roundWind: Wind? = null
        ) = KokushiHoraHandPattern(
            agari = agari,
            tsumo = tsumo,
            selfWind = selfWind,
            roundWind = roundWind,
            pattern = pattern
        )
    }
}
