from mahjong_utils.models.hora_hand import StdHoraHand, ChitoiHoraHand, KokushiHoraHand
from mahjong_utils.models.mentsu import parse_mentsu
from mahjong_utils.models.tatsu import parse_tatsu
from mahjong_utils.models.tile import tile, parse_tiles


def test_std_hora_hand():
    print(StdHoraHand.schema())

    hora_hand = StdHoraHand(jyantou=tile("1p"),
                            menzen_mentsu=[parse_mentsu("123p"), parse_mentsu("789p"), parse_mentsu("789p")],
                            tatsu=[parse_tatsu("56p")],
                            agari=tile("4p"),
                            tsumo=False)
    print(hora_hand)


def test_chitoi_hora_hand():
    print(ChitoiHoraHand.schema())

    hora_hand = ChitoiHoraHand(pairs=parse_tiles("123567p"),
                               remaining=[tile("4p")],
                               agari=tile("4p"),
                               tsumo=True)
    print(hora_hand)


def test_kokushi_hora_hand():
    print(KokushiHoraHand.schema())

    hora_hand = KokushiHoraHand(repeated=tile("1z"),
                                thirteen_waiting=True,
                                agari=tile("1p"),
                                tsumo=True)
    print(hora_hand)
