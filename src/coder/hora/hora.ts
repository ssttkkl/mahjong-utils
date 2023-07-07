import { type AbstractHoraHandPattern, type Hora } from 'hora'
import { decodeHoraHandPattern } from './hora-hand-pattern'

export function decodeHora<T extends AbstractHoraHandPattern['type']> (
  raw: any,
  patternType: T
): Hora<AbstractHoraHandPattern & { type: T }>

export function decodeHora (raw: any): Hora

export function decodeHora (
  raw: any,
  patternType?: AbstractHoraHandPattern['type']
): Hora {
  return {
    pattern: decodeHoraHandPattern({ ...raw.pattern, type: raw.pattern.type ?? patternType }),
    han: raw.han,
    dora: raw.dora,
    yaku: raw.yaku,
    extraYaku: raw.extraYaku,
    hasYakuman: raw.hasYakuman,
    parentPoint: raw.parentPoint,
    childPoint: raw.childPoint
  }
}
