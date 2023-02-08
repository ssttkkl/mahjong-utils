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
exports.buildHora = void 0;
var entry_1 = require("../entry");
var models_1 = require("../models");
var coder_1 = require("../shanten/coder");
var coder_2 = require("./coder");
__exportStar(require("./models"), exports);
function buildHora(opts) {
    var _a, _b;
    var result = entry_1.ENTRY.call("hora", __assign(__assign({}, opts), { tiles: (_a = opts.tiles) === null || _a === void 0 ? void 0 : _a.map(models_1.Tile.encode), furo: (_b = opts.furo) === null || _b === void 0 ? void 0 : _b.map(models_1.Furo.encode), shantenResult: opts.shantenResult ? (0, coder_1.encodeShantenWithGot)(opts.shantenResult) : undefined, agari: models_1.Tile.encode(opts.agari) }));
    return (0, coder_2.decodeHora)((0, entry_1.handleResult)(result));
}
exports.buildHora = buildHora;
