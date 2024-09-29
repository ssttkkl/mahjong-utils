import { type Decoder } from '../../../models/types.js'
import {
  type AbstractHandPattern,
  type ChitoiHandPattern,
  type KokushiHandPattern,
  type RegularHandPattern
} from '../../../models/index.js'
import { InvalidTypeException } from '../../errors.js'
import { decodeFuro } from '../furo.js'
import { decodeMentsu } from '../mentsu.js'
import { decodeTatsu } from '../tatsu.js'
import { decodeTile } from '../tile.js'

export const decodeRegularHandPattern: Decoder<RegularHandPattern> = (raw) => {
  return {
    k: raw.k,
    jyantou: raw.jyantou !== null ? decodeTile(raw.jyantou) : null,
    menzenMentsu: raw.menzenMentsu.map(decodeMentsu),
    furo: raw.furo.map(decodeFuro),
    tatsu: raw.tatsu.map(decodeTatsu)
  }
}

export const decodeChitoiHandPattern: Decoder<ChitoiHandPattern> = (raw) => {
  return {
    pairs: raw.pairs.map(decodeTile),
    remaining: raw.remaining.map(decodeTile)
  }
}

export const decodeKokushiHandPattern: Decoder<KokushiHandPattern> = (raw) => {
  return {
    yaochu: raw.yaochu.map(decodeTile),
    repeated: raw.repeated !== null ? decodeTile(raw.repeated) : null,
    remaining: raw.remaining.map(decodeTile)
  }
}

export const decodeHandPattern: Decoder<AbstractHandPattern> = (raw) => {
  switch (raw.type) {
    case 'RegularHandPattern':
      return {
        type: 'RegularHandPattern',
        ...decodeRegularHandPattern(raw)
      }
    case 'ChitoiHandPattern':
      return {
        type: 'ChitoiHandPattern',
        ...decodeChitoiHandPattern(raw)
      }
    case 'KokushiHandPattern':
      return {
        type: 'KokushiHandPattern',
        ...decodeKokushiHandPattern(raw)
      }
    default:
      throw new InvalidTypeException(raw.type)
  }
}
