import json
import sys
import threading
from collections.abc import Sequence
from importlib import resources
from pathlib import Path
from typing import Optional, Mapping, Any

try:
    import cffi
except ImportError:
    raise RuntimeError("Please \"pip install cffi\"")

from ..protocol import MahjongUtilsBridge


class LibMahjongUtils(MahjongUtilsBridge):
    def __init__(self, lib_search_path: Optional[Sequence[str]] = None) -> None:
        self.ffi = cffi.FFI()
        self.lib = None

        with resources.open_text(__name__, "libmahjongutils_api.i") as f:
            h = f.read()
            self.ffi.cdef(h)

        if sys.platform == 'win32':
            libname = "libmahjongutils.dll"  # windows
        elif sys.platform == 'darwin':
            libname = "libmahjongutils.dylib"  # macOS
        else:
            libname = "libmahjongutils.so"  # unix/linux

        if lib_search_path:
            for search_path in lib_search_path:
                lib_path = Path(search_path) / libname
                if lib_path.exists():
                    self.lib = self.ffi.dlopen(str(lib_path))
                    break

        if self.lib is None:
            self.lib = self.ffi.dlopen(libname)

        self._lib_sy = threading.local()

    @property
    def lib_sy(self):
        if not hasattr(self._lib_sy, "value"):
            self._lib_sy.value = self.lib.libmahjongutils_symbols()
        return self._lib_sy.value

    def call(self, name: str, params: dict,
             params_dumps_kwargs: Optional[Mapping[str, Any]] = None,
             result_loads_kwargs: Optional[Mapping[str, Any]] = None) -> dict:
        if params_dumps_kwargs is None:
            params_dumps_kwargs = {}
        if result_loads_kwargs is None:
            result_loads_kwargs = {}

        params = json.dumps(params, **params_dumps_kwargs)

        result = self.lib_sy.kotlin.root.mahjongutils.entry.call(
            self.ffi.new("char[]", name.encode()),
            self.ffi.new("char[]", params.encode()))
        result = self.ffi.string(result)

        result = json.loads(result, **result_loads_kwargs)

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
