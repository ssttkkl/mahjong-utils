import { ENTRY, handleResult } from '../entry'
import { type Furo, type Tile } from '../models'
import {
  type ChitoiShantenResult,
  type FuroChanceShantenResult,
  type KokushiShantenResult,
  type RegularShantenResult,
  type UnionShantenResult
} from './models'
import {
  decodeChitoiShantenResult,
  decodeFuroChanceShantenResult,
  decodeKokushiShantenResult,
  decodeRegularShantenResult,
  decodeUnionShantenResult
} from '../coder/shanten'
import { encodeFuro } from '../coder/models/furo'
import { encodeTile } from '../coder/models/tile'

export * from './models'

export function shanten (
  tiles: Tile[],
  opts: {
    furo?: Furo[]
    bestShantenOnly?: boolean
  } = {}
): UnionShantenResult {
  const result = ENTRY.call('shanten', {
    ...opts,
    tiles: tiles.map(it => encodeTile(it)),
    furo: opts.furo?.map(it => encodeFuro(it))
  })
  return decodeUnionShantenResult(handleResult(result))
}

export function regularShanten (
  tiles: Tile[],
  opts: {
    furo?: Furo[]
    bestShantenOnly?: boolean
  } = {}
): RegularShantenResult {
  const result = ENTRY.call('regularShanten', {
    ...opts,
    tiles: tiles.map(it => encodeTile(it)),
    furo: opts.furo?.map(it => encodeFuro(it))
  })
  return decodeRegularShantenResult(handleResult(result))
}

export function chitoiShanten (
  tiles: Tile[],
  opts: {
    bestShantenOnly?: boolean
  } = {}
): ChitoiShantenResult {
  const result = ENTRY.call('chitoiShanten', {
    ...opts,
    tiles: tiles.map(it => encodeTile(it))
  })
  return decodeChitoiShantenResult(handleResult(result))
}

export function kokushiShanten (
  tiles: Tile[],
  opts: {
    bestShantenOnly?: boolean
  } = {}
): KokushiShantenResult {
  const result = ENTRY.call('kokushiShanten', {
    ...opts,
    tiles: tiles.map(it => encodeTile(it))
  })
  return decodeKokushiShantenResult(handleResult(result))
}

export function furoChanceShanten (
  tiles: Tile[], chanceTile: Tile,
  opts: {
    allowChi?: boolean
    bestShantenOnly?: boolean
    allowKuikae?: boolean
  } = {}
): FuroChanceShantenResult {
  const result = ENTRY.call('furoChanceShanten', {
    ...opts,
    tiles: tiles.map(it => encodeTile(it)),
    chanceTile: encodeTile(chanceTile)
  })
  return decodeFuroChanceShantenResult(handleResult(result))
}
