from typing import List

from mahjong_utils.algorithm.hand_searcher import HandSearcher
from mahjong_utils.models.mentsu import Mentsu
from mahjong_utils.models.tatsu import Tatsu
from mahjong_utils.models.tile import Tile


def syanten(hand: List[Tile]):
    if (len(hand) - 2) % 3 == 0:
        k = (len(hand) - 2) // 3
    else:
        k = (len(hand) - 1) // 3

    ans = 10000

    def callback(jyantou: Tile, mentsu: List[Mentsu], tatsu: List[Tatsu], remaining: List[Tile]):
        nonlocal ans

        pending_ans = 2 * (k - len(mentsu)) - len(tatsu)
        if jyantou is not None:
            pending_ans -= 1

        ans = min(ans, pending_ans)

    searcher = HandSearcher(hand, callback)
    searcher.run()

    return ans
