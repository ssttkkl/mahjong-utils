import React from 'react';
import { Tile } from 'mahjong-utils';
import { type TableProps } from 'antd/lib';
export interface ActionToDiscard {
    action: Array<string | Tile> | string;
    advance: Tile[];
    advanceNum: number;
    goodShapeAdvance?: Tile[];
    goodShapeAdvanceNum?: number;
    goodShapeRate?: number;
}
export type ActionToDiscardTableProps = TableProps<ActionToDiscard> & {
    showGoodShapeInfo?: boolean;
};
declare const ActionToDiscardTable: React.FC<ActionToDiscardTableProps>;
export default ActionToDiscardTable;
