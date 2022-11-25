from asyncio import Protocol
from typing import Any

from typing_extensions import Self, runtime_checkable


@runtime_checkable
class Encodable(Protocol):
    def __encode__(self) -> Any:
        ...


@runtime_checkable
class Decodable(Protocol):
    @classmethod
    def __decode__(cls, data: Any) -> Self:
        ...
