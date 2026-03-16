package mahjongutils.cli

import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.Furo
import mahjongutils.shanten.*
import mahjongutils.hora.Hora

object Formatter {
    var language: Language = Language.detect()
    
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
            -1 -> println(Messages.agari(language))
            0 -> println(Messages.tenpai(language))
            else -> println(Messages.shanten(info.shantenNum, language))
        }
        println()
        
        when (info) {
            is ShantenWithoutGot -> formatShantenWithoutGot(info)
            is ShantenWithGot -> formatShantenWithGot(info)
        }
    }
    
    private fun formatShantenWithoutGot(info: ShantenWithoutGot) {
        val advanceCount = info.advance.size * 4
        println("${Messages.advance(language)} (${info.advanceNum}${Messages.types(language)}${advanceCount}${Messages.tiles(language)}):")
        println(formatTiles(info.advance.sorted()))
        
        if (info.shantenNum == 1) {
            val goodShapeAdvance = info.goodShapeAdvance
            if (goodShapeAdvance != null) {
                val goodShapeCount = goodShapeAdvance.size * 4
                println()
                println("${Messages.goodShapeAdvance(language)} (${info.goodShapeAdvanceNum}${Messages.types(language)}${goodShapeCount}${Messages.tiles(language)}):")
                println(formatTiles(goodShapeAdvance.sorted()))
            }
        }
        
        if (info.shantenNum == 0) {
            val improvement = info.improvement
            if (improvement != null && improvement.isNotEmpty()) {
                println()
                println("${Messages.improvement(language)} (${info.improvementNum}${Messages.types(language)}):")
                improvement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
            
            val goodShapeImprovement = info.goodShapeImprovement
            if (goodShapeImprovement != null && goodShapeImprovement.isNotEmpty()) {
                println()
                println("${Messages.goodShapeImprovement(language)} (${info.goodShapeImprovementNum}${Messages.types(language)}):")
                goodShapeImprovement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
        }
    }
    
    private fun formatShantenWithGot(info: ShantenWithGot) {
        println("${Messages.discardChoice(language)}:")
        info.discardToAdvance.forEach { (discard, advance) ->
            val advanceCount = advance.advance.size * 4
            println("  ${Messages.discard(language)}$discard: ${advance.advanceNum}${Messages.types(language)}${advanceCount}${Messages.tiles(language)}${Messages.advance(language)}")
        }
    }
    
    fun formatHoraResult(result: Hora) {
        println("${Messages.yaku(language)}:")
        result.yaku.forEach { println("  - $it") }
        println()
        println("${Messages.han(language)}: ${result.han}")
        println("${Messages.hu(language)}: ${result.hu}")
        println()
        
        if (result.tsumo) {
            println("${Messages.parentTsumo(language)}: ${result.parentPoint.tsumo}${Messages.points(language)}")
            println("${Messages.childTsumo(language)}: ${Messages.parent(language)}${result.childPoint.tsumoParent}${Messages.points(language)} ${Messages.child(language)}${result.childPoint.tsumoChild}${Messages.points(language)}")
        } else {
            println("${Messages.parentRon(language)}: ${result.parentPoint.ron}${Messages.points(language)}")
            println("${Messages.childRon(language)}: ${result.childPoint.ron}${Messages.points(language)}")
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

