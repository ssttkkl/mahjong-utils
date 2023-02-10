import { Tile, TileType } from './Tile'

test('tile-byTypeAndNum', () => {
  for (let i = 0; i <= 3; i++) {
    for (let j = 1; j <= (i !== 3 ? 9 : 7); j++) {
      const a = Tile.byTypeAndNum(i, j)
      expect(a).toBeTruthy()
      expect(a?.num).toBe(j)
      expect(a?.type).toBe(i)
    }
  }
})

test('tile-byCode', () => {
  for (let i = 0; i <= 3; i++) {
    for (let j = 1; j <= (i !== 3 ? 9 : 7); j++) {
      const code = i * 10 + j
      const a = Tile.byCode(code)
      expect(a).toBeTruthy()
      expect(a?.num).toBe(j)
      expect(a?.type).toBe(i)
    }
  }
})

test('tile-byText', () => {
  for (let i = 0; i <= 3; i++) {
    for (let j = 1; j <= (i !== 3 ? 9 : 7); j++) {
      const text = `${j}${TileType[i]}`
      const a = Tile.byText(text)
      expect(a).toBeTruthy()
      expect(a?.num).toBe(j)
      expect(a?.type).toBe(i)
    }
  }
})

test('tile-parseTiles', () => {
  expect(
    Tile.parseTiles('3678m12345p678s')
  ).toStrictEqual(
    ['3m', '6m', '7m', '8m', '1p', '2p', '3p', '4p', '5p', '6s', '7s', '8s'].map(Tile.byText)
  )
})

test('tile-equality', () => {
  const a = Tile.byTypeAndNum(TileType.Z, 1)
  const b = Tile.byText('1z')
  const c = Tile.byCode(31)
  const d = Tile.byTypeAndNum(TileType.Z, 1)
  const e = Tile.parseTiles('1z')![0]

  expect(a).toBe(b)
  expect(b).toBe(c)
  expect(c).toBe(d)
  expect(d).toBe(e)
})
