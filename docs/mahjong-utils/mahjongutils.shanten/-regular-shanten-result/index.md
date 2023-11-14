//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[RegularShantenResult](index.md)

# RegularShantenResult

[common]\
@Serializable

@SerialName(value = &quot;RegularShantenResult&quot;)

data class [RegularShantenResult](index.md)(val hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;, val shantenInfo: [CommonShanten](../-common-shanten/index.md)) : [CommonShantenResult](../-common-shanten-result/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;

## Constructors

| | |
|---|---|
| [RegularShantenResult](-regular-shanten-result.md) | [common]<br>constructor(hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;, shantenInfo: [CommonShanten](../-common-shanten/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [hand](hand.md) | [common]<br>open override val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;<br>手牌 |
| [shantenInfo](shanten-info.md) | [common]<br>open override val [shantenInfo](shanten-info.md): [CommonShanten](../-common-shanten/index.md)<br>向听信息 |
