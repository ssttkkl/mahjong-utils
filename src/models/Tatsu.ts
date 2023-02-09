import { Tile, TileType } from "./Tile";
import { Decoder, Encoder } from "./types";

export enum TatsuType {
    Ryanmen = "Ryanmen",
    Penchan = "Penchan",
    Kanchan = "Kanchan",
    Toitsu = "Toitsu"
}

export class Tatsu {
    constructor(readonly type: TatsuType, readonly first: Tile) {
        let valid = true
        switch (this.type) {
            case TatsuType.Ryanmen:
                valid &&= first.num >= 2 && first.num <= 7 && first.type !== TileType.Z
                break
            case TatsuType.Penchan:
                valid &&= (first.num === 1 || first.num ===8) && first.type !== TileType.Z
                break
            case TatsuType.Kanchan:
                valid &&= first.num >= 1 && first.num <= 7 && first.type !== TileType.Z
                break
        }

        if (!valid) {
            throw new Error(`${first} cannot be the first tile of ${type}`)
        }
    }

    static encode: Encoder<Tatsu, string> = (data) => {
        return data.toString()
    }

    static decode: Decoder<Tatsu, string> = (raw) => {
        const t = Tatsu.parse(raw)
        if (t === undefined) {
            throw new Error("invalid value: " + raw)
        }
        return t
    }

    get second(): Tile {
        switch (this.type) {
            case TatsuType.Ryanmen:
            case TatsuType.Penchan:
                return this.first.advance(1)!
            case TatsuType.Toitsu:
                return this.first
            case TatsuType.Kanchan:
                return this.first.advance(2)!
        }
    }

    toString(): string {
        return `${this.first.num}${this.second.num}${TileType[this.first.type]}`
    }

    static parse(tiles: string | Tile[]): Tatsu | undefined {
        if (typeof tiles === 'string') {
            const tiles_ = Tile.parseTiles(tiles)
            if (tiles_ === undefined) {
                return undefined
            }
            return Tatsu.parse(tiles_)
        } else {
            if (tiles.length !== 2) {
                return undefined
            }

            tiles.sort((a, b) => a.compareTo(b))

            switch (tiles[1].distance(tiles[0])) {
                case 0:
                    return new Tatsu(TatsuType.Toitsu, tiles[0])
                case 1:
                    if (tiles[0].num === 1 || tiles[1].num === 9) {
                        return new Tatsu(TatsuType.Penchan, tiles[0])
                    } else {
                        return new Tatsu(TatsuType.Ryanmen, tiles[0])
                    }
                case 2:
                    return new Tatsu(TatsuType.Kanchan, tiles[0])
                default:
                    return undefined
            }
        }
    }
}
