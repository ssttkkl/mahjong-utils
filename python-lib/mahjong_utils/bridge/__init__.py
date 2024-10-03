import atexit
import os
from pathlib import Path

from .protocol import MahjongUtilsBridge

ENV_MAHJONG_UTILS_BRIDGE = "ENV_MAHJONG_UTILS_BRIDGE"
ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH = "ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH"
ENV_MAHJONG_UTILS_HTTP_HOST = "ENV_MAHJONG_UTILS_HTTP_HOST"
ENV_MAHJONG_UTILS_HTTP_PORT = "ENV_MAHJONG_UTILS_HTTP_PORT"
ENV_MAHJONG_UTILS_HTTP_SCHEME = "ENV_MAHJONG_UTILS_HTTP_SCHEME"

DEFAULT_BRIDGE_TYPE = "js"


class RouterMahjongUtils(MahjongUtilsBridge):
    def __init__(self):
        self._delegate = None

    @property
    def delegate(self) -> MahjongUtilsBridge:
        if self._delegate is None:
            bridgeType = os.getenv(ENV_MAHJONG_UTILS_BRIDGE)
            if bridgeType is None:
                bridgeType = DEFAULT_BRIDGE_TYPE

            if bridgeType == "webapi_jar":
                from .webapi_jar import WebApiJarMahjongUtils
                webapi_jar_path = os.getenv(ENV_MAHJONG_UTILS_WEBAPI_JAR_PATH)
                if Path(webapi_jar_path).exists():
                    self._delegate = WebApiJarMahjongUtils(Path(webapi_jar_path))
                else:
                    raise FileNotFoundError(webapi_jar_path)
            elif bridgeType == "http":
                from .http import HttpMahjongUtils
                host = os.getenv(ENV_MAHJONG_UTILS_HTTP_HOST) or "127.0.0.1"
                port = int(os.getenv(ENV_MAHJONG_UTILS_HTTP_PORT) or "8080")
                scheme = os.getenv(ENV_MAHJONG_UTILS_HTTP_SCHEME) or "http"
                self._delegate = HttpMahjongUtils(host, port, scheme)
            else:
                from .js import JsMahjongUtils
                self._delegate = JsMahjongUtils()
        return self._delegate

    def call(self, name: str, params: dict) -> dict:
        return self.delegate.call(name, params)

    def close(self):
        return self.delegate.close()


bridge_mahjongutils = RouterMahjongUtils()
atexit.register(bridge_mahjongutils.close)
