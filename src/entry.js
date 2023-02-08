"use strict";
exports.__esModule = true;
exports.handleResult = exports.ENTRY = void 0;
var mahjong_utils_entry_1 = require("mahjong-utils-entry");
exports.ENTRY = mahjong_utils_entry_1.mahjongutils.ENTRY;
function handleResult(result) {
    if (result.code !== 200) {
        throw new Error(result.msg);
    }
    else if (result.data === undefined) {
        throw new Error('No data');
    }
    return result.data;
}
exports.handleResult = handleResult;
