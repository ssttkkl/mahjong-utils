export function objToMap<K = any, V = any>(
    obj: any,
    opts: {
        keyMapper: (key: string) => K,
        valueMapper: (value: any) => V
    }
): Map<K, V> {
    return new Map(Object.keys(obj).map((key: string) => {
        return [opts.keyMapper(key), opts.valueMapper(obj[key])]
    }))
}

export function mapToObj<K, V>(
    map: Map<K, V>,
    opts: {
        keyMapper: (key: K) => string,
        valueMapper: (value: V) => any
    }
): any {
    let obj: any = {}
    map.forEach((value, key) => {
        obj[opts.keyMapper(key)] = opts.valueMapper(value)
    })
    return obj
}
