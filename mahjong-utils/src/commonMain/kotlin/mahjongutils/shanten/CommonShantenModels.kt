package mahjongutils.shanten

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.models.Tile
import mahjongutils.models.hand.*


@Serializable
sealed interface CommonShanten : Shanten

@Serializable
data class Improvement(
    /**
     * 摸上改良张后的弃牌
     */
    val discard: Tile,
    /**
     * 摸上改良张且弃牌后的进张
     */
    val advance: Set<Tile>,
    /**
     * 摸上改良张且弃牌后的进张数
     */
    @EncodeDefault val advanceNum: Int = 0,
)

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
    @EncodeDefault val advanceNum: Int = 0,
    /**
     * 好型进张
     * 仅当一向听时进行计算
     */
    @EncodeDefault val goodShapeAdvance: Set<Tile>? = if (shantenNum == 1) emptySet() else null,
    /**
     * 好型进张数
     * 仅当一向听时进行计算
     */
    @EncodeDefault val goodShapeAdvanceNum: Int? = if (shantenNum == 1) 0 else null,
    /**
     * 改良张（能让听牌数目增加的牌）
     * 对于每种改良张，只计算能让进张最多的打法
     * 仅当听牌时进行计算
     */
    @EncodeDefault val improvement: Map<Tile, List<Improvement>>? = if (shantenNum == 0) emptyMap() else null,
    /**
     * 改良张数（能让听牌数目增加的牌）
     * 仅当听牌时进行计算
     */
    @EncodeDefault val improvementNum: Int? = if (shantenNum == 0) 0 else null,
    /**
     * 好型改良张（能让听牌数目增加到大于4张的牌）
     * 对于每种改良张，只计算能让进张最多的打法
     * 仅当听牌时进行计算
     */
    @EncodeDefault val goodShapeImprovement: Map<Tile, List<Improvement>>? = if (shantenNum == 0) emptyMap() else null,
    /**
     * 好型改良张数（能让听牌数目增加到大于4张的牌）
     * 仅当听牌时进行计算
     */
    @EncodeDefault val goodShapeImprovementNum: Int? = if (shantenNum == 0) 0 else null,
) : CommonShanten

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
) : CommonShanten

val CommonShanten.asWithoutGot: ShantenWithoutGot
    get() = this as ShantenWithoutGot

val CommonShanten.asWithGot: ShantenWithGot
    get() = this as ShantenWithGot

@Serializable
sealed interface CommonShantenResult<out P : CommonHandPattern> : ShantenResult<CommonShanten, P>

@Serializable
@SerialName("RegularShantenResult")
data class RegularShantenResult(
    override val hand: Hand<RegularHandPattern>,
    override val shantenInfo: CommonShanten
) : CommonShantenResult<RegularHandPattern>

@Serializable
@SerialName("ChitoiShantenResult")
data class ChitoiShantenResult(
    override val hand: Hand<ChitoiHandPattern>,
    override val shantenInfo: CommonShanten
) : CommonShantenResult<ChitoiHandPattern>

@Serializable
@SerialName("KokushiShantenResult")
data class KokushiShantenResult(
    override val hand: Hand<KokushiHandPattern>,
    override val shantenInfo: CommonShanten
) : CommonShantenResult<KokushiHandPattern>

@Serializable
@SerialName("UnionShantenResult")
data class UnionShantenResult(
    override val hand: Hand<CommonHandPattern>,
    override val shantenInfo: CommonShanten,
    /**
     * 标准形向听分析结果
     */
    val regular: RegularShantenResult,

    /**
     * 标准形向听分析结果
     */
    @EncodeDefault
    val chitoi: ChitoiShantenResult? = null,

    /**
     * 标准形向听分析结果
     */
    @EncodeDefault
    val kokushi: KokushiShantenResult? = null,
) : CommonShantenResult<CommonHandPattern>
