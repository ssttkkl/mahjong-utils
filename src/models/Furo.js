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
exports.Furo = exports.FuroType = void 0;
var Tile_1 = require("./Tile");
var FuroType;
(function (FuroType) {
    FuroType["Chi"] = "Chi";
    FuroType["Pon"] = "Pon";
    FuroType["Minkan"] = "Minkan";
    FuroType["Ankan"] = "Ankan";
})(FuroType = exports.FuroType || (exports.FuroType = {}));
var Furo = /** @class */ (function () {
    function Furo(type, tile) {
        this.type = type;
        this.tile = tile;
    }
    Furo.parse = function (tiles, ankan) {
        if (typeof tiles === 'string') {
            var ankan_ = ankan;
            var tiles_ = void 0;
            if (tiles.length === 5 && tiles[0] == tiles[3] && tiles[0] == '0' && tiles[1] == tiles[2]) {
                ankan_ = true;
                tiles_ = Tile_1.Tile.parseTiles("".concat(tiles[1]).concat(tiles[1]).concat(tiles[1]).concat(tiles[1]).concat(tiles[4]));
            }
            else {
                tiles_ = Tile_1.Tile.parseTiles(tiles);
            }
            if (tiles_ === undefined) {
                return undefined;
            }
            return Furo.parse(tiles_, ankan_);
        }
        else {
            if (tiles.length == 3) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2]) {
                    return new Furo(FuroType.Pon, tiles[0]);
                }
                else {
                    if (tiles.some(function (it) { return it.type === Tile_1.TileType.Z; })) {
                        return undefined;
                    }
                    var tiles_ = __spreadArray([], tiles, true);
                    tiles_.sort(function (a, b) { return a.compareTo(b); });
                    if (tiles[1].distance(tiles[0]) === 1 && tiles[2].distance(tiles[1]) === 1) {
                        return new Furo(FuroType.Chi, tiles[0]);
                    }
                    else {
                        return undefined;
                    }
                }
            }
            else if (tiles.length == 4) {
                if (tiles[0] == tiles[1] && tiles[1] == tiles[2] && tiles[2] == tiles[3]) {
                    return ankan ? new Furo(FuroType.Ankan, tiles[0]) : new Furo(FuroType.Minkan, tiles[0]);
                }
            }
            return undefined;
        }
    };
    Object.defineProperty(Furo.prototype, "tiles", {
        get: function () {
            switch (this.type) {
                case FuroType.Chi:
                    return [this.tile, this.tile.advance(1), this.tile.advance(2)];
                case FuroType.Pon:
                    return [this.tile, this.tile, this.tile];
                case FuroType.Minkan:
                case FuroType.Ankan:
                    return [this.tile, this.tile, this.tile, this.tile];
            }
        },
        enumerable: false,
        configurable: true
    });
    Furo.prototype.toString = function () {
        switch (this.type) {
            case FuroType.Chi:
                return "".concat(this.tile.num).concat(this.tile.num + 1).concat(this.tile.num + 2).concat(FuroType[this.type]).toLowerCase();
            case FuroType.Pon:
                return "".concat(this.tile.num).concat(this.tile.num).concat(this.tile.num).concat(FuroType[this.type]).toLowerCase();
            case FuroType.Minkan:
                return "".concat(this.tile.num).concat(this.tile.num).concat(this.tile.num).concat(this.tile.num).concat(FuroType[this.type]).toLowerCase();
            case FuroType.Ankan:
                return "0".concat(this.tile.num).concat(this.tile.num, "0").concat(FuroType[this.type]).toLowerCase();
        }
    };
    Furo.encode = function (data) {
        var raw = { type: data.type, tile: Tile_1.Tile.encode(data.tile) };
        if (data.type === FuroType.Minkan) {
            raw.type = "Kan";
            raw.ankan = false;
        }
        else if (data.type === FuroType.Ankan) {
            raw.type = "Kan";
            raw.ankan = true;
        }
        return raw;
    };
    Furo.decode = function (raw) {
        var type;
        if (raw.type === "Kan") {
            type = raw.ankan ? FuroType.Ankan : FuroType.Minkan;
        }
        else {
            type = raw.type;
        }
        return new Furo(type, Tile_1.Tile.decode(raw.tile));
    };
    return Furo;
}());
exports.Furo = Furo;
