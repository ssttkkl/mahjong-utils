//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[ChitoiShantenResult](index.md)

# ChitoiShantenResult

[common]\
@Serializable

@SerialName(value = &quot;ChitoiShantenResult&quot;)

data class [ChitoiShantenResult](index.md)(val hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[ChitoiHandPattern](../../mahjongutils.models.hand/-chitoi-hand-pattern/index.md)&gt;, val shantenInfo: [CommonShanten](../-common-shanten/index.md)) : [CommonShantenResult](../-common-shanten-result/index.md)&lt;[ChitoiHandPattern](../../mahjongutils.models.hand/-chitoi-hand-pattern/index.md)&gt;

## Constructors

| | |
|---|---|
| [ChitoiShantenResult](-chitoi-shanten-result.md) | [common]<br>constructor(hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[ChitoiHandPattern](../../mahjongutils.models.hand/-chitoi-hand-pattern/index.md)&gt;, shantenInfo: [CommonShanten](../-common-shanten/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [hand](hand.md) | [common]<br>open override val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[ChitoiHandPattern](../../mahjongutils.models.hand/-chitoi-hand-pattern/index.md)&gt;<br>手牌 |
| [shantenInfo](shanten-info.md) | [common]<br>open override val [shantenInfo](shanten-info.md): [CommonShanten](../-common-shanten/index.md)<br>向听信息 |
