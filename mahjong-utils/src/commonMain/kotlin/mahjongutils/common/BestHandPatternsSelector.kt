package mahjongutils.common

import mahjongutils.models.hand.HandPattern

internal class BestHandPatternsSelector<T : HandPattern>(
    private val calcShanten: (T) -> Int
) {
    var bestShanten = 100
        private set

    var bestPatterns = ArrayList<T>()
        private set

    fun receive(pattern: T) {
        val patShanten = calcShanten(pattern)
        if (patShanten < bestShanten) {
            bestShanten = patShanten
            bestPatterns = ArrayList()
        }
        if (patShanten == bestShanten) {
            bestPatterns.add(pattern)
        }
    }
}
