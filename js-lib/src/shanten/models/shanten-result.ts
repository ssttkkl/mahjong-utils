import { type ChitoiHandPattern, type Hand, type KokushiHandPattern, type RegularHandPattern, type HandPattern, type AbstractHandPattern } from '../../models'
import {
  type ShantenInfo,
  type ShantenWithFuroChance,
  type AbstractCommonShantenInfo
} from './shanten-info'

interface _ShantenResult<S extends ShantenInfo, P extends HandPattern> {
  hand: Hand<P>
  shantenInfo: S
}

interface _CommonShantenResult<P extends HandPattern> extends _ShantenResult<AbstractCommonShantenInfo, P> {

}

export type RegularShantenResult = _CommonShantenResult<RegularHandPattern>
export type ChitoiShantenResult = _CommonShantenResult<ChitoiHandPattern>
export type KokushiShantenResult = _CommonShantenResult<KokushiHandPattern>
export type UnionShantenResult = _CommonShantenResult<AbstractHandPattern> & {
  regular: RegularShantenResult
  chitoi?: ChitoiShantenResult
  kokushi?: KokushiShantenResult
}

export type CommonShantenResult = (RegularShantenResult | ChitoiShantenResult | KokushiShantenResult | UnionShantenResult)
export type AbstractCommonShantenResult = (
    RegularShantenResult & { type: 'RegularShantenResult' }
    | ChitoiShantenResult & { type: 'ChitoiShantenResult' }
    | KokushiShantenResult & { type: 'KokushiShantenResult' }
    | UnionShantenResult & { type: 'UnionShantenResult' }
)

export interface FuroChanceShantenResult {
  hand: Hand<RegularHandPattern>
  shantenInfo: ShantenWithFuroChance
}

export type ShantenResult = (CommonShantenResult | FuroChanceShantenResult)
export type AbstractShantenResult = AbstractCommonShantenResult
| FuroChanceShantenResult & { type: 'FuroChanceShantenResult' }
