import { buildHora } from './index'
import { Tile } from '../models'

test('hora', () => {
  const result = buildHora({
    tiles: Tile.parseTiles('11123456778899p')!,
    agari: Tile.byText('4p')!,
    tsumo: true,
    dora: 4,
    extraYaku: ['Richi']
  })
  expect(result).toMatchObject({
    han: 16,
    dora: 4,
    yaku: expect.arrayContaining(['Richi', 'Ittsu', 'Chinitsu', 'Ipe', 'Tsumo', 'Pinhu']),
    extraYaku: ['Richi'],
    hasYakuman: false,
    pattern: {
      type: 'RegularHoraHandPattern',
      hu: 20
    },
    parentPoint: { tsumo: 16000, ron: 0 },
    childPoint: { tsumoParent: 16000, tsumoChild: 8000, ron: 0 }
  })
  expect(result.yaku.length).toEqual(6)
})
