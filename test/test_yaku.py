from mahjong_utils.models.furo import parse_furo
from mahjong_utils.models.hora_hand import RegularHoraHand, ChitoiHoraHand, KokushiHoraHand
from mahjong_utils.models.mentsu import parse_mentsu
from mahjong_utils.models.tatsu import parse_tatsu
from mahjong_utils.models.tile import tile, parse_tiles
from mahjong_utils.yaku.common import tsumo, chitoi
from mahjong_utils.yaku.yakuman import churen


def test_tsumo():
    hand_menzen = RegularHoraHand(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("333m"), parse_mentsu("789m"), parse_mentsu("666s"), parse_mentsu("234p")],
        agari_tatsu=parse_tatsu("23p"),
        agari=tile("4p"),
        tsumo=True
    )
    assert tsumo(hand_menzen)

    hand_menzen.tsumo = False
    assert not tsumo(hand_menzen)

    hand_furo = RegularHoraHand(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("789m"), parse_mentsu("666s"), parse_mentsu("234p")],
        furo=[parse_furo("333m")],
        agari_tatsu=parse_tatsu("23p"),
        agari=tile("4p"),
        tsumo=True
    )
    assert not tsumo(hand_furo)

    hand_furo.tsumo = False
    assert not tsumo(hand_furo)


def test_chitoi():
    chitoi_hand = ChitoiHoraHand(
        pairs=parse_tiles("1234567p"),
        agari=tile("7p"),
        tsumo=False
    )
    assert chitoi(chitoi_hand)

    regular_hand = RegularHoraHand(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("333m"), parse_mentsu("789m"), parse_mentsu("666s"), parse_mentsu("234p")],
        agari_tatsu=parse_tatsu("23p"),
        agari=tile("4p"),
        tsumo=True
    )
    assert not chitoi(regular_hand)

    kokushi_hand = KokushiHoraHand(
        repeated=tile("5z"),
        agari=tile("5z"),
        tsumo=True
    )
    assert not chitoi(kokushi_hand)


def test_churen():
    regular_hand = RegularHoraHand(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("999m")],
        agari_tatsu=parse_tatsu("23m"),
        agari=tile("1m"),
        tsumo=True
    )
    assert churen(regular_hand)

    regular_hand = RegularHoraHand(
        jyantou=tile("8m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("789m"), parse_mentsu("222m"), parse_mentsu("456m")],
        agari_tatsu=parse_tatsu("22m"),
        agari=tile("2m"),
        tsumo=True
    )
    assert not churen(regular_hand)

    regular_hand = RegularHoraHand(
        jyantou=tile("2m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("888m")],
        agari_tatsu=parse_tatsu("78m"),
        agari=tile("9m"),
        tsumo=True
    )
    assert not churen(regular_hand)
