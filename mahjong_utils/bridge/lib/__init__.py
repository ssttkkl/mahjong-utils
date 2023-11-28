import json
import sys
import threading
from importlib import resources

import cffi

from ..protocol import MahjongUtilsBridge


class LibMahjongUtils(MahjongUtilsBridge):
    def __init__(self) -> None:
        self.ffi = cffi.FFI()

        with resources.open_text(__name__, "libmahjongutils_api.i") as f:
            h = f.read()
            self.ffi.cdef(h)

        if sys.platform == 'win32':
            libname = "libmahjongutils.dll"  # windows
        elif sys.platform == 'darwin':
            libname = "libmahjongutils.dylib"  # macOS
        else:
            libname = "libmahjongutils.so"  # unix/linux

        with resources.path(__name__, libname) as libpath:
            self.lib = self.ffi.dlopen(str(libpath))

        self._lib_sy = threading.local()

    @property
    def lib_sy(self):
        if not hasattr(self._lib_sy, "value"):
            self._lib_sy.value = self.lib.libmahjongutils_symbols()
        return self._lib_sy.value

    def call(self, name: str, params: dict) -> dict:
        params = json.dumps(params)

        result = self.lib_sy.kotlin.root.mahjongutils.entry.call(
            self.ffi.new("char[]", name.encode()),
            self.ffi.new("char[]", params.encode()))
        result = self.ffi.string(result)

        result = json.loads(result)

        if result['code'] == 200:
            return result['data']
        elif result['code'] == 404:
            raise ValueError(result['msg'])
        elif result['code'] == 400:
            raise ValueError(result['msg'])
        else:
            raise RuntimeError(result['msg'])

    def close(self):
        self.ffi.dlclose(self.lib)


__all__ = ("LibMahjongUtils",)
