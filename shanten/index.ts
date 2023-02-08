import {ENTRY, handleResult} from "../entry";
import {Furo, Tile} from "../models";
import {
    ChitoiShantenResult,
    FuroChanceShantenResult,
    KokushiShantenResult,
    RegularShantenResult,
    UnionShantenResult
} from "./models";
import {decodeShantenResult} from "./coder";

export * from './models'

export function shanten(
    tiles: Tile[],
    opts: {
        furo?: Furo[],
        calcAdvanceNum?: boolean,
        bestShantenOnly?: boolean,
        allowAnkan?: boolean
    } = {}
): UnionShantenResult {
    const result = ENTRY.call("shanten", {
        ...opts,
        tiles: tiles.map(it => Tile.encode(it)),
        furo: opts.furo?.map(it => Furo.encode(it))
    })
    return decodeShantenResult(handleResult(result)) as UnionShantenResult
}

export function regularShanten(
    tiles: Tile[],
    opts: {
        furo?: Furo[],
        calcAdvanceNum?: boolean,
        bestShantenOnly?: boolean,
        allowAnkan?: boolean
    } = {}
): RegularShantenResult {
    const result = ENTRY.call("regularShanten", {
        ...opts,
        tiles: tiles.map(it => Tile.encode(it)),
        furo: opts.furo?.map(it => Furo.encode(it))
    })
    return decodeShantenResult(handleResult(result)) as RegularShantenResult
}

export function chitoiShanten(
    tiles: Tile[],
    opts: {
        furo?: Furo[],
        calcAdvanceNum?: boolean,
        bestShantenOnly?: boolean
    } = {}
): ChitoiShantenResult {
    const result = ENTRY.call("chitoiShanten", {
        ...opts,
        tiles: tiles.map(it => Tile.encode(it)),
        furo: opts.furo?.map(it => Furo.encode(it))
    })
    return decodeShantenResult(handleResult(result)) as ChitoiShantenResult
}

export function kokushiShanten(
    tiles: Tile[],
    opts: {
        furo?: Furo[],
        calcAdvanceNum?: boolean,
        bestShantenOnly?: boolean
    } = {}
): KokushiShantenResult {
    const result = ENTRY.call("kokushiShanten", {
        ...opts,
        tiles: tiles.map(it => Tile.encode(it)),
        furo: opts.furo?.map(it => Furo.encode(it))
    })
    return decodeShantenResult(handleResult(result)) as KokushiShantenResult
}

export function furoChanceShanten(
    tiles: Tile[], chanceTile: Tile,
    opts: {
        allowChi?: boolean,
        calcAdvanceNum?: boolean,
        bestShantenOnly?: boolean,
        allowKuikae?: boolean
    } = {}
): FuroChanceShantenResult {
    const result = ENTRY.call("furoChanceShanten", {
        ...opts,
        tiles: tiles.map(it => Tile.encode(it)),
        chanceTile: Tile.encode(chanceTile)
    })
    return decodeShantenResult(handleResult(result)) as FuroChanceShantenResult
}
