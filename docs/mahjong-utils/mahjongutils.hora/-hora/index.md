//[mahjong-utils](../../../index.md)/[mahjongutils.hora](../index.md)/[Hora](index.md)

# Hora

[common]\
@Serializable

data class [Hora](index.md) : [HoraInfo](../-hora-info/index.md)

和牌分析结果

## Properties

| Name | Summary |
|---|---|
| [agari](../-hora-info/agari.md) | [common]<br>open override val [agari](../-hora-info/agari.md): [Tile](../../mahjongutils.models/-tile/index.md)<br>和牌张 |
| [childPoint](child-point.md) | [common]<br>@EncodeDefault<br>val [childPoint](child-point.md): [ChildPoint](../../mahjongutils.hanhu/-child-point/index.md)<br>子家（闲家）和牌点数 |
| [dora](dora.md) | [common]<br>val [dora](dora.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>宝牌数目 |
| [extraYaku](extra-yaku.md) | [common]<br>val [extraYaku](extra-yaku.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;@Serializable(with = [DefaultYakuSerializer::class](../../mahjongutils.yaku/-default-yaku-serializer/index.md))[Yaku](../../mahjongutils.yaku/-yaku/index.md)&gt;<br>额外役种 |
| [han](han.md) | [common]<br>@EncodeDefault<br>val [han](han.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>番 |
| [hasYakuman](has-yakuman.md) | [common]<br>@EncodeDefault<br>val [hasYakuman](has-yakuman.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否含役满役种 |
| [hu](hu.md) | [common]<br>@EncodeDefault<br>val [hu](hu.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [options](options.md) | [common]<br>val [options](options.md): [HoraOptions](../-hora-options/index.md)<br>和牌规则选项 |
| [parentPoint](parent-point.md) | [common]<br>@EncodeDefault<br>val [parentPoint](parent-point.md): [ParentPoint](../../mahjongutils.hanhu/-parent-point/index.md)<br>亲家（庄家）和牌点数 |
| [pattern](pattern.md) | [common]<br>val [pattern](pattern.md): [HoraHandPattern](../-hora-hand-pattern/index.md)<br>和牌形 |
| [roundWind](../-hora-info/round-wind.md) | [common]<br>open override val [roundWind](../-hora-info/round-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)?<br>场风 |
| [selfWind](../-hora-info/self-wind.md) | [common]<br>open override val [selfWind](../-hora-info/self-wind.md): [Wind](../../mahjongutils.models/-wind/index.md)?<br>自风 |
| [tsumo](../-hora-info/tsumo.md) | [common]<br>open override val [tsumo](../-hora-info/tsumo.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>是否自摸 |
| [yaku](yaku.md) | [common]<br>@EncodeDefault<br>val [yaku](yaku.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;@Serializable(with = [DefaultYakuSerializer::class](../../mahjongutils.yaku/-default-yaku-serializer/index.md))[Yaku](../../mahjongutils.yaku/-yaku/index.md)&gt;<br>役种 |
