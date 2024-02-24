package mahjongutils.shanten

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.Tatsu
import mahjongutils.models.hand.Hand
import mahjongutils.models.hand.RegularHandPattern

/**
 * 有副露机会的手牌的向听信息
 */
@Serializable
@SerialName("ShantenWithFuroChance")
data class ShantenWithFuroChance(
    override val shantenNum: Int,
    /**
     * Pass后的向听信息
     */
    val pass: ShantenWithoutGot?,
    /**
     * 每种搭子吃后的向听信息
     */
    val chi: Map<Tatsu, ShantenWithGot>,
    /**
     * 碰后的向听信息（若无法碰则为null）
     */
    val pon: ShantenWithGot?,
    /**
     * 明杠后的向听信息（若无法明杠则为null）
     */
    val minkan: ShantenWithoutGot?
) : Shanten


@Serializable
@SerialName("FuroChanceShantenResult")
data class FuroChanceShantenResult(
    override val hand: Hand<RegularHandPattern>,
    override val shantenInfo: ShantenWithFuroChance
) : ShantenResult<ShantenWithFuroChance, RegularHandPattern>