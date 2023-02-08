import {Decoder} from "../types";
import {Tile} from "../Tile";
import {Furo} from "../Furo";
import {ChitoiHandPattern, HandPattern, KokushiHandPattern, RegularHandPattern} from "./HandPattern";
import {Hand} from "./Hand";
import {Mentsu} from "../Mentsu";
import {Tatsu} from "../Tatsu";


interface RawRegularHandPattern {
    k: number
    jyantou: string | null
    menzenMentsu: string[],
    furo: string[],
    tatsu: string[],
    remaining: string[]
}

const decodeRegularHandPattern: Decoder<RegularHandPattern, RawRegularHandPattern> = (raw) => {
    return {
        k: raw.k,
        jyantou: raw.jyantou !== null ? Tile.decode(raw.jyantou) : null,
        menzenMentsu: raw.menzenMentsu.map(Mentsu.decode),
        furo: raw.furo.map(Furo.decode),
        tatsu: raw.tatsu.map(Tatsu.decode)
    }
}

interface RawChitoiHandPattern {
    pairs: string[],
    remaining: string[]
}

const decodeChitoiHandPattern: Decoder<ChitoiHandPattern, RawChitoiHandPattern> = (raw) => {
    return {
        pairs: raw.pairs.map(Tile.decode),
        remaining: raw.remaining.map(Tile.decode)
    }
}

interface RawKokushiHandPattern {
    yaochu: string[]
    repeated: string | null
    remaining: string[]
}

const decodeKokushiHandPattern: Decoder<KokushiHandPattern, RawKokushiHandPattern> = (raw) => {
    return {
        yaochu: raw.yaochu.map(Tile.decode),
        repeated: raw.repeated !== null ? Tile.decode(raw.repeated) : null,
        remaining: raw.remaining.map(Tile.decode)
    }
}

type RawHandPattern = (RawRegularHandPattern | RawChitoiHandPattern | RawKokushiHandPattern) & {
    type: 'RegularHandPattern' | 'ChitoiHandPattern' | 'KokushiHandPattern'
}

export const decodeHandPattern: Decoder<HandPattern, RawHandPattern> = (raw) => {
    switch (raw.type) {
        case 'RegularHandPattern':
            return {
                type: 'RegularHandPattern',
                ...decodeRegularHandPattern(raw as RawRegularHandPattern)
            }
        case 'ChitoiHandPattern':
            return {
                type: 'ChitoiHandPattern',
                ...decodeChitoiHandPattern(raw as RawChitoiHandPattern)
            }
        case 'KokushiHandPattern':
            return {
                type: 'KokushiHandPattern',
                ...decodeKokushiHandPattern(raw as RawKokushiHandPattern)
            }
    }
}


export const decodeHand: Decoder<Hand> = (raw) => {
    return {
        tiles: raw.tiles.map(Tile.decode),
        furo: raw.furo.map(Furo.decode),
        patterns: raw.patterns.map(decodeHandPattern),
    }
}
