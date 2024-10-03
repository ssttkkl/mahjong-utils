import json
import threading
from pathlib import Path
from typing import Optional, Mapping, Any

try:
    from py_mini_racer import MiniRacer
except ImportError:
    raise RuntimeError("To use mahjong-utils JS Bridge, please \"pip install mini_racer\" first")

from ..protocol import MahjongUtilsBridge


class JsMahjongUtils(MahjongUtilsBridge):
    def __init__(self) -> None:
        self._ctx = threading.local()

    @property
    def ctx(self):
        if not hasattr(self._ctx, "value"):
            ctx = MiniRacer()
            with open(Path(__file__).parent / "mahjong-utils-entry.js", "r", encoding="utf-8") as f:
                script = f.read()
            ctx.eval(script)
            ctx.eval("""
            function callByJson(name, paramsJson) {
                var params = JSON.parse(paramsJson);
                var result = MahjongUtilsEntry.call(name, params);
                return JSON.stringify(result);
            }
            """)
            self._ctx.value = ctx
        return self._ctx.value

    def call(self, name: str, params: dict,
             params_dumps_kwargs: Optional[Mapping[str, Any]] = None,
             result_loads_kwargs: Optional[Mapping[str, Any]] = None) -> dict:
        if params_dumps_kwargs is None:
            params_dumps_kwargs = {}
        if result_loads_kwargs is None:
            result_loads_kwargs = {}

        params = json.dumps(params, **params_dumps_kwargs)
        result = self.ctx.call("callByJson", name, params)
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
        self.ctx.close()


__all__ = ("JsMahjongUtils",)
