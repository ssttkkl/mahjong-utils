from typing import Optional, Sequence

from mahjong_utils.bridge import bridge_mahjongutils
from mahjong_utils.models.furo import Furo
from mahjong_utils.models.tile import Tile
from mahjong_utils.shanten.models import ShantenResult, UnionShantenResult, KokushiShantenResult, ChitoiShantenResult, \
    RegularShantenResult, FuroChanceShantenResult


def regular_shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        best_shanten_only: bool = False
) -> RegularShantenResult:
    """
    标准形向听分析

    :param tiles: 门前的牌
    :param furo: 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = bridge_mahjongutils.call("regularShanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [fr.__encode__() for fr in furo] if furo is not None else [],
        "bestShantenOnly": best_shanten_only
    })

    return RegularShantenResult.__decode__(result)


def chitoi_shanten(
        tiles: Sequence[Tile],
        best_shanten_only: bool = False,
) -> ChitoiShantenResult:
    """
    七对子向听分析

    :param tiles: 门前的牌
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = bridge_mahjongutils.call("chitoiShanten", {
        "tiles": [str(t) for t in tiles],
        "bestShantenOnly": best_shanten_only,
    })

    return ChitoiShantenResult.__decode__(result)


def kokushi_shanten(
        tiles: Sequence[Tile],
        best_shanten_only: bool = False,
) -> KokushiShantenResult:
    """
    国士无双向听分析

    :param tiles: 门前的牌
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = bridge_mahjongutils.call("kokushiShanten", {
        "tiles": [str(t) for t in tiles],
        "bestShantenOnly": best_shanten_only,
    })

    return KokushiShantenResult.__decode__(result)


def shanten(
        tiles: Sequence[Tile],
        furo: Optional[Sequence[Furo]] = None,
        best_shanten_only: bool = False
) -> UnionShantenResult:
    """
    向听分析

    :param tiles: 门前的牌
    :param furo: 副露（对向听分析本身无用，但若需要将结果用于和了分析则需要传入）
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :return 向听分析结果
    """
    result = bridge_mahjongutils.call("shanten", {
        "tiles": [str(t) for t in tiles],
        "furo": [fr.__encode__() for fr in furo] if furo is not None else [],
        "bestShantenOnly": best_shanten_only
    })

    return UnionShantenResult.__decode__(result)


def furo_chance_shanten(
        tiles: Sequence[Tile],
        chance_tile: Tile,
        allow_chi: bool = True,
        best_shanten_only: bool = False,
        allow_kuikae: bool = False
):
    """
    副露判断分析

    :param tiles: 门前的牌
    :param chance_tile: 副露机会牌（能够吃、碰的牌）
    :param allow_chi: 是否允许吃
    :param best_shanten_only: 仅计算最优向听数的打法（不计算退向打法）
    :param allow_kuikae: 是否允许食替
    :return 向听分析结果
    """
    result = bridge_mahjongutils.call("furoChanceShanten", {
        "tiles": [str(t) for t in tiles],
        "chanceTile": chance_tile.__encode__(),
        "allowChi": allow_chi,
        "bestShantenOnly": best_shanten_only,
        "allowKuikae": allow_kuikae
    })

    return FuroChanceShantenResult.__decode__(result)


__all__ = ("regular_shanten",
           "chitoi_shanten",
           "kokushi_shanten",
           "furo_chance_shanten",
           "shanten",
           "ShantenResult",)
