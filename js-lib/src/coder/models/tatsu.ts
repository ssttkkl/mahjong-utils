import { type Decoder, type Encoder } from '../../models/types.js'
import { Tatsu } from '../../models/index.js'

export const encodeTatsu: Encoder<Tatsu, string> = (data) => {
  return data.toString()
}

export const decodeTatsu: Decoder<Tatsu, string> = (raw) => {
  const t = Tatsu.parse(raw)
  if (t === undefined) {
    throw new Error('invalid value: ' + raw)
  }
  return t
}
