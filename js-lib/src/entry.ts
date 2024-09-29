// @ts-ignore
import { mahjongutils } from 'mahjong-utils-entry'

export const ENTRY = { call: mahjongutils.entry.call }

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
