import { type ChildPoint, type ParentPoint } from 'point-by-han-hu/models.js'
import { type RegularHandPattern, type Tile, type Wind } from '../models/index.js'
import { type ExtraYaku, type Yaku } from './yaku.js'

interface _HoraHandPattern {
  agari: Tile
  tsumo: boolean
  selfWind?: Wind
  roundWind?: Wind
}

export type RegularHoraHandPattern = (_HoraHandPattern & RegularHandPattern & {
  jyantou: Tile
})

export interface ChitoiHoraHandPattern extends _HoraHandPattern {
  pairs: Tile[]
}

export interface KokushiHoraHandPattern extends _HoraHandPattern {
  repeated: Tile
}

export type HoraHandPattern = (RegularHoraHandPattern | ChitoiHoraHandPattern | KokushiHoraHandPattern)
export type AbstractHoraHandPattern = (
  RegularHoraHandPattern & { type: 'RegularHoraHandPattern' }
  | ChitoiHoraHandPattern & { type: 'ChitoiHoraHandPattern' }
  | KokushiHoraHandPattern & { type: 'KokushiHoraHandPattern' })

export interface Hora<P extends HoraHandPattern = AbstractHoraHandPattern> {
  pattern: P
  han: number
  hu: number
  dora: number
  yaku: Yaku[]
  extraYaku: ExtraYaku[]
  hasYakuman: boolean
  parentPoint: ParentPoint
  childPoint: ChildPoint
}
