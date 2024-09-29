import { Furo } from './Furo.js'
import { Tile } from './Tile.js'

export type TileArg = Tile | string
export type TilesArg = string | TileArg[]
export type FuroArg = Furo | TilesArg

export function unifyTileArg (tile: TileArg): Tile {
  if (tile instanceof Tile) {
    return tile
  } else {
    const t = Tile.byText(tile)
    if (t === undefined) {
      throw Error(`${tile} cannot be parsed`)
    }
    return t
  }
}

export function unifyTilesArg (tiles: TilesArg): Tile[] {
  let _tiles: Tile[]

  if (typeof tiles === 'string') {
    const parsedTiles = Tile.parseTiles(tiles)
    if (parsedTiles === undefined) {
      throw Error(`${tiles} cannot be parsed`)
    }
    _tiles = parsedTiles
  } else {
    _tiles = tiles.map(x => unifyTileArg(x))
  }

  return _tiles
}

export function unifyFuroArg (furo: FuroArg): Furo {
  if (furo instanceof Furo) {
    return furo
  } else {
    const parsedFuro = Furo.parse(unifyTilesArg(furo))
    if (parsedFuro === undefined) {
      throw Error(`${furo} cannot be parsed`)
    }
    return parsedFuro
  }
}
