from typing import Callable, NamedTuple, Optional

from mahjong_utils.models.hora_hand import HoraHand


class Yaku(NamedTuple):
    name: str
    han: int
    furo_loss: int
    is_yakuman: bool = False
    checker: Optional[Callable[[HoraHand], bool]] = None

    def __call__(self, hora_hand: "HoraHand") -> bool:
        if self.checker:
            return self.checker(hora_hand)
        else:
            return False

    def __hash__(self):
        return hash(self.name)


def _yaku(han: int, furo_loss: int, is_yakuman: bool = False):
    def decorator(func):
        return Yaku(func.__name__, han, furo_loss, is_yakuman, func)

    return decorator


__all__ = ("Yaku", "_yaku")
