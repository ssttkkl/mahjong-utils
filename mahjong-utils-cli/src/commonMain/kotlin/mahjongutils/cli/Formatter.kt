package mahjongutils.cli

import mahjongutils.models.Tile
import mahjongutils.models.TileType
import mahjongutils.models.Furo
import mahjongutils.shanten.*
import mahjongutils.hora.Hora

object Formatter {
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
            -1 -> println("和牌")
            0 -> println("听牌")
            else -> println("${info.shantenNum}向听")
        }
        println()
        
        when (info) {
            is ShantenWithoutGot -> formatShantenWithoutGot(info)
            is ShantenWithGot -> formatShantenWithGot(info)
        }
    }
    
    private fun formatShantenWithoutGot(info: ShantenWithoutGot) {
        val advanceCount = info.advance.size * 4
        println("进张 (${info.advanceNum}种${advanceCount}张):")
        println(formatTiles(info.advance.sorted()))
        
        if (info.shantenNum == 1) {
            val goodShapeAdvance = info.goodShapeAdvance
            if (goodShapeAdvance != null) {
                val goodShapeCount = goodShapeAdvance.size * 4
                println()
                println("好型进张 (${info.goodShapeAdvanceNum}种${goodShapeCount}张):")
                println(formatTiles(goodShapeAdvance.sorted()))
            }
        }
        
        if (info.shantenNum == 0) {
            val improvement = info.improvement
            if (improvement != null && improvement.isNotEmpty()) {
                println()
                println("改良张 (${info.improvementNum}种):")
                improvement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
            
            val goodShapeImprovement = info.goodShapeImprovement
            if (goodShapeImprovement != null && goodShapeImprovement.isNotEmpty()) {
                println()
                println("好型改良张 (${info.goodShapeImprovementNum}种):")
                goodShapeImprovement.forEach { (tile, improvements) ->
                    println("  $tile -> ${improvements.map { "${it.discard}(${it.advanceNum})" }.joinToString(", ")}")
                }
            }
        }
    }
    
    private fun formatShantenWithGot(info: ShantenWithGot) {
        println("弃牌选择:")
        info.discardToAdvance.forEach { (discard, advance) ->
            val advanceCount = advance.advance.size * 4
            println("  打$discard: ${advance.advanceNum}种${advanceCount}张进张")
        }
    }
    
    fun formatHoraResult(result: Hora) {
        println("役种:")
        result.yaku.forEach { println("  - $it") }
        println()
        println("番数: ${result.han}番")
        println("符数: ${result.hu}符")
        println()
        
        if (result.tsumo) {
            println("亲家自摸: ${result.parentPoint.tsumo}点")
            println("子家自摸: 亲${result.childPoint.tsumoParent}点 子${result.childPoint.tsumoChild}点")
        } else {
            println("亲家荣和: ${result.parentPoint.ron}点")
            println("子家荣和: ${result.childPoint.ron}点")
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
