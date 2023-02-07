import {ChitoiHandPattern, Hand, KokushiHandPattern, RegularHandPattern, Tatsu, Tile} from "../models";

export interface ShantenWithoutGot {
    type: 'ShantenWithoutGot'
    shantenNum: number
    advance: Tile[]
    advanceNum: number
    goodShapeAdvance?: Tile[]
    goodShapeAdvanceNum?: number
}

export interface ShantenWithGot {
    type: 'ShantenWithGot'
    shantenNum: number
    discardToAdvance: Map<Tile, ShantenWithoutGot>
    ankanToAdvance: Map<Tile, ShantenWithoutGot>
}

export interface ShantenWithFuroChance {
    type: 'ShantenWithFuroChance'
    shantenNum: number
    pass?: ShantenWithoutGot
    // @ts-ignore
    chi: [Tatsu, ShantenWithGot][]
    pon?: ShantenWithGot
    minkan?: ShantenWithoutGot
}

export interface RegularShantenResult {
    type: 'Regular'
    hand: Hand<RegularHandPattern>
    shantenInfo: ShantenWithGot | ShantenWithoutGot
}

export interface ChitoiShantenResult {
    type: 'Chitoi'
    hand: Hand<ChitoiHandPattern>
    shantenInfo: ShantenWithGot | ShantenWithoutGot
}

export interface KokushiShantenResult {
    type: 'Kokushi'
    hand: Hand<KokushiHandPattern>
    shantenInfo: ShantenWithGot | ShantenWithoutGot
}

export interface UnionShantenResult {
    type: 'Union'
    hand: Hand
    shantenInfo: ShantenWithGot | ShantenWithoutGot
    regular: RegularShantenResult
    chitoi?: ChitoiShantenResult
    kokushi?: KokushiShantenResult
}

export interface FuroChanceShantenResult {
    type: 'FuroChance'
    hand: Hand
    shantenInfo: ShantenWithFuroChance
}
