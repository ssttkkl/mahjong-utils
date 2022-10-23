from typing import Optional, Union

from mahjong_utils.models.mentsu import Mentsu, Kotsu, Shuntsu
from mahjong_utils.models.tatsu import Tatsu, Toitsu, Penchan, Ryanmen, Kanchan
from mahjong_utils.models.tile import Tile


def kotsu_try_exclude_got(kotsu: Kotsu, got: Tile) -> Optional[Toitsu]:
    if got == kotsu.tile:
        return Toitsu(got)
    else:
        return None


def shuntsu_try_exclude_got(shuntsu: Shuntsu, got: Tile) -> Optional[Union[Penchan, Ryanmen, Kanchan]]:
    if got == shuntsu.tile:
        if got.num == 7:
            return Penchan(got + 1)
        else:
            return Ryanmen(got + 1)
    elif got == shuntsu.tile + 1:
        return Kanchan(shuntsu.tile)
    elif got == shuntsu.tile + 2:
        if got.num == 1:
            return Penchan(shuntsu.tile)
        else:
            return Ryanmen(shuntsu.tile)


def mentsu_try_exclude_got(mentsu: Mentsu, got: Tile) -> Optional[Tatsu]:
    if isinstance(mentsu, Kotsu):
        return kotsu_try_exclude_got(mentsu, got)
    elif isinstance(mentsu, Shuntsu):
        return shuntsu_try_exclude_got(mentsu, got)
