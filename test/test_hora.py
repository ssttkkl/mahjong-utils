from mahjong_utils.models.furo import parse_furo
from mahjong_utils.models.tile import tile, parse_tiles
from mahjong_utils.models.wind import Wind
from mahjong_utils.yaku.common import ittsu, chinitsu, ipe, tsumo, pinhu, honitsu, sananko, toitoi, self_wind, \
    round_wind, haku
from mahjong_utils.yaku.extra import richi, tenhou, ippatsu
from mahjong_utils.yaku.yakuman import churen, tsuiso, daisushi, suanko_tanki, sukantsu, lyuiso


def test_build_hora():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1112356778899p"), None, tile("4p"), True, dora=4, extra_yaku={richi})

    assert hora.yaku == {richi, ittsu, chinitsu, ipe, tsumo, pinhu}
    assert hora.han == 16
    assert hora.hand.hu == 20
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)


def test_build_hora_2():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1112356789999p"), None, tile("4p"), True, extra_yaku={richi})

    assert hora.yaku == {churen}
    assert hora.han == 13
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)


def test_build_hora_3():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1345556m111z"), [parse_furo("789m")], tile("2m"), True)

    assert hora.yaku == {ittsu, honitsu}
    assert hora.han == 3
    assert hora.hand.hu == 40
    assert hora.parent_point == (7700, 2600)
    assert hora.child_point == (5200, 2600, 1300)


def test_build_hora_4():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("12323467m11z"), [parse_furo("789p")], tile("5m"), True, dora=13)

    assert len(hora.yaku) == 0
    assert hora.han == 0
    assert hora.hand.hu == 30
    assert hora.parent_point == (0, 0)
    assert hora.child_point == (0, 0, 0)


def test_build_hora_5():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("1112223334445z"), None, tile("5z"), True, extra_yaku={tenhou})

    assert hora.yaku == {tsuiso, daisushi, suanko_tanki, tenhou}
    assert hora.han == 13 * 6
    assert hora.parent_point == (48000 * 6, 16000 * 6)
    assert hora.child_point == (32000 * 6, 16000 * 6, 8000 * 6)


def test_build_hora_6():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("6z"),
                      [parse_furo("3333s"), parse_furo("2222s"), parse_furo("0440s"), parse_furo("8888s")], tile("6z"),
                      True)

    assert hora.yaku == {lyuiso, sukantsu}
    assert hora.han == 13 * 2
    assert hora.parent_point == (48000 * 2, 16000 * 2)
    assert hora.child_point == (32000 * 2, 16000 * 2, 8000 * 2)


def test_build_hora_7():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("6z"),
                      [parse_furo("0330s"), parse_furo("0220s"), parse_furo("0440s"), parse_furo("0880s")], tile("6z"),
                      True)

    assert hora.yaku == {lyuiso, sukantsu, suanko_tanki}
    assert hora.han == 13 * 4
    assert hora.parent_point == (48000 * 4, 16000 * 4)
    assert hora.child_point == (32000 * 4, 16000 * 4, 8000 * 4)


def test_build_hora_8():
    from mahjong_utils.hora import build_hora

    hora = build_hora(parse_tiles("111333555z22s44p"), None, tile("2s"), False, dora=4,
                      self_wind=Wind.west, round_wind=Wind.east,
                      extra_yaku={richi, ippatsu})

    assert hora.yaku == {sananko, toitoi, richi, ippatsu, self_wind, round_wind, haku}
    assert hora.han == 13
    assert hora.hand.hu == 60
    assert hora.parent_point == (48000, 16000)
    assert hora.child_point == (32000, 16000, 8000)
