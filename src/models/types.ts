export type Encoder<T, RAW = any> = (data: T) => RAW

export type Decoder<T, RAW = any> = (raw: RAW) => T
