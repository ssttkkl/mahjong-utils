from mahjong_utils.hora import build_hora, build_hora_from_shanten_result
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.tile import Tile, parse_tiles
from mahjong_utils.models.wind import Wind
from mahjong_utils.shanten import shanten
from mahjong_utils.yaku.common import ittsu, chinitsu, ipe, tsumo, pinhu, honitsu, sananko, toitoi, self_wind, \
    round_wind, haku, chitoi
from mahjong_utils.yaku.extra import richi, tenhou, ippatsu
from mahjong_utils.yaku.yakuman import churen, tsuiso, daisushi, suanko_tanki, sukantsu, lyuiso, \
    kokushi_thirteen_waiting


def test_build_hora():
    hora = build_hora(parse_tiles("11123456778899p"), None, Tile.by_text("4p"), True, dora=4, extra_yaku={richi})

    assert hora.yaku == {richi, ittsu, chinitsu, ipe, tsumo, pinhu}
    assert hora.han == 16
    assert hora.hu == 20
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)


def test_build_hora_2():
    hora = build_hora(parse_tiles("11123456789999p"), None, Tile.by_text("4p"), True, extra_yaku={richi})

    assert hora.yaku == {churen}
    assert hora.han == 13
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)


def test_build_hora_3():
    hora = build_hora(parse_tiles("1345556m111z2m"), [Furo.parse("789m")], Tile.by_text("2m"), True)

    assert hora.yaku == {ittsu, honitsu}
    assert hora.han == 3
    assert hora.hu == 40
    assert hora.parent_point == (7700, 2600)
    assert hora.child_point == (5200, 2600, 1300)


def test_build_hora_4():
    hora = build_hora(parse_tiles("12323467m11z5m"), [Furo.parse("789p")], Tile.by_text("5m"), True, dora=13)

    assert len(hora.yaku) == 0
    assert hora.han == 0
    assert hora.hu == 30
    assert hora.parent_point == (0, 0)
    assert hora.child_point == (0, 0, 0)


def test_build_hora_5():
    hora = build_hora(parse_tiles("11122233344455z"), None, Tile.by_text("5z"), True, extra_yaku={tenhou})

    assert hora.yaku == {tsuiso, daisushi, suanko_tanki, tenhou}
    assert hora.han == 13 * 6
    assert hora.parent_point == (48000 * 6, 16000 * 6)
    assert hora.child_point == (32000 * 6, 16000 * 6, 8000 * 6)


def test_build_hora_6():
    hora = build_hora(parse_tiles("66z"),
                      [Furo.parse("3333s"), Furo.parse("2222s"), Furo.parse("0440s"), Furo.parse("8888s")],
                      Tile.by_text("6z"),
                      True)

    assert hora.yaku == {lyuiso, sukantsu}
    assert hora.han == 13 * 2
    assert hora.parent_point == (48000 * 2, 16000 * 2)
    assert hora.child_point == (32000 * 2, 16000 * 2, 8000 * 2)


def test_build_hora_7():
    hora = build_hora(parse_tiles("66z"),
                      [Furo.parse("0330s"), Furo.parse("0220s"), Furo.parse("0440s"), Furo.parse("0880s")],
                      Tile.by_text("6z"),
                      True)

    assert hora.yaku == {lyuiso, sukantsu, suanko_tanki}
    assert hora.han == 13 * 4
    assert hora.parent_point == (48000 * 4, 16000 * 4)
    assert hora.child_point == (32000 * 4, 16000 * 4, 8000 * 4)


def test_build_hora_8():
    hora = build_hora(parse_tiles("111333555z222s44p"), None, Tile.by_text("2s"), False, dora=4,
                      self_wind=Wind.west, round_wind=Wind.east,
                      extra_yaku={richi, ippatsu})

    assert hora.yaku == {sananko, toitoi, richi, ippatsu, self_wind, round_wind, haku}
    assert hora.han == 13
    assert hora.hu == 60
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)


def test_build_hora_9():
    hora = build_hora(parse_tiles("55z"),
                      [Furo.parse("0110z"), Furo.parse("0220z"), Furo.parse("0330z"), Furo.parse("0440z")],
                      Tile.by_text("5z"),
                      True)

    assert hora.yaku == {suanko_tanki, sukantsu, daisushi, tsuiso}
    assert hora.han == 13 * 6
    assert hora.parent_point == (48000 * 6, 16000 * 6)
    assert hora.child_point == (32000 * 6, 16000 * 6, 8000 * 6)


def test_build_hora_10():
    hora = build_hora(parse_tiles("19s19p19m12345677z"),
                      [],
                      Tile.by_text("7z"),
                      True)

    assert hora.yaku == {kokushi_thirteen_waiting}
    assert hora.han == 13 * 2
    assert hora.parent_point == (48000 * 2, 16000 * 2)
    assert hora.child_point == (32000 * 2, 16000 * 2, 8000 * 2)


def test_build_hora_11():
    hora = build_hora(parse_tiles("114477s225588m33z"),
                      [],
                      Tile.by_text("3z"),
                      False)

    assert hora.yaku == {chitoi}
    assert hora.han == 2
    assert hora.parent_point == (2400, 0)
    assert hora.child_point == (1600, 0, 0)


def test_build_hora_from_shanten_result():
    shanten_result = shanten(parse_tiles("11123445678999m"))
    hora = build_hora_from_shanten_result(shanten_result, Tile.by_text("9m"), True)

    assert hora.yaku == {churen}
    assert hora.han == 13
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)
