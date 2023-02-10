import { type Decoder, type Encoder } from '../../models/types'
import {
  type AbstractCommonShantenInfo,
  type AbstractShantenInfo,
  type ShantenWithFuroChance,
  type ShantenWithGot,
  type ShantenWithoutGot
} from '../../shanten'
import { mapToObj, objToMap } from '../utils/obj-map'
import { InvalidTypeException } from '../errors'
import { decodeTatsu, encodeTatsu } from '../models/tatsu'
import { decodeTile, encodeTile } from '../models/tile'

export const decodeShantenWithoutGot: Decoder<ShantenWithoutGot> = (raw) => {
  return {
    shantenNum: raw.shantenNum,
    advance: raw.advance.map(decodeTile),
    advanceNum: raw.advanceNum ?? undefined,
    goodShapeAdvance: raw.goodShapeAdvance?.map(decodeTile),
    goodShapeAdvanceNum: raw.goodShapeAdvanceNum ?? undefined
  }
}

export const encodeShantenWithoutGot: Encoder<ShantenWithoutGot> = (data) => {
  return {
    shantenNum: data.shantenNum,
    advance: data.advance.map(encodeTile),
    advanceNum: data.advanceNum ?? null,
    goodShapeAdvance: data.goodShapeAdvance?.map(encodeTile) ?? null,
    goodShapeAdvanceNum: data.goodShapeAdvanceNum ?? null
  }
}

export const decodeShantenWithGot: Decoder<ShantenWithGot> = (raw) => {
  return {
    shantenNum: raw.shantenNum,
    discardToAdvance: objToMap(raw.discardToAdvance, {
      keyMapper: decodeTile,
      valueMapper: decodeShantenWithoutGot
    }),
    ankanToAdvance: objToMap(raw.ankanToAdvance, {
      keyMapper: decodeTile,
      valueMapper: decodeShantenWithoutGot
    })
  }
}

export const encodeShantenWithGot: Encoder<ShantenWithGot> = (data) => {
  return {
    shantenNum: data.shantenNum,
    discardToAdvance: mapToObj(data.discardToAdvance, {
      keyMapper: encodeTile,
      valueMapper: encodeShantenWithoutGot
    }),
    ankanToAdvance: mapToObj(data.ankanToAdvance, {
      keyMapper: encodeTile,
      valueMapper: encodeShantenWithoutGot
    })
  }
}

export const decodeCommonShantenInfo: Decoder<AbstractCommonShantenInfo> = (raw) => {
  switch (raw.type) {
    case 'ShantenWithGot':
      return {
        type: 'ShantenWithGot',
        ...decodeShantenWithGot(raw)
      }
    case 'ShantenWithoutGot':
      return {
        type: 'ShantenWithoutGot',
        ...decodeShantenWithoutGot(raw)
      }
    default:
      throw new InvalidTypeException(raw.type)
  }
}

export const encodeCommonShantenInfo: Encoder<AbstractCommonShantenInfo> = (data) => {
  switch (data.type) {
    case 'ShantenWithGot':
      return {
        ...encodeShantenWithGot(data as ShantenWithGot),
        type: 'ShantenWithGot'
      }
    case 'ShantenWithoutGot':
      return {
        ...encodeShantenWithoutGot(data as ShantenWithoutGot),
        type: 'ShantenWithoutGot'
      }
  }
}

export const decodeShantenWithFuroChance: Decoder<ShantenWithFuroChance> = (raw) => {
  return {
    shantenNum: raw.shantenNum,
    pass: raw.pass !== null ? decodeShantenWithoutGot(raw.pass) : undefined,
    chi: Object.entries(raw.chi).map(([key, value]) => [decodeTatsu(key), decodeShantenWithGot(value)]),
    pon: raw.pon !== null ? decodeShantenWithGot(raw.pon) : undefined,
    minkan: raw.minkan !== null ? decodeShantenWithoutGot(raw.minkan) : undefined
  }
}

export const encodeShantenWithFuroChance: Encoder<ShantenWithFuroChance> = (data) => {
  return {
    shantenNum: data.shantenNum,
    pass: data.pass !== undefined ? encodeShantenWithoutGot(data.pass) : null,
    chi: Object.fromEntries(data.chi.map(([tatsu, shanten]) => [encodeTatsu(tatsu), encodeShantenWithGot(shanten)])),
    pon: data.pon !== undefined ? encodeShantenWithGot(data.pon) : null,
    minkan: data.minkan !== undefined ? encodeShantenWithoutGot(data.minkan) : null
  }
}

export const decodeShantenInfo: Decoder<AbstractShantenInfo> = (raw) => {
  switch (raw.type) {
    case 'ShantenWithFuroChance':
      return {
        type: 'ShantenWithFuroChance',
        ...decodeShantenWithFuroChance(raw)
      }
    default:
      return decodeCommonShantenInfo(raw)
  }
}

export const encodeShantenInfo: Encoder<AbstractShantenInfo> = (data) => {
  switch (data.type) {
    case 'ShantenWithFuroChance':
      return {
        ...encodeShantenWithFuroChance(data as ShantenWithFuroChance),
        type: 'ShantenWithFuroChance'
      }
    default:
      return encodeCommonShantenInfo(data)
  }
}
