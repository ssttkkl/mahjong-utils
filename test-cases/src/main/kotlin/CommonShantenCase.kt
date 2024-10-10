import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mahjongutils.ErrorInfo
import mahjongutils.models.Tile
import mahjongutils.shanten.CommonShantenArgs

@Serializable
sealed class CommonShantenCase {
    abstract val args: CommonShantenArgs

    @Serializable
    @SerialName("success")
    data class Success(
        override val args: CommonShantenArgs,
        val shantenNum: Int,
        val advance: Advance? = null,  // only when n=3k+1
        val discardToAdvance: Map<String, Advance>? = null  // only when n=3k+1
    ) : CommonShantenCase()

    @Serializable
    @SerialName("fail")
    data class Fail(
        override val args: CommonShantenArgs,
        val errors: Collection<ErrorInfo>
    ) : CommonShantenCase()

    @Serializable
    data class Advance(
        /**
         * 进张
         */
        val advance: Set<Tile>,
        /**
         * 进张数
         */
        val advanceNum: Int
    )
}