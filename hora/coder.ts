import {Decoder} from "../models/types";
import {ChitoiHoraHandPattern, Hora, HoraHandPattern, KokushiHoraHandPattern, RegularHoraHandPattern} from "./models";
import {Furo, Mentsu, Tatsu, Tile, Wind} from "../models";

interface RawBaseHoraHandPattern {
    agari: string,
    tsumo: boolean,
    hu: number,
    selfWind?: Wind,
    roundWind?: Wind
}

interface RawRegularHoraHandPattern extends RawBaseHoraHandPattern {
    pattern: {
        k: number
        jyantou: string
        menzenMentsu: string[],
        furo: string[],
        tatsu: string[],
        remaining: string[]
    }
}

const decodeRegularHoraHandPattern: Decoder<RegularHoraHandPattern, RawRegularHoraHandPattern> = (raw) => {
    return {
        agari: Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: raw.hu,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        k: raw.pattern.k,
        jyantou: raw.pattern.jyantou ? Tile.decode(raw.pattern.jyantou) : null,
        menzenMentsu: raw.pattern.menzenMentsu.map(Mentsu.decode),
        furo: raw.pattern.furo.map(Furo.decode),
        tatsu: raw.pattern.tatsu.map(Tatsu.decode),
        remaining: raw.pattern.remaining.map(Tile.decode)
    }
}

interface RawChitoiHoraHandPattern extends RawBaseHoraHandPattern {
    pairs: string[]
}

const decodeChitoiHoraHandPattern: Decoder<ChitoiHoraHandPattern, RawChitoiHoraHandPattern> = (raw) => {
    return {
        agari: Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: raw.hu,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        pairs: raw.pairs.map(Tile.decode)
    }
}

interface RawKokushiHoraHandPattern extends RawBaseHoraHandPattern {
    repeated: string
}

const decodeKokushiHoraHandPattern: Decoder<KokushiHoraHandPattern, RawKokushiHoraHandPattern> = (raw) => {
    return {
        agari: Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: raw.hu,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        repeated: Tile.decode(raw.repeated)
    }
}

type RawHoraHandPattern = (RawRegularHoraHandPattern | RawChitoiHoraHandPattern | RawKokushiHoraHandPattern) & {
    type: 'RegularHoraHandPattern' | 'ChitoiHoraHandPattern' | 'KokushiHoraHandPattern'
}

export const decodeHoraHandPattern: Decoder<HoraHandPattern, RawHoraHandPattern> = (raw) => {
    switch (raw.type) {
        case 'RegularHoraHandPattern':
            return {
                type: 'RegularHoraHandPattern',
                ...decodeRegularHoraHandPattern(raw as RawRegularHoraHandPattern)
            }
        case 'ChitoiHoraHandPattern':
            return {
                type: 'RegularHoraHandPattern',
                ...decodeChitoiHoraHandPattern(raw as RawChitoiHoraHandPattern)
            }
        case 'KokushiHoraHandPattern':
            return {
                type: 'RegularHoraHandPattern',
                ...decodeKokushiHoraHandPattern(raw as RawKokushiHoraHandPattern)
            }
    }
}

export const decodeHora: Decoder<Hora> = (raw) => {
    return {
        ...raw,
        pattern: decodeHoraHandPattern(raw.pattern)
    }
}
