from mahjong_utils.models.tile import tile


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
