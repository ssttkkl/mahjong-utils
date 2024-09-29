// @ts-expect-error aaa
import { mahjongutils } from 'mahjong-utils-entry-wasm'

export const ENTRY = {
  call<T>(name: string, params: any): Result<T> {
    const res = mahjongutils.entry.call(name, JSON.stringify(params))
    return JSON.parse(res)
  }
}

export interface Result<T> {
  data?: T
  code: number
  msg: string
}

export function handleResult<T> (result: Result<T>): T {
  if (result.code !== 200) {
    throw new Error(result.msg)
  } else if (result.data === undefined) {
    throw new Error('No data')
  }
  return result.data
}
