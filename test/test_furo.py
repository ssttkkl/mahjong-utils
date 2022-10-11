from mahjong_utils.models.furo import Chi, Pon, Kan, KanType
from mahjong_utils.models.tile import tile


def test_furo():
    chi = Chi(tile("1p"))
    pon = Pon(tile("1p"))
    kan = Kan(tile("1p"), KanType.minkan)

    print(chi, pon, kan)
