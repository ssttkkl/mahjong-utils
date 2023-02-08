"use strict";
exports.__esModule = true;
exports.mapToObj = exports.objToMap = void 0;
function objToMap(obj, opts) {
    return new Map(Object.keys(obj).map(function (key) {
        return [opts.keyMapper(key), opts.valueMapper(obj[key])];
    }));
}
exports.objToMap = objToMap;
function mapToObj(map, opts) {
    var obj = {};
    map.forEach(function (value, key) {
        obj[opts.keyMapper(key)] = opts.valueMapper(value);
    });
    return obj;
}
exports.mapToObj = mapToObj;
