from mahjong_utils.yaku.yaku import Yaku
from mahjong_utils.yaku.common import all_common_yaku
from mahjong_utils.yaku.extra import all_extra_yaku
from mahjong_utils.yaku.yakuman import all_yakuman

all_yaku = all_common_yaku | all_extra_yaku | all_yakuman

_all_yaku_mapping = dict(map(lambda x: (x.name, x), all_yaku))


def get_yaku(name: str) -> Yaku:
    yaku = _all_yaku_mapping.get(name, None)
    if yaku is None:
        raise ValueError(f"{name} is not a yaku")
    return yaku


__all__ = ("all_yaku", "get_yaku")
