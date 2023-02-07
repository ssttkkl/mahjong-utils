import {
    chitoiShanten,
    furoChanceShanten,
    kokushiShanten,
    regularShanten,
    shanten,
    ShantenWithGot,
    ShantenWithoutGot
} from "./index";
import {Tatsu, Tile} from "../models";

function checkShantenWithoutGot(match: ShantenWithoutGot, actual: ShantenWithoutGot) {
    expect(actual).toMatchObject({
        ...match,
        advance: expect.arrayContaining(match.advance),
        goodShapeAdvance: match.goodShapeAdvance ? expect.arrayContaining(match.goodShapeAdvance) : undefined
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

function checkShantenWithGot(match: ShantenWithGot, actual: ShantenWithGot) {
    let match_: any = {...match}
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
        Tile.parseTiles("34568m235p68s")!
    )
    expect(result.type).toBe('Regular')

    checkShantenWithoutGot({
        type: 'ShantenWithoutGot',
        shantenNum: 2,
        advance: Tile.parseTiles("3678m12345p678s")!,
        advanceNum: 40
    }, result.shantenInfo as ShantenWithoutGot)
})

test('chitoiShanten-withoutGot', () => {
    const result = chitoiShanten(
        Tile.parseTiles("112233p44556s12z")!
    )
    expect(result.type).toBe('Chitoi')

    checkShantenWithoutGot({
        type: 'ShantenWithoutGot',
        shantenNum: 1,
        advance: Tile.parseTiles("6s12z")!,
        advanceNum: 9,
        goodShapeAdvanceNum: 0,
        goodShapeAdvance: []
    }, result.shantenInfo as ShantenWithoutGot)
})

test('shanten-withoutGot', () => {
    const result = shanten(
        Tile.parseTiles("1112345678999p")!
    )
    expect(result.type).toBe('Union')

    checkShantenWithoutGot({
        type: 'ShantenWithoutGot',
        shantenNum: 0,
        advance: Tile.parseTiles("123456789p")!,
        advanceNum: 23
    }, result.shantenInfo as ShantenWithoutGot)
})

test('regularShanten-withoutGot', () => {
    const result = regularShanten(
        Tile.parseTiles("34568m235p68s")!
    )
    expect(result.type).toBe('Regular')

    checkShantenWithoutGot({
        type: 'ShantenWithoutGot',
        shantenNum: 2,
        advance: Tile.parseTiles("3678m12345p678s")!,
        advanceNum: 40
    }, result.shantenInfo as ShantenWithoutGot)
})

test('regularShanten-withGot', () => {
    const result = regularShanten(
        Tile.parseTiles("34568m235p368s")!
    )
    expect(result.type).toBe("Regular")
    checkShantenWithGot({
        type: 'ShantenWithGot',
        shantenNum: 2,
        discardToAdvance: new Map([
            [Tile.byText("5p")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("3678m1234p3678s")!,
                advanceNum: 40
            }],
            [Tile.byText("3s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("3678m12345p678s")!,
                advanceNum: 40
            }],
            [Tile.byText("2p")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("3678m345p3678s")!,
                advanceNum: 36
            }],
            [Tile.byText("8m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("36m1245p37s")!,
                advanceNum: 27
            }],
            [Tile.byText("6s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("7m1245p38s")!,
                advanceNum: 24
            }],
            [Tile.byText("8s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("7m1245p36s")!,
                advanceNum: 24
            }],
            [Tile.byText("6m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("8m1245p37s")!,
                advanceNum: 24
            }],
            [Tile.byText("3m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("8m1245p37s")!,
                advanceNum: 24
            }],
            [Tile.byText("3p")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 2,
                advance: Tile.parseTiles("7m25p37s")!,
                advanceNum: 17
            }],
            [Tile.byText("5m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 3,
                advance: Tile.parseTiles("2345678m12345p3678s")!,
                advanceNum: 53
            }],
            [Tile.byText("4m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 3,
                advance: Tile.parseTiles("345678m12345p3678s")!,
                advanceNum: 49
            }],
        ]),
        ankanToAdvance: new Map(),
    }, result.shantenInfo as ShantenWithGot)
})

test('chitoiShanten-withGot', () => {
    const result = chitoiShanten(
        Tile.parseTiles("3344z6699p11345s8m")!,
        {
            bestShantenOnly: true
        }
    )
    expect(result.type).toBe("Chitoi")
    checkShantenWithGot({
        type: 'ShantenWithGot',
        shantenNum: 1,
        discardToAdvance: new Map([
            [Tile.byText("8m")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("345s")!,
                advanceNum: 9,
                goodShapeAdvance: [],
                goodShapeAdvanceNum: 0
            }],
            [Tile.byText("3s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("8m45s")!,
                advanceNum: 9,
                goodShapeAdvance: [],
                goodShapeAdvanceNum: 0
            }],
            [Tile.byText("4s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("8m35s")!,
                advanceNum: 9,
                goodShapeAdvance: [],
                goodShapeAdvanceNum: 0
            }],
            [Tile.byText("5s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("8m34s")!,
                advanceNum: 9,
                goodShapeAdvance: [],
                goodShapeAdvanceNum: 0
            }],
        ]),
        ankanToAdvance: new Map(),
    }, result.shantenInfo as ShantenWithGot)
})


test('kokushiShanten-withGot', () => {
    const result = kokushiShanten(
        Tile.parseTiles("119m19p19266s135z3s")!,
        {
            bestShantenOnly: true
        }
    )
    expect(result.type).toBe("Kokushi")
    checkShantenWithGot({
        type: 'ShantenWithGot',
        shantenNum: 3,
        discardToAdvance: new Map([
            [Tile.byText("2s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 3,
                advance: Tile.parseTiles("2467z")!,
                advanceNum: 16
            }],
            [Tile.byText("6s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 3,
                advance: Tile.parseTiles("2467z")!,
                advanceNum: 16
            }],
            [Tile.byText("3s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 3,
                advance: Tile.parseTiles("2467z")!,
                advanceNum: 16
            }]
        ]),
        ankanToAdvance: new Map(),
    }, result.shantenInfo as ShantenWithGot)
})

test('shanten-withGot', () => {
    const result = shanten(
        Tile.parseTiles("112233p44556s127z")!,
        {
            bestShantenOnly: true
        }
    )
    expect(result.type).toBe("Union")
    checkShantenWithGot({
        type: 'ShantenWithGot',
        shantenNum: 1,
        discardToAdvance: new Map([
            [Tile.byText("1z")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("36s27z")!,
                advanceNum: 13,
                goodShapeAdvance: Tile.parseTiles("27z")!,
                goodShapeAdvanceNum: 6
            }],
            [Tile.byText("2z")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("36s17z")!,
                advanceNum: 13,
                goodShapeAdvance: Tile.parseTiles("17z")!,
                goodShapeAdvanceNum: 6
            }],
            [Tile.byText("7z")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("36s12z")!,
                advanceNum: 13,
                goodShapeAdvance: Tile.parseTiles("12z")!,
                goodShapeAdvanceNum: 6
            }],
            [Tile.byText("6s")!, {
                type: 'ShantenWithoutGot',
                shantenNum: 1,
                advance: Tile.parseTiles("127z")!,
                advanceNum: 9,
                goodShapeAdvance: [],
                goodShapeAdvanceNum: 0
            }],
        ]),
        ankanToAdvance: new Map(),
    }, result.shantenInfo as ShantenWithGot)
})

test('furoChanceShanten', () => {
    const result = furoChanceShanten(
        Tile.parseTiles("3456778m123457p")!,
        Tile.byText("7m")!,
        {
            bestShantenOnly: true
        }
    )

    expect(result).toMatchObject({
        type: 'FuroChance',
        shantenInfo: {
            type: 'ShantenWithFuroChance',
            shantenNum: 0,
            pass: undefined,
            pon: undefined,
            minkan: undefined
        }
    })

    const expectedChi: [Tatsu, ShantenWithGot][] = [
        [Tatsu.parse("68m")!, {
            type: 'ShantenWithGot',
            shantenNum: 0,
            discardToAdvance: new Map([
                [Tile.byText('1p')!, {
                    type: 'ShantenWithoutGot',
                    shantenNum: 0,
                    advance: Tile.parseTiles("6p")!,
                    advanceNum: 4
                }],
                [Tile.byText('4p')!, {
                    type: 'ShantenWithoutGot',
                    shantenNum: 0,
                    advance: Tile.parseTiles("6p")!,
                    advanceNum: 4
                }],
                [Tile.byText('7p')!, {
                    type: 'ShantenWithoutGot',
                    shantenNum: 0,
                    advance: Tile.parseTiles("36p")!,
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
