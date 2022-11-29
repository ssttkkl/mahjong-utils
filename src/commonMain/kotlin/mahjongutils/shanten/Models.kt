@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.shanten

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.common.*
import mahjongutils.models.*
import mahjongutils.models.hand.*


/**
 * 向听信息
 */
@Serializable
sealed interface Shanten {
    /**
     * 向听数
     */
    val shantenNum: Int
}

/**
 * 未摸牌的手牌的向听信息
 */
@Serializable
@SerialName("ShantenWithoutGot")
data class ShantenWithoutGot(
    override val shantenNum: Int,
    /**
     * 进张
     */
    val advance: Set<Tile>,
    /**
     * 进张数
     */
    @EncodeDefault val advanceNum: Int? = null,
    /**
     * 好型进张
     */
    @EncodeDefault val goodShapeAdvance: Set<Tile>? = null,
    /**
     * 好型进张数
     */
    @EncodeDefault val goodShapeAdvanceNum: Int? = null
) : Shanten

/**
 * 摸牌的手牌的向听信息
 */
@Serializable
@SerialName("ShantenWithGot")
data class ShantenWithGot(
    override val shantenNum: Int,
    /**
     * 每种弃牌后的向听信息
     */
    val discardToAdvance: Map<Tile, ShantenWithoutGot>,
    /**
     * 每种暗杠后的向听信息
     */
    @EncodeDefault val ankanToAdvance: Map<Tile, ShantenWithoutGot> = emptyMap()
) : Shanten

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
    val pass: ShantenWithoutGot,
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

/**
 * 向听分析结果
 */
@Serializable
data class ShantenResult internal constructor(
    /**
     * 向听分析种类
     */
    val type: Type,
    /**
     * 手牌
     */
    val hand: Hand,
    /**
     * 向听信息
     */
    val shantenInfo: Shanten,
    /**
     * 标准形向听分析结果（仅在type为Union时有值）
     */
    @EncodeDefault val regular: ShantenResult? = null,
    /**
     * 标准形向听分析结果（仅在type为Union且手牌无副露时时有值）
     */
    @EncodeDefault val chitoi: ShantenResult? = null,
    /**
     * 标准形向听分析结果（仅在type为Union且手牌无副露时有值）
     */
    @EncodeDefault val kokushi: ShantenResult? = null,
) {
    /**
     * 向听分析种类
     */
    enum class Type {
        Union, Regular, Chitoi, Kokushi, FuroChance
    }
}
