mahjong-utils-py
========

[![PyPI version](https://badge.fury.io/py/mahjong-utils.svg)](https://badge.fury.io/py/mahjong-utils)

日麻小工具[ssttkkl/mahjong-utils](https://github.com/ssttkkl/mahjong-utils)的Python绑定

已实现功能：

- [x] 获取番符对应和牌点数
- [x] 向听数、进张分析
- [x] 和了分析（役种、番数、符数）

## 安装

```shell
pip install mahjong-utils
```

或通过源码安装（本机必须配置JDK环境用于编译）：

```shell
pip install git+https://github.com/ssttkkl/mahjong-utils-py.git
```

## 使用

### 获取番符对应和牌点数

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

### 向听数、进张分析

```python
from mahjong_utils.models.tile import parse_tiles
from mahjong_utils.shanten import shanten

# 向听数、进张分析（未摸牌状态）
result = shanten(parse_tiles("34568m235p68s"))
result.shanten
# 1
result.advance
# {3m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6s, 7s, 8s}

# 向听数、进张分析（已摸牌状态）
result = shanten(parse_tiles("112233p44556s127z"))
result.shanten
# 1
result.discard_to_advance
# {1p: ShantenWithoutGot(shanten=2, advance={2z, 7z, 1p, 4p, 3s, 6s, 1z}, advance_num=22, good_shape_advance=None, good_shape_advance_num=None),
# 2p: ShantenWithoutGot(shanten=2, advance={2z, 7z, 2p, 3s, 6s, 1z}, advance_num=18, good_shape_advance=None, good_shape_advance_num=None),
# 3p: ShantenWithoutGot(shanten=2, advance={2z, 7z, 3p, 3s, 6s, 1z}, advance_num=18, good_shape_advance=None, good_shape_advance_num=None),
# 4s: ShantenWithoutGot(shanten=2, advance={2z, 7z, 3s, 4s, 5s, 6s, 7s, 1z}, advance_num=24, good_shape_advance=None, good_shape_advance_num=None),
# 5s: ShantenWithoutGot(shanten=2, advance={2z, 7z, 2s, 3s, 4s, 5s, 6s, 1z}, advance_num=24, good_shape_advance=None, good_shape_advance_num=None),
# 6s: ShantenWithoutGot(shanten=1, advance={2z, 7z, 1z}, advance_num=9, good_shape_advance=set(), good_shape_advance_num=0),
# 1z: ShantenWithoutGot(shanten=1, advance={2z, 6s, 7z, 3s}, advance_num=13, good_shape_advance={2z, 7z}, good_shape_advance_num=6),
# 2z: ShantenWithoutGot(shanten=1, advance={6s, 3s, 7z, 1z}, advance_num=13, good_shape_advance={7z, 1z}, good_shape_advance_num=6),
# 7z: ShantenWithoutGot(shanten=1, advance={2z, 6s, 3s, 1z}, advance_num=13, good_shape_advance={2z, 1z}, good_shape_advance_num=6)}
```

### 和了分析

```python
from mahjong_utils.hora import build_hora
from mahjong_utils.models.tile import parse_tiles, Tile
from mahjong_utils.models.wind import Wind
from mahjong_utils.models.furo import Furo
from mahjong_utils.yaku.common import self_wind, round_wind

# 和了分析
hora = build_hora(
    tiles=parse_tiles("12233466m111z"),
    furo=[Furo.parse("789p")],
    agari=Tile.by_text("1z"),
    tsumo=True,
    dora=4,
    self_wind=Wind.east,
    round_wind=Wind.east
)

# hora.yaku == {self_wind, round_wind}
# hora.han == 6
# hora.hu == 30
# hora.parent_point == (18000, 6000)
# hora.child_point == (12000, 6000, 3000)
```
