import {Furo} from "../Furo";
import {ChitoiHandPattern, HandPattern, KokushiHandPattern, RegularHandPattern} from "./HandPattern";
import {Tile} from "../Tile";

export interface Hand<P extends (RegularHandPattern | ChitoiHandPattern | KokushiHandPattern) = HandPattern> {
    tiles: Tile[]
    furo: Furo[]
    patterns: P[]
}
