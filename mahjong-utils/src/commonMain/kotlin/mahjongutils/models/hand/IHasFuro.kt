package mahjongutils.models.hand

import mahjongutils.models.Furo
import mahjongutils.models.FuroType

interface IHasFuro {
    /**
     * 副露
     */
    val furo: List<Furo>

    /**
     * 是否门清
     */
    val menzen: Boolean
        get() = furo.all { it.type == FuroType.Ankan }
}