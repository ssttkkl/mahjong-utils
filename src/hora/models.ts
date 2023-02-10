import { ChildPoint, ParentPoint } from 'point-by-han-hu/models'
import { type RegularHandPattern, type Tile, type Wind } from '../models'
import { type ExtraYaku, type Yaku } from './yaku'

interface _HoraHandPattern {
  agari: Tile
  tsumo: boolean
  hu: number
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
  dora: number
  yaku: Yaku[]
  extraYaku: ExtraYaku[]
  hasYakuman: boolean
  parentPoint: ParentPoint
  childPoint: ChildPoint
}
