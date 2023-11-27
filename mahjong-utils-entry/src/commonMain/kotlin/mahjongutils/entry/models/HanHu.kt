package mahjongutils.entry.models

import kotlinx.serialization.Serializable

@Serializable
internal data class HanHu(
    val han: Int,
    val hu: Int
)


