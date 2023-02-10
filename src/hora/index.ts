import { ENTRY, handleResult } from '../entry'
import { type Furo, type Tile, type Wind } from '../models'
import { type Hora } from './models'
import { decodeHora } from '../coder/hora'
import { encodeFuro } from '../coder/models/furo'
import { encodeTile } from '../coder/models/tile'

export * from './models'

interface BasicBuildHoraParams {
  agari: Tile
  tsumo: boolean
  dora?: number
  selfWind?: Wind
  roundWind?: Wind
  extraYaku?: string[]
}

interface BuildHoraByTilesParams extends BasicBuildHoraParams {
  tiles: Tile[]
  furo?: Furo[]
}

export function buildHora (opts: BuildHoraByTilesParams): Hora {
  const result = ENTRY.call('hora', {
    ...opts,
    tiles: opts.tiles?.map(encodeTile),
    furo: opts.furo?.map(encodeFuro),
    // shantenResult: opts.shantenResult ? encodeCommonShantenResult(opts.shantenResult) : undefined,
    agari: encodeTile(opts.agari)
  })
  return decodeHora(handleResult(result))
}
