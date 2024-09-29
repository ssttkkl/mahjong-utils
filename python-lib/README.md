mahjong-utils-py
========

[![PyPI version](https://badge.fury.io/py/mahjong-utils.svg)](https://badge.fury.io/py/mahjong-utils)

日麻小工具[ssttkkl/mahjong-utils](https://github.com/ssttkkl/mahjong-utils)的Python绑定

## 安装

```shell
pip install mahjong-utils
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

### 手牌向听数、进张分析

```python
from mahjong_utils.models.tile import parse_tiles
from mahjong_utils.shanten import shanten

result = shanten(parse_tiles("34568m235p68s"))
result.shanten
# 1
result.advance
# {3m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6s, 7s, 8s}
```

### 摸牌何切分析
```python
from mahjong_utils.models.tile import parse_tiles
from mahjong_utils.shanten import shanten

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

### 鸣牌分析
```python
from mahjong_utils.models.tile import parse_tiles, Tile
from mahjong_utils.shanten import furo_chance_shanten

result = furo_chance_shanten(parse_tiles("3456778m123457p"),
                             Tile.by_text("7m"))
result.shanten
# 0
result.shanten_info.chi
# {56m: ShantenWithGot(shanten=1, discard_to_advance={
#   8m: ShantenWithoutGot(shanten=1, advance={6p, 2m, 5m, 3p}, advance_num=15, good_shape_advance={6p, 2m, 5m, 3p}, good_shape_advance_num=15), 
#   7p: ShantenWithoutGot(shanten=1, advance={6p, 2m, 5m, 3p}, advance_num=15, good_shape_advance={6p, 2m, 5m, 3p}, good_shape_advance_num=15), 
#   4p: ShantenWithoutGot(shanten=1, advance={6p, 2m, 5m}, advance_num=12, good_shape_advance={6p}, good_shape_advance_num=4), 
#   1p: ShantenWithoutGot(shanten=1, advance={6p, 2m, 5m}, advance_num=12, good_shape_advance={6p}, good_shape_advance_num=4), 
#   3m: ShantenWithoutGot(shanten=2, advance={2m, 3m, 4m, 5m, 6m, 7m, 8m, 9m, 1p, 2p, 3p, 4p, 5p, 6p, 7p, 8p, 9p}, advance_num=57, good_shape_advance=None, good_shape_advance_num=None),
#   4m: ShantenWithoutGot(shanten=2, advance={1m, 2m, 3m, 4m, 5m, 6m, 7m, 8m, 9m, 1p, 2p, 3p, 4p, 5p, 6p, 7p, 8p, 9p}, advance_num=61, good_shape_advance=None, good_shape_advance_num=None), 
#   7m: ShantenWithoutGot(shanten=2, advance={2m, 3m, 4m, 5m, 6m, 7m, 8m, 9m, 1p, 2p, 3p, 4p, 5p, 6p, 7p}, advance_num=49, good_shape_advance=None, good_shape_advance_num=None), 
#   2p: ShantenWithoutGot(shanten=2, advance={2m, 5m, 6m, 7m, 8m, 9m, 1p, 2p, 3p, 5p, 6p, 7p, 8p, 9p}, advance_num=48, good_shape_advance=None, good_shape_advance_num=None),
#   3p: ShantenWithoutGot(shanten=2, advance={6p, 2m, 5m, 3p}, advance_num=15, good_shape_advance=None, good_shape_advance_num=None), 
#   5p: ShantenWithoutGot(shanten=2, advance={2m, 5m, 6m, 7m, 8m, 9m, 1p, 2p, 3p, 4p, 5p, 6p, 7p, 8p, 9p}, advance_num=51, good_shape_advance=None, good_shape_advance_num=None)}, ankan_to_advance={}), 
# 68m: ShantenWithGot(shanten=0, discard_to_advance={
#   7p: ShantenWithoutGot(shanten=0, advance={6p, 3p}, advance_num=7, good_shape_advance=None, good_shape_advance_num=None), 
#   4p: ShantenWithoutGot(shanten=0, advance={6p}, advance_num=4, good_shape_advance=None, good_shape_advance_num=None), 
#   1p: ShantenWithoutGot(shanten=0, advance={6p}, advance_num=4, good_shape_advance=None, good_shape_advance_num=None), 
#   3m: ShantenWithoutGot(shanten=1, advance={6p, 3m, 3p, 6m}, advance_num=14, good_shape_advance={6p, 3m, 3p, 6m}, good_shape_advance_num=14),
#   4m: ShantenWithoutGot(shanten=1, advance={6p, 4m, 3p}, advance_num=10, good_shape_advance={4m}, good_shape_advance_num=3), 
#   5m: ShantenWithoutGot(shanten=1, advance={6p, 2m, 5m, 3p}, advance_num=14, good_shape_advance={6p, 2m, 5m, 3p}, good_shape_advance_num=14), 
#   7m: ShantenWithoutGot(shanten=1, advance={7m, 1p, 3p, 4p, 6p, 7p}, advance_num=18, good_shape_advance={6p, 7p, 7m}, good_shape_advance_num=9), 
#   2p: ShantenWithoutGot(shanten=1, advance={1p, 2p, 3p, 5p, 6p, 7p, 8p, 9p}, advance_num=27, good_shape_advance={6p, 8p, 2p}, good_shape_advance_num=11),
#   3p: ShantenWithoutGot(shanten=1, advance={6p, 3p}, advance_num=7, good_shape_advance={3p}, good_shape_advance_num=3),
#   5p: ShantenWithoutGot(shanten=1, advance={1p, 2p, 3p, 4p, 5p, 6p, 7p, 8p, 9p}, advance_num=30, good_shape_advance={6p, 8p, 3p, 5p}, good_shape_advance_num=14)}, ankan_to_advance={})}

result.shanten_info.pon
# ShantenWithGot(shanten=1, discard_to_advance={
#   7p: ShantenWithoutGot(shanten=1, advance={3m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6p}, advance_num=32, good_shape_advance={3m, 6m, 7m, 8m, 3p, 6p}, good_shape_advance_num=20), 
#   4p: ShantenWithoutGot(shanten=1, advance={3m, 6m, 7m, 8m, 5p, 6p, 7p}, advance_num=23, good_shape_advance={6p}, good_shape_advance_num=4), 
#   5p: ShantenWithoutGot(shanten=1, advance={7p, 1p, 4p, 7m}, advance_num=13, good_shape_advance={7m}, good_shape_advance_num=4), 
#   6m: ShantenWithoutGot(shanten=1, advance={8m, 1p, 3p, 4p, 6p, 7p}, advance_num=19, good_shape_advance={6p, 7p, 8m}, good_shape_advance_num=10), 
#   8m: ShantenWithoutGot(shanten=1, advance={3m, 6m, 1p, 3p, 4p, 6p, 7p}, advance_num=22, good_shape_advance={3m, 6m, 3p, 6p, 7p}, good_shape_advance_num=16), 
#   1p: ShantenWithoutGot(shanten=1, advance={3m, 6m, 7m, 8m, 2p, 5p, 6p, 7p}, advance_num=26, good_shape_advance={6p, 7m}, good_shape_advance_num=8), 
#   2p: ShantenWithoutGot(shanten=1, advance={7p, 1p, 7m}, advance_num=10, good_shape_advance=set(), good_shape_advance_num=0), 
#   3m: ShantenWithoutGot(shanten=1, advance={8m, 1p, 3p, 4p, 6p, 7p}, advance_num=19, good_shape_advance={6p, 8m, 7p}, good_shape_advance_num=10), 
#   4m: ShantenWithoutGot(shanten=2, advance={3m, 4m, 5m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6p, 7p}, advance_num=41, good_shape_advance=None, good_shape_advance_num=None), 
#   5m: ShantenWithoutGot(shanten=2, advance={2m, 3m, 4m, 5m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6p, 7p}, advance_num=45, good_shape_advance=None, good_shape_advance_num=None), 
#   3p: ShantenWithoutGot(shanten=2, advance={3m, 6m, 7m, 8m, 1p, 2p, 3p, 4p, 5p, 6p, 7p}, advance_num=35, good_shape_advance=None, good_shape_advance_num=None)}, ankan_to_advance={})

result.shanten_info.pass_
# ShantenWithoutGot(shanten=1, advance={7m, 1p, 3p, 4p, 6p, 7p}, advance_num=18, good_shape_advance={6p, 7p, 7m}, good_shape_advance_num=9)

result.shanten_info.minkan
# None
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
