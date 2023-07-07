//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ShantenResult](index.md)

# ShantenResult

@Serializable

interface [ShantenResult](index.md)&lt;out [S](index.md) : [Shanten](../-shanten/index.md), out [P](index.md) : [CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt;

#### Inheritors

| |
|---|
| [CommonShantenResult](../-common-shanten-result/index.md) |
| [FuroChanceShantenResult](../-furo-chance-shanten-result/index.md) |

## Properties

| Name | Summary |
|---|---|
| [hand](hand.md) | [common]<br>abstract val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[P](index.md)&gt;<br>手牌 |
| [shantenInfo](shanten-info.md) | [common]<br>abstract val [shantenInfo](shanten-info.md): [S](index.md)<br>向听信息 |
