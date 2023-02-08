import {ENTRY, handleResult} from "../entry";
import {ShantenWithGot} from "../shanten";
import {Furo, Tile, Wind} from "../models";
import {Hora} from "./models";
import {encodeShantenWithGot} from "../shanten/coder";
import {decodeHora} from "./coder";

export * from './models'

export function buildHora(
    opts: {
        tiles: Tile[],
        furo?: Furo[],
        agari: Tile
        tsumo: boolean
        dora?: number
        selfWind?: Wind
        roundWind?: Wind
        extraYaku?: string[]
    }
): Hora

export function buildHora(
    opts: {
        shantenResult: ShantenWithGot
        agari: Tile
        tsumo: boolean
        dora?: number
        selfWind?: Wind
        roundWind?: Wind
        extraYaku?: string[]
    }
): Hora

export function buildHora(
    opts: {
        tiles?: Tile[],
        furo?: Furo[],
        shantenResult?: ShantenWithGot
        agari: Tile
        tsumo: boolean
        dora?: number
        selfWind?: Wind
        roundWind?: Wind
        extraYaku?: string[]
    }
): Hora {
    const result = ENTRY.call("hora", {
        ...opts,
        tiles: opts.tiles?.map(Tile.encode),
        furo: opts.furo?.map(Furo.encode),
        shantenResult: opts.shantenResult ? encodeShantenWithGot(opts.shantenResult) : undefined,
        agari: Tile.encode(opts.agari)
    })
    return decodeHora(handleResult(result))
}
