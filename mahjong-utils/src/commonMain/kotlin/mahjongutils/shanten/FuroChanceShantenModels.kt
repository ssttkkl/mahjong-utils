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
     * 是否能荣和机会牌
     */
    val canRon: Boolean,
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
    val minkan: ShantenWithoutGot?,
    /**
     * 结构向听数，不将自己手牌和副露中已用满的和牌张排除在外。
     */
    @kotlinx.serialization.EncodeDefault override val structuralShantenNum: Int = shantenNum
) : Shanten


@Serializable
@SerialName("FuroChanceShantenResult")
data class FuroChanceShantenResult(
    override val hand: Hand<RegularHandPattern>,
    override val shantenInfo: ShantenWithFuroChance
) : ShantenResult<ShantenWithFuroChance, RegularHandPattern>
