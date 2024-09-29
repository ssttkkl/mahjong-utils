import { type Decoder, type Encoder } from '../../models/types.js'
import { Furo, FuroType } from '../../models/index.js'
import { decodeTile, encodeTile } from './tile.js'

export const encodeFuro: Encoder<Furo> = (data) => {
  const raw: any = {
    type: data.type,
    tile: encodeTile(data.tile)
  }
  if (data.type === FuroType.Minkan) {
    raw.type = 'Kan'
    raw.ankan = false
  } else if (data.type === FuroType.Ankan) {
    raw.type = 'Kan'
    raw.ankan = true
  }
  return raw
}

export const decodeFuro: Decoder<Furo> = (raw) => {
  let type: FuroType
  if (raw.type === 'Kan') {
    type = raw.ankan ? FuroType.Ankan : FuroType.Minkan
  } else {
    type = raw.type
  }
  return new Furo(type, decodeTile(raw.tile))
}
