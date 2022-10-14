from typing import Set, Optional

from mahjong_utils.models.hora_hand import HoraHand
from mahjong_utils.yaku import Yaku
from mahjong_utils.yaku.common import all_common_yaku
from mahjong_utils.yaku.yakuman import all_yakuman


def check_yaku(hand: HoraHand, extra_yaku: Optional[Set[Yaku]] = None) -> Set[Yaku]:
    ans = set()
    has_yakuman = False

    for yakuman in all_yakuman:
        # 副露无效的役直接跳过
        if (hand.menzen or yakuman.furo_loss != yakuman.han) and yakuman(hand):
            has_yakuman = True
            ans.add(yakuman)

    if extra_yaku is not None:
        for yakuman in filter(lambda x: x in all_yakuman, extra_yaku):
            has_yakuman = True
            ans.add(yakuman)

    if not has_yakuman:
        for yaku in all_common_yaku:
            if (hand.menzen or yaku.furo_loss != yaku.han) and yaku(hand):
                ans.add(yaku)
        if extra_yaku is not None:
            for yaku in filter(lambda x: x not in all_yakuman, extra_yaku):
                ans.add(yaku)

    return ans
