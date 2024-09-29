import atexit
import os
from pathlib import Path

from .js import JsMahjongUtils
from .lib import LibMahjongUtils
from .protocol import MahjongUtilsBridge
from .webapi_jar import WebApiJarMahjongUtils

ENV_MAHJONG_UTILS_BRIDGE = "ENV_MAHJONG_UTILS_BRIDGE"
ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH = "ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH"


class RouterMahjongUtils(MahjongUtilsBridge):
    def __init__(self):
        self._delegate = None

    @property
    def delegate(self) -> MahjongUtilsBridge:
        if self._delegate is None:
            bridgeType = os.getenv(ENV_MAHJONG_UTILS_BRIDGE)
            if bridgeType == "webapi_jar":
                webapi_jar_path = os.getenv(ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH)
                if Path(webapi_jar_path).exists():
                    self._delegate = WebApiJarMahjongUtils()
                else:
                    raise FileNotFoundError(webapi_jar_path)
            elif bridgeType == "js":
                self._delegate = JsMahjongUtils()
            else:
                self._delegate = LibMahjongUtils()
        return self._delegate

    def call(self, name: str, params: dict) -> dict:
        return self.delegate.call(name, params)

    def close(self):
        return self.delegate.close()


bridge_mahjongutils = RouterMahjongUtils()
atexit.register(bridge_mahjongutils.close)
