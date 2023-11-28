from .lib import LibMahjongUtils
from .cli import CliMahjongUtils, mahjongutils_cli_path
from .protocol import MahjongUtilsBridge


class RouterMahjongUtils(MahjongUtilsBridge):
    def __init__(self):
        self._delegate = None

    @property
    def delegate(self) -> MahjongUtilsBridge:
        if self._delegate is None:
            print(mahjongutils_cli_path())
            if mahjongutils_cli_path().exists():
                self._delegate = CliMahjongUtils()
            else:
                self._delegate = LibMahjongUtils()
        return self._delegate

    def call(self, name: str, params: dict) -> dict:
        return self.delegate.call(name, params)

    def close(self):
        return self.close()


bridge_mahjongutils = RouterMahjongUtils()
