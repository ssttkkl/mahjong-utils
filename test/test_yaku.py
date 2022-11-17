import pytest

from mahjong_utils.models.furo import parse_furo
from mahjong_utils.models.hora_hand_pattern import RegularHoraHandPattern, ChitoiHoraHandPattern, KokushiHoraHandPattern
from mahjong_utils.models.mentsu import parse_mentsu
from mahjong_utils.models.tatsu import parse_tatsu
from mahjong_utils.models.tile import tile, parse_tiles
from mahjong_utils.yaku.common import tsumo, chitoi, tanyao
from mahjong_utils.yaku.yakuman import churen, daisangen, daisushi, kokushi, kokushi13, tsuiso, suanko, shousushi, \
    lyuiso, chinroto, sukantsu, churen9, suanko_tanki


@pytest.fixture
def chitoi_hora_hand():
    chitoi_hand = ChitoiHoraHandPattern(
        pairs=parse_tiles("1234567p"),
        agari=tile("7p"),
        tsumo=False
    )
    return chitoi_hand


@pytest.fixture
def kokushi_hora_hand():
    kokushi_hand = KokushiHoraHandPattern(
        repeated=tile("5z"),
        agari=tile("1z"),
        tsumo=True
    )
    return kokushi_hand


@pytest.fixture
def kokushi_hora_hand_thirteen_waiting():
    kokushi_hand = KokushiHoraHandPattern(
        repeated=tile("5z"),
        agari=tile("5z"),
        tsumo=True
    )
    return kokushi_hand


@pytest.fixture
def regular_hora_hand_menzen():
    hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("333m"), parse_mentsu("789m"), parse_mentsu("666s"), parse_mentsu("234p")],
        agari_tatsu=parse_tatsu("23p"),
        agari=tile("4p"),
        tsumo=True
    )
    return hand


@pytest.fixture
def regular_hora_hand_furo():
    hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("789m"), parse_mentsu("666s"), parse_mentsu("234p")],
        furo=[parse_furo("333m")],
        agari_tatsu=parse_tatsu("23p"),
        agari=tile("4p"),
        tsumo=True
    )
    return hand


def test_tsumo(regular_hora_hand_menzen, regular_hora_hand_furo):
    assert tsumo(regular_hora_hand_menzen)
    regular_hora_hand_menzen.tsumo = False
    assert not tsumo(regular_hora_hand_menzen)

    assert not tsumo(regular_hora_hand_furo)
    regular_hora_hand_furo.tsumo = False
    assert not tsumo(regular_hora_hand_furo)


def test_tanyao():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("3p"),
        menzen_mentsu=[parse_mentsu("234p"), parse_mentsu("456p")],
        furo=[parse_furo("777s"), parse_furo("0880m")],
        agari_tatsu=None,
        agari=tile("3p"),
        tsumo=True
    )
    assert tanyao(regular_hand)

    regular_hand.jyantou = tile("1m")
    regular_hand.agari = tile("1m")

    assert not tanyao(regular_hand)


def test_chitoi(regular_hora_hand_menzen, kokushi_hora_hand, chitoi_hora_hand):
    assert chitoi(chitoi_hora_hand)
    assert not chitoi(regular_hora_hand_menzen)
    assert not chitoi(kokushi_hora_hand)


def test_kokushi(regular_hora_hand_menzen, chitoi_hora_hand, kokushi_hora_hand, kokushi_hora_hand_thirteen_waiting):
    assert not kokushi(regular_hora_hand_menzen)
    assert not kokushi(chitoi_hora_hand)
    assert kokushi(kokushi_hora_hand)
    assert not kokushi(kokushi_hora_hand_thirteen_waiting)


def test_suanko():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("8p"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("777z")],
        agari_tatsu=parse_tatsu("77z"),
        agari=tile("7z"),
        tsumo=True
    )
    assert suanko(regular_hand)

    regular_hand.tsumo = False
    assert not suanko(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("444z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert not suanko(regular_hand)


def test_daisangen():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("2z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("555z"), parse_mentsu("666z"), parse_mentsu("777z")],
        agari_tatsu=None,
        agari=tile("2z"),
        tsumo=True
    )
    assert daisangen(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("666z"), parse_mentsu("777z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert not daisangen(regular_hand)


def test_tsuiso():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("444z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert tsuiso(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("8p"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("777z")],
        agari_tatsu=None,
        agari=tile("8p"),
        tsumo=True
    )
    assert not tsuiso(regular_hand)


def test_shousushi():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("444z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert not shousushi(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("4z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("777z")],
        agari_tatsu=None,
        agari=tile("4z"),
        tsumo=True
    )
    assert shousushi(regular_hand)


def test_lyuiso():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("345s"), parse_mentsu("234s"), parse_mentsu("666s"), parse_mentsu("888s")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert not lyuiso(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("6z"),
        menzen_mentsu=[parse_mentsu("234s"), parse_mentsu("234s"), parse_mentsu("666s"), parse_mentsu("888s")],
        agari_tatsu=None,
        agari=tile("6z"),
        tsumo=True
    )
    assert lyuiso(regular_hand)


def test_chinroto():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1z"),
        menzen_mentsu=[parse_mentsu("111m"), parse_mentsu("999m"), parse_mentsu("111s"), parse_mentsu("999s")],
        agari_tatsu=None,
        agari=tile("1z"),
        tsumo=True
    )
    assert not chinroto(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1p"),
        menzen_mentsu=[parse_mentsu("111m"), parse_mentsu("999m"), parse_mentsu("111s"), parse_mentsu("999s")],
        agari_tatsu=None,
        agari=tile("1p"),
        tsumo=True
    )
    assert chinroto(regular_hand)


def test_sukantsu():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1z"),
        menzen_mentsu=[parse_mentsu("111m"), parse_mentsu("999m"), parse_mentsu("111s"), parse_mentsu("999s")],
        agari_tatsu=None,
        agari=tile("1z"),
        tsumo=True
    )
    assert not sukantsu(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1p"), furo=[parse_furo("0550z"), parse_furo("1111p"), parse_furo("7777p"), parse_furo("6666m")],
        agari_tatsu=None,
        agari=tile("1p"),
        tsumo=True
    )
    assert sukantsu(regular_hand)


def test_churen():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("999m")],
        agari_tatsu=parse_tatsu("23m"),
        agari=tile("1m"),
        tsumo=True
    )
    assert churen(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("999m")],
        agari_tatsu=parse_tatsu("99m"),
        agari=tile("9m"),
        tsumo=True
    )
    assert not churen(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("8m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("789m"), parse_mentsu("222m"), parse_mentsu("456m")],
        agari_tatsu=parse_tatsu("22m"),
        agari=tile("2m"),
        tsumo=True
    )
    assert not churen(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("2m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("888m")],
        agari_tatsu=parse_tatsu("78m"),
        agari=tile("9m"),
        tsumo=True
    )
    assert not churen(regular_hand)


def test_daisushi():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("444z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert daisushi(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("4z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("777z")],
        agari_tatsu=None,
        agari=tile("4z"),
        tsumo=True
    )
    assert not daisangen(regular_hand)


def test_churen9():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("999m")],
        agari_tatsu=parse_tatsu("23m"),
        agari=tile("1m"),
        tsumo=True
    )
    assert not churen9(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("1m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("999m")],
        agari_tatsu=parse_tatsu("99m"),
        agari=tile("9m"),
        tsumo=True
    )
    assert churen9(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("8m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("789m"), parse_mentsu("222m"), parse_mentsu("456m")],
        agari_tatsu=parse_tatsu("22m"),
        agari=tile("2m"),
        tsumo=True
    )
    assert not churen9(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("2m"),
        menzen_mentsu=[parse_mentsu("123m"), parse_mentsu("456m"), parse_mentsu("789m"), parse_mentsu("888m")],
        agari_tatsu=parse_tatsu("78m"),
        agari=tile("9m"),
        tsumo=True
    )
    assert not churen9(regular_hand)


def test_suanko_tanki():
    regular_hand = RegularHoraHandPattern(
        jyantou=tile("8p"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("777z")],
        agari_tatsu=parse_tatsu("77z"),
        agari=tile("7z"),
        tsumo=True
    )
    assert not suanko_tanki(regular_hand)

    regular_hand = RegularHoraHandPattern(
        jyantou=tile("5z"),
        menzen_mentsu=[parse_mentsu("111z"), parse_mentsu("222z"), parse_mentsu("333z"), parse_mentsu("444z")],
        agari_tatsu=None,
        agari=tile("5z"),
        tsumo=True
    )
    assert suanko_tanki(regular_hand)

    regular_hand.tsumo = False
    assert suanko_tanki(regular_hand)


def test_kokushi13(regular_hora_hand_menzen, chitoi_hora_hand, kokushi_hora_hand, kokushi_hora_hand_thirteen_waiting):
    assert not kokushi13(regular_hora_hand_menzen)
    assert not kokushi13(chitoi_hora_hand)
    assert not kokushi13(kokushi_hora_hand)
    assert kokushi13(kokushi_hora_hand_thirteen_waiting)
