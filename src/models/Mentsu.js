"use strict";
exports.__esModule = true;
exports.Mentsu = exports.MentsuType = void 0;
var Tile_1 = require("./Tile");
var MentsuType;
(function (MentsuType) {
    MentsuType["Shuntsu"] = "Shuntsu";
    MentsuType["Kotsu"] = "Kotsu";
})(MentsuType = exports.MentsuType || (exports.MentsuType = {}));
var Mentsu = /** @class */ (function () {
    function Mentsu(type, tile) {
        this.type = type;
        this.tile = tile;
    }
    Object.defineProperty(Mentsu.prototype, "tiles", {
        get: function () {
            switch (this.type) {
                case MentsuType.Kotsu:
                    return [this.tile, this.tile, this.tile];
                case MentsuType.Shuntsu:
                    return [this.tile, this.tile.advance(1), this.tile.advance(2)];
            }
        },
        enumerable: false,
        configurable: true
    });
    Mentsu.prototype.toString = function () {
        switch (this.type) {
            case MentsuType.Kotsu:
                return "".concat(this.tile.num).concat(this.tile.num).concat(this.tile.num).concat(this.tile.type);
            case MentsuType.Shuntsu:
                return "".concat(this.tile.num).concat(this.tile.num + 1).concat(this.tile.num + 2).concat(this.tile.type);
        }
    };
    Mentsu.parse = function (tiles) {
        if (typeof tiles === 'string') {
            var tiles_ = Tile_1.Tile.parseTiles(tiles);
            if (tiles_ === undefined) {
                return undefined;
            }
            return Mentsu.parse(tiles_);
        }
        else {
            if (tiles.length !== 3) {
                return undefined;
            }
            tiles.sort(function (a, b) { return a.compareTo(b); });
            if (tiles[0] === tiles[1] && tiles[1] === tiles[2]) {
                return new Mentsu(MentsuType.Kotsu, tiles[0]);
            }
            else if (tiles[2].distance(tiles[1]) === 1 && tiles[1].distance(tiles[0]) === 1) {
                return new Mentsu(MentsuType.Shuntsu, tiles[0]);
            }
            else {
                return undefined;
            }
        }
    };
    Mentsu.encode = function (data) {
        return data.toString();
    };
    Mentsu.decode = function (raw) {
        var mentsu = Mentsu.parse(raw);
        if (mentsu === undefined) {
            throw new Error("invalid value: " + raw);
        }
        return mentsu;
    };
    return Mentsu;
}());
exports.Mentsu = Mentsu;
