import {Tile, TileType} from "./Tile";
import {Decoder, Encoder} from "./types";

export enum FuroType {
    Chi = "Chi",
    Pon = "Pon",
    Minkan = "Minkan",
    Ankan = "Ankan"
}

export class Furo {
    constructor(readonly type: FuroType, readonly tile: Tile) {
    }

    static encode: Encoder<Furo> = (data) => {
        let raw: any = {type: data.type, tile: Tile.encode(data.tile)}
        if (data.type === FuroType.Minkan) {
            raw.type = "Kan"
            raw.ankan = false
        } else if (data.type === FuroType.Ankan) {
            raw.type = "Kan"
            raw.ankan = true
        }
        return raw
    }

    static decode: Decoder<Furo> = (raw) => {
        let type: FuroType
        if (raw.type === "Kan") {
            type = raw.ankan ? FuroType.Ankan : FuroType.Minkan
        } else {
            type = raw.type
        }
        return new Furo(type, Tile.decode(raw.tile))
    }

    static parse(tiles: string | Tile[], ankan?: boolean): Furo | undefined {
        if (typeof tiles === 'string') {
            let ankan_ = ankan
            let tiles_: Tile[] | undefined

            if (tiles.length === 5 && tiles[0] == tiles[3] && tiles[0] == '0' && tiles[1] == tiles[2]) {
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
            if (tiles.length == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
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
            } else if (tiles.length == 4) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2] && tiles[2] == tiles[3]) {
                    return ankan ? new Furo(FuroType.Ankan, tiles[0]) : new Furo(FuroType.Minkan, tiles[0])
                }
            }

            return undefined
        }
    }

}
