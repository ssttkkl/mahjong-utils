import { type Decoder, type Encoder } from '../../models/types'
import { Furo } from '../../models'

export const encodeFuro: Encoder<Furo> = (data) => {
  return data.toString()
}

export const decodeFuro: Decoder<Furo> = (raw) => {
  const furo = Furo.parse(raw)
  if (furo === undefined) {
    throw new Error('invalid value: ' + raw)
  }
  return furo
}
