//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[FuroChanceShantenResult](index.md)

# FuroChanceShantenResult

[common]\
@Serializable

@SerialName(value = &quot;FuroChanceShantenResult&quot;)

data class [FuroChanceShantenResult](index.md)(val hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;, val shantenInfo: [ShantenWithFuroChance](../-shanten-with-furo-chance/index.md)) : [ShantenResult](../-shanten-result/index.md)&lt;[ShantenWithFuroChance](../-shanten-with-furo-chance/index.md), [RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;

## Constructors

| | |
|---|---|
| [FuroChanceShantenResult](-furo-chance-shanten-result.md) | [common]<br>constructor(hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;, shantenInfo: [ShantenWithFuroChance](../-shanten-with-furo-chance/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [hand](hand.md) | [common]<br>open override val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[RegularHandPattern](../../mahjongutils.models.hand/-regular-hand-pattern/index.md)&gt;<br>手牌 |
| [shantenInfo](shanten-info.md) | [common]<br>open override val [shantenInfo](shanten-info.md): [ShantenWithFuroChance](../-shanten-with-furo-chance/index.md)<br>向听信息 |
