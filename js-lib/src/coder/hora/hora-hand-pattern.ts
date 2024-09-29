import { type Decoder } from '../../models/types.js'
import {
  type AbstractHoraHandPattern,
  type ChitoiHoraHandPattern,
  type KokushiHoraHandPattern,
  type RegularHoraHandPattern
} from '../../hora/index.js'
import { InvalidTypeException } from '../errors.js'
import { decodeFuro } from '../models/furo.js'
import { decodeMentsu } from '../models/mentsu.js'
import { decodeTatsu } from '../models/tatsu.js'
import { decodeTile } from '../models/tile.js'

export const decodeRegularHoraHandPattern: Decoder<RegularHoraHandPattern> = (raw) => {
  return {
    agari: decodeTile(raw.agari),
    tsumo: raw.tsumo,
    hu: raw.hu,
    selfWind: raw.selfWind,
    roundWind: raw.roundWind,
    k: raw.pattern.k,
    jyantou: decodeTile(raw.pattern.jyantou),
    menzenMentsu: raw.pattern.menzenMentsu.map(decodeMentsu),
    furo: raw.pattern.furo.map(decodeFuro),
    tatsu: raw.pattern.tatsu.map(decodeTatsu)
  }
}

export const decodeChitoiHoraHandPattern: Decoder<ChitoiHoraHandPattern> = (raw) => {
  return {
    agari: decodeTile(raw.agari),
    tsumo: raw.tsumo,
    hu: 25, // 符数不传过来
    selfWind: raw.selfWind,
    roundWind: raw.roundWind,
    pairs: raw.pairs.map(decodeTile)
  }
}

export const decodeKokushiHoraHandPattern: Decoder<KokushiHoraHandPattern> = (raw) => {
  return {
    agari: decodeTile(raw.agari),
    tsumo: raw.tsumo,
    hu: 20, // 符数不传过来
    selfWind: raw.selfWind,
    roundWind: raw.roundWind,
    repeated: decodeTile(raw.repeated)
  }
}

export const decodeHoraHandPattern: Decoder<AbstractHoraHandPattern> = (raw) => {
  switch (raw.type) {
    case 'RegularHoraHandPattern':
      return {
        type: 'RegularHoraHandPattern',
        ...decodeRegularHoraHandPattern(raw)
      }
    case 'ChitoiHoraHandPattern':
      return {
        type: 'ChitoiHoraHandPattern',
        ...decodeChitoiHoraHandPattern(raw)
      }
    case 'KokushiHoraHandPattern':
      return {
        type: 'KokushiHoraHandPattern',
        ...decodeKokushiHoraHandPattern(raw)
      }
    default:
      throw new InvalidTypeException(raw.type)
  }
}
