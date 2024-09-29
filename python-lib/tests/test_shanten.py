from mahjong_utils.models.tile import parse_tiles, all_yaochu, Tile
from mahjong_utils.shanten import shanten, kokushi_shanten, regular_shanten, furo_chance_shanten


def shanten_tester(tiles, expected_shanten,
                   expected_advance,
                   expected_advance_num,
                   expected_good_shape_advance_num=None,
                   expected_improvement_num=None,
                   *,
                   test_func=shanten):
    tiles = parse_tiles(tiles)
    result = test_func(tiles)

    assert result.shanten == expected_shanten
    assert result.advance == expected_advance
    assert result.advance_num == expected_advance_num
    assert result.good_shape_advance_num == expected_good_shape_advance_num
    assert result.improvement_num == expected_improvement_num
    assert result.discard_to_advance is None


# noinspection PyTypeChecker
def test_regular_shanten():
    shanten_tester("34568m235p68s", 2, {*parse_tiles("3678m12345p678s"), }, 40, test_func=regular_shanten)
    shanten_tester("112233p44556s12z", 1, {*parse_tiles("36s12z"), }, 13, 6, test_func=regular_shanten)
    shanten_tester("1112345678999p", 0, {*parse_tiles("123456789p"), }, 23, None, 0, test_func=regular_shanten)
    shanten_tester("114514p1919810s", 2, {*parse_tiles("234567p3456789s"), }, 45, test_func=regular_shanten)


# noinspection PyTypeChecker
def test_shanten():
    shanten_tester("34568m235p68s", 2, {*parse_tiles("3678m12345p678s"), }, 40)
    shanten_tester("3344z6699p11345s", 1, {*parse_tiles("345s"), }, 9, 0)
    shanten_tester("112233p44556s12z", 1, {*parse_tiles("36s12z"), }, 13, 6)
    shanten_tester("1112345678999p", 0, {*parse_tiles("123456789p"), }, 23, None, 0)
    shanten_tester("114514p1919810s", 2, {*parse_tiles("234567p3456789s"), }, 45)
    shanten_tester("119m19p19266s135z", 3, {*parse_tiles("2467z"), }, 16)
    shanten_tester("19m19p19266s1235z", 3, all_yaochu, 42)
    shanten_tester("1119m19p19s12355z", 2, {*parse_tiles("467z"), }, 12)
    shanten_tester("119m19p19s123456z", 0, {*parse_tiles("7z"), }, 4, None, 0)


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
        Tile.by_text("5p"): {*parse_tiles("3678m1234p3678s"), },
        Tile.by_text("3s"): {*parse_tiles("3678m12345p678s"), },
        Tile.by_text("2p"): {*parse_tiles("3678m345p3678s"), },
        Tile.by_text("8m"): {*parse_tiles("36m1245p37s"), },
        Tile.by_text("3m"): {*parse_tiles("8m1245p37s"), },
        Tile.by_text("6m"): {*parse_tiles("8m1245p37s"), },
        Tile.by_text("6s"): {*parse_tiles("7m1245p38s"), },
        Tile.by_text("8s"): {*parse_tiles("7m1245p36s"), },
        Tile.by_text("3p"): {*parse_tiles("7m25p37s"), },
    }, regular_shanten)

    shanten_with_got_tester("112233p44556s127z", 1, {
        Tile.by_text("1z"): {*parse_tiles("36s27z"), },
        Tile.by_text("2z"): {*parse_tiles("36s17z"), },
        Tile.by_text("7z"): {*parse_tiles("36s12z"), },
    }, regular_shanten)

    shanten_with_got_tester("11123456789999p", -1, {}, regular_shanten)

    shanten_with_got_tester("1112345678999s1z", 0, {
        Tile.by_text("1z"): {*parse_tiles("123456789s")},
        Tile.by_text("2s"): {*parse_tiles("1z")},
        Tile.by_text("5s"): {*parse_tiles("1z")},
        Tile.by_text("8s"): {*parse_tiles("1z")}
    }, regular_shanten)

    shanten_with_got_tester("114514p1919810s8p", 2, {
        Tile.by_text("8s"): {*parse_tiles("2p3p4p5p6p7p8p9p3s4s5s6s7s9s")},
        Tile.by_text("8p"): {*parse_tiles("2p3p4p5p6p7p3s4s5s6s7s8s9s")},
        Tile.by_text("4p"): {*parse_tiles("3p6p7p8p9p3s4s5s6s7s8s9s")},
        Tile.by_text("9s"): {*parse_tiles("3p4p5p6p7p8p9p3s4s5s6s7s")},
        Tile.by_text("5p"): {*parse_tiles("4p6p7p8p9p3s4s5s6s7s8s9s")},
        Tile.by_text("5s"): {*parse_tiles("2p3p4p5p6p7p8p9p6s7s8s9s")},
    }, regular_shanten)

    shanten_with_got_tester("111p456p123s678p23s", 0, {
        Tile.by_text("1p"): {*parse_tiles("1s4s")},
        Tile.by_text("1s"): {*parse_tiles("23s")},
        Tile.by_text("2s"): {*parse_tiles("3s")},
        Tile.by_text("3s"): {*parse_tiles("2s")}
    }, regular_shanten)

    shanten_with_got_tester("4456m3334556p345s", 0, {
        Tile.by_text("4m"): {*parse_tiles("457p")},
        Tile.by_text("5p"): {*parse_tiles("47m")}
    }, regular_shanten)


# noinspection PyTypeChecker
def test_shanten_with_got_tile():
    shanten_with_got_tester("34568m235p368s", 2, {
        Tile.by_text("5p"): {*parse_tiles("3678m1234p3678s"), },
        Tile.by_text("3s"): {*parse_tiles("3678m12345p678s"), },
        Tile.by_text("2p"): {*parse_tiles("3678m345p3678s"), },
        Tile.by_text("8m"): {*parse_tiles("36m1245p37s"), },
        Tile.by_text("3m"): {*parse_tiles("8m1245p37s"), },
        Tile.by_text("6m"): {*parse_tiles("8m1245p37s"), },
        Tile.by_text("6s"): {*parse_tiles("7m1245p38s"), },
        Tile.by_text("8s"): {*parse_tiles("7m1245p36s"), },
        Tile.by_text("3p"): {*parse_tiles("7m25p37s"), },
    })

    shanten_with_got_tester("3344z6699p11345s8m", 1, {
        Tile.by_text("8m"): {*parse_tiles("345s"), },
        Tile.by_text("3s"): {*parse_tiles("8m45s"), },
        Tile.by_text("4s"): {*parse_tiles("8m35s"), },
        Tile.by_text("5s"): {*parse_tiles("8m34s"), },
    })

    shanten_with_got_tester("112233p44556s127z", 1, {
        Tile.by_text("1z"): {*parse_tiles("36s27z"), },
        Tile.by_text("2z"): {*parse_tiles("36s17z"), },
        Tile.by_text("7z"): {*parse_tiles("36s12z"), },
        Tile.by_text("6s"): {*parse_tiles("127z"), }
    })

    shanten_with_got_tester("11123456789999p", -1, {})

    shanten_with_got_tester("11223344556677z", -1, {})

    shanten_with_got_tester("1112345678999s1z", 0, {
        Tile.by_text("1z"): {*parse_tiles("123456789s")},
        Tile.by_text("2s"): {*parse_tiles("1z")},
        Tile.by_text("5s"): {*parse_tiles("1z")},
        Tile.by_text("8s"): {*parse_tiles("1z")}
    })

    shanten_with_got_tester("114514p1919810s8p", 2, {
        Tile.by_text("8s"): {*parse_tiles("2p3p4p5p6p7p8p9p3s4s5s6s7s9s")},
        Tile.by_text("8p"): {*parse_tiles("2p3p4p5p6p7p3s4s5s6s7s8s9s")},
        Tile.by_text("4p"): {*parse_tiles("3p6p7p8p9p3s4s5s6s7s8s9s")},
        Tile.by_text("9s"): {*parse_tiles("3p4p5p6p7p8p9p3s4s5s6s7s")},
        Tile.by_text("5p"): {*parse_tiles("4p6p7p8p9p3s4s5s6s7s8s9s")},
        Tile.by_text("5s"): {*parse_tiles("2p3p4p5p6p7p8p9p6s7s8s9s")},
        Tile.by_text("1p"): {*parse_tiles("5p8p5s8s")},
        Tile.by_text("1s"): {*parse_tiles("5p8p5s8s")}
    })

    shanten_with_got_tester("111p456p123s678p23s", 0, {
        Tile.by_text("1p"): {*parse_tiles("1s4s")},
        Tile.by_text("1s"): {*parse_tiles("23s")},
        Tile.by_text("2s"): {*parse_tiles("3s")},
        Tile.by_text("3s"): {*parse_tiles("2s")}
    })

    shanten_with_got_tester("4456m3334556p345s", 0, {
        Tile.by_text("4m"): {*parse_tiles("457p")},
        Tile.by_text("5p"): {*parse_tiles("47m")}
    })

    shanten_with_got_tester("119m19p19266s135z3s", 3, {
        Tile.by_text("2s"): {*parse_tiles("2467z"), },
        Tile.by_text("6s"): {*parse_tiles("2467z"), },
        Tile.by_text("3s"): {*parse_tiles("2467z"), }
    }, kokushi_shanten)

    shanten_with_got_tester("19m19p19266s1235z3s", 3, {
        Tile.by_text("2s"): all_yaochu,
        Tile.by_text("6s"): all_yaochu,
        Tile.by_text("3s"): all_yaochu
    }, kokushi_shanten)

    shanten_with_got_tester("1119m19p19s12355z3s", 2, {
        Tile.by_text("1m"): {*parse_tiles("467z"), },
        Tile.by_text("5z"): {*parse_tiles("467z"), },
        Tile.by_text("3s"): {*parse_tiles("467z"), }
    }, kokushi_shanten)

    shanten_with_got_tester("19m19p19s12345566z", 0, {
        Tile.by_text("5z"): {*parse_tiles("7z"), },
        Tile.by_text("6z"): {*parse_tiles("7z"), }
    })

    shanten_with_got_tester("19m19p19s12345667z", -1, {})

    shanten_with_got_tester("35m11223399p7799s", 0, {
        Tile.by_text("3m"): {*parse_tiles("5m"), },
        Tile.by_text("5m"): {*parse_tiles("3m"), }
    })


def test_furo_chance_shanten():
    result = furo_chance_shanten(parse_tiles("3456778m123457p"), Tile.by_text("7m"))
    print(result)
