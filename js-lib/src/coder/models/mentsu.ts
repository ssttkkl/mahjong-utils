import { type Decoder, type Encoder } from '../../models/types'
import { Mentsu } from '../../models'

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
