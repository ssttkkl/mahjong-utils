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
exports.decodeHand = exports.decodeHandPattern = void 0;
var Tile_1 = require("../Tile");
var Furo_1 = require("../Furo");
var Mentsu_1 = require("../Mentsu");
var Tatsu_1 = require("../Tatsu");
var decodeRegularHandPattern = function (raw) {
    return {
        k: raw.k,
        jyantou: raw.jyantou !== null ? Tile_1.Tile.decode(raw.jyantou) : null,
        menzenMentsu: raw.menzenMentsu.map(Mentsu_1.Mentsu.decode),
        furo: raw.furo.map(Furo_1.Furo.decode),
        tatsu: raw.tatsu.map(Tatsu_1.Tatsu.decode)
    };
};
var decodeChitoiHandPattern = function (raw) {
    return {
        pairs: raw.pairs.map(Tile_1.Tile.decode),
        remaining: raw.remaining.map(Tile_1.Tile.decode)
    };
};
var decodeKokushiHandPattern = function (raw) {
    return {
        yaochu: raw.yaochu.map(Tile_1.Tile.decode),
        repeated: raw.repeated !== null ? Tile_1.Tile.decode(raw.repeated) : null,
        remaining: raw.remaining.map(Tile_1.Tile.decode)
    };
};
var decodeHandPattern = function (raw) {
    switch (raw.type) {
        case 'RegularHandPattern':
            return __assign({ type: 'RegularHandPattern' }, decodeRegularHandPattern(raw));
        case 'ChitoiHandPattern':
            return __assign({ type: 'ChitoiHandPattern' }, decodeChitoiHandPattern(raw));
        case 'KokushiHandPattern':
            return __assign({ type: 'KokushiHandPattern' }, decodeKokushiHandPattern(raw));
    }
};
exports.decodeHandPattern = decodeHandPattern;
var decodeHand = function (raw) {
    return {
        tiles: raw.tiles.map(Tile_1.Tile.decode),
        furo: raw.furo.map(Furo_1.Furo.decode),
        patterns: raw.patterns.map(exports.decodeHandPattern)
    };
};
exports.decodeHand = decodeHand;
