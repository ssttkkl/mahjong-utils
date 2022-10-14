from mahjong_utils.models.furo import parse_furo
from mahjong_utils.models.tile import tile, parse_tiles
from mahjong_utils.yaku.common import ittsu, chinitsu, ipe, tsumo, pinhu, honitsu
from mahjong_utils.yaku.extra import richi
from mahjong_utils.yaku.yakuman import churen


def test_build_hora():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1112356778899p"), None, tile("4p"), True, dora=4, extra_yaku={richi})

    assert hora.yaku == {richi, ittsu, chinitsu, ipe, tsumo, pinhu}
    assert hora.han == 16
    assert hora.hand.hu == 20


def test_build_hora_2():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1112356789999p"), None, tile("4p"), True, extra_yaku={richi})

    assert hora.yaku == {churen}
    assert hora.han == 13


def test_build_hora_3():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1345556m111z"), [parse_furo("789m")], tile("2m"), True)

    assert hora.yaku == {ittsu, honitsu}
    assert hora.han == 3
    assert hora.hand.hu == 40


def test_build_hora_4():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("12323467m11z"), [parse_furo("789p")], tile("5m"), True, dora=13)

    assert len(hora.yaku) == 0
    assert hora.han == 0
    assert hora.hand.hu == 30
