import { type Furo } from '../Furo.js'
import { type AbstractHandPattern, type HandPattern } from './HandPattern.js'
import { type Tile } from '../Tile.js'

export interface Hand<P extends HandPattern = AbstractHandPattern> {
  tiles: Tile[]
  furo: Furo[]
  patterns: P[]
}
