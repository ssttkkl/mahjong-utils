from .lib import LibMahjongUtils
from .protocol import MahjongUtilsBridge
from .webapi_jar import WebApiJarMahjongUtils, mahjongutils_webapi_jar_path


class RouterMahjongUtils(MahjongUtilsBridge):
    def __init__(self):
        self._delegate = None

    @property
    def delegate(self) -> MahjongUtilsBridge:
        if self._delegate is None:
            if mahjongutils_webapi_jar_path().exists():
                self._delegate = WebApiJarMahjongUtils()
            else:
                self._delegate = LibMahjongUtils()
        return self._delegate

    def call(self, name: str, params: dict) -> dict:
        return self.delegate.call(name, params)

    def close(self):
        return self.delegate.close()


bridge_mahjongutils = RouterMahjongUtils()
