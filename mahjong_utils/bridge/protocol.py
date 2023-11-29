from typing import Protocol


class MahjongUtilsBridge(Protocol):
    def call(self, name: str, params: dict) -> dict:
        ...

    def close(self):
        ...
