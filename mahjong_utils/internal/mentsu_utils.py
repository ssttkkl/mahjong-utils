from typing import Optional, Union

from mahjong_utils.models.mentsu import Mentsu, Kotsu, Shuntsu
from mahjong_utils.models.tatsu import Tatsu, Toitsu, Penchan, Ryanmen, Kanchan
from mahjong_utils.models.tile import Tile


def kotsu_after_discard(kotsu: Kotsu, got: Tile) -> Optional[Toitsu]:
    if got == kotsu.tile:
        return Toitsu(got)
    else:
        return None


def shuntsu_after_discard(shuntsu: Shuntsu, got: Tile) -> Optional[Union[Penchan, Ryanmen, Kanchan]]:
    if got == shuntsu.tile:
        if got.num == 7:
            return Penchan(got + 1)
        elif got.num == 3:
            return Penchan(got - 2)
        else:
            return Ryanmen(got + 1)
    elif got == shuntsu.tile + 1:
        return Kanchan(shuntsu.tile)
    elif got == shuntsu.tile + 2:
        if got.num == 1:
            return Penchan(shuntsu.tile)
        else:
            return Ryanmen(shuntsu.tile)


def mentsu_after_discard(mentsu: Mentsu, got: Tile) -> Optional[Tatsu]:
    if isinstance(mentsu, Kotsu):
        return kotsu_after_discard(mentsu, got)
    elif isinstance(mentsu, Shuntsu):
        return shuntsu_after_discard(mentsu, got)
