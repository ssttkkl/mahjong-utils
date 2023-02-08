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
exports.Tile = exports.TileType = void 0;
var TileType;
(function (TileType) {
    TileType[TileType["M"] = 0] = "M";
    TileType[TileType["P"] = 1] = "P";
    TileType[TileType["S"] = 2] = "S";
    TileType[TileType["Z"] = 3] = "Z";
})(TileType = exports.TileType || (exports.TileType = {}));
var Tile = /** @class */ (function () {
    function Tile(type, num) {
        this.type = type;
        this.num = num;
    }
    Object.defineProperty(Tile.prototype, "realNum", {
        get: function () {
            if (this.type !== TileType.Z && this.num === 0) {
                return 5;
            }
            else {
                return this.num;
            }
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Tile.prototype, "code", {
        get: function () {
            return this.type * 10 + this.num;
        },
        enumerable: false,
        configurable: true
    });
    Tile.prototype.compareTo = function (other) {
        if (this.type !== other.type) {
            return this.type - other.type;
        }
        else if (this.num !== 0 && other.num !== 0) {
            return this.num - other.num;
        }
        else if (this.num === 0 && other.num === 0) {
            return 0;
        }
        else if (this.num === 0) {
            return other.num > 5 ? -1 : 1;
        }
        else {
            return other.num <= 5 ? -1 : 1;
        }
    };
    Tile.prototype.distance = function (other) {
        var a = this.num === 0 ? Tile.byTypeAndNum(this.type, 5) : this;
        var b = other.num === 0 ? Tile.byTypeAndNum(other.type, 5) : other;
        return a.type === b.type ? a.num - b.num : Infinity;
    };
    Tile.prototype.advance = function (step) {
        return Tile.byTypeAndNum(this.type, this.num + step);
    };
    Tile.prototype.toString = function () {
        return "".concat(this.num).concat(TileType[this.type]);
    };
    Tile.byTypeAndNum = function (type, num) {
        var code = type * 10 + num;
        var tile = Tile.pool[code];
        if (num < 0 || num > 9) {
            return undefined;
        }
        return tile;
    };
    Tile.byCode = function (code) {
        var type = Math.floor(code / 10);
        var num = code % 10;
        if (type < 0 || type > 3) {
            return undefined;
        }
        return Tile.byTypeAndNum(type, num);
    };
    Tile.byText = function (text) {
        if (text.length != 2) {
            return undefined;
        }
        var type;
        switch (text[1].toLowerCase()) {
            case 'm':
                type = TileType.M;
                break;
            case 'p':
                type = TileType.P;
                break;
            case 's':
                type = TileType.S;
                break;
            case 'z':
                type = TileType.Z;
                break;
            default:
                return undefined;
        }
        var num = parseInt(text[0]);
        if (isNaN(num)) {
            return undefined;
        }
        return Tile.byTypeAndNum(type, num);
    };
    Tile.parseTiles = function (code) {
        var pending = [];
        var tiles = [];
        var _loop_1 = function (c) {
            if (c === 'M' || c === 'm' || c === 'P' || c === 'p' || c === 'S' || c === 's') {
                tiles = __spreadArray(__spreadArray([], tiles, true), pending.map(function (value) { return Tile.byText("".concat(value).concat(c)); }), true);
                pending = [];
            }
            else if (c === 'Z' || c === 'z') {
                if (pending.every(function (value) { return value >= 1 && value <= 7; })) {
                    tiles = __spreadArray(__spreadArray([], tiles, true), pending.map(function (value) { return Tile.byText("".concat(value).concat(c)); }), true);
                    pending = [];
                }
                else {
                    return { value: undefined };
                }
            }
            else if (c >= '1' && c <= '9') {
                pending.push(parseInt(c));
            }
            else {
                return { value: undefined };
            }
        };
        for (var _i = 0, code_1 = code; _i < code_1.length; _i++) {
            var c = code_1[_i];
            var state_1 = _loop_1(c);
            if (typeof state_1 === "object")
                return state_1.value;
        }
        if (pending.length > 0) {
            return undefined;
        }
        return tiles;
    };
    Tile.tilesToString = function (tiles, opts) {
        if (opts === void 0) { opts = {}; }
        var tiles_;
        if (opts.sorted) {
            tiles_ = __spreadArray([], tiles, true);
            tiles_.sort(function (a, b) { return a.compareTo(b); });
        }
        else {
            tiles_ = tiles;
        }
        var result = "";
        var prev = null;
        for (var _i = 0, tiles_1 = tiles_; _i < tiles_1.length; _i++) {
            var t = tiles_1[_i];
            if (prev !== null && prev.type !== t.type) {
                result += TileType[prev.type];
            }
            result += t.num.toString();
            prev = t;
        }
        if (prev !== null) {
            result += TileType[prev.type];
        }
        return result;
    };
    Tile.encode = function (data) {
        return "".concat(data.num).concat(TileType[data.type]);
    };
    Tile.decode = function (raw) {
        var t = Tile.byText(raw);
        if (t === undefined) {
            throw new Error("invalid value: " + raw);
        }
        return t;
    };
    Tile.pool = [];
    (function () {
        for (var i = 0; i <= 9; i++) {
            Tile.pool.push(new Tile(TileType.M, i));
        }
        for (var i = 0; i <= 9; i++) {
            Tile.pool.push(new Tile(TileType.P, i));
        }
        for (var i = 0; i <= 9; i++) {
            Tile.pool.push(new Tile(TileType.S, i));
        }
        Tile.pool.push(undefined);
        for (var i = 1; i <= 7; i++) {
            Tile.pool.push(new Tile(TileType.Z, i));
        }
    })();
    return Tile;
}());
exports.Tile = Tile;
