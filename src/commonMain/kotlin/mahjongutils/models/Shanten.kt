package mahjongutils.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Shanten {
    val shantenNum: Int
}

@Serializable
@SerialName("ShantenWithoutGot")
data class ShantenWithoutGot(
    override val shantenNum: Int,
    val advance: Set<Tile>,
    val advanceNum: Int = -1,
    val wellShapeAdvance: Set<Tile>? = null,
    val wellShapeAdvanceNum: Int = -1
) : Shanten

@Serializable
@SerialName("ShantenWithGot")
data class ShantenWithGot(
    override val shantenNum: Int,
    val discardToAdvance: Map<Tile, ShantenWithoutGot>
) : Shanten