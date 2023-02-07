import {ENTRY, handleResult} from "../entry";

export function getParentPointByHanHu(han: number, hu: number): { ron: number, tsumo: number } {
    const result = ENTRY.call("getParentPointByHanHu", {han, hu})
    return handleResult(result)
}

export function getChildPointByHanHu(han: number, hu: number): { ron: number, tsumoParent: number, tsumoChild: number } {
    const result = ENTRY.call("getChildPointByHanHu", {han, hu})
    return handleResult(result)
}
