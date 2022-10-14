from mahjong_utils.models.tile import tile, all_yaochu


def test_std_shanten():
    from mahjong_utils.shanten import std_shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("34568m235p68s")
    result = std_shanten(tiles)

    assert result.shanten == 2
    assert result.advance == {*parse_tiles("3678m12345p678s"), }


def test_std_shanten_with_got_tile():
    from mahjong_utils.shanten import std_shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("34568m235p368s")
    result = std_shanten_with_got_tile(tiles)

    assert result.shanten == 2
    assert result.discard_to_advance == {
        tile("5p"): {*parse_tiles("3678m1234p3678s"), },
        tile("3s"): {*parse_tiles("3678m12345p678s"), },
        tile("2p"): {*parse_tiles("3678m345p3678s"), },
        tile("8m"): {*parse_tiles("36m1245p37s"), },
        tile("3m"): {*parse_tiles("8m1245p37s"), },
        tile("6m"): {*parse_tiles("8m1245p37s"), },
        tile("6s"): {*parse_tiles("7m1245p38s"), },
        tile("8s"): {*parse_tiles("7m1245p36s"), },
        tile("3p"): {*parse_tiles("7m25p37s"), },
    }


def test_chitoi_shanten():
    from mahjong_utils.shanten import chitoi_shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("3344z6699p11345s")
    result = chitoi_shanten(tiles)

    assert result.shanten == 1
    assert result.advance == {*parse_tiles("345s"), }


def test_chitoi_shanten_with_got_tile():
    from mahjong_utils.shanten import chitoi_shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("3344z6699p11345s8m")
    result = chitoi_shanten_with_got_tile(tiles)

    assert result.shanten == 1
    assert result.discard_to_advance == {
        tile("8m"): {*parse_tiles("345s"), },
        tile("3s"): {*parse_tiles("8m45s"), },
        tile("4s"): {*parse_tiles("8m35s"), },
        tile("5s"): {*parse_tiles("8m34s"), },
    }


def test_kokushi_shanten_1():
    from mahjong_utils.shanten import kokushi_shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("119m19p19266s135z")
    result = kokushi_shanten(tiles)

    assert result.shanten == 3
    assert result.advance == {*parse_tiles("2467z"), }


def test_kokushi_shanten_2():
    from mahjong_utils.shanten import kokushi_shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("19m19p19266s1235z")
    result = kokushi_shanten(tiles)

    assert result.shanten == 3
    assert result.advance == all_yaochu


def test_kokushi_shanten_3():
    from mahjong_utils.shanten import kokushi_shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("1119m19p19s12355z")
    result = kokushi_shanten(tiles)

    assert result.shanten == 2
    assert result.advance == {*parse_tiles("467z"), }


def test_kokushi_shanten_with_got_tile_1():
    from mahjong_utils.shanten import kokushi_shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("119m19p19266s135z3s")
    result = kokushi_shanten_with_got_tile(tiles)

    assert result.shanten == 3
    assert result.discard_to_advance == {
        tile("2s"): {*parse_tiles("2467z"), },
        tile("6s"): {*parse_tiles("2467z"), },
        tile("3s"): {*parse_tiles("2467z"), }
    }


def test_kokushi_shanten_with_got_tile_2():
    from mahjong_utils.shanten import kokushi_shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("19m19p19266s1235z3s")
    result = kokushi_shanten_with_got_tile(tiles)

    assert result.shanten == 3
    assert result.discard_to_advance == {
        tile("2s"): all_yaochu,
        tile("6s"): all_yaochu,
        tile("3s"): all_yaochu
    }


def test_kokushi_shanten_with_got_tile_3():
    from mahjong_utils.shanten import kokushi_shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("1119m19p19s12355z3s")
    result = kokushi_shanten_with_got_tile(tiles)

    assert result.shanten == 2
    assert result.discard_to_advance == {
        tile("1m"): {*parse_tiles("467z"), },
        tile("5z"): {*parse_tiles("467z"), },
        tile("3s"): {*parse_tiles("467z"), }
    }


def test_shanten():
    from mahjong_utils.shanten import shanten
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("112233p44556s12z")
    result = shanten(tiles)

    assert result.shanten == 1
    assert result.advance == {*parse_tiles("36s12z"), }


def test_shanten_with_got_tile():
    from mahjong_utils.shanten import shanten_with_got_tile
    from mahjong_utils.models.tile import parse_tiles

    tiles = parse_tiles("112233p44556s127z")
    result = shanten_with_got_tile(tiles)

    assert result.shanten == 1
    assert result.discard_to_advance == {
        tile("1z"): {*parse_tiles("36s27z"), },
        tile("2z"): {*parse_tiles("36s17z"), },
        tile("7z"): {*parse_tiles("36s12z"), },
        tile("6s"): {*parse_tiles("127z"), }
    }
