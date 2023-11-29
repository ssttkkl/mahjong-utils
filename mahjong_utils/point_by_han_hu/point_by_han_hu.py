from .models import ParentPoint, ChildPoint
from ..bridge import bridge_mahjongutils


def get_parent_point_by_han_hu(han: int, hu: int) -> ParentPoint:
    """
    获取亲家X番Y符的点数

    :param han: 番
    :param hu: 符
    :return: (荣和点数, 自摸各家点数)
    """
    result = bridge_mahjongutils.call("getParentPointByHanHu", {
        "han": han,
        "hu": hu
    })

    return ParentPoint.__decode__(result)


def get_child_point_by_han_hu(han: int, hu: int) -> ChildPoint:
    """
    获取子家X番Y符的点数

    :param han: 番
    :param hu: 符
    :return: (荣和点数, 自摸庄家点数, 自摸闲家点数)
    """
    result = bridge_mahjongutils.call("getChildPointByHanHu", {
        "han": han,
        "hu": hu
    })

    return ChildPoint.__decode__(result)


__all__ = ("get_parent_point_by_han_hu", "get_child_point_by_han_hu",)
