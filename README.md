mahjong-utils
========

已实现功能：

- [x] 获取番符对应和牌点数
- [x] 向听数、进张分析
- [x] 和了分析（役种、番数、符数）

## 获取番符对应和牌点数

```python
from mahjong_utils.point_by_han_hu import get_parent_point_by_han_hu, get_child_point_by_han_hu

# 获取亲家X番Y符的点数，返回(荣和点数, 自摸各家点数)
parent_ron, parent_tsumo = get_parent_point_by_han_hu(3, 40)
# parent_ron == 7700
# parent_tsumo == 2600

# 获取子家X番Y符的点数，返回(荣和点数, 自摸庄家点数, 自摸闲家点数)
child_ron, child_tsumo_parent, child_tsumo_child = get_child_point_by_han_hu(3, 40)
# child_ron == 5200
# child_tsumo_child == 1300
# child_tsumo_parent == 2600
```

## 向听数、进张分析

```python
from mahjong_utils.models.tile import parse_tiles
from mahjong_utils.shanten import shanten

# 向听数、进张分析（未摸牌状态）
result = shanten(parse_tiles("34568m235p68s"))
# result.shanten == 2
# result.advance == {3M, 6M, 7M, 8M, 1P, 2P, 3P, 4P, 5P, 6S, 7S, 8S}

# 向听数、进张分析（已摸牌状态）
result = shanten(parse_tiles("34568m235p368s"))
# result.shanten == 2
# result.discard_to_advance == {
#   5P: {3M, 6M, 7M, 8M, 1P, 2P, 3P, 4P, 3S, 6S, 7S, 8S},
#   3S: {3M, 6M, 7M, 8M, 1P, 2P, 3P, 4P, 5P, 6S, 7S, 8S},
#   2P: {3M, 6M, 7M, 8M, 3P, 4P, 5P, 3S, 6S, 7S, 8S},
#   8M: {3M, 6M, 1P, 2P, 4P, 5P, 3S, 7S},
#   3M: {8M, 1P, 2P, 4P, 5P, 3S, 7S},
#   6M: {8M ,1P, 2P, 4P, 5P, 3S, 7S},
#   6S: {7M, 1P, 2P, 4P, 5P, 3S, 8S},
#   8S: {7M, 1P, 2P, 4P, 5P, 3S, 6S},
#   3P: {7M, 2P, 5P, 3S, 7S},
# }
```

## 和了分析

```python
from mahjong_utils.hora import build_hora
from mahjong_utils.models.tile import parse_tiles, tile
from mahjong_utils.models.wind import Wind
from mahjong_utils.models.furo import parse_furo
from mahjong_utils.yaku.common import self_wind, round_wind

# 和了分析
hora = build_hora(
    tiles=parse_tiles("12233466m11z"),
    furo=[parse_furo("789p")],
    agari=tile("1z"),
    tsumo=True,
    dora=4,
    self_wind=Wind.east,
    round_wind=Wind.east
)

# hora.yaku == {self_wind, round_wind}
# hora.han == 6
# hora.hand.hu == 30
# hora.parent_point == (18000, 6000)
# hora.child_point == (12000, 6000, 3000)
```