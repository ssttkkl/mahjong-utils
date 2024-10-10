import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import mahjongutils.ValidationException
import mahjongutils.shanten.asWithGot
import mahjongutils.shanten.asWithoutGot
import mahjongutils.shanten.shanten

class CommonShantenTest : FunSpec({
    context("CommonShanten tests") {
        withData(readCases<CommonShantenCase>("commonShanten")) {
            when (it) {
                is CommonShantenCase.Success -> {
                    val res = shanten(it.args)

                    res.shantenInfo.shantenNum shouldBe it.shantenNum

                    it.advance?.let { advance ->
                        val withoutGot = res.shantenInfo.asWithoutGot
                        withoutGot.advance shouldBe advance.advance
                        withoutGot.advanceNum shouldBe advance.advanceNum
                    }
                    it.discardToAdvance?.let { discardToAdvance ->
                        val withGot = res.shantenInfo.asWithGot
                        val transform = withGot.discardToAdvance
                            .mapKeys { it.key.toString() }
                            .mapValues { CommonShantenCase.Advance(it.value.advance, it.value.advanceNum) }

                        transform shouldBe discardToAdvance
                    }
                }

                is CommonShantenCase.Fail -> {
                    shouldThrow<ValidationException> { shanten(it.args) }
                        .errors shouldBe it.errors
                }
            }
        }
    }
})