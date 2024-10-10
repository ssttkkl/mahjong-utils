import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import mahjongutils.ValidationException
import mahjongutils.models.Tile
import mahjongutils.shanten.CommonShantenArgs
import mahjongutils.shanten.ShantenWithGot
import mahjongutils.shanten.asWithGot
import mahjongutils.shanten.asWithoutGot
import mahjongutils.shanten.shanten
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlin.random.nextInt

object HappyCaseGenerator {
    private fun getCurrentTimeString(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss"))
    }

    fun genCommonShantenCases(dest: File = File("test-cases/commonShanten/happy").canonicalFile) {
        val cases = sequence {
            repeat(100) {
                val tiles = buildList {
                    repeat(Random.nextInt(1..14)) {
                        add(Tile.allExcludeAkaDora.random())
                    }
                }

                val args = CommonShantenArgs(tiles)

                runCatching { shanten(args) }
                    .onSuccess {
                        if (it.shantenInfo is ShantenWithGot) {
                            yield(
                                CommonShantenCase.Success(
                                    args,
                                    it.shantenInfo.shantenNum,
                                    discardToAdvance = it.shantenInfo.asWithGot.discardToAdvance
                                        .mapKeys { it.key.toString() }
                                        .mapValues { CommonShantenCase.Advance(it.value.advance, it.value.advanceNum) }
                                )
                            )
                        } else {
                            yield(
                                CommonShantenCase.Success(
                                    args,
                                    it.shantenInfo.shantenNum,
                                    advance = CommonShantenCase.Advance(
                                        it.shantenInfo.asWithoutGot.advance,
                                        it.shantenInfo.asWithoutGot.advanceNum
                                    )
                                )
                            )
                        }
                    }
                    .onFailure {
                        if (it is ValidationException) {
                            yield(CommonShantenCase.Fail(args, it.errors))
                        } else {
                            throw it
                        }
                    }
            }
        }

        dest.mkdirs()
        dest.resolve("${getCurrentTimeString()}-generated.json").outputStream().use { stream ->
            Json.encodeToStream(cases.toList(), stream)
        }
    }
}