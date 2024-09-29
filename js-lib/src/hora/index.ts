import { ENTRY, handleResult } from '../entry.js'
import {
  type FuroArg,
  type TileArg,
  type TilesArg,
  unifyFuroArg,
  unifyTileArg,
  unifyTilesArg,
  type Wind
} from '../models/index.js'
import { type Hora } from './models.js'
import { decodeHora } from '../coder/hora/index.js'
import { encodeFuro } from '../coder/models/furo.js'
import { encodeTile } from '../coder/models/tile.js'

export * from './models.js'

interface BasicBuildHoraParams {
  agari: TileArg
  tsumo: boolean
  dora?: number
  selfWind?: Wind
  roundWind?: Wind
  extraYaku?: string[]
}

interface BuildHoraByTilesParams extends BasicBuildHoraParams {
  tiles: TilesArg
  furo?: FuroArg[]
}

export function buildHora (opts: BuildHoraByTilesParams): Hora {
  const result = ENTRY.call('hora', {
    ...opts,
    tiles: unifyTilesArg(opts.tiles).map(encodeTile),
    furo: opts.furo?.map((x) => encodeFuro(unifyFuroArg(x))),
    // shantenResult: opts.shantenResult ? encodeCommonShantenResult(opts.shantenResult) : undefined,
    agari: encodeTile(unifyTileArg(opts.agari))
  })
  return decodeHora(handleResult(result))
}
