import {RegularHandPattern, Tile, Wind} from "../models";
import {ExtraYaku, Yaku} from "./yaku";

interface BaseHoraHandPattern {
    agari: Tile,
    tsumo: boolean,
    hu: number,
    selfWind?: Wind,
    roundWind?: Wind
}

export type RegularHoraHandPattern = (BaseHoraHandPattern & RegularHandPattern)

export interface ChitoiHoraHandPattern extends BaseHoraHandPattern {
    pairs: Tile[]
}

export interface KokushiHoraHandPattern extends BaseHoraHandPattern {
    repeated: Tile
}


export type HoraHandPattern = RegularHoraHandPattern | ChitoiHoraHandPattern | KokushiHoraHandPattern & {
    type: 'RegularHoraHandPattern' | 'ChitoiHoraHandPattern' | 'KokushiHoraHandPattern'
}

export interface Hora {
    pattern: HoraHandPattern
    han: number
    dora: number
    yaku: Yaku[]
    extraYaku: ExtraYaku[]
    hasYakuman: boolean
}
