import json
import sys
from importlib import resources

import cffi


class LibMahjongUtils:
    def __init__(self) -> None:
        self.ffi = cffi.FFI()

        with resources.open_text(__name__, "libmahjongutils_api_processed.h") as f:
            h = f.read()
            self.ffi.cdef(h)

        if sys.platform == 'win32':
            libname = "libmahjongutils.dll"  # windows
        elif sys.platform == 'darwin':
            libname = ""  # macOS
        else:
            libname = "libmahjongutils.so"  # unix/linux

        with resources.path(__name__, libname) as libpath:
            self.lib = self.ffi.dlopen(str(libpath))
        self.lib_sy = self.lib.libmahjongutils_symbols()

    def call(self, name: str, params: dict) -> dict:
        params = json.dumps(params)

        entry = self.lib_sy.kotlin.root.mahjongutils.get_ENTRY()
        result = self.lib_sy.kotlin.root.mahjongutils.Entry.call(
            entry,
            self.ffi.new("char[]", name.encode()),
            self.ffi.new("char[]", params.encode()))
        result = self.ffi.string(result)

        result = json.loads(result)

        if result['code'] == 200:
            return result['data']
        elif result.code == 404:
            raise ValueError(result['msg'])
        elif result.code == 400:
            raise ValueError(result['msg'])
        else:
            raise RuntimeError(result['msg'])

    def close(self):
        self.ffi.dlclose(self.lib)


libmahjongutils = LibMahjongUtils()

__all__ = ("LibMahjongUtils", "libmahjongutils")
