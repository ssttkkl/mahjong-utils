import { type Decoder, type Encoder } from '../../models/types'
import { Tile, TileType } from '../../models'

export const encodeTile: Encoder<Tile, string> = (data) => {
  return `${data.num}${TileType[data.type]}`
}

export const decodeTile: Decoder<Tile, string> = (raw) => {
  const t = Tile.byText(raw)
  if (t === undefined) {
    throw new Error('invalid value: ' + raw)
  }
  return t
}
