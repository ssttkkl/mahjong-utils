//[mahjong-utils](../../../index.md)/[mahjongutils.yaku](../index.md)/[YakuSerializer](index.md)

# YakuSerializer

[common]\
class [YakuSerializer](index.md)(val yakus: [Yakus](../-yakus/index.md)) : KSerializer&lt;[Yaku](../-yaku/index.md)&gt;

## Constructors

| | |
|---|---|
| [YakuSerializer](-yaku-serializer.md) | [common]<br>constructor(yakus: [Yakus](../-yakus/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [descriptor](descriptor.md) | [common]<br>open override val [descriptor](descriptor.md): SerialDescriptor |
| [yakus](yakus.md) | [common]<br>val [yakus](yakus.md): [Yakus](../-yakus/index.md) |

## Functions

| Name | Summary |
|---|---|
| [deserialize](deserialize.md) | [common]<br>open override fun [deserialize](deserialize.md)(decoder: Decoder): [Yaku](../-yaku/index.md) |
| [serialize](serialize.md) | [common]<br>open override fun [serialize](serialize.md)(encoder: Encoder, value: [Yaku](../-yaku/index.md)) |
