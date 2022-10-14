from mahjong_utils.models.tile import tile
from mahjong_utils.yaku.common import ittsu, chinitsu, ipe, tsumo, pinhu
from mahjong_utils.yaku.extra import richi


def test_build_hora_from_shanten_result():
    from mahjong_utils.shanten import shanten
    from mahjong_utils.hora import build_hora_from_shanten_result

    result = shanten("1112356778899p")
    hora = build_hora_from_shanten_result(result, tile("4p"), True, extra_yaku={richi})

    assert hora.yaku == {richi, ittsu, chinitsu, ipe, tsumo, pinhu}
    assert hora.han == 12
    assert hora.hand.hu == 20
