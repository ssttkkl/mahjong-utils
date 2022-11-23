package mahjongutils.models

enum class Wind(val tile: Tile) {
    East(Tile.get(TileType.Z, 1)),
    South(Tile.get(TileType.Z, 2)),
    West(Tile.get(TileType.Z, 3)),
    North(Tile.get(TileType.Z, 4)),
}