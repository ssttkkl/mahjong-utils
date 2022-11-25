@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.hora

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import mahjongutils.hanhu.ChildPoint
import mahjongutils.hanhu.ParentPoint
import mahjongutils.hanhu.getChildPointByHanHu
import mahjongutils.hanhu.getParentPointByHanHu
import mahjongutils.models.Furo
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.models.hand.HoraHandPattern
import mahjongutils.shanten.ShantenResult
import mahjongutils.shanten.ShantenWithGot
import mahjongutils.shanten.shanten
import mahjongutils.yaku.Yaku
import mahjongutils.yaku.Yakus

/**
 * 和牌分析结果
 */
@Serializable
data class Hora internal constructor(
    /**
     * 和牌形
     */
    val pattern: HoraHandPattern,
    /**
     * 宝牌数目
     */
    val dora: Int,
    /**
     * 额外役种
     */
    val extraYaku: Set<Yaku>,
) : HoraInfo by pattern {
    /**
     * 役种
     */
    @EncodeDefault
    val yaku: Set<Yaku> = buildSet {
        if (pattern.menzen) {
            addAll(Yakus.allYakuman.filter { it.check(pattern) })
        } else {
            addAll(Yakus.allYakuman.filter { !it.menzenOnly && it.check(pattern) })
        }
        addAll(extraYaku.filter { it.isYakuman })

        if (isEmpty()) {
            // 非役满情况才判断其他役种
            if (pattern.menzen) {
                addAll(Yakus.allCommonYaku.filter { it.check(pattern) })
            } else {
                addAll(Yakus.allCommonYaku.filter { !it.menzenOnly && it.check(pattern) })
            }
            addAll(extraYaku.filter { !it.isYakuman })
        }
    }

    /**
     * 是否含役满役种
     */
    @EncodeDefault
    val hasYakuman: Boolean = yaku.any { it.isYakuman }

    /**
     * 番
     */
    @EncodeDefault
    val han: Int = run {
        var ans = if (pattern.menzen) {
            yaku.sumOf { it.han }
        } else {
            yaku.sumOf { it.han - it.furoLoss }
        }

        if (ans > 0 && !hasYakuman) {
            ans += dora
        }

        ans
    }

    /**
     * 亲家（庄家）和牌点数
     */
    val parentPoint: ParentPoint
        get() = if (han == 0) {
            ParentPoint(0, 0)
        } else if (hasYakuman) {
            val times = yaku.filter { it.isYakuman }.sumOf { it.han / 13 }
            val ans = getParentPointByHanHu(13, 20)
            ParentPoint(ans.ron * times, ans.tsumo * times)
        } else {
            getParentPointByHanHu(han, hu)
        }

    /**
     * 子家（闲家）和牌点数
     */
    val childPoint: ChildPoint
        get() = if (han == 0) {
            ChildPoint(0, 0, 0)
        } else if (hasYakuman) {
            val times = yaku.filter { it.isYakuman }.sumOf { it.han / 13 }
            val ans = getChildPointByHanHu(13, 20)
            ChildPoint(ans.ron * times, ans.tsumoParent * times, ans.tsumoChild * times)
        } else {
            getChildPointByHanHu(han, hu)
        }
}

/**
 * 和牌分析
 *
 * @param tiles 门前的牌
 * @param furo 副露
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种
 * @return 和牌分析结果
 */
fun hora(
    tiles: List<Tile>, furo: List<Furo> = emptyList(), agari: Tile, tsumo: Boolean,
    dora: Int = 0, selfWind: Wind? = null, roundWind: Wind? = null, extraYaku: Set<Yaku> = emptySet()
): Hora {
    val k = tiles.size / 3 + furo.size
    if (k != 4) {
        throw IllegalArgumentException("invalid length of tiles")
    }

    val shantenResult = shanten(tiles, furo, false)
    return hora(shantenResult, agari, tsumo, dora, selfWind, roundWind, extraYaku)

}

/**
 * 和牌分析
 *
 * @param shantenResult 向听分析结果
 * @param agari 和牌张
 * @param tsumo 是否自摸
 * @param dora 宝牌数目
 * @param selfWind 自风
 * @param roundWind 场风
 * @param extraYaku 额外役种
 * @return 和牌分析结果
 */
fun hora(
    shantenResult: ShantenResult, agari: Tile, tsumo: Boolean,
    dora: Int = 0, selfWind: Wind? = null, roundWind: Wind? = null, extraYaku: Set<Yaku> = emptySet()
): Hora {
    if (shantenResult.shantenInfo !is ShantenWithGot) {
        throw IllegalArgumentException("shantenInfo is not with got")
    }
    if (shantenResult.shantenInfo.shantenNum != -1) {
        throw IllegalArgumentException("shantenNum != -1")
    }

    val patterns = buildList {
        if (shantenResult.regular?.shantenInfo?.shantenNum == -1) {
            addAll(shantenResult.regular.hand.patterns)
        }
        if (shantenResult.chitoi?.shantenInfo?.shantenNum == -1) {
            addAll(shantenResult.chitoi.hand.patterns)
        }
        if (shantenResult.kokushi?.shantenInfo?.shantenNum == -1) {
            addAll(shantenResult.kokushi.hand.patterns)
        }
    }

    val possibleHora = patterns.map { pat ->
        HoraHandPattern.build(pat, agari, tsumo, selfWind, roundWind).map { horaHandPat ->
            Hora(horaHandPat, dora, extraYaku)
        }
    }.flatten()
    val hora = possibleHora.maxBy {
        it.han * 1000 + it.pattern.hu  // first key: han, second key: hu
    }
    return hora
}