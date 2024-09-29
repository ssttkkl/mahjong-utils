import json
from urllib.error import HTTPError
from urllib.request import Request, urlopen

from ..protocol import MahjongUtilsBridge


class HttpMahjongUtils(MahjongUtilsBridge):
    def __init__(self, host: str, port: int, scheme: str = "http"):
        self.host = host
        self.port = port
        self.scheme = scheme

    def call(self, name: str, params: dict) -> dict:
        body = json.dumps(params).encode("utf-8")
        req = Request(f'{self.scheme}://{self.host}:{self.port}/{name}', data=body, method="POST")
        try:
            resp = urlopen(req)
            result = resp.read().decode('utf-8')
            result = json.loads(result)
            return result
        except HTTPError as e:
            if e.code == 404 or e.code == 400:
                msg = e.read().decode('utf-8')
                raise ValueError(msg)
            else:
                msg = e.read().decode('utf-8')
                raise RuntimeError(msg)


__all__ = ("HttpMahjongUtils",)
