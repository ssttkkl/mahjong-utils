import { type Tile, type Tatsu } from '../../models'

interface _ShantenInfo {
  shantenNum: number
}

export interface ShantenWithoutGot extends _ShantenInfo {
  advance: Tile[]
  advanceNum: number
  goodShapeAdvance?: Tile[]
  goodShapeAdvanceNum?: number
}

export interface ShantenWithGot extends _ShantenInfo {
  discardToAdvance: Map<Tile, ShantenWithoutGot>
  ankanToAdvance: Map<Tile, ShantenWithoutGot>
}

export type CommonShantenInfo = (ShantenWithoutGot | ShantenWithGot)
export type AbstractCommonShantenInfo = (
    ShantenWithoutGot & { type: 'ShantenWithoutGot' }
    | ShantenWithGot & { type: 'ShantenWithGot' }
)

export interface ShantenWithFuroChance extends _ShantenInfo {
  pass?: ShantenWithoutGot
  chi: Array<[Tatsu, ShantenWithGot]>
  pon?: ShantenWithGot
  minkan?: ShantenWithoutGot
}

export type ShantenInfo = (CommonShantenInfo | ShantenWithFuroChance)
export type AbstractShantenInfo = AbstractCommonShantenInfo
| ShantenWithFuroChance & { type: 'ShantenWithFuroChance' }
