import { type Furo } from '../Furo'
import { type AbstractHandPattern, type HandPattern } from './HandPattern'
import { type Tile } from '../Tile'

export interface Hand<P extends HandPattern = AbstractHandPattern> {
  tiles: Tile[]
  furo: Furo[]
  patterns: P[]
}
