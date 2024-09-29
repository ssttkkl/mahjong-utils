import { type Decoder } from '../../models/types'
import {
  type AbstractHoraHandPattern,
  type ChitoiHoraHandPattern,
  type KokushiHoraHandPattern,
  type RegularHoraHandPattern
} from '../../hora'
import { InvalidTypeException } from '../errors'
import { decodeFuro } from '../models/furo'
import { decodeMentsu } from '../models/mentsu'
import { decodeTatsu } from '../models/tatsu'
import { decodeTile } from '../models/tile'

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
