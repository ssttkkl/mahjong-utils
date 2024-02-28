from typing import Optional

from .models import ParentPoint, ChildPoint, HanHuOptions
from ..bridge import bridge_mahjongutils


def get_parent_point_by_han_hu(han: int, hu: int, options: Optional[HanHuOptions] = None) -> ParentPoint:
    """
    获取亲家X番Y符的点数

    :param han: 番
    :param hu: 符
    :param options: 计算点数时应用的选项
    :return: (荣和点数, 自摸各家点数)
    """
    args = {
        "han": han,
        "hu": hu
    }
    if options is not None:
        args["options"] = options.__encode__()
    result = bridge_mahjongutils.call("getParentPointByHanHu", args)

    return ParentPoint.__decode__(result)


def get_child_point_by_han_hu(han: int, hu: int, options: Optional[HanHuOptions] = None) -> ChildPoint:
    """
    获取子家X番Y符的点数

    :param han: 番
    :param hu: 符
    :return: (荣和点数, 自摸庄家点数, 自摸闲家点数)
    """
    args = {
        "han": han,
        "hu": hu
    }
    if options is not None:
        args["options"] = options.__encode__()
    result = bridge_mahjongutils.call("getChildPointByHanHu", args)

    return ChildPoint.__decode__(result)


__all__ = ("get_parent_point_by_han_hu", "get_child_point_by_han_hu",)
