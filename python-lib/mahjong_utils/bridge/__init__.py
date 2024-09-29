import atexit
import os
from pathlib import Path

from .http import HttpMahjongUtils
from .js import JsMahjongUtils
from .lib import LibMahjongUtils
from .protocol import MahjongUtilsBridge
from .webapi_jar import WebApiJarMahjongUtils

ENV_MAHJONG_UTILS_BRIDGE = "ENV_MAHJONG_UTILS_BRIDGE"
ENV_MAHJONG_UTILS_LIB_SEARCH_PATH = "ENV_MAHJONG_UTILS_LIB_SEARCH_PATH"
ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH = "ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH"
ENV_MAHJONG_UTILS_HTTP_HOST = "ENV_MAHJONG_UTILS_HTTP_HOST"
ENV_MAHJONG_UTILS_HTTP_PORT = "ENV_MAHJONG_UTILS_HTTP_PORT"
ENV_MAHJONG_UTILS_HTTP_SCHEME = "ENV_MAHJONG_UTILS_HTTP_SCHEME"


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
                    self._delegate = WebApiJarMahjongUtils(Path(webapi_jar_path))
                else:
                    raise FileNotFoundError(webapi_jar_path)
            elif bridgeType == "http":
                host = os.getenv(ENV_MAHJONG_UTILS_HTTP_HOST) or "127.0.0.1"
                port = int(os.getenv(ENV_MAHJONG_UTILS_HTTP_PORT) or "8080")
                scheme = os.getenv(ENV_MAHJONG_UTILS_HTTP_SCHEME) or "http"
                self._delegate = HttpMahjongUtils(host, port, scheme)
            elif bridgeType == "js":
                self._delegate = JsMahjongUtils()
            else:
                lib_search_path = os.getenv(ENV_MAHJONG_UTILS_LIB_SEARCH_PATH)
                if lib_search_path:
                    lib_search_path = [Path(p) for p in lib_search_path.split(":")]
                else:
                    lib_search_path = []
                self._delegate = LibMahjongUtils(lib_search_path)
        return self._delegate

    def call(self, name: str, params: dict) -> dict:
        return self.delegate.call(name, params)

    def close(self):
        return self.delegate.close()


bridge_mahjongutils = RouterMahjongUtils()
atexit.register(bridge_mahjongutils.close)
