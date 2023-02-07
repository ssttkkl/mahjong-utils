import {Tile} from "./Tile";
import {Decoder, Encoder} from "./types";

export enum MentsuType {
    Shuntsu = "Shuntsu",
    Kotsu = "Kotsu"
}

export class Mentsu {
    constructor(readonly type: MentsuType, readonly tile: Tile) {
    }

    static encode: Encoder<Mentsu, string> = (data) => {
        return data.toString()
    }

    static decode: Decoder<Mentsu, string> = (raw) => {
        const mentsu = Mentsu.parse(raw)
        if (mentsu === undefined) {
            throw new Error("invalid value: " + raw)
        }
        return mentsu
    }

    toString(): string {
        switch (this.type) {
            case MentsuType.Kotsu:
                return `${this.tile.num}${this.tile.num}${this.tile.num}${this.tile.type}`
            case MentsuType.Shuntsu:
                return `${this.tile.num}${this.tile.num + 1}${this.tile.num + 2}${this.tile.type}`
        }
    }

    static parse(tiles: string | Tile[]): Mentsu | undefined {
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
