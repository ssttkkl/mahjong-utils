import { ENTRY, handleResult } from '../entry.js'
import {
  type ChitoiShantenResult,
  type FuroChanceShantenResult,
  type KokushiShantenResult,
  type RegularShantenResult,
  type UnionShantenResult
} from './models/index.js'
import {
  decodeChitoiShantenResult,
  decodeFuroChanceShantenResult,
  decodeKokushiShantenResult,
  decodeRegularShantenResult,
  decodeUnionShantenResult
} from '../coder/shanten/index.js'
import { encodeFuro } from '../coder/models/furo.js'
import { encodeTile } from '../coder/models/tile.js'
import {
  type FuroArg,
  type TileArg,
  type TilesArg,
  unifyFuroArg,
  unifyTileArg,
  unifyTilesArg
} from '../models/index.js'

export * from './models/index.js'

export function shanten (
  tiles: TilesArg,
  opts: {
    furo?: FuroArg[]
    bestShantenOnly?: boolean
  } = {}
): UnionShantenResult {
  const result = ENTRY.call('shanten', {
    ...opts,
    tiles: unifyTilesArg(tiles).map((it) => encodeTile(it)),
    furo: opts.furo?.map((it) => encodeFuro(unifyFuroArg(it)))
  })
  return decodeUnionShantenResult(handleResult(result))
}

export function regularShanten (
  tiles: TilesArg,
  opts: {
    furo?: FuroArg[]
    bestShantenOnly?: boolean
  } = {}
): RegularShantenResult {
  const result = ENTRY.call('regularShanten', {
    ...opts,
    tiles: unifyTilesArg(tiles).map((it) => encodeTile(it)),
    furo: opts.furo?.map((it) => encodeFuro(unifyFuroArg(it)))
  })
  return decodeRegularShantenResult(handleResult(result))
}

export function chitoiShanten (
  tiles: TilesArg,
  opts: {
    bestShantenOnly?: boolean
  } = {}
): ChitoiShantenResult {
  const result = ENTRY.call('chitoiShanten', {
    ...opts,
    tiles: unifyTilesArg(tiles).map((it) => encodeTile(it))
  })
  return decodeChitoiShantenResult(handleResult(result))
}

export function kokushiShanten (
  tiles: TilesArg,
  opts: {
    bestShantenOnly?: boolean
  } = {}
): KokushiShantenResult {
  const result = ENTRY.call('kokushiShanten', {
    ...opts,
    tiles: unifyTilesArg(tiles).map((it) => encodeTile(it))
  })
  return decodeKokushiShantenResult(handleResult(result))
}

export function furoChanceShanten (
  tiles: TilesArg,
  chanceTile: TileArg,
  opts: {
    allowChi?: boolean
    bestShantenOnly?: boolean
    allowKuikae?: boolean
  } = {}
): FuroChanceShantenResult {
  const result = ENTRY.call('furoChanceShanten', {
    ...opts,
    tiles: unifyTilesArg(tiles).map((it) => encodeTile(it)),
    chanceTile: encodeTile(unifyTileArg(chanceTile))
  })
  return decodeFuroChanceShantenResult(handleResult(result))
}
