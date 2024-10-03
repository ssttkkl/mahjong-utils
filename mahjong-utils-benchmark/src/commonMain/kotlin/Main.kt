import kotlin.time.measureTime

fun main() {
    val cost = measureTime {
        TestFuroChanceShanten().apply {
            test1()
            test2()
            test3()
            test4()
            test5()
        }
        TestShanten().apply {
            testWithGot()
            testWithoutGot()
            testRegularWithoutGot()
            testKokushiWithGot()
        }
    }
    println("cost: ${cost}")
}
