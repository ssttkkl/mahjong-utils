export type CommonYaku =
    'Tsumo'
    | 'Pinhu'
    | 'Tanyao'
    | 'Ipe'
    | 'SelfWind'
    | 'RoundWind'
    | 'Haku'
    | 'Hatsu'
    | 'Chun'
    | 'Sanshoku'
    | 'Ittsu'
    | 'Chanta'
    | 'Chitoi'
    | 'Toitoi'
    | 'Sananko'
    | 'Honroto'
    | 'Sandoko'
    | 'Sankantsu'
    | 'Shosangen'
    | 'Honitsu'
    | 'Junchan'
    | 'Ryanpe'
    | 'Chinitsu'

export function getAllCommonYaku (): CommonYaku[] {
  return ['Tsumo',
    'Pinhu',
    'Tanyao',
    'Ipe',
    'SelfWind',
    'RoundWind',
    'Haku',
    'Hatsu',
    'Chun',
    'Sanshoku',
    'Ittsu',
    'Chanta',
    'Chitoi',
    'Toitoi',
    'Sananko',
    'Honroto',
    'Sandoko',
    'Sankantsu',
    'Shosangen',
    'Honitsu',
    'Junchan',
    'Ryanpe',
    'Chinitsu']
}

export type Yakuman =
    'Tenhou'
    | 'Chihou'
    | 'Kokushi'
    | 'Suanko'
    | 'Daisangen'
    | 'Tsuiso'
    | 'Shousushi'
    | 'Lyuiso'
    | 'Chinroto'
    | 'Sukantsu'
    | 'Churen'
    | 'Daisushi'
    | 'SuankoTanki'
    | 'ChurenNineWaiting'
    | 'KokushiThirteenWaiting'

export function getAllYakuman (): Yakuman[] {
  return ['Tenhou',
    'Chihou',
    'Kokushi',
    'Suanko',
    'Daisangen',
    'Tsuiso',
    'Shousushi',
    'Lyuiso',
    'Chinroto',
    'Sukantsu',
    'Churen',
    'Daisushi',
    'SuankoTanki',
    'ChurenNineWaiting',
    'KokushiThirteenWaiting']
}

export type ExtraYaku =
    'Richi'
    | 'Ippatsu'
    | 'Rinshan'
    | 'Chankan'
    | 'Haitei'
    | 'Houtei'
    | 'WRichi'
    | 'Tenhou'
    | 'Chihou'

export function getAllExtraYaku (): ExtraYaku[] {
  return ['Richi',
    'Ippatsu',
    'Rinshan',
    'Chankan',
    'Haitei',
    'Houtei',
    'WRichi',
    'Tenhou',
    'Chihou']
}

export type ExtraYakuman =
    'Tenhou'
    | 'Chihou'

export function getAllExtraYakuman (): ExtraYaku[] {
  return ['Tenhou',
    'Chihou']
}

export type Yaku = CommonYaku | Yakuman | ExtraYaku | ExtraYakuman

export function getAllYaku (): Yaku[] {
  return [...getAllCommonYaku(), ...getAllYakuman(), ...getAllExtraYaku()]
}
