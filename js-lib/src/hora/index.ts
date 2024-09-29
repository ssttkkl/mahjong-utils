import { ENTRY, handleResult } from "../entry";
import {
  FuroArg,
  TileArg,
  TilesArg,
  unifyFuroArg,
  unifyTileArg,
  unifyTilesArg,
  type Wind
} from "../models";
import { type Hora } from "./models";
import { decodeHora } from "../coder/hora";
import { encodeFuro } from "../coder/models/furo";
import { encodeTile } from "../coder/models/tile";

export * from "./models";

interface BasicBuildHoraParams {
  agari: TileArg;
  tsumo: boolean;
  dora?: number;
  selfWind?: Wind;
  roundWind?: Wind;
  extraYaku?: string[];
}

interface BuildHoraByTilesParams extends BasicBuildHoraParams {
  tiles: TilesArg;
  furo?: FuroArg[];
}

export function buildHora(opts: BuildHoraByTilesParams): Hora {
  const result = ENTRY.call("hora", {
    ...opts,
    tiles: unifyTilesArg(opts.tiles).map(encodeTile),
    furo: opts.furo?.map((x) => encodeFuro(unifyFuroArg(x))),
    // shantenResult: opts.shantenResult ? encodeCommonShantenResult(opts.shantenResult) : undefined,
    agari: encodeTile(unifyTileArg(opts.agari))
  });
  return decodeHora(handleResult(result));
}
