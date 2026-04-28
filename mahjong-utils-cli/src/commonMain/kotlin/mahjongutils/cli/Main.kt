package mahjongutils.cli

import mahjongutils.shanten.*
import mahjongutils.hora.*
import mahjongutils.models.Tile
import mahjongutils.models.Wind
import mahjongutils.models.Furo

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: mahjong-utils-cli <command> <tiles> [options]")
        println("Commands:")
        println("  shanten <tiles> [--furo <furo>] [--lang <zh|en|ja>]")
        println("  furo-chance <tiles> <chance-tile> [--no-chi] [--lang <zh|en|ja>]")
        println("  hora <tiles> [--agari <tile>] [--tsumo] [--dora <n>] [--self-wind <E|S|W|N>] [--round-wind <E|S|W|N>] [--furo <furo>] [--lang <zh|en|ja>]")
        println("  point <han> <hu> [--tsumo] [--parent] [--lang <zh|en|ja>]")
        println("Example: mahjong-utils-cli hora 123m456p789s1122z --agari 2z --tsumo --dora 2 --lang en")
        return
    }
    
    val command = args[0]
    
    // Parse global --lang option
    val langIndex = args.indexOf("--lang")
    if (langIndex != -1 && langIndex + 1 < args.size) {
        Formatter.language = when (args[langIndex + 1].lowercase()) {
            "zh" -> Language.ZH
            "en" -> Language.EN
            "ja" -> Language.JA
            else -> Language.detect()
        }
    }
    
    try {
        when (command) {
            "shanten" -> handleShanten(args.drop(1))
            "furo-chance" -> handleFuroChance(args.drop(1))
            "hora" -> handleHora(args.drop(1))
            "point" -> handlePoint(args.drop(1))
            else -> println("Unknown command: $command")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun handleShanten(args: List<String>) {
    if (args.isEmpty()) {
        println("Usage: shanten <tiles> [--furo <furo>]")
        return
    }

    val tiles = TileCodeParser.parse(args[0])
    if (tiles.isEmpty()) {
        println("Error: Failed to parse tiles. Please provide valid tile notation (e.g., 123m456p789s1122z).")
        return
    }
    var furo = emptyList<Furo>()
    
    var i = 1
    while (i < args.size) {
        when (args[i]) {
            "--furo" -> {
                if (i + 1 < args.size) {
                    furo = TileCodeParser.parseFuro(args[i + 1])
                    i += 2
                } else i++
            }
            else -> i++
        }
    }
    
    val shantenArgs = CommonShantenArgs(tiles = tiles, furo = furo)
    val result = shanten(shantenArgs)
    println(Formatter.formatHand(tiles, furo))
    println()
    
    Formatter.formatShantenResult(result)
}

fun handleFuroChance(args: List<String>) {
    if (args.size < 2) {
        println("Usage: furo-chance <tiles> <chance-tile> [--no-chi]")
        return
    }

    val tiles = TileCodeParser.parse(args[0])
    if (tiles.isEmpty()) {
        println("Error: Failed to parse tiles. Please provide valid tile notation (e.g., 123m456p789s1122z).")
        return
    }
    val chanceTile = TileCodeParser.parse(args[1]).firstOrNull() ?: run {
        println("Error: Failed to parse chance tile. Please provide a valid single tile (e.g., 5m).")
        return
    }
    var allowChi = true
    
    var i = 2
    while (i < args.size) {
        when (args[i]) {
            "--no-chi" -> {
                allowChi = false
                i++
            }
            else -> i++
        }
    }
    
    val furoArgs = FuroChanceShantenArgs(
        tiles = tiles,
        chanceTile = chanceTile,
        allowChi = allowChi
    )
    val result = furoChanceShanten(furoArgs)
    
    println(Formatter.formatHand(tiles))
    println()
    
    Formatter.formatFuroChanceResult(result, chanceTile)
}

fun handleHora(args: List<String>) {
    if (args.isEmpty()) {
        println("Usage: hora <tiles> [options]")
        return
    }
    
    val tiles = TileCodeParser.parse(args[0])
    if (tiles.isEmpty()) {
        println("Error: Failed to parse tiles. Please provide valid tile notation (e.g., 123m456p789s1122z).")
        return
    }
    var agari: Tile? = null
    var tsumo = false
    var dora = 0
    var selfWind: Wind? = null
    var roundWind: Wind? = null
    var furo = emptyList<Furo>()
    
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
            "--furo" -> {
                if (i + 1 < args.size) {
                    furo = TileCodeParser.parseFuro(args[i + 1])
                    i += 2
                } else i++
            }
            else -> i++
        }
    }
    
    val finalAgari = agari ?: tiles.lastOrNull() ?: run {
        println("Error: Cannot determine winning tile (agari). Please specify with --agari or ensure hand has at least one tile.")
        return
    }
    val horaArgs = HoraArgs(
        tiles = tiles,
        furo = furo,
        agari = finalAgari,
        tsumo = tsumo,
        dora = dora,
        selfWind = selfWind,
        roundWind = roundWind
    )
    
    val result = hora(horaArgs)
    println(Formatter.formatHand(tiles, furo))
    println()
    
    val msg = Messages.get(Formatter.language)
    println("${msg.winningTile}: $finalAgari")
    println(if (tsumo) msg.tsumo else msg.ron)
    if (dora > 0) println("${msg.dora}: $dora")
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

fun handlePoint(args: List<String>) {
    if (args.size < 2) {
        println("Usage: point <han> <hu> [--tsumo] [--parent]")
        return
    }
    
    val han = args[0].toIntOrNull() ?: run {
        println("Error: Invalid han value. Please provide a valid number.")
        return
    }
    val hu = args[1].toIntOrNull() ?: run {
        println("Error: Invalid hu value. Please provide a valid number.")
        return
    }
    var tsumo = false
    var parent = false
    
    var i = 2
    while (i < args.size) {
        when (args[i]) {
            "--tsumo" -> {
                tsumo = true
                i++
            }
            "--parent" -> {
                parent = true
                i++
            }
            else -> i++
        }
    }
    
    val msg = Messages.get(Formatter.language)
    println("${msg.han}: $han")
    println("${msg.hu}: $hu")
    println()
    
    val parentPoint = mahjongutils.hanhu.getParentPointByHanHu(han, hu)
    val childPoint = mahjongutils.hanhu.getChildPointByHanHu(han, hu)
    
    if (tsumo) {
        println("${msg.parentTsumo}: ${parentPoint.tsumo}${msg.points}")
        println("${msg.childTsumo}: ${msg.parent}${childPoint.tsumoParent}${msg.points} ${msg.child}${childPoint.tsumoChild}${msg.points}")
    } else {
        println("${msg.parentRon}: ${parentPoint.ron}${msg.points}")
        println("${msg.childRon}: ${childPoint.ron}${msg.points}")
    }
}
