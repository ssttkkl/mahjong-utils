//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[KokushiShantenResult](index.md)

# KokushiShantenResult

[common]\
@Serializable

data class [KokushiShantenResult](index.md)(val hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[KokushiHandPattern](../../mahjongutils.models.hand/-kokushi-hand-pattern/index.md)&gt;, val shantenInfo: [CommonShanten](../-common-shanten/index.md)) : [CommonShantenResult](../-common-shanten-result/index.md)&lt;[KokushiHandPattern](../../mahjongutils.models.hand/-kokushi-hand-pattern/index.md)&gt;

## Constructors

| | |
|---|---|
| [KokushiShantenResult](-kokushi-shanten-result.md) | [common]<br>constructor(hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[KokushiHandPattern](../../mahjongutils.models.hand/-kokushi-hand-pattern/index.md)&gt;, shantenInfo: [CommonShanten](../-common-shanten/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [hand](hand.md) | [common]<br>open override val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[KokushiHandPattern](../../mahjongutils.models.hand/-kokushi-hand-pattern/index.md)&gt;<br>手牌 |
| [shantenInfo](shanten-info.md) | [common]<br>open override val [shantenInfo](shanten-info.md): [CommonShanten](../-common-shanten/index.md)<br>向听信息 |
