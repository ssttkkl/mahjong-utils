package mahjongutils.cli

import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.Furo
import mahjongutils.shanten.*
import mahjongutils.hora.Hora

object Formatter {
    var language: Language = Language.detect()
    private val messages: Messages get() = Messages.get(language)
    
    fun formatTiles(tiles: List<Tile>): String = tiles.joinToString("")
    
    fun formatHand(tiles: List<Tile>, furo: List<Furo> = emptyList()): String {
        val sorted = if (tiles.size % 3 == 2) {
            tiles.dropLast(1).sorted() + tiles.last()
        } else tiles.sorted()
        return buildString {
            append(formatTiles(sorted))
            if (furo.isNotEmpty()) {
                append(" ")
                append(furo.joinToString(" "))
            }
        }
    }
    
    fun formatShantenResult(result: CommonShantenResult<*>) {
        val info = result.shantenInfo
        
        when (info.shantenNum) {
            -1 -> println(messages.agari)
            0 -> println(messages.tenpai)
            else -> println(messages.shanten(info.shantenNum))
        }
        println()
        
        when (info) {
            is ShantenWithoutGot -> formatShantenWithoutGot(info)
            is ShantenWithGot -> formatShantenWithGot(info)
        }
    }
    
    private fun formatShantenWithoutGot(info: ShantenWithoutGot) {
        val advanceCount = info.advance.size * 4
        println("${messages.advance} (${info.advanceNum}${messages.types}${advanceCount}${messages.tiles}):")
        println(formatTiles(info.advance.sorted()))
        
        if (info.shantenNum == 1) {
            val goodShapeAdvance = info.goodShapeAdvance
            if (goodShapeAdvance != null) {
                val goodShapeCount = goodShapeAdvance.size * 4
                println()
                println("${messages.goodShapeAdvance} (${info.goodShapeAdvanceNum}${messages.types}${goodShapeCount}${messages.tiles}):")
                println(formatTiles(goodShapeAdvance.sorted()))
            }
        }
        
        if (info.shantenNum == 0) {
            val improvement = info.improvement
            if (improvement != null && improvement.isNotEmpty()) {
                println()
                println("${messages.improvement} (${info.improvementNum}${messages.types}):")
                improvement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
            
            val goodShapeImprovement = info.goodShapeImprovement
            if (goodShapeImprovement != null && goodShapeImprovement.isNotEmpty()) {
                println()
                println("${messages.goodShapeImprovement} (${info.goodShapeImprovementNum}${messages.types}):")
                goodShapeImprovement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
        }
    }
    
    private fun formatShantenWithGot(info: ShantenWithGot) {
        println("${messages.discardChoice}:")
        info.discardToAdvance.forEach { (discard, advance) ->
            val advanceCount = advance.advance.size * 4
            println("  ${messages.discard}$discard: ${advance.advanceNum}${messages.types}${advanceCount}${messages.tiles}${messages.advance}")
        }
    }
    
    fun formatHoraResult(result: Hora) {
        println("${messages.yaku}:")
        result.yaku.forEach { println("  - $it") }
        println()
        println("${messages.han}: ${result.han}")
        println("${messages.hu}: ${result.hu}")
        println()
        
        if (result.tsumo) {
            println("${messages.parentTsumo}: ${result.parentPoint.tsumo}${messages.points}")
            println("${messages.childTsumo}: ${messages.parent}${result.childPoint.tsumoParent}${messages.points} ${messages.child}${result.childPoint.tsumoChild}${messages.points}")
        } else {
            println("${messages.parentRon}: ${result.parentPoint.ron}${messages.points}")
            println("${messages.childRon}: ${result.childPoint.ron}${messages.points}")
        }
    }
    
    fun formatFuroChanceResult(result: FuroChanceShantenResult, chanceTile: Tile) {
        val info = result.shantenInfo
        
        val chanceLabel = when (language) {
            Language.ZH -> "机会牌"
            Language.EN -> "Chance tile"
            Language.JA -> "鳴き牌"
        }
        println("$chanceLabel: $chanceTile")
        println()
        
        if (info.canRon) {
            println(messages.agari)
            return
        }
        
        val passLabel = when (language) {
            Language.ZH -> "Pass"
            Language.EN -> "Pass"
            Language.JA -> "スルー"
        }
        val chiLabel = when (language) {
            Language.ZH -> "吃"
            Language.EN -> "Chi"
            Language.JA -> "チー"
        }
        val ponLabel = when (language) {
            Language.ZH -> "碰"
            Language.EN -> "Pon"
            Language.JA -> "ポン"
        }
        val minkanLabel = when (language) {
            Language.ZH -> "明杠"
            Language.EN -> "Minkan"
            Language.JA -> "明カン"
        }
        
        // Pass
        info.pass?.let { pass ->
            println("$passLabel: ${messages.shanten(pass.shantenNum)}")
        }
        
        // Chi
        if (info.chi.isNotEmpty()) {
            println()
            println("$chiLabel:")
            info.chi.forEach { (tatsu, shanten) ->
                println("  $tatsu: ${messages.shanten(shanten.shantenNum)}")
            }
        }
        
        // Pon
        info.pon?.let { pon ->
            println()
            println("$ponLabel: ${messages.shanten(pon.shantenNum)}")
        }
        
        // Minkan
        info.minkan?.let { minkan ->
            println()
            println("$minkanLabel: ${messages.shanten(minkan.shantenNum)}")
        }
    }
}

object TileCodeParser {
    fun parse(code: String): List<Tile> {
        val tiles = mutableListOf<Tile>()
        var nums = mutableListOf<Int>()
        
        for (c in code) {
            when {
                c.isDigit() -> nums.add(c.digitToInt())
                c in "mpsz" -> {
                    val type = when (c) {
                        'm' -> TileType.M
                        'p' -> TileType.P
                        's' -> TileType.S
                        'z' -> TileType.Z
                        else -> continue
                    }
                    nums.forEach { tiles.add(Tile.get(type, it)) }
                    nums.clear()
                }
            }
        }
        return tiles
    }
    
    fun parseFuro(code: String): List<Furo> {
        if (code.isEmpty()) return emptyList()
        return code.split(",").map { Furo(it.trim()) }
    }
}

