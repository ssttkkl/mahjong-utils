from mahjong_utils.models.tile import tile, yaochu


def test_std_syanten():
    from mahjong_utils.syanten import std_syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("34568m235p68s")
    syanten, advance = std_syanten(hand)

    assert syanten == 2
    assert advance == {*tiles("3678m12345p678s"), }


def test_std_syanten_with_got_tile():
    from mahjong_utils.syanten import std_syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("34568m235p368s")
    syanten, discard_advance_mapping = std_syanten_with_got_tile(hand)

    assert syanten == 2
    assert discard_advance_mapping == {
        tile("5p"): {*tiles("3678m1234p3678s"), },
        tile("3s"): {*tiles("3678m12345p678s"), },
        tile("2p"): {*tiles("3678m345p3678s"), },
        tile("8m"): {*tiles("36m1245p37s"), },
        tile("3m"): {*tiles("8m1245p37s"), },
        tile("6m"): {*tiles("8m1245p37s"), },
        tile("6s"): {*tiles("7m1245p38s"), },
        tile("8s"): {*tiles("7m1245p36s"), },
        tile("3p"): {*tiles("7m25p37s"), },
    }


def test_chitoi_syanten():
    from mahjong_utils.syanten import chitoi_syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("3344z6699p11345s")
    syanten, advance = chitoi_syanten(hand)

    assert syanten == 1
    assert advance == {*tiles("345s"), }


def test_chitoi_syanten_with_got_tile():
    from mahjong_utils.syanten import chitoi_syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("3344z6699p11345s8m")
    syanten, discard_advance_mapping = chitoi_syanten_with_got_tile(hand)

    assert syanten == 1
    assert discard_advance_mapping == {
        tile("8m"): {*tiles("345s"), },
        tile("3s"): {*tiles("8m45s"), },
        tile("4s"): {*tiles("8m35s"), },
        tile("5s"): {*tiles("8m34s"), },
    }


def test_kokushi_syanten_1():
    from mahjong_utils.syanten import kokushi_syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("119m19p19266s135z")
    syanten, advance = kokushi_syanten(hand)

    assert syanten == 3
    assert advance == {*tiles("2467z"), }


def test_kokushi_syanten_2():
    from mahjong_utils.syanten import kokushi_syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("19m19p19266s1235z")
    syanten, advance = kokushi_syanten(hand)

    assert syanten == 3
    assert advance == yaochu


def test_kokushi_syanten_3():
    from mahjong_utils.syanten import kokushi_syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("1119m19p19s12355z")
    syanten, advance = kokushi_syanten(hand)

    assert syanten == 2
    assert advance == {*tiles("467z"), }


def test_kokushi_syanten_with_got_tile_1():
    from mahjong_utils.syanten import kokushi_syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("119m19p19266s135z3s")
    syanten, discard_advance_mapping = kokushi_syanten_with_got_tile(hand)

    assert syanten == 3
    assert discard_advance_mapping == {
        tile("2s"): {*tiles("2467z"), },
        tile("6s"): {*tiles("2467z"), },
        tile("3s"): {*tiles("2467z"), }
    }


def test_kokushi_syanten_with_got_tile_2():
    from mahjong_utils.syanten import kokushi_syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("19m19p19266s1235z3s")
    syanten, discard_advance_mapping = kokushi_syanten_with_got_tile(hand)

    assert syanten == 3
    assert discard_advance_mapping == {
        tile("2s"): yaochu,
        tile("6s"): yaochu,
        tile("3s"): yaochu
    }


def test_kokushi_syanten_with_got_tile_3():
    from mahjong_utils.syanten import kokushi_syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("1119m19p19s12355z3s")
    syanten, discard_advance_mapping = kokushi_syanten_with_got_tile(hand)

    assert syanten == 2
    assert discard_advance_mapping == {
        tile("1m"): {*tiles("467z"), },
        tile("5z"): {*tiles("467z"), },
        tile("3s"): {*tiles("467z"), }
    }


def test_syanten():
    from mahjong_utils.syanten import syanten
    from mahjong_utils.models.tile import tiles

    hand = tiles("112233p44556s12z")
    syanten, advance = syanten(hand)

    assert syanten == 1
    assert advance == {*tiles("36s12z"), }


def test_syanten_with_got_tile():
    from mahjong_utils.syanten import syanten_with_got_tile
    from mahjong_utils.models.tile import tiles

    hand = tiles("112233p44556s127z")
    syanten, discard_advance_mapping = syanten_with_got_tile(hand)

    assert syanten == 1
    assert discard_advance_mapping == {
        tile("1z"): {*tiles("36s27z"), },
        tile("2z"): {*tiles("36s17z"), },
        tile("7z"): {*tiles("36s12z"), },
        tile("6s"): {*tiles("127z"), }
    }
