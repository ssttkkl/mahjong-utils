import { getChildPointByHanHu, getParentPointByHanHu } from './index.js'

test('test-parent-point-by-han-hu', () => {
  expect(
    getParentPointByHanHu(2, 30)
  ).toStrictEqual(
    { ron: 2900, tsumo: 1000 }
  )

  expect(
    getParentPointByHanHu(3, 40)
  ).toStrictEqual(
    { ron: 7700, tsumo: 2600 }
  )

  expect(
    getParentPointByHanHu(4, 40)
  ).toStrictEqual(
    { ron: 12000, tsumo: 4000 }
  )

  expect(
    getParentPointByHanHu(5, 40)
  ).toStrictEqual(
    { ron: 12000, tsumo: 4000 }
  )

  expect(
    getParentPointByHanHu(16, 40)
  ).toStrictEqual(
    { ron: 48000, tsumo: 16000 }
  )

  expect(() => {
    getParentPointByHanHu(3, 28)
  }).toThrow()

  expect(() => {
    getParentPointByHanHu(-1, 30)
  }).toThrow()

  expect(() => {
    getParentPointByHanHu(114, 514)
  }).toThrow()
})

test('test-child-point-by-han-hu', () => {
  expect(
    getChildPointByHanHu(3, 40)
  ).toStrictEqual(
    { ron: 5200, tsumoParent: 2600, tsumoChild: 1300 }
  )

  expect(
    getChildPointByHanHu(4, 40)
  ).toStrictEqual(
    { ron: 8000, tsumoParent: 4000, tsumoChild: 2000 }
  )

  expect(
    getChildPointByHanHu(5, 40)
  ).toStrictEqual(
    { ron: 8000, tsumoParent: 4000, tsumoChild: 2000 }
  )

  expect(
    getChildPointByHanHu(16, 40)
  ).toStrictEqual(
    { ron: 32000, tsumoParent: 16000, tsumoChild: 8000 }
  )

  expect(() => {
    getChildPointByHanHu(3, 28)
  }).toThrow()

  expect(() => {
    getChildPointByHanHu(-1, 30)
  }).toThrow()

  expect(() => {
    getChildPointByHanHu(114, 514)
  }).toThrow()
})
