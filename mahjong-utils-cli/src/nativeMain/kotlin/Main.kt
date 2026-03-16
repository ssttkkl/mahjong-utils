package mahjongutils.cli

import kotlinx.serialization.json.Json
import mahjongutils.shanten.*
import mahjongutils.hora.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: mahjong-utils-cli <command> <json-args>")
        return
    }
    
    val command = args[0]
    val jsonArgs = if (args.size > 1) args[1] else ""
    
    try {
        when (command) {
            "shanten" -> handleShanten(jsonArgs)
            "hora" -> handleHora(jsonArgs)
            else -> println("Unknown command: $command")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun handleShanten(jsonArgs: String) {
    val args = Json.decodeFromString<CommonShantenArgs>(jsonArgs)
    val result = shanten(args)
    println(Formatter.formatHand(args.tiles, args.furo ?: emptyList()))
    println()
    
    when (result.shantenInfo.shantenNum) {
        -1 -> println("和牌")
        0 -> println("听牌")
        else -> println("${result.shantenInfo.shantenNum}向听")
    }
}

fun handleHora(jsonArgs: String) {
    val args = Json.decodeFromString<HoraArgs>(jsonArgs)
    val result = hora(args)
    val tiles = args.tiles ?: emptyList()
    println(Formatter.formatHand(tiles, args.furo))
    println()
    println("番数: ${result.han}番")
    println("符数: ${result.hu}")
}
