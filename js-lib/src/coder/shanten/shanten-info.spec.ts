import { decodeShantenWithoutGot, encodeShantenWithoutGot } from './shanten-info'

test('round-trips structural shanten separately from effective shanten', () => {
  const decoded = decodeShantenWithoutGot({
    shantenNum: 1,
    structuralShantenNum: 0,
    advance: [],
    advanceNum: 0,
    goodShapeAdvance: null,
    goodShapeAdvanceNum: null,
    improvement: null,
    improvementNum: null,
    goodShapeImprovement: null,
    goodShapeImprovementNum: null
  })

  expect(decoded.shantenNum).toBe(1)
  expect(decoded.structuralShantenNum).toBe(0)
  expect(encodeShantenWithoutGot(decoded)).toMatchObject({
    shantenNum: 1,
    structuralShantenNum: 0
  })
})

test('defaults structural shanten to legacy shanten when decoding older backend output', () => {
  const decoded = decodeShantenWithoutGot({
    shantenNum: 2,
    advance: [],
    advanceNum: 0,
    goodShapeAdvance: null,
    goodShapeAdvanceNum: null,
    improvement: null,
    improvementNum: null,
    goodShapeImprovement: null,
    goodShapeImprovementNum: null
  })

  expect(decoded.structuralShantenNum).toBe(2)
})
