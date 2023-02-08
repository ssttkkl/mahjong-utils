"use strict";
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
exports.__esModule = true;
exports.getAllYaku = exports.getAllExtraYakuman = exports.getAllExtraYaku = exports.getAllYakuman = exports.getAllCommonYaku = void 0;
function getAllCommonYaku() {
    return ['Tsumo',
        'Pinhu',
        'Tanyao',
        'Ipe',
        'SelfWind',
        'RoundWind',
        'Haku',
        'Hatsu',
        'Chun',
        'Sanshoku',
        'Ittsu',
        'Chanta',
        'Chitoi',
        'Toitoi',
        'Sananko',
        'Honroto',
        'Sandoko',
        'Sankantsu',
        'Shosangen',
        'Honitsu',
        'Junchan',
        'Ryanpe',
        'Chinitsu'];
}
exports.getAllCommonYaku = getAllCommonYaku;
function getAllYakuman() {
    return ['Tenhou',
        'Chihou',
        'Kokushi',
        'Suanko',
        'Daisangen',
        'Tsuiso',
        'Shousushi',
        'Lyuiso',
        'Chinroto',
        'Sukantsu',
        'Churen',
        'Daisushi',
        'SuankoTanki',
        'ChurenNineWaiting',
        'KokushiThirteenWaiting'];
}
exports.getAllYakuman = getAllYakuman;
function getAllExtraYaku() {
    return ['Richi',
        'Ippatsu',
        'Rinshan',
        'Chankan',
        'Haitei',
        'Houtei',
        'WRichi',
        'Tenhou',
        'Chihou'];
}
exports.getAllExtraYaku = getAllExtraYaku;
function getAllExtraYakuman() {
    return ['Tenhou',
        'Chihou'];
}
exports.getAllExtraYakuman = getAllExtraYakuman;
function getAllYaku() {
    return __spreadArray(__spreadArray(__spreadArray([], getAllCommonYaku(), true), getAllYakuman(), true), getAllExtraYaku(), true);
}
exports.getAllYaku = getAllYaku;
