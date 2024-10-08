import { type Furo } from '../Furo'
import { type Tatsu } from '../Tatsu'
import { type Mentsu } from '../Mentsu'
import { type Tile } from '../Tile'

export interface RegularHandPattern {
  k: number
  jyantou: Tile | null
  menzenMentsu: Mentsu[]
  furo: Furo[]
  tatsu: Tatsu[]
}
export interface ChitoiHandPattern {
  pairs: Tile[]
  remaining: Tile[]
}

export interface KokushiHandPattern {
  yaochu: Tile[]
  repeated: Tile | null
  remaining: Tile[]
}

export type HandPattern = (RegularHandPattern | ChitoiHandPattern | KokushiHandPattern)
export type AbstractHandPattern = (
    RegularHandPattern & { type: 'RegularHandPattern' }
    | ChitoiHandPattern & { type: 'ChitoiHandPattern' }
    | KokushiHandPattern & { type: 'KokushiHandPattern' }
)
