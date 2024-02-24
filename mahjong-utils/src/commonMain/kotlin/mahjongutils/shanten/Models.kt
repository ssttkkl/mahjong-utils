package mahjongutils.shanten

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mahjongutils.models.hand.CommonHandPattern
import mahjongutils.models.hand.Hand

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

@Serializable
sealed interface ShantenResult<out S : Shanten, out P : CommonHandPattern> {
    /**
     * 手牌
     */
    val hand: Hand<P>

    /**
     * 向听信息
     */
    val shantenInfo: S
}