import { type Decoder, type Encoder } from '../../models/types.js'
import { Mentsu } from '../../models/index.js'

export const encodeMentsu: Encoder<Mentsu, string> = (data) => {
  return data.toString()
}

export const decodeMentsu: Decoder<Mentsu, string> = (raw) => {
  const mentsu = Mentsu.parse(raw)
  if (mentsu === undefined) {
    throw new Error('invalid value: ' + raw)
  }
  return mentsu
}
