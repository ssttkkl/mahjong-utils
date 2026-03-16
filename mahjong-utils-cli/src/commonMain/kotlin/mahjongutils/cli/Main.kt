package mahjongutils.cli

import mahjongutils.shanten.*
import mahjongutils.hora.*
import mahjongutils.models.Tile
import mahjongutils.models.Wind

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: mahjong-utils-cli <command> <tiles> [options]")
        println("Commands:")
        println("  shanten <tiles>")
        println("  hora <tiles> [--agari <tile>] [--tsumo] [--dora <n>] [--self-wind <E|S|W|N>] [--round-wind <E|S|W|N>]")
        println("Example: mahjong-utils-cli hora 123m456p789s1122z --agari 2z --tsumo --dora 2")
        return
    }
    
    val command = args[0]
    
    try {
        when (command) {
            "shanten" -> handleShanten(args.drop(1))
            "hora" -> handleHora(args.drop(1))
            else -> println("Unknown command: $command")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun handleShanten(args: List<String>) {
    if (args.isEmpty()) {
        println("Usage: shanten <tiles>")
        return
    }
    
    val tiles = TileCodeParser.parse(args[0])
    val shantenArgs = CommonShantenArgs(tiles = tiles)
    val result = shanten(shantenArgs)
    println(Formatter.formatHand(tiles))
    println()
    
    Formatter.formatShantenResult(result)
}

fun handleHora(args: List<String>) {
    if (args.isEmpty()) {
        println("Usage: hora <tiles> [options]")
        return
    }
    
    val tiles = TileCodeParser.parse(args[0])
    var agari: Tile? = null
    var tsumo = false
    var dora = 0
    var selfWind: Wind? = null
    var roundWind: Wind? = null
    
    var i = 1
    while (i < args.size) {
        when (args[i]) {
            "--agari" -> {
                if (i + 1 < args.size) {
                    agari = TileCodeParser.parse(args[i + 1]).firstOrNull()
                    i += 2
                } else i++
            }
            "--tsumo" -> {
                tsumo = true
                i++
            }
            "--dora" -> {
                if (i + 1 < args.size) {
                    dora = args[i + 1].toIntOrNull() ?: 0
                    i += 2
                } else i++
            }
            "--self-wind" -> {
                if (i + 1 < args.size) {
                    selfWind = parseWind(args[i + 1])
                    i += 2
                } else i++
            }
            "--round-wind" -> {
                if (i + 1 < args.size) {
                    roundWind = parseWind(args[i + 1])
                    i += 2
                } else i++
            }
            else -> i++
        }
    }
    
    val finalAgari = agari ?: tiles.lastOrNull() ?: return
    val horaArgs = HoraArgs(
        tiles = tiles,
        agari = finalAgari,
        tsumo = tsumo,
        dora = dora,
        selfWind = selfWind,
        roundWind = roundWind
    )
    
    val result = hora(horaArgs)
    println(Formatter.formatHand(tiles))
    println()
    println("和牌张: $finalAgari")
    println("${if (tsumo) "自摸" else "荣和"}")
    if (dora > 0) println("宝牌: ${dora}枚")
    println()
    
    Formatter.formatHoraResult(result)
}

fun parseWind(s: String): Wind? = when (s.uppercase()) {
    "E", "EAST" -> Wind.East
    "S", "SOUTH" -> Wind.South
    "W", "WEST" -> Wind.West
    "N", "NORTH" -> Wind.North
    else -> null
}
