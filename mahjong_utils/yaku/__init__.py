from typing import Callable, NamedTuple, Optional

from mahjong_utils.models.hora_hand import HoraHand


class Yaku(NamedTuple):
    name: str
    han: int
    furo_loss: int
    checker: Optional[Callable[[HoraHand], bool]] = None

    def __call__(self, hora_hand: "HoraHand") -> bool:
        if self.checker:
            return self.checker(hora_hand)
        else:
            return False


def _yaku(name: str, han: int, furo_loss: int):
    def decorator(func):
        return Yaku(name, han, furo_loss, func)

    return decorator


__all__ = ("Yaku", "_yaku")
