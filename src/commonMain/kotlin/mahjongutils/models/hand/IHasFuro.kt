package mahjongutils.models.hand

import mahjongutils.models.Furo
import mahjongutils.models.Kan

interface IHasFuro {
    val furo: List<Furo>

    val menzen: Boolean
        get() = furo.all { it !is Kan || !it.ankan }
}