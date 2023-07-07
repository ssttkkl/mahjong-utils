// @ts-expect-error 无法找到模块“mahjong-utils-entry”的声明文件 
import { mahjongutils } from 'mahjong-utils-entry'

export const { ENTRY } = mahjongutils

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
