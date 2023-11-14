//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[UnionShantenResult](index.md)

# UnionShantenResult

[common]\
@Serializable

@SerialName(value = &quot;UnionShantenResult&quot;)

data class [UnionShantenResult](index.md)(val hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt;, val shantenInfo: [CommonShanten](../-common-shanten/index.md), val regular: [RegularShantenResult](../-regular-shanten-result/index.md), val chitoi: [ChitoiShantenResult](../-chitoi-shanten-result/index.md)? = null, val kokushi: [KokushiShantenResult](../-kokushi-shanten-result/index.md)? = null) : [CommonShantenResult](../-common-shanten-result/index.md)&lt;[CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt;

## Constructors

| | |
|---|---|
| [UnionShantenResult](-union-shanten-result.md) | [common]<br>constructor(hand: [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt;, shantenInfo: [CommonShanten](../-common-shanten/index.md), regular: [RegularShantenResult](../-regular-shanten-result/index.md), chitoi: [ChitoiShantenResult](../-chitoi-shanten-result/index.md)? = null, kokushi: [KokushiShantenResult](../-kokushi-shanten-result/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [chitoi](chitoi.md) | [common]<br>@EncodeDefault<br>val [chitoi](chitoi.md): [ChitoiShantenResult](../-chitoi-shanten-result/index.md)? = null<br>标准形向听分析结果 |
| [hand](hand.md) | [common]<br>open override val [hand](hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt;<br>手牌 |
| [kokushi](kokushi.md) | [common]<br>@EncodeDefault<br>val [kokushi](kokushi.md): [KokushiShantenResult](../-kokushi-shanten-result/index.md)? = null<br>标准形向听分析结果 |
| [regular](regular.md) | [common]<br>val [regular](regular.md): [RegularShantenResult](../-regular-shanten-result/index.md)<br>标准形向听分析结果 |
| [shantenInfo](shanten-info.md) | [common]<br>open override val [shantenInfo](shanten-info.md): [CommonShanten](../-common-shanten/index.md)<br>向听信息 |
