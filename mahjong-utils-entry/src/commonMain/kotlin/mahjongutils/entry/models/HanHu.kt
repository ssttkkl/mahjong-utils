package mahjongutils.entry.models

import kotlinx.serialization.Serializable

@Serializable
data class HanHu(
    val han: Int,
    val hu: Int
)


