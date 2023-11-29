from pathlib import Path

_jar_path: Path = Path("mahjong-utils-webapi-all.jar").absolute()


def mahjongutils_webapi_jar_path() -> Path:
    return _jar_path


def set_mahjongutils_webapi_jar_path(jar_path: Path):
    global _jar_path
    _jar_path = jar_path
