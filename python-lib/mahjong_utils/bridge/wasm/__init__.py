import json
import threading
from pathlib import Path
from typing import Optional, Mapping, Any

from py_mini_racer import MiniRacer

from ..protocol import MahjongUtilsBridge


class WasmMahjongUtils(MahjongUtilsBridge):
    def __init__(self) -> None:
        self._ctx = threading.local()

    @property
    def ctx(self):
        if not hasattr(self._ctx, "value"):
            js_file = Path(__file__).parent / "mahjong-utils-entry.js"
            wasm_file = Path(__file__).parent / "mahjong-utils-mahjong-utils-entry-wasm-js.wasm"

            ctx = MiniRacer()

            # 让js加载脚本认为当前是V8环境
            ctx.eval("const d8 = {};")

            # 读取wasm文件并mock一个read函数供加载脚本使用
            size = wasm_file.stat().st_size
            module_raw = ctx.eval(
                f"""
            const moduleRaw = new SharedArrayBuffer({size});
            moduleRaw
            """
            )
            with open(wasm_file, "rb") as f:
                assert f.readinto(module_raw) == size
            ctx.eval(
                """
            function read() {
                return new Uint8Array(moduleRaw);
            }
            """
            )

            # 加载js加载脚本
            js_raw = ctx.eval("const jsRaw = {}; jsRaw")

            with open(js_file, "r", encoding="utf-8") as f:
                js_raw["content"] = f.read()

            ctx.eval("eval(jsRaw['content'])")

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
        result = self.ctx.eval("MahjongUtilsEntry").get()["call"](name, params)
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


__all__ = ("WasmMahjongUtils",)
