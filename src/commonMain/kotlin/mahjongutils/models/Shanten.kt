@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
    @EncodeDefault val advanceNum: Int? = null,
    @EncodeDefault val wellShapeAdvance: Set<Tile>? = null,
    @EncodeDefault val wellShapeAdvanceNum: Int? = null
) : Shanten

@Serializable
@SerialName("ShantenWithGot")
data class ShantenWithGot(
    override val shantenNum: Int,
    val discardToAdvance: Map<Tile, ShantenWithoutGot>
) : Shanten