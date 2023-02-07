import {Furo} from "../Furo";
import {Tatsu} from "../Tatsu";
import {Mentsu} from "../Mentsu";
import {Tile} from "../Tile";

export interface RegularHandPattern {
    k: number
    jyantou: Tile | null
    menzenMentsu: Mentsu[],
    furo: Furo[],
    tatsu: Tatsu[]
}
export interface ChitoiHandPattern {
    pairs: Tile[]
    remaining: Tile[]
}

export interface KokushiHandPattern {
    yaochu: Tile[]
    repeated: Tile | null
    remaining: Tile[]
}


export type HandPattern = (RegularHandPattern | ChitoiHandPattern | KokushiHandPattern) & {
    type: 'RegularHandPattern' | 'ChitoiHandPattern' | 'KokushiHandPattern'
}
