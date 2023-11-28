import sys
from pathlib import Path

_cli_path: Path

if sys.platform == 'win32':
    _cli_path = Path("mahjong-utils-cli.exe").absolute()  # windows
else:
    _cli_path = Path("mahjong-utils").absolute()  # macos/unix/linux


def mahjongutils_cli_path() -> Path:
    return _cli_path


def set_mahjongutils_cli_path(cli_path: Path):
    global _cli_path
    _cli_path = cli_path
