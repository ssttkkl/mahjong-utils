from typing import NamedTuple


class Yaku(NamedTuple):
    name: str
    han: int
    furo_loss: int = 0
    is_yakuman: bool = False


__all__ = ("Yaku",)
