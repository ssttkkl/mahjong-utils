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
    East(Tile.get(TileType.Honour, 1)),
    South(Tile.get(TileType.Honour, 2)),
    West(Tile.get(TileType.Honour, 3)),
    North(Tile.get(TileType.Honour, 4)),
}