import { Mentsu, MentsuType } from './Mentsu.js'
import { Tile, TileType } from './Tile.js'

export enum FuroType {
  Chi = 'Chi',
  Pon = 'Pon',
  Minkan = 'Minkan',
  Ankan = 'Ankan'
}

export class Furo {
  constructor (readonly type: FuroType, readonly tile: Tile) {
    let valid = true
    if (this.type === FuroType.Chi) {
      valid &&= tile.num >= 1 && tile.num <= 7 && tile.type !== TileType.Z
    }

    if (!valid) {
      throw new Error(`${tile.toString()} cannot be the tile of ${type}`)
    }
  }

  static parse (tiles: string | Tile[], ankan?: boolean): Furo | undefined {
    if (typeof tiles === 'string') {
      let ankan_ = ankan
      let tiles_: Tile[] | undefined

      if (tiles.length === 5 && tiles[0] === tiles[3] && tiles[0] === '0' && tiles[1] === tiles[2]) {
        ankan_ = true
        tiles_ = Tile.parseTiles(`${tiles[1]}${tiles[1]}${tiles[1]}${tiles[1]}${tiles[4]}`)
      } else {
        tiles_ = Tile.parseTiles(tiles)
      }

      if (tiles_ === undefined) {
        return undefined
      }

      return Furo.parse(tiles_, ankan_)
    } else {
      if (tiles.length === 3) {
        if (tiles[0] === tiles[1] && tiles[1] === tiles[2]) {
          return new Furo(FuroType.Pon, tiles[0])
        } else {
          if (tiles.some(it => it.type === TileType.Z)) {
            return undefined
          }

          const tiles_ = [...tiles]
          tiles_.sort((a, b) => a.compareTo(b))
          if (tiles[1].distance(tiles[0]) === 1 && tiles[2].distance(tiles[1]) === 1) {
            return new Furo(FuroType.Chi, tiles[0])
          } else {
            return undefined
          }
        }
      } else if (tiles.length === 4) {
        if (tiles[0] === tiles[1] && tiles[1] === tiles[2] && tiles[2] === tiles[3]) {
          return ankan ? new Furo(FuroType.Ankan, tiles[0]) : new Furo(FuroType.Minkan, tiles[0])
        }
      }

      return undefined
    }
  }

  get tiles (): Tile[] {
    switch (this.type) {
      case FuroType.Chi:
        return [this.tile, this.tile.advance(1)!, this.tile.advance(2)!]
      case FuroType.Pon:
        return [this.tile, this.tile, this.tile]
      case FuroType.Minkan:
      case FuroType.Ankan:
        return [this.tile, this.tile, this.tile, this.tile]
    }
  }

  toString (): string {
    switch (this.type) {
      case FuroType.Chi:
        return `${this.tile.num}${this.tile.num + 1}${this.tile.num + 2}${FuroType[this.type]}`.toLowerCase()
      case FuroType.Pon:
        return `${this.tile.num}${this.tile.num}${this.tile.num}${FuroType[this.type]}`.toLowerCase()
      case FuroType.Minkan:
        return `${this.tile.num}${this.tile.num}${this.tile.num}${this.tile.num}${FuroType[this.type]}`.toLowerCase()
      case FuroType.Ankan:
        return `0${this.tile.num}${this.tile.num}0${FuroType[this.type]}`.toLowerCase()
    }
  }

  asMentsu (): Mentsu {
    switch (this.type) {
      case FuroType.Chi:
        return new Mentsu(MentsuType.Shuntsu, this.tile)
      case FuroType.Pon:
      case FuroType.Minkan:
      case FuroType.Ankan:
        return new Mentsu(MentsuType.Kotsu, this.tile)
    }
  }
}
