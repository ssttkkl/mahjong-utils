"use strict";
exports.__esModule = true;
exports.getChildPointByHanHu = exports.getParentPointByHanHu = void 0;
var entry_1 = require("../entry");
function getParentPointByHanHu(han, hu) {
    var result = entry_1.ENTRY.call("getParentPointByHanHu", { han: han, hu: hu });
    return (0, entry_1.handleResult)(result);
}
exports.getParentPointByHanHu = getParentPointByHanHu;
function getChildPointByHanHu(han, hu) {
    var result = entry_1.ENTRY.call("getChildPointByHanHu", { han: han, hu: hu });
    return (0, entry_1.handleResult)(result);
}
exports.getChildPointByHanHu = getChildPointByHanHu;
