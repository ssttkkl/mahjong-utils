import { ENTRY, handleResult } from '../entry.js'
import { type ChildPoint, type ParentPoint } from './models.js'

export function getParentPointByHanHu (han: number, hu: number): ParentPoint {
  const result = ENTRY.call<ParentPoint>('getParentPointByHanHu', { han, hu })
  return handleResult(result)
}

export function getChildPointByHanHu (han: number, hu: number): ChildPoint {
  const result = ENTRY.call<ChildPoint>('getChildPointByHanHu', { han, hu })
  return handleResult(result)
}
