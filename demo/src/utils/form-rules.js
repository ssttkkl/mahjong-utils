"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.tileRule = exports.tilesRule = void 0;
const mahjong_utils_1 = require("mahjong-utils");
function tilesRule(opts = {}) {
    var _a;
    return {
        validateTrigger: (_a = opts.validateTrigger) !== null && _a !== void 0 ? _a : 'onSubmit',
        validator: (rule, value) => __awaiter(this, void 0, void 0, function* () {
            var _b;
            const tiles = mahjong_utils_1.Tile.parseTiles(value);
            if (tiles === undefined) {
                throw new Error('请填入合法的牌代码！');
            }
            if (opts.minLength !== undefined && tiles.length < opts.minLength) {
                throw new Error(`手牌不得少于${opts.minLength}张！`);
            }
            if (opts.maxLength !== undefined && tiles.length > opts.maxLength) {
                throw new Error(`手牌不得多于${opts.maxLength}张！`);
            }
            if (((_b = opts.allowRestModThree) === null || _b === void 0 ? void 0 : _b.every(v => v !== tiles.length % 3)) === true) {
                throw new Error('手牌数量不合法！');
            }
        })
    };
}
exports.tilesRule = tilesRule;
exports.tileRule = {
    validator: (rule, value) => __awaiter(void 0, void 0, void 0, function* () {
        const tile = mahjong_utils_1.Tile.byText(value);
        if (tile === undefined) {
            throw new Error('请填入合法的牌代码！');
        }
    })
};
