"use strict";
exports.__esModule = true;
exports.Tatsu = exports.TatsuType = void 0;
var Tile_1 = require("./Tile");
var TatsuType;
(function (TatsuType) {
    TatsuType["Ryanmen"] = "Ryanmen";
    TatsuType["Penchan"] = "Penchan";
    TatsuType["Kanchan"] = "Kanchan";
    TatsuType["Toitsu"] = "Toitsu";
})(TatsuType = exports.TatsuType || (exports.TatsuType = {}));
var Tatsu = /** @class */ (function () {
    function Tatsu(type, first) {
        this.type = type;
        this.first = first;
    }
    Object.defineProperty(Tatsu.prototype, "second", {
        get: function () {
            switch (this.type) {
                case TatsuType.Ryanmen:
                case TatsuType.Penchan:
                    return this.first.advance(1);
                case TatsuType.Toitsu:
                    return this.first;
                case TatsuType.Kanchan:
                    return this.first.advance(2);
            }
        },
        enumerable: false,
        configurable: true
    });
    Tatsu.prototype.toString = function () {
        return "".concat(this.first.num).concat(this.second.num).concat(Tile_1.TileType[this.first.type]);
    };
    Tatsu.parse = function (tiles) {
        if (typeof tiles === 'string') {
            var tiles_ = Tile_1.Tile.parseTiles(tiles);
            if (tiles_ === undefined) {
                return undefined;
            }
            return Tatsu.parse(tiles_);
        }
        else {
            if (tiles.length !== 2) {
                return undefined;
            }
            tiles.sort(function (a, b) { return a.compareTo(b); });
            switch (tiles[1].distance(tiles[0])) {
                case 0:
                    return new Tatsu(TatsuType.Toitsu, tiles[0]);
                case 1:
                    if (tiles[0].num === 1 || tiles[1].num === 9) {
                        return new Tatsu(TatsuType.Penchan, tiles[0]);
                    }
                    else {
                        return new Tatsu(TatsuType.Ryanmen, tiles[0]);
                    }
                case 2:
                    return new Tatsu(TatsuType.Kanchan, tiles[0]);
                default:
                    return undefined;
            }
        }
    };
    Tatsu.encode = function (data) {
        return data.toString();
    };
    Tatsu.decode = function (raw) {
        var t = Tatsu.parse(raw);
        if (t === undefined) {
            throw new Error("invalid value: " + raw);
        }
        return t;
    };
    return Tatsu;
}());
exports.Tatsu = Tatsu;
