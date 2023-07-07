import { ENTRY, handleResult } from '../entry'
import { type ChildPoint, type ParentPoint } from './models'

export function getParentPointByHanHu (han: number, hu: number): ParentPoint {
  const result = ENTRY.call('getParentPointByHanHu', { han, hu })
  return handleResult(result)
}

export function getChildPointByHanHu (han: number, hu: number): ChildPoint {
  const result = ENTRY.call('getChildPointByHanHu', { han, hu })
  return handleResult(result)
}
