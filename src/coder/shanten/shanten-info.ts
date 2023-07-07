import { type Decoder, type Encoder } from '../../models/types'
import {
  type Improvement,
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

export const decodeImprovement: Decoder<Improvement> = (raw) => {
  return {
    discard: decodeTile(raw.discard),
    advance: raw.advance.map(decodeTile),
    advanceNum: raw.advanceNum
  }
}

export const encodeImprovement: Encoder<Improvement> = (data) => {
  return {
    discard: encodeTile(data.discard),
    advance: data.advance.map(encodeTile),
    advanceNum: data.advanceNum
  }
}

export const decodeShantenWithoutGot: Decoder<ShantenWithoutGot> = (raw) => {
  return {
    shantenNum: raw.shantenNum,
    advance: raw.advance.map(decodeTile),
    advanceNum: raw.advanceNum,
    goodShapeAdvance: raw.goodShapeAdvance?.map(decodeTile) ?? null,
    goodShapeAdvanceNum: raw.goodShapeAdvanceNum,
    improvement: raw.improvement !== null
      ? objToMap(raw.improvement, {
        keyMapper: decodeTile,
        valueMapper: (rawImprovementArray) => rawImprovementArray.map(decodeImprovement)
      })
      : null,
    improvementNum: raw.improvementNum,
    goodShapeImprovement: raw.goodShapeImprovement !== null
      ? objToMap(raw.goodShapeImprovement, {
        keyMapper: decodeTile,
        valueMapper: (rawImprovementArray) => rawImprovementArray.map(decodeImprovement)
      })
      : null,
    goodShapeImprovementNum: raw.goodShapeImprovementNum
  }
}

export const encodeShantenWithoutGot: Encoder<ShantenWithoutGot> = (data) => {
  return {
    shantenNum: data.shantenNum,
    advance: data.advance.map(encodeTile),
    advanceNum: data.advanceNum,
    goodShapeAdvance: data.goodShapeAdvance?.map(encodeTile),
    goodShapeAdvanceNum: data.goodShapeAdvanceNum,
    improvement: data.improvement !== null
      ? mapToObj(data.improvement, {
        keyMapper: encodeTile,
        valueMapper: (improvementArray) => improvementArray.map(encodeImprovement)
      })
      : null,
    improvementNum: data.improvementNum,
    goodShapeImprovement: data.goodShapeImprovement !== null
      ? mapToObj(data.goodShapeImprovement, {
        keyMapper: encodeTile,
        valueMapper: (improvementArray) => improvementArray.map(encodeImprovement)
      })
      : null,
    goodShapeImprovementNum: data.goodShapeImprovementNum
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
    pass: raw.pass !== null ? decodeShantenWithoutGot(raw.pass) : null,
    chi: Object.entries(raw.chi).map(([key, value]) => [decodeTatsu(key), decodeShantenWithGot(value)]),
    pon: raw.pon !== null ? decodeShantenWithGot(raw.pon) : null,
    minkan: raw.minkan !== null ? decodeShantenWithoutGot(raw.minkan) : null
  }
}

export const encodeShantenWithFuroChance: Encoder<ShantenWithFuroChance> = (data) => {
  return {
    shantenNum: data.shantenNum,
    pass: data.pass !== null ? encodeShantenWithoutGot(data.pass) : null,
    chi: Object.fromEntries(data.chi.map(([tatsu, shanten]) => [encodeTatsu(tatsu), encodeShantenWithGot(shanten)])),
    pon: data.pon !== null ? encodeShantenWithGot(data.pon) : null,
    minkan: data.minkan !== null ? encodeShantenWithoutGot(data.minkan) : null
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
