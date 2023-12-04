import json
import os
import socket
import sys
from pathlib import Path
from random import randint
from shutil import which
from subprocess import Popen, DEVNULL
from threading import Lock
from time import sleep
from typing import Optional
from urllib.error import HTTPError
from urllib.request import Request, urlopen

from ..protocol import MahjongUtilsBridge
from .path import mahjongutils_webapi_jar_path


def _java_executable() -> Path:
    java_home = os.getenv("JAVA_HOME")
    if java_home is not None:
        java_home = Path(os.getenv("JAVA_HOME")).absolute()
        if sys.platform == 'win32':
            java_executable = java_home / "bin" / "java.exe"  # windows
        else:
            java_executable = java_home / "bin" / "java"  # macos/unix/linux

        if not java_executable.exists():
            raise RuntimeError("Environment variable JAVA_HOME was set to an invalid path. ")
        return java_executable
    else:
        java_executable = which("java")
        if java_executable is not None:
            return Path(java_executable)
        else:
            raise RuntimeError("Cannot find java executable in your PATH environment variable. ")

def _is_port_occupied(port: int):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    code = s.connect_ex(("127.0.0.1", port))
    if code == 0:
        s.close()
        return True
    else:
        return False


class WebApiJarMahjongUtils(MahjongUtilsBridge):
    _process: Optional[Popen]
    _port: Optional[int]

    def __init__(self):
        self._process = None
        self._port = None
        self._init_lock = Lock()
        self.verbose = os.getenv("VERBOSE")

        if self.verbose is None:
            self.verbose = False
        else:
            self.verbose = self.verbose.lower() == "true"

        self._start_process()

    def _choose_port(self) -> int:
        retry = 10
        while retry > 0:
            if self._port is None or _is_port_occupied(self._port):
                self._port = randint(10000, 65535)
                retry -= 1
            else:
                return self._port
        raise RuntimeError(f"Failed to choose a port for some reason. ")

    def _start_process(self):
        if self._process is None or self._process.poll() is not None:
            with self._init_lock:
                if self._process is None or self._process.poll() is not None:
                    port = self._choose_port()
                    self._process = Popen([_java_executable(), "-jar", mahjongutils_webapi_jar_path()],
                                          stdout=sys.stdout if self.verbose else DEVNULL,
                                          stderr=sys.stderr,
                                          env={"PORT": str(port)})

                    handshake_retry = 10
                    while self._process.poll() is None and handshake_retry > 0:
                        if _is_port_occupied(port):
                            return
                        sleep(0.2)
                        handshake_retry -= 1

                    self._process.kill()
                    self._process = None
                    raise RuntimeError(f"Failed to connect with local server at port {port}")

    def call(self, name: str, params: dict) -> dict:
        body = json.dumps(params).encode("utf-8")
        req = Request(f'http://127.0.0.1:{self._port}/{name}', data=body, method="POST")
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

    def close(self):
        current: Optional[Popen] = getattr(self._process, "value", None)
        current.terminate()
