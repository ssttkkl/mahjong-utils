import json
import sys
import threading
from pathlib import Path
from subprocess import Popen, PIPE, DEVNULL
from typing import Optional

from .path import mahjongutils_cli_path
from ..protocol import MahjongUtilsBridge


class CliMahjongUtils(MahjongUtilsBridge):
    def __init__(self):
        self._process = threading.local()

    def _new_process(self):
        prev: Optional[Popen] = getattr(self._process, "value", None)
        if prev is not None:
            prev.terminate()

        self._process.value = Popen(mahjongutils_cli_path(), stdin=PIPE, stdout=PIPE, stderr=DEVNULL, encoding="utf-8")

    @property
    def process(self) -> Popen:
        if not hasattr(self._process, "value"):
            self._new_process()

        return self._process.value

    def call(self, name: str, params: dict) -> dict:
        payload = json.dumps({"method": name, "params": params})

        self.process.stdin.write(payload)
        self.process.stdin.write("\n")
        self.process.stdin.flush()

        result = self.process.stdout.readline()
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
        current: Optional[Popen] = getattr(self._process, "value", None)
        current.terminate()
