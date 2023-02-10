import { Tile, TileType } from './Tile'

export enum MentsuType {
  Shuntsu = 'Shuntsu',
  Kotsu = 'Kotsu'
}

export class Mentsu {
  constructor (readonly type: MentsuType, readonly tile: Tile) {
    let valid = true
    if (this.type === MentsuType.Shuntsu) {
      valid &&= tile.num >= 1 && tile.num <= 7 && tile.type !== TileType.Z
    }

    if (!valid) {
      throw new Error(`${tile.toString()} cannot be the tile of ${type}`)
    }
  }

  get tiles (): Tile[] {
    switch (this.type) {
      case MentsuType.Kotsu:
        return [this.tile, this.tile, this.tile]
      case MentsuType.Shuntsu:
        return [this.tile, this.tile.advance(1)!, this.tile.advance(2)!]
    }
  }

  toString (): string {
    switch (this.type) {
      case MentsuType.Kotsu:
        return `${this.tile.num}${this.tile.num}${this.tile.num}${this.tile.type}`
      case MentsuType.Shuntsu:
        return `${this.tile.num}${this.tile.num + 1}${this.tile.num + 2}${this.tile.type}`
    }
  }

  static parse (tiles: string | Tile[]): Mentsu | undefined {
    if (typeof tiles === 'string') {
      const tiles_ = Tile.parseTiles(tiles)
      if (tiles_ === undefined) {
        return undefined
      }
      return Mentsu.parse(tiles_)
    } else {
      if (tiles.length !== 3) {
        return undefined
      }

      tiles.sort((a, b) => a.compareTo(b))

      if (tiles[0] === tiles[1] && tiles[1] === tiles[2]) {
        return new Mentsu(MentsuType.Kotsu, tiles[0])
      } else if (tiles[2].distance(tiles[1]) === 1 && tiles[1].distance(tiles[0]) === 1) {
        return new Mentsu(MentsuType.Shuntsu, tiles[0])
      } else {
        return undefined
      }
    }
  }
}
