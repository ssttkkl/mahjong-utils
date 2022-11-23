@file:OptIn(ExperimentalSerializationApi::class)

package mahjongutils.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Furo {
    fun asMentsu(): Mentsu

    companion object {
        operator fun invoke(tiles: List<Tile>, ankan: Boolean = false): Furo {
            if (tiles.size == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return Pon(tiles[0])
                } else {
                    if (tiles.any { it.type == TileType.Z }) {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }

                    val tiles = tiles.sorted()
                    if (tiles[1].distance(tiles[0]) == 1 && tiles[2].distance(tiles[1]) == 1) {
                        return Chi(tiles[0])
                    } else {
                        throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
                    }
                }
            } else if (tiles.size == 4) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2] && tiles[2] == tiles[3]) {
                    return Kan(tiles[0], ankan)
                }
            }

            throw IllegalArgumentException("invalid tiles: ${tiles.toTilesString()}")
        }

        operator fun invoke(text: String, ankan: Boolean = false): Furo {
            var ankan_ = ankan

            val tiles = if (text.length == 5 && text[0] == text[3] && text[0] == '0' && text[1] == text[2]) {
                ankan_ = true
                Tile.parseTiles("${text[1]}${text[1]}${text[1]}${text[1]}${text[4]}")
            } else {
                Tile.parseTiles(text)
            }

            return invoke(tiles, ankan_)
        }
    }
}

@Serializable
@SerialName("Chi")
data class Chi(val tile: Tile) : Furo {
    override fun asMentsu(): Shuntsu {
        return Shuntsu(tile)
    }
}


@Serializable
@SerialName("Pon")
data class Pon(val tile: Tile) : Furo {
    override fun asMentsu(): Kotsu {
        return Kotsu(tile)
    }
}

@Serializable
@SerialName("Kan")
data class Kan(
    val tile: Tile,
    @EncodeDefault val ankan: Boolean = false
) : Furo {
    override fun asMentsu(): Kotsu {
        return Kotsu(tile)
    }
}
