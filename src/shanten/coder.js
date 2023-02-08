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
exports.decodeShantenResult = exports.encodeShantenInfo = exports.decodeShantenInfo = exports.decodeShantenWithFuroChance = exports.encodeShantenWithGot = exports.decodeShantenWithGot = exports.encodeShantenWithoutGot = exports.decodeShantenWithoutGot = void 0;
var models_1 = require("../models");
var obj_map_1 = require("../utils/obj-map");
var coder_1 = require("../models/hand/coder");
var decodeShantenWithoutGot = function (raw) {
    var _a, _b, _c;
    return {
        type: 'ShantenWithoutGot',
        shantenNum: raw.shantenNum,
        advance: raw.advance.map(models_1.Tile.decode),
        advanceNum: (_a = raw.advanceNum) !== null && _a !== void 0 ? _a : undefined,
        goodShapeAdvance: (_b = raw.goodShapeAdvance) === null || _b === void 0 ? void 0 : _b.map(models_1.Tile.decode),
        goodShapeAdvanceNum: (_c = raw.goodShapeAdvanceNum) !== null && _c !== void 0 ? _c : undefined
    };
};
exports.decodeShantenWithoutGot = decodeShantenWithoutGot;
var encodeShantenWithoutGot = function (data) {
    var _a, _b, _c, _d;
    return {
        shantenNum: data.shantenNum,
        advance: data.advance.map(models_1.Tile.encode),
        advanceNum: (_a = data.advanceNum) !== null && _a !== void 0 ? _a : null,
        goodShapeAdvance: (_c = (_b = data.goodShapeAdvance) === null || _b === void 0 ? void 0 : _b.map(models_1.Tile.encode)) !== null && _c !== void 0 ? _c : null,
        goodShapeAdvanceNum: (_d = data.goodShapeAdvanceNum) !== null && _d !== void 0 ? _d : null
    };
};
exports.encodeShantenWithoutGot = encodeShantenWithoutGot;
var decodeShantenWithGot = function (raw) {
    return {
        type: 'ShantenWithGot',
        shantenNum: raw.shantenNum,
        discardToAdvance: (0, obj_map_1.objToMap)(raw.discardToAdvance, {
            keyMapper: models_1.Tile.decode,
            valueMapper: exports.decodeShantenWithoutGot
        }),
        ankanToAdvance: (0, obj_map_1.objToMap)(raw.ankanToAdvance, {
            keyMapper: models_1.Tile.decode,
            valueMapper: exports.decodeShantenWithoutGot
        })
    };
};
exports.decodeShantenWithGot = decodeShantenWithGot;
var encodeShantenWithGot = function (data) {
    return {
        shantenNum: data.shantenNum,
        discardToAdvance: (0, obj_map_1.mapToObj)(data.discardToAdvance, {
            keyMapper: models_1.Tile.encode,
            valueMapper: exports.encodeShantenWithoutGot
        }),
        ankanToAdvance: (0, obj_map_1.mapToObj)(data.ankanToAdvance, {
            keyMapper: models_1.Tile.encode,
            valueMapper: exports.encodeShantenWithoutGot
        })
    };
};
exports.encodeShantenWithGot = encodeShantenWithGot;
var decodeShantenWithFuroChance = function (raw) {
    return {
        type: 'ShantenWithFuroChance',
        shantenNum: raw.shantenNum,
        pass: raw.pass !== null ? (0, exports.decodeShantenWithoutGot)(raw.pass) : undefined,
        chi: Object.entries(raw.chi).map(function (_a) {
            var key = _a[0], value = _a[1];
            return [models_1.Tatsu.decode(key), (0, exports.decodeShantenWithGot)(value)];
        }),
        pon: raw.pon !== null ? (0, exports.decodeShantenWithGot)(raw.pon) : undefined,
        minkan: raw.minkan !== null ? (0, exports.decodeShantenWithoutGot)(raw.minkan) : undefined
    };
};
exports.decodeShantenWithFuroChance = decodeShantenWithFuroChance;
var decodeShantenInfo = function (raw) {
    switch (raw.type) {
        case 'ShantenWithGot':
            return (0, exports.decodeShantenWithGot)(raw);
        case 'ShantenWithoutGot':
            return (0, exports.decodeShantenWithoutGot)(raw);
        case 'ShantenWithFuroChance':
            return (0, exports.decodeShantenWithFuroChance)(raw);
    }
};
exports.decodeShantenInfo = decodeShantenInfo;
var encodeShantenInfo = function (data) {
    switch (data.type) {
        case 'ShantenWithGot':
            return __assign(__assign({}, (0, exports.encodeShantenWithGot)(data)), { type: 'ShantenWithGot' });
        case 'ShantenWithoutGot':
            return __assign(__assign({}, (0, exports.encodeShantenWithoutGot)(data)), { type: 'ShantenWithoutGot' });
        default:
            throw new Error("invalid type: " + data.type);
    }
};
exports.encodeShantenInfo = encodeShantenInfo;
var decodeShantenResult = function (raw) {
    return {
        type: raw.type,
        hand: (0, coder_1.decodeHand)(raw.hand),
        shantenInfo: (0, exports.decodeShantenInfo)(raw.shantenInfo),
        regular: raw.regular ? (0, exports.decodeShantenResult)(raw.regular) : undefined,
        chitoi: raw.chitoi ? (0, exports.decodeShantenResult)(raw.chitoi) : undefined,
        kokushi: raw.kokushi ? (0, exports.decodeShantenResult)(raw.kokushi) : undefined
    };
};
exports.decodeShantenResult = decodeShantenResult;
