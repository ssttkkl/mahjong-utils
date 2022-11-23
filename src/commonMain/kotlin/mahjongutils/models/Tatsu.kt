package mahjongutils.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Tatsu {
    val first: Tile
    val second: Tile
    val waiting: Set<Tile>
    fun withWaiting(tile: Tile): Mentsu

    companion object {
        operator fun invoke(first: Tile, second: Tile): Tatsu {
            if (first > second)
                return invoke(second, first)

            if (first == second) {
                return Toitsu(first)
            } else {
                if (first.type == TileType.Z || second.type == TileType.Z) {
                    throw IllegalArgumentException("invalid tiles: $first $second")
                }

                when (second.distance(first)) {
                    1 -> {
                        if (first.num == 1 || first.num == 8) {
                            return Penchan(first)
                        } else {
                            return Ryanmen(first)
                        }
                    }

                    2 -> {
                        return Kanchan(first)
                    }

                    else -> {
                        throw IllegalArgumentException("invalid tiles: $first $second")
                    }
                }
            }
        }
    }
}

@Serializable
@SerialName("Ryanmen")
data class Ryanmen(override val first: Tile) : Tatsu {
    init {
        require(first.num in 2..7)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(1)

    override val waiting: Set<Tile>
        get() = setOf(first.advance(-1), first.advance(2))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (tile == first.advance(-1))
            return Shuntsu(tile)
        else if (tile == second.advance(1))
            return Shuntsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

@Serializable
@SerialName("Kanchan")
data class Kanchan(override val first: Tile) : Tatsu {
    init {
        require(first.num in 1..7)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(2)

    override val waiting: Set<Tile>
        get() = setOf(first.advance(1))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (tile == first.advance(1))
            return Shuntsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

@Serializable
@SerialName("Penchan")
data class Penchan(override val first: Tile) : Tatsu {
    init {
        require(first.num == 1 || first.num == 8)
        require(first.type != TileType.Z)
    }

    override val second: Tile
        get() = first.advance(1)

    override val waiting: Set<Tile>
        get() = if (first.num == 1)
            setOf(first.advance(2))
        else
            setOf(first.advance(-1))

    override fun withWaiting(tile: Tile): Shuntsu {
        if (first.num == 1 && tile == first.advance(2))
            return Shuntsu(first)
        if (first.num == 8 && tile == first.advance(-1))
            return Shuntsu(tile)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}

@Serializable
@SerialName("Toitsu")
data class Toitsu(override val first: Tile) : Tatsu {
    override val second: Tile
        get() = first

    override val waiting: Set<Tile>
        get() = setOf(first)

    override fun withWaiting(tile: Tile): Kotsu {
        if (tile == first)
            return Kotsu(first)
        else
            throw IllegalArgumentException("tile $tile is not waiting")
    }

    override fun toString(): String {
        return "${first.num}${second.num}${first.type.name.lowercase()}"
    }
}