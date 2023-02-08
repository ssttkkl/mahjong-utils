import {Decoder, Encoder} from "../models/types";
import {Tatsu, Tile} from "../models";
import {
    ChitoiShantenResult, FuroChanceShantenResult,
    KokushiShantenResult,
    RegularShantenResult, ShantenWithFuroChance,
    ShantenWithGot,
    ShantenWithoutGot, UnionShantenResult
} from "./models";
import {mapToObj, objToMap} from "../utils/obj-map";
import {decodeHand} from "../models/hand/coder";

interface RawShantenWithoutGot {
    shantenNum: number
    advance: string[]
    advanceNum: number
    goodShapeAdvance: string[] | null
    goodShapeAdvanceNum: number | null
}

export const decodeShantenWithoutGot: Decoder<ShantenWithoutGot, RawShantenWithoutGot> = (raw) => {
    return {
        type: 'ShantenWithoutGot',
        shantenNum: raw.shantenNum,
        advance: raw.advance.map(Tile.decode),
        advanceNum: raw.advanceNum ?? undefined,
        goodShapeAdvance: raw.goodShapeAdvance?.map(Tile.decode),
        goodShapeAdvanceNum: raw.goodShapeAdvanceNum ?? undefined
    }
}

export const encodeShantenWithoutGot: Encoder<ShantenWithoutGot, RawShantenWithoutGot> = (data) => {
    return {
        shantenNum: data.shantenNum,
        advance: data.advance.map(Tile.encode),
        advanceNum: data.advanceNum ?? null,
        goodShapeAdvance: data.goodShapeAdvance?.map(Tile.encode) ?? null,
        goodShapeAdvanceNum: data.goodShapeAdvanceNum ?? null
    }
}

interface RawShantenWithGot {
    shantenNum: number
    discardToAdvance: { [key: string]: RawShantenWithoutGot }
    ankanToAdvance: { [key: string]: RawShantenWithoutGot }
}

export const decodeShantenWithGot: Decoder<ShantenWithGot, RawShantenWithGot> = (raw) => {
    return {
        type: 'ShantenWithGot',
        shantenNum: raw.shantenNum,
        discardToAdvance: objToMap(raw.discardToAdvance, {
            keyMapper: Tile.decode,
            valueMapper: decodeShantenWithoutGot
        }),
        ankanToAdvance: objToMap(raw.ankanToAdvance, {
            keyMapper: Tile.decode,
            valueMapper: decodeShantenWithoutGot
        }),
    }
}

export const encodeShantenWithGot: Encoder<ShantenWithGot, RawShantenWithGot> = (data) => {
    return {
        shantenNum: data.shantenNum,
        discardToAdvance: mapToObj(data.discardToAdvance, {
            keyMapper: Tile.encode,
            valueMapper: encodeShantenWithoutGot
        }),
        ankanToAdvance: mapToObj(data.ankanToAdvance, {
            keyMapper: Tile.encode,
            valueMapper: encodeShantenWithoutGot
        })
    }
}

interface RawShantenWithFuroChance {
    shantenNum: number
    pass: RawShantenWithoutGot | null
    // @ts-ignore
    chi: { [key: string]: RawShantenWithGot }
    pon: RawShantenWithGot | null
    minkan: RawShantenWithoutGot | null
}

export const decodeShantenWithFuroChance: Decoder<ShantenWithFuroChance, RawShantenWithFuroChance> = (raw) => {
    return {
        type: 'ShantenWithFuroChance',
        shantenNum: raw.shantenNum,
        pass: raw.pass !== null ? decodeShantenWithoutGot(raw.pass) : undefined,
        chi: Object.entries(raw.chi).map(([key, value]) => [Tatsu.decode(key), decodeShantenWithGot(value)]),
        pon: raw.pon !== null ? decodeShantenWithGot(raw.pon) : undefined,
        minkan: raw.minkan !== null ? decodeShantenWithoutGot(raw.minkan) : undefined,
    }
}

type ShantenInfo = ShantenWithGot | ShantenWithoutGot | ShantenWithFuroChance
type RawShantenInfo =
    { type: 'ShantenWithGot' | 'ShantenWithoutGot' | 'ShantenWithFuroChance' }
    & (RawShantenWithGot | RawShantenWithoutGot | RawShantenWithFuroChance)

export const decodeShantenInfo: Decoder<ShantenInfo, RawShantenInfo> = (raw) => {
    switch (raw.type) {
        case 'ShantenWithGot':
            return decodeShantenWithGot(raw as RawShantenWithGot)
        case 'ShantenWithoutGot':
            return decodeShantenWithoutGot(raw as RawShantenWithoutGot)
        case 'ShantenWithFuroChance':
            return decodeShantenWithFuroChance(raw as RawShantenWithFuroChance)
    }
}

export const encodeShantenInfo: Encoder<ShantenInfo, RawShantenInfo> = (data) => {
    switch (data.type) {
        case 'ShantenWithGot':
            return {...encodeShantenWithGot(data), type: 'ShantenWithGot'}
        case 'ShantenWithoutGot':
            return {...encodeShantenWithoutGot(data), type: 'ShantenWithoutGot'}
        default:
            throw new Error("invalid type: " + data.type)
    }
}

type ShantenResult =
    RegularShantenResult
    | ChitoiShantenResult
    | KokushiShantenResult
    | UnionShantenResult
    | FuroChanceShantenResult

interface RawShantenResult {
    type: 'Union' | 'Regular' | 'Chitoi' | 'Kokushi' | 'FuroChance',
    hand: any,
    shantenInfo: RawShantenInfo,
    regular: RawShantenResult | null,
    chitoi: RawShantenResult | null,
    kokushi: RawShantenResult | null
}

export const decodeShantenResult: Decoder<ShantenResult, RawShantenResult> = (raw) => {
    return {
        type: raw.type,
        hand: decodeHand(raw.hand),
        shantenInfo: decodeShantenInfo(raw.shantenInfo),
        regular: raw.regular ? decodeShantenResult(raw.regular) : undefined,
        chitoi: raw.chitoi ? decodeShantenResult(raw.chitoi) : undefined,
        kokushi: raw.kokushi ? decodeShantenResult(raw.kokushi) : undefined
    } as ShantenResult
}
