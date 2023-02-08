"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
exports.__esModule = true;
exports.decodeHora = exports.decodeHoraHandPattern = void 0;
var models_1 = require("../models");
var decodeRegularHoraHandPattern = function (raw) {
    return {
        agari: models_1.Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: raw.hu,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        k: raw.pattern.k,
        jyantou: models_1.Tile.decode(raw.pattern.jyantou),
        menzenMentsu: raw.pattern.menzenMentsu.map(models_1.Mentsu.decode),
        furo: raw.pattern.furo.map(models_1.Furo.decode),
        tatsu: raw.pattern.tatsu.map(models_1.Tatsu.decode),
        remaining: raw.pattern.remaining.map(models_1.Tile.decode)
    };
};
var decodeChitoiHoraHandPattern = function (raw) {
    return {
        agari: models_1.Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: 25,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        pairs: raw.pairs.map(models_1.Tile.decode)
    };
};
var decodeKokushiHoraHandPattern = function (raw) {
    return {
        agari: models_1.Tile.decode(raw.agari),
        tsumo: raw.tsumo,
        hu: 20,
        selfWind: raw.selfWind,
        roundWind: raw.roundWind,
        repeated: models_1.Tile.decode(raw.repeated)
    };
};
var decodeHoraHandPattern = function (raw) {
    switch (raw.type) {
        case 'RegularHoraHandPattern':
            return __assign({ type: 'RegularHoraHandPattern' }, decodeRegularHoraHandPattern(raw));
        case 'ChitoiHoraHandPattern':
            return __assign({ type: 'ChitoiHoraHandPattern' }, decodeChitoiHoraHandPattern(raw));
        case 'KokushiHoraHandPattern':
            return __assign({ type: 'KokushiHoraHandPattern' }, decodeKokushiHoraHandPattern(raw));
    }
};
exports.decodeHoraHandPattern = decodeHoraHandPattern;
var decodeHora = function (raw) {
    return __assign(__assign({}, raw), { pattern: (0, exports.decodeHoraHandPattern)(raw.pattern) });
};
exports.decodeHora = decodeHora;
