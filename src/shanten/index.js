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
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __exportStar = (this && this.__exportStar) || function(m, exports) {
    for (var p in m) if (p !== "default" && !Object.prototype.hasOwnProperty.call(exports, p)) __createBinding(exports, m, p);
};
exports.__esModule = true;
exports.furoChanceShanten = exports.kokushiShanten = exports.chitoiShanten = exports.regularShanten = exports.shanten = void 0;
var entry_1 = require("../entry");
var models_1 = require("../models");
var coder_1 = require("./coder");
__exportStar(require("./models"), exports);
function shanten(tiles, opts) {
    var _a;
    if (opts === void 0) { opts = {}; }
    var result = entry_1.ENTRY.call("shanten", __assign(__assign({}, opts), { tiles: tiles.map(function (it) { return models_1.Tile.encode(it); }), furo: (_a = opts.furo) === null || _a === void 0 ? void 0 : _a.map(function (it) { return models_1.Furo.encode(it); }) }));
    return (0, coder_1.decodeShantenResult)((0, entry_1.handleResult)(result));
}
exports.shanten = shanten;
function regularShanten(tiles, opts) {
    var _a;
    if (opts === void 0) { opts = {}; }
    var result = entry_1.ENTRY.call("regularShanten", __assign(__assign({}, opts), { tiles: tiles.map(function (it) { return models_1.Tile.encode(it); }), furo: (_a = opts.furo) === null || _a === void 0 ? void 0 : _a.map(function (it) { return models_1.Furo.encode(it); }) }));
    return (0, coder_1.decodeShantenResult)((0, entry_1.handleResult)(result));
}
exports.regularShanten = regularShanten;
function chitoiShanten(tiles, opts) {
    var _a;
    if (opts === void 0) { opts = {}; }
    var result = entry_1.ENTRY.call("chitoiShanten", __assign(__assign({}, opts), { tiles: tiles.map(function (it) { return models_1.Tile.encode(it); }), furo: (_a = opts.furo) === null || _a === void 0 ? void 0 : _a.map(function (it) { return models_1.Furo.encode(it); }) }));
    return (0, coder_1.decodeShantenResult)((0, entry_1.handleResult)(result));
}
exports.chitoiShanten = chitoiShanten;
function kokushiShanten(tiles, opts) {
    var _a;
    if (opts === void 0) { opts = {}; }
    var result = entry_1.ENTRY.call("kokushiShanten", __assign(__assign({}, opts), { tiles: tiles.map(function (it) { return models_1.Tile.encode(it); }), furo: (_a = opts.furo) === null || _a === void 0 ? void 0 : _a.map(function (it) { return models_1.Furo.encode(it); }) }));
    return (0, coder_1.decodeShantenResult)((0, entry_1.handleResult)(result));
}
exports.kokushiShanten = kokushiShanten;
function furoChanceShanten(tiles, chanceTile, opts) {
    if (opts === void 0) { opts = {}; }
    var result = entry_1.ENTRY.call("furoChanceShanten", __assign(__assign({}, opts), { tiles: tiles.map(function (it) { return models_1.Tile.encode(it); }), chanceTile: models_1.Tile.encode(chanceTile) }));
    return (0, coder_1.decodeShantenResult)((0, entry_1.handleResult)(result));
}
exports.furoChanceShanten = furoChanceShanten;
