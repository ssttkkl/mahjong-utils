import { type Tile, type Tatsu } from '../../models'

interface _ShantenInfo {
  /** Effective shanten, excluding waits that require a fifth copy in your hand or own melds. */
  shantenNum: number
  /** Shape-only shanten, ignoring whether a wait requires a fifth copy in your hand or own melds. */
  structuralShantenNum: number
}

export interface Improvement {
  discard: Tile
  advance: Tile[]
  advanceNum: number
}

export interface ShantenWithoutGot extends _ShantenInfo {
  advance: Tile[]
  advanceNum: number
  goodShapeAdvance: Tile[] | null
  goodShapeAdvanceNum: number | null
  improvement: Map<Tile, Improvement[]> | null
  improvementNum: number | null
  goodShapeImprovement: Map<Tile, Improvement[]> | null
  goodShapeImprovementNum: number | null
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
  pass: ShantenWithoutGot | null
  chi: Array<[Tatsu, ShantenWithGot]>
  pon: ShantenWithGot | null
  minkan: ShantenWithoutGot | null
}

export type ShantenInfo = (CommonShantenInfo | ShantenWithFuroChance)
export type AbstractShantenInfo = AbstractCommonShantenInfo
| ShantenWithFuroChance & { type: 'ShantenWithFuroChance' }
