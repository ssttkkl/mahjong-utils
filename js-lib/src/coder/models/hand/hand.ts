import { type AbstractHandPattern, type Hand } from '../../../models/index.js'
import { decodeHandPattern } from './hand-pattern.js'
import { decodeFuro } from '../furo.js'
import { decodeTile } from '../tile.js'

export function decodeHand<T extends AbstractHandPattern['type']> (
  raw: any,
  patternType?: T
): Hand<AbstractHandPattern & { type: T }>

export function decodeHand (raw: any): Hand

export function decodeHand (
  raw: any,
  patternType?: AbstractHandPattern['type']
): Hand {
  return {
    tiles: raw.tiles.map(decodeTile),
    furo: raw.furo.map(decodeFuro),
    patterns: raw.patterns.map((x: any) => decodeHandPattern({ ...x, type: x.type ?? patternType }))
  }
}
