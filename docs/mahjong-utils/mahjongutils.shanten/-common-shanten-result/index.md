//[mahjong-utils](../../../index.md)/[mahjongutils.shanten](../index.md)/[CommonShantenResult](index.md)

# CommonShantenResult

@Serializable

sealed interface [CommonShantenResult](index.md)&lt;out [P](index.md) : [CommonHandPattern](../../mahjongutils.models.hand/-common-hand-pattern/index.md)&gt; : [ShantenResult](../-shanten-result/index.md)&lt;[CommonShanten](../-common-shanten/index.md), [P](index.md)&gt; 

#### Inheritors

| |
|---|
| [RegularShantenResult](../-regular-shanten-result/index.md) |
| [ChitoiShantenResult](../-chitoi-shanten-result/index.md) |
| [KokushiShantenResult](../-kokushi-shanten-result/index.md) |
| [UnionShantenResult](../-union-shanten-result/index.md) |

## Properties

| Name | Summary |
|---|---|
| [hand](../-shanten-result/hand.md) | [common]<br>abstract val [hand](../-shanten-result/hand.md): [Hand](../../mahjongutils.models.hand/-hand/index.md)&lt;[P](index.md)&gt;<br>手牌 |
| [shantenInfo](../-shanten-result/shanten-info.md) | [common]<br>abstract val [shantenInfo](../-shanten-result/shanten-info.md): [CommonShanten](../-common-shanten/index.md)<br>向听信息 |
