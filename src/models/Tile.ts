export enum TileType {
  M,
  P,
  S,
  Z
}

export class Tile {
  private constructor (readonly type: TileType, readonly num: number) {
  }

  get realNum (): number {
    if (this.type !== TileType.Z && this.num === 0) {
      return 5
    } else {
      return this.num
    }
  }

  get code (): number {
    return this.type * 10 + this.num
  }

  compareTo (other: Tile): number {
    if (this.type !== other.type) {
      return this.type - other.type
    } else if (this.num !== 0 && other.num !== 0) {
      return this.num - other.num
    } else if (this.num === 0 && other.num === 0) {
      return 0
    } else if (this.num === 0) {
      return other.num > 5 ? -1 : 1
    } else {
      return other.num <= 5 ? -1 : 1
    }
  }

  distance (other: Tile): number {
    const a = this.num === 0 ? Tile.byTypeAndNum(this.type, 5)! : this
    const b = other.num === 0 ? Tile.byTypeAndNum(other.type, 5)! : other
    return a.type === b.type ? a.num - b.num : Infinity
  }

  advance (step: number): Tile | undefined {
    return Tile.byTypeAndNum(this.type, this.num + step)
  }

  toString (): string {
    return `${this.num}${TileType[this.type]}`
  }

  private static readonly pool: Array<Tile | undefined> = []

  static {
    for (let i = 0; i <= 9; i++) {
      Tile.pool.push(new Tile(TileType.M, i))
    }
    for (let i = 0; i <= 9; i++) {
      Tile.pool.push(new Tile(TileType.P, i))
    }
    for (let i = 0; i <= 9; i++) {
      Tile.pool.push(new Tile(TileType.S, i))
    }
    Tile.pool.push(undefined)
    for (let i = 1; i <= 7; i++) {
      Tile.pool.push(new Tile(TileType.Z, i))
    }
  }

  static byTypeAndNum (type: TileType, num: number): Tile | undefined {
    const code = type * 10 + num
    const tile = Tile.pool[code]
    if (num < 0 || num > 9) {
      return undefined
    }
    return tile
  }

  static byCode (code: number): Tile | undefined {
    const type = Math.floor(code / 10)
    const num = code % 10
    if (type < 0 || type > 3) {
      return undefined
    }
    return Tile.byTypeAndNum(type, num)
  }

  static byText (text: string): Tile | undefined {
    if (text.length !== 2) {
      return undefined
    }

    let type: TileType
    switch (text[1].toLowerCase()) {
      case 'm':
        type = TileType.M
        break
      case 'p':
        type = TileType.P
        break
      case 's':
        type = TileType.S
        break
      case 'z':
        type = TileType.Z
        break
      default:
        return undefined
    }

    const num = parseInt(text[0])
    if (isNaN(num)) {
      return undefined
    }

    return Tile.byTypeAndNum(type, num)
  }

  static parseTiles (code: string): Tile[] | undefined {
    let pending: number[] = []
    let tiles: Tile[] = []
    for (const c of code) {
      if (c === 'M' || c === 'm' || c === 'P' || c === 'p' || c === 'S' || c === 's') {
        tiles = [...tiles, ...pending.map(value => Tile.byText(`${value}${c}`)!)]
        pending = []
      } else if (c === 'Z' || c === 'z') {
        if (pending.every(value => value >= 1 && value <= 7)) {
          tiles = [...tiles, ...pending.map(value => Tile.byText(`${value}${c}`)!)]
          pending = []
        } else {
          return undefined
        }
      } else if (c >= '1' && c <= '9') {
        pending.push(parseInt(c))
      } else {
        return undefined
      }
    }

    if (pending.length > 0) {
      return undefined
    }

    return tiles
  }

  static tilesToString (tiles: Tile[], opts: {
    sorted?: boolean
  } = {}): string {
    let tiles_: Tile[]

    if (opts.sorted) {
      tiles_ = [...tiles]
      tiles_.sort((a, b) => a.compareTo(b))
    } else {
      tiles_ = tiles
    }

    let result = ''
    let prev: Tile | null = null
    for (const t of tiles_) {
      if (prev !== null && prev.type !== t.type) {
        result += TileType[prev.type]
      }

      result += t.num.toString()
      prev = t
    }

    if (prev !== null) {
      result += TileType[prev.type]
    }

    return result
  }
}
