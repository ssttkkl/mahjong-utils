import { buildHora } from './index'

test('hora', () => {
  const result = buildHora({
    tiles: '11456778899p',
    agari: '4p',
    furo: ['123p'],
    tsumo: true,
    dora: 4,
    extraYaku: ['Richi']
  })
  expect(result).toMatchObject({
    han: 10,
    hu: 30,
    dora: 4,
    yaku: expect.arrayContaining(['Ittsu', 'Chinitsu']),
    extraYaku: ['Richi'],
    hasYakuman: false,
    pattern: {
      type: 'RegularHoraHandPattern'
    },
    parentPoint: { tsumo: 8000, ron: 0 },
    childPoint: { tsumoParent: 8000, tsumoChild: 4000, ron: 0 }
  })
})
