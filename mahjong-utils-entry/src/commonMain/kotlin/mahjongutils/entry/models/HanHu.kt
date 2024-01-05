package mahjongutils.entry.models

import kotlinx.serialization.Serializable
import mahjongutils.hanhu.HanHuOptions

@Serializable
internal data class HanHu(
    val han: Int,
    val hu: Int,
    val options: HanHuOptions = HanHuOptions.Default,
)


