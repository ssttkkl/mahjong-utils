package mahjongutils.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Mentsu {
    val tiles: Iterable<Tile>
    fun afterDiscard(discard: Tile): Tatsu

    companion object {
        operator fun invoke(tiles: List<Tile>): Mentsu {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Kotsu(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Z }) {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Shuntsu(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }
                }
            }

            throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
        }

        operator fun invoke(text: String): Mentsu {
            return invoke(Tile.parseTiles(text))
        }
    }
}

@Serializable
@SerialName("Kotsu")
data class Kotsu(val tile: Tile) : Mentsu {
    override val tiles: Iterable<Tile>
        get() = listOf(tile, tile, tile)

    override fun afterDiscard(discard: Tile): Tatsu {
        if (discard == tile) {
            return Toitsu(discard)
        }
        throw IllegalArgumentException("invalid discard: $discard")
    }

    override fun toString(): String {
        return "${tile.num}${tile.num}${tile.num}${tile.type.name.lowercase()}"
    }
}

@Serializable
@SerialName("Shuntsu")
data class Shuntsu(val tile: Tile) : Mentsu {
    init {
        require(tile.num in 1..7)
        require(tile.type != TileType.Z)
    }

    override val tiles: Iterable<Tile>
        get() = listOf(tile, tile.advance(1), tile.advance(2))

    override fun afterDiscard(discard: Tile): Tatsu {
        if (discard == tile) {
            if (discard.num == 7)
                return Penchan(discard.advance(1))
            else if (discard.num == 3)
                return Penchan(discard.advance(-2))
            else
                return Ryanmen(discard.advance(1))
        } else if (discard == tile.advance(1))
            return Kanchan(tile)
        else if (discard == tile.advance(2)) {
            if (tile.num == 1)
                return Penchan(tile)
            else
                return Ryanmen(tile)
        }
        throw IllegalArgumentException("invalid discard: $discard")
    }

    override fun toString(): String {
        return "${tile.num}${tile.num + 1}${tile.num + 2}${tile.type.name.lowercase()}"
    }
}