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
     * 向听数，考虑自己手牌和副露中已用满的牌。
     */
    val shantenNum: Int

    /**
     * 结构向听数，仅按面子、搭子和雀头结构计算，不检查和牌张是否已被自己用满。
     */
    val structuralShantenNum: Int
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
