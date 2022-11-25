package mahjongutils.models

/**
 * 风（东、南、西、北）
 */
enum class Wind(
    /**
     * 对应的风牌
     */
    val tile: Tile
) {
    East(Tile.get(TileType.Z, 1)),
    South(Tile.get(TileType.Z, 2)),
    West(Tile.get(TileType.Z, 3)),
    North(Tile.get(TileType.Z, 4)),
}