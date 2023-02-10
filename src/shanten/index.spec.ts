import {
  chitoiShanten,
  furoChanceShanten,
  kokushiShanten,
  regularShanten,
  shanten,
  type ShantenWithGot,
  type ShantenWithoutGot
} from './index'
import { Tatsu, Tile } from '../models'

function checkShantenWithoutGot (match: ShantenWithoutGot, actual: ShantenWithoutGot): void {
  expect(actual).toMatchObject({
    ...match,
    advance: expect.arrayContaining(match.advance),
    goodShapeAdvance: (match.goodShapeAdvance != null) ? expect.arrayContaining(match.goodShapeAdvance) : undefined
  })

  expect(
    actual.advance.length
  ).toEqual(
    match.advance.length
  )

  expect(
    actual.goodShapeAdvance?.length
  ).toEqual(
    match.goodShapeAdvance?.length
  )
}

function checkShantenWithGot (match: ShantenWithGot, actual: ShantenWithGot): void {
  const match_: any = { ...match }
  delete match_.discardToAdvance
  delete match_.ankanToAdvance
  expect(actual).toMatchObject(match_)

  actual.discardToAdvance.forEach((value, key) => {
    const matchValue = match.discardToAdvance.get(key)
    expect(matchValue).not.toBeUndefined()
    checkShantenWithoutGot(matchValue!, value)
  })
  expect(actual.discardToAdvance.size).toEqual(match.discardToAdvance.size)

  actual.ankanToAdvance.forEach((value, key) => {
    const matchValue = match.ankanToAdvance.get(key)
    expect(matchValue).not.toBeUndefined()
    checkShantenWithoutGot(matchValue!, value)
  })
  expect(actual.ankanToAdvance.size).toEqual(match.ankanToAdvance.size)
}

test('regularShanten-withoutGot', () => {
  const result = regularShanten(
    Tile.parseTiles('34568m235p68s')!
  )

  checkShantenWithoutGot({
    shantenNum: 2,
    advance: Tile.parseTiles('3678m12345p678s')!,
    advanceNum: 40
  }, result.shantenInfo as ShantenWithoutGot)
})

test('chitoiShanten-withoutGot', () => {
  const result = chitoiShanten(
    Tile.parseTiles('112233p44556s12z')!
  )

  checkShantenWithoutGot({
    shantenNum: 1,
    advance: Tile.parseTiles('6s12z')!,
    advanceNum: 9,
    goodShapeAdvanceNum: 0,
    goodShapeAdvance: []
  }, result.shantenInfo as ShantenWithoutGot)
})

test('shanten-withoutGot', () => {
  const result = shanten(
    Tile.parseTiles('1112345678999p')!
  )

  checkShantenWithoutGot({
    shantenNum: 0,
    advance: Tile.parseTiles('123456789p')!,
    advanceNum: 23
  }, result.shantenInfo as ShantenWithoutGot)
})

test('regularShanten-withoutGot', () => {
  const result = regularShanten(
    Tile.parseTiles('34568m235p68s')!
  )

  checkShantenWithoutGot({
    shantenNum: 2,
    advance: Tile.parseTiles('3678m12345p678s')!,
    advanceNum: 40
  }, result.shantenInfo as ShantenWithoutGot)
})

test('regularShanten-withGot', () => {
  const result = regularShanten(
    Tile.parseTiles('34568m235p368s')!
  )
  checkShantenWithGot({
    shantenNum: 2,
    discardToAdvance: new Map([
      [Tile.byText('5p')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('3678m1234p3678s')!,
        advanceNum: 40
      }],
      [Tile.byText('3s')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('3678m12345p678s')!,
        advanceNum: 40
      }],
      [Tile.byText('2p')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('3678m345p3678s')!,
        advanceNum: 36
      }],
      [Tile.byText('8m')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('36m1245p37s')!,
        advanceNum: 27
      }],
      [Tile.byText('6s')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('7m1245p38s')!,
        advanceNum: 24
      }],
      [Tile.byText('8s')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('7m1245p36s')!,
        advanceNum: 24
      }],
      [Tile.byText('6m')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('8m1245p37s')!,
        advanceNum: 24
      }],
      [Tile.byText('3m')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('8m1245p37s')!,
        advanceNum: 24
      }],
      [Tile.byText('3p')!, {
        shantenNum: 2,
        advance: Tile.parseTiles('7m25p37s')!,
        advanceNum: 17
      }],
      [Tile.byText('5m')!, {
        shantenNum: 3,
        advance: Tile.parseTiles('2345678m12345p3678s')!,
        advanceNum: 53
      }],
      [Tile.byText('4m')!, {
        shantenNum: 3,
        advance: Tile.parseTiles('345678m12345p3678s')!,
        advanceNum: 49
      }]
    ]),
    ankanToAdvance: new Map()
  }, result.shantenInfo as ShantenWithGot)
})

test('chitoiShanten-withGot', () => {
  const result = chitoiShanten(
    Tile.parseTiles('3344z6699p11345s8m')!,
    {
      bestShantenOnly: true
    }
  )
  checkShantenWithGot({
    shantenNum: 1,
    discardToAdvance: new Map([
      [Tile.byText('8m')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('345s')!,
        advanceNum: 9,
        goodShapeAdvance: [],
        goodShapeAdvanceNum: 0
      }],
      [Tile.byText('3s')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('8m45s')!,
        advanceNum: 9,
        goodShapeAdvance: [],
        goodShapeAdvanceNum: 0
      }],
      [Tile.byText('4s')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('8m35s')!,
        advanceNum: 9,
        goodShapeAdvance: [],
        goodShapeAdvanceNum: 0
      }],
      [Tile.byText('5s')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('8m34s')!,
        advanceNum: 9,
        goodShapeAdvance: [],
        goodShapeAdvanceNum: 0
      }]
    ]),
    ankanToAdvance: new Map()
  }, result.shantenInfo as ShantenWithGot)
})

test('kokushiShanten-withGot', () => {
  const result = kokushiShanten(
    Tile.parseTiles('119m19p19266s135z3s')!,
    {
      bestShantenOnly: true
    }
  )
  checkShantenWithGot({
    shantenNum: 3,
    discardToAdvance: new Map([
      [Tile.byText('2s')!, {
        shantenNum: 3,
        advance: Tile.parseTiles('2467z')!,
        advanceNum: 16
      }],
      [Tile.byText('6s')!, {
        shantenNum: 3,
        advance: Tile.parseTiles('2467z')!,
        advanceNum: 16
      }],
      [Tile.byText('3s')!, {
        shantenNum: 3,
        advance: Tile.parseTiles('2467z')!,
        advanceNum: 16
      }]
    ]),
    ankanToAdvance: new Map()
  }, result.shantenInfo as ShantenWithGot)
})

test('shanten-withGot', () => {
  const result = shanten(
    Tile.parseTiles('112233p44556s127z')!,
    {
      bestShantenOnly: true
    }
  )
  checkShantenWithGot({
    shantenNum: 1,
    discardToAdvance: new Map([
      [Tile.byText('1z')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('36s27z')!,
        advanceNum: 13,
        goodShapeAdvance: Tile.parseTiles('27z')!,
        goodShapeAdvanceNum: 6
      }],
      [Tile.byText('2z')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('36s17z')!,
        advanceNum: 13,
        goodShapeAdvance: Tile.parseTiles('17z')!,
        goodShapeAdvanceNum: 6
      }],
      [Tile.byText('7z')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('36s12z')!,
        advanceNum: 13,
        goodShapeAdvance: Tile.parseTiles('12z')!,
        goodShapeAdvanceNum: 6
      }],
      [Tile.byText('6s')!, {
        shantenNum: 1,
        advance: Tile.parseTiles('127z')!,
        advanceNum: 9,
        goodShapeAdvance: [],
        goodShapeAdvanceNum: 0
      }]
    ]),
    ankanToAdvance: new Map()
  }, result.shantenInfo as ShantenWithGot)
})

test('furoChanceShanten', () => {
  const result = furoChanceShanten(
    Tile.parseTiles('3456778m123457p')!,
    Tile.byText('7m')!,
    {
      bestShantenOnly: true
    }
  )

  expect(result).toMatchObject({
    shantenInfo: {
      shantenNum: 0,
      pass: undefined,
      pon: undefined,
      minkan: undefined
    }
  })

  const expectedChi: Array<[Tatsu, ShantenWithGot]> = [
    [Tatsu.parse('68m')!, {
      shantenNum: 0,
      discardToAdvance: new Map([
        [Tile.byText('1p')!, {
          shantenNum: 0,
          advance: Tile.parseTiles('6p')!,
          advanceNum: 4
        }],
        [Tile.byText('4p')!, {
          shantenNum: 0,
          advance: Tile.parseTiles('6p')!,
          advanceNum: 4
        }],
        [Tile.byText('7p')!, {
          shantenNum: 0,
          advance: Tile.parseTiles('36p')!,
          advanceNum: 7
        }]
      ]),
      ankanToAdvance: new Map()
    }]
  ]

  result.shantenInfo.chi.forEach(([tatsu, shantenAfterChi]) => {
    const match = expectedChi.find(it => it[0].type === tatsu.type && it[0].first === tatsu.first)
    expect(match).not.toBeUndefined()
    checkShantenWithGot(match![1], shantenAfterChi)
  })
  expect(result.shantenInfo.chi.length).toEqual(expectedChi.length)
})
