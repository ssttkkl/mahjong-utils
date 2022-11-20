from mahjong_utils.models.tile import parse_tiles, tile, all_yaochu
from mahjong_utils.shanten import shanten, kokushi_shanten, regular_shanten


def shanten_tester(tiles, expected_shanten, expected_advance, expected_advance_num, test_func=shanten):
    tiles = parse_tiles(tiles)
    result = test_func(tiles)

    assert result.shanten == expected_shanten
    assert result.advance == expected_advance
    assert result.advance_num == expected_advance_num
    assert result.discard_to_advance is None


# noinspection PyTypeChecker
def test_regular_shanten():
    shanten_tester("34568m235p68s", 2, {*parse_tiles("3678m12345p678s"), }, 40, regular_shanten)
    shanten_tester("112233p44556s12z", 1, {*parse_tiles("36s12z"), }, 13, regular_shanten)
    shanten_tester("1112345678999p", 0, {*parse_tiles("123456789p"), }, 23, regular_shanten)
    shanten_tester("114514p1919810s", 2, {*parse_tiles("234567p3456789s"), }, 45, regular_shanten)


# noinspection PyTypeChecker
def test_shanten():
    shanten_tester("34568m235p68s", 2, {*parse_tiles("3678m12345p678s"), }, 40)
    shanten_tester("3344z6699p11345s", 1, {*parse_tiles("345s"), }, 9)
    shanten_tester("112233p44556s12z", 1, {*parse_tiles("36s12z"), }, 13)
    shanten_tester("1112345678999p", 0, {*parse_tiles("123456789p"), }, 23)
    shanten_tester("114514p1919810s", 2, {*parse_tiles("234567p3456789s"), }, 45)
    shanten_tester("119m19p19266s135z", 3, {*parse_tiles("2467z"), }, 16)
    shanten_tester("19m19p19266s1235z", 3, all_yaochu, 42)
    shanten_tester("1119m19p19s12355z", 2, {*parse_tiles("467z"), }, 12)
    shanten_tester("119m19p19s123456z", 0, {*parse_tiles("7z"), }, 4)


def shanten_with_got_tester(tiles, expected_shanten, expected_discard_to_advance, test_func=shanten):
    tiles = parse_tiles(tiles)
    result = test_func(tiles)

    actual_discard_to_advance = dict()
    for k, v in result.discard_to_advance.items():
        if v.shanten == result.shanten:
            actual_discard_to_advance[k] = v.advance

    assert result.shanten == expected_shanten
    assert result.advance is None
    assert actual_discard_to_advance == expected_discard_to_advance


# noinspection PyTypeChecker
def test_regular_shanten_with_got_tile():
    shanten_with_got_tester("34568m235p368s", 2, {
        tile("5p"): {*parse_tiles("3678m1234p3678s"), },
        tile("3s"): {*parse_tiles("3678m12345p678s"), },
        tile("2p"): {*parse_tiles("3678m345p3678s"), },
        tile("8m"): {*parse_tiles("36m1245p37s"), },
        tile("3m"): {*parse_tiles("8m1245p37s"), },
        tile("6m"): {*parse_tiles("8m1245p37s"), },
        tile("6s"): {*parse_tiles("7m1245p38s"), },
        tile("8s"): {*parse_tiles("7m1245p36s"), },
        tile("3p"): {*parse_tiles("7m25p37s"), },
    }, regular_shanten)

    shanten_with_got_tester("112233p44556s127z", 1, {
        tile("1z"): {*parse_tiles("36s27z"), },
        tile("2z"): {*parse_tiles("36s17z"), },
        tile("7z"): {*parse_tiles("36s12z"), },
    }, regular_shanten)

    shanten_with_got_tester("11123456789999p", -1, {}, regular_shanten)

    shanten_with_got_tester("1112345678999s1z", 0, {
        tile("1z"): {*parse_tiles("123456789s")},
        tile("2s"): {*parse_tiles("1z")},
        tile("5s"): {*parse_tiles("1z")},
        tile("8s"): {*parse_tiles("1z")}
    }, regular_shanten)

    shanten_with_got_tester("114514p1919810s8p", 2, {
        tile("8s"): {*parse_tiles("2p3p4p5p6p7p8p9p3s4s5s6s7s9s")},
        tile("8p"): {*parse_tiles("2p3p4p5p6p7p3s4s5s6s7s8s9s")},
        tile("4p"): {*parse_tiles("3p6p7p8p9p3s4s5s6s7s8s9s")},
        tile("9s"): {*parse_tiles("3p4p5p6p7p8p9p3s4s5s6s7s")},
        tile("5p"): {*parse_tiles("4p6p7p8p9p3s4s5s6s7s8s9s")},
        tile("5s"): {*parse_tiles("2p3p4p5p6p7p8p9p6s7s8s9s")},
    }, regular_shanten)

    shanten_with_got_tester("111p456p123s678p23s", 0, {
        tile("1p"): {*parse_tiles("1s4s")},
        tile("1s"): {*parse_tiles("23s")},
        tile("2s"): {*parse_tiles("3s")},
        tile("3s"): {*parse_tiles("2s")}
    }, regular_shanten)

    shanten_with_got_tester("4456m3334556p345s", 0, {
        tile("4m"): {*parse_tiles("457p")},
        tile("5p"): {*parse_tiles("47m")}
    }, regular_shanten)


# noinspection PyTypeChecker
def test_shanten_with_got_tile():
    shanten_with_got_tester("34568m235p368s", 2, {
        tile("5p"): {*parse_tiles("3678m1234p3678s"), },
        tile("3s"): {*parse_tiles("3678m12345p678s"), },
        tile("2p"): {*parse_tiles("3678m345p3678s"), },
        tile("8m"): {*parse_tiles("36m1245p37s"), },
        tile("3m"): {*parse_tiles("8m1245p37s"), },
        tile("6m"): {*parse_tiles("8m1245p37s"), },
        tile("6s"): {*parse_tiles("7m1245p38s"), },
        tile("8s"): {*parse_tiles("7m1245p36s"), },
        tile("3p"): {*parse_tiles("7m25p37s"), },
    })

    shanten_with_got_tester("3344z6699p11345s8m", 1, {
        tile("8m"): {*parse_tiles("345s"), },
        tile("3s"): {*parse_tiles("8m45s"), },
        tile("4s"): {*parse_tiles("8m35s"), },
        tile("5s"): {*parse_tiles("8m34s"), },
    })

    shanten_with_got_tester("112233p44556s127z", 1, {
        tile("1z"): {*parse_tiles("36s27z"), },
        tile("2z"): {*parse_tiles("36s17z"), },
        tile("7z"): {*parse_tiles("36s12z"), },
        tile("6s"): {*parse_tiles("127z"), }
    })

    shanten_with_got_tester("11123456789999p", -1, {})

    shanten_with_got_tester("1112345678999s1z", 0, {
        tile("1z"): {*parse_tiles("123456789s")},
        tile("2s"): {*parse_tiles("1z")},
        tile("5s"): {*parse_tiles("1z")},
        tile("8s"): {*parse_tiles("1z")}
    })

    shanten_with_got_tester("114514p1919810s8p", 2, {
        tile("8s"): {*parse_tiles("2p3p4p5p6p7p8p9p3s4s5s6s7s9s")},
        tile("8p"): {*parse_tiles("2p3p4p5p6p7p3s4s5s6s7s8s9s")},
        tile("4p"): {*parse_tiles("3p6p7p8p9p3s4s5s6s7s8s9s")},
        tile("9s"): {*parse_tiles("3p4p5p6p7p8p9p3s4s5s6s7s")},
        tile("5p"): {*parse_tiles("4p6p7p8p9p3s4s5s6s7s8s9s")},
        tile("5s"): {*parse_tiles("2p3p4p5p6p7p8p9p6s7s8s9s")},
        tile("1p"): {*parse_tiles("5p8p5s8s")},
        tile("1s"): {*parse_tiles("5p8p5s8s")}
    })

    shanten_with_got_tester("111p456p123s678p23s", 0, {
        tile("1p"): {*parse_tiles("1s4s")},
        tile("1s"): {*parse_tiles("23s")},
        tile("2s"): {*parse_tiles("3s")},
        tile("3s"): {*parse_tiles("2s")}
    })

    shanten_with_got_tester("4456m3334556p345s", 0, {
        tile("4m"): {*parse_tiles("457p")},
        tile("5p"): {*parse_tiles("47m")}
    })

    shanten_with_got_tester("119m19p19266s135z3s", 3, {
        tile("2s"): {*parse_tiles("2467z"), },
        tile("6s"): {*parse_tiles("2467z"), },
        tile("3s"): {*parse_tiles("2467z"), }
    }, kokushi_shanten)

    shanten_with_got_tester("19m19p19266s1235z3s", 3, {
        tile("2s"): all_yaochu,
        tile("6s"): all_yaochu,
        tile("3s"): all_yaochu
    }, kokushi_shanten)

    shanten_with_got_tester("1119m19p19s12355z3s", 2, {
        tile("1m"): {*parse_tiles("467z"), },
        tile("5z"): {*parse_tiles("467z"), },
        tile("3s"): {*parse_tiles("467z"), }
    }, kokushi_shanten)

    shanten_with_got_tester("19m19p19s12345566z", 0, {
        tile("5z"): {*parse_tiles("7z"), },
        tile("6z"): {*parse_tiles("7z"), }
    })

    shanten_with_got_tester("19m19p19s12345667z", -1, {})

    shanten_with_got_tester("35m11223399p7799s", 0, {
        tile("3m"): {*parse_tiles("5m"), },
        tile("5m"): {*parse_tiles("3m"), }
    })
