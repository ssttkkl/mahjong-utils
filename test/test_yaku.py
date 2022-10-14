from mahjong_utils.models.furo import parse_furo
from mahjong_utils.models.hand import RegularHand, ChitoiHand, KokushiHand
from mahjong_utils.models.hora_hand import build_hora_hand
from mahjong_utils.models.mentsu import parse_mentsu
from mahjong_utils.models.tatsu import parse_tatsu
from mahjong_utils.models.tile import tile, parse_tiles
from mahjong_utils.yaku.common import tsumo, chitoi


def test_tsumo():
    hand_menzen = build_hora_hand(
        hand=RegularHand(
            jyantou=tile("1m"),
            menzen_mentsu=[parse_mentsu("333m"), parse_mentsu("789m"), parse_mentsu("666s")],
            tatsu=[parse_tatsu("23p")]
        ),
        agari=tile("4p"),
        tsumo=True
    )
    assert tsumo(hand_menzen)

    hand_menzen.tsumo = False
    assert not tsumo(hand_menzen)

    hand_furo = build_hora_hand(
        hand=RegularHand(
            jyantou=tile("1m"),
            menzen_mentsu=[parse_mentsu("789m"), parse_mentsu("666s")],
            furo=[parse_furo("333m")],
            tatsu=[parse_tatsu("23p")]
        ),
        agari=tile("4p"),
        tsumo=True
    )
    assert not tsumo(hand_furo)

    hand_furo.tsumo = False
    assert not tsumo(hand_furo)


def test_chitoi():
    chitoi_hand = build_hora_hand(
        hand=ChitoiHand(
            pairs=parse_tiles("123456p"),
            remaining=[tile("7p")]
        ),
        agari=tile("7p"),
        tsumo=False
    )
    assert chitoi(chitoi_hand)

    regular_hand = build_hora_hand(
        hand=RegularHand(
            jyantou=tile("1m"),
            menzen_mentsu=[parse_mentsu("333m"), parse_mentsu("789m"), parse_mentsu("666s")],
            tatsu=[parse_tatsu("23p")]
        ),
        agari=tile("4p"),
        tsumo=True
    )
    assert not chitoi(regular_hand)

    kokushi_hand = build_hora_hand(
        hand=KokushiHand(
            tiles=parse_tiles("119m19p19s123467z")
        ),
        agari=tile("5z"),
        tsumo=True
    )
    assert not chitoi(kokushi_hand)
