export interface Encoder<T, RAW = any> {
    (data: T): RAW
}

export interface Decoder<T, RAW = any> {
    (raw: RAW): T
}
