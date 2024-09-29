import { type ChitoiShantenResult, type FuroChanceShantenResult, type KokushiShantenResult, type RegularShantenResult, type UnionShantenResult } from 'shanten/index.js'
import { decodeHand } from '../models/hand/index.js'
import { decodeCommonShantenInfo, decodeShantenWithFuroChance } from './shanten-info.js'

export function decodeRegularShantenResult (raw: any): RegularShantenResult {
  return {
    hand: decodeHand(raw.hand, 'RegularHandPattern'),
    shantenInfo: decodeCommonShantenInfo(raw.shantenInfo)
  } as RegularShantenResult
}

export function decodeChitoiShantenResult (raw: any): ChitoiShantenResult {
  return {
    hand: decodeHand(raw.hand, 'ChitoiHandPattern'),
    shantenInfo: decodeCommonShantenInfo(raw.shantenInfo)
  } as ChitoiShantenResult
}

export function decodeKokushiShantenResult (raw: any): KokushiShantenResult {
  return {
    hand: decodeHand(raw.hand, 'KokushiHandPattern'),
    shantenInfo: decodeCommonShantenInfo(raw.shantenInfo)
  } as KokushiShantenResult
}

export function decodeUnionShantenResult (raw: any): UnionShantenResult {
  return {
    hand: decodeHand(raw.hand),
    shantenInfo: decodeCommonShantenInfo(raw.shantenInfo),
    regular: decodeRegularShantenResult(raw.regular),
    chitoi: raw.chitoi ? decodeChitoiShantenResult(raw.chitoi) : undefined,
    kokushi: raw.kokushi ? decodeKokushiShantenResult(raw.kokushi) : undefined
  } as UnionShantenResult
}

export function decodeFuroChanceShantenResult (raw: any): FuroChanceShantenResult {
  return {
    hand: decodeHand(raw.hand, 'RegularHandPattern'),
    shantenInfo: decodeShantenWithFuroChance(raw.shantenInfo)
  } as FuroChanceShantenResult
}
