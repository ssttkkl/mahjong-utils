from typing import Dict, Tuple


def _ceil100(x):
    if x % 100 > 0:
        x += 100 - x % 100
    return x


no_ron = {
    (1, 20), (1, 25),
    (2, 20),
    (3, 20),
    (4, 20)
}

no_tsumo = {
    (1, 20), (1, 25), (1, 110),
    (2, 25),
}


def _calc_parent_point(han: int, hu: int):
    a = hu * (2 ** (han + 2))
    if a > 2000:
        a = 2000

    ron = _ceil100(6 * a)
    tsumo = _ceil100(2 * a)

    if (han, hu) in no_ron:
        ron = 0
    if (han, hu) in no_tsumo:
        tsumo = 0

    return ron, tsumo


def build_han_hu_to_parent_point(mapping):
    """
    (番, 符) -> (荣和点数, 自摸各家点数)，5番及以上的结果存到20符里
    :param mapping:
    :return:
    """
    for han in range(1, 5):
        for hu in range(20, 120, 10):
            if (han, hu) not in no_ron and (han, hu) not in no_tsumo:
                mapping[han, hu] = _calc_parent_point(han, hu)

    for han in range(2, 4):
        mapping[han, 25] = _calc_parent_point(han, 25)

    mapping[5, 20] = 12000, 4000
    mapping[6, 20] = 18000, 6000
    mapping[7, 20] = 18000, 6000
    mapping[8, 20] = 24000, 8000
    mapping[9, 20] = 24000, 8000
    mapping[10, 20] = 24000, 8000
    mapping[11, 20] = 36000, 12000
    mapping[12, 20] = 36000, 12000
    mapping[13, 20] = 48000, 16000


def _calc_child_point(han: int, hu: int):
    a = hu * (2 ** (han + 2))
    if a > 2000:
        a = 2000

    ron = _ceil100(4 * a)
    tsumo_parent = _ceil100(2 * a)
    tsumo_child = _ceil100(a)

    if (han, hu) in no_ron:
        ron = 0
    if (han, hu) in no_tsumo:
        tsumo_parent = 0
        tsumo_child = 0

    return ron, tsumo_parent, tsumo_child


def build_han_hu_to_child_point(mapping):
    """
    (番, 符) -> (荣和点数, 自摸庄家点数, 自摸闲家点数)，5番及以上的结果存到20符里
    :param mapping:
    :return:
    """
    for han in range(1, 5):
        for hu in range(20, 120, 10):
            if (han, hu) not in no_ron and (han, hu) not in no_tsumo:
                mapping[han, hu] = _calc_child_point(han, hu)

    for han in range(2, 4):
        mapping[han, 25] = _calc_child_point(han, 25)

    mapping[5, 20] = 8000, 4000, 2000
    mapping[6, 20] = 12000, 6000, 3000
    mapping[7, 20] = 12000, 6000, 3000
    mapping[8, 20] = 16000, 8000, 4000
    mapping[9, 20] = 16000, 8000, 4000
    mapping[10, 20] = 16000, 8000, 4000
    mapping[11, 20] = 24000, 12000, 6000
    mapping[12, 20] = 24000, 12000, 6000
    mapping[13, 20] = 32000, 16000, 8000


parent_han_hu_mapping: Dict[Tuple[int, int], Tuple[int, int, int]] = {}
child_han_hu_mapping: Dict[Tuple[int, int], Tuple[int, int]] = {}

build_han_hu_to_parent_point(parent_han_hu_mapping)
build_han_hu_to_child_point(child_han_hu_mapping)


def get_parent_point_by_han_hu(han: int, hu: int):
    """
    获取亲家X番Y符的点数

    :param han: 番
    :param hu: 符
    :return: (荣和点数, 自摸各家点数)
    """
    if han >= 13:
        han = 13
    if han >= 5:
        hu = 20
    if (han, hu) not in parent_han_hu_mapping:
        raise ValueError(f"invalid arguments: han={han}, hu={hu}")

    return parent_han_hu_mapping[han, hu]


def get_child_point_by_han_hu(han: int, hu: int):
    """
    获取子家X番Y符的点数

    :param han: 番
    :param hu: 符
    :return: (荣和点数, 自摸庄家点数, 自摸闲家点数)
    """
    if han >= 13:
        han = 13
    if han >= 5:
        hu = 20
    if (han, hu) not in child_han_hu_mapping:
        raise ValueError(f"invalid arguments: han={han}, hu={hu}")

    return child_han_hu_mapping[han, hu]


__all__ = ("get_parent_point_by_han_hu", "get_child_point_by_han_hu",)
