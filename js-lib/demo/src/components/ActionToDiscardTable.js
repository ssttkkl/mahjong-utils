"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const react_1 = __importDefault(require("react"));
const mahjong_utils_1 = require("mahjong-utils");
const antd_1 = require("antd");
const Tiles_1 = __importDefault(require("./Tiles/Tiles"));
const window_size_1 = require("@react-hook/window-size");
const ActionToDiscardTable = (props) => {
    const windowWidth = (0, window_size_1.useWindowWidth)();
    const tileSize = windowWidth < 540 ? 'small' : 'normal';
    const tableSize = windowWidth < 540 ? 'middle' : 'large';
    const columns = [
        {
            title: '',
            key: 'action',
            dataIndex: 'action',
            render: (action) => {
                if (action instanceof Array) {
                    const children = [];
                    let pending = [];
                    action.forEach(value => {
                        if (typeof value === 'string') {
                            if (pending.length > 0) {
                                children.push(<Tiles_1.default key={mahjong_utils_1.Tile.tilesToString(pending)} tiles={pending} size={tileSize} sorted/>);
                                pending = [];
                            }
                            children.push(<span>{value}</span>);
                        }
                        else {
                            pending.push(value);
                            console.log(pending);
                        }
                    });
                    if (pending.length > 0) {
                        children.push(<Tiles_1.default key={mahjong_utils_1.Tile.tilesToString(pending)} tiles={pending} size={tileSize} sorted/>);
                    }
                    return <react_1.default.Fragment>{children}</react_1.default.Fragment>;
                }
                else {
                    return <span>{action}</span>;
                }
            }
        },
        {
            title: '进张',
            key: 'advance',
            dataIndex: 'advance',
            sorter: (a, b) => {
                return a.advance.length - b.advance.length;
            },
            render: (advance, record) => (<>
          <Tiles_1.default tiles={advance} size={tileSize} sorted/>
          <span>（共{record.advanceNum}张）</span>
        </>)
        },
        {
            title: '好型进张',
            key: 'goodShapeAdvance',
            dataIndex: 'goodShapeAdvance',
            sorter: (a, b) => {
                var _a, _b, _c, _d;
                return ((_b = (_a = a.goodShapeAdvance) === null || _a === void 0 ? void 0 : _a.length) !== null && _b !== void 0 ? _b : 0) - ((_d = (_c = b.goodShapeAdvance) === null || _c === void 0 ? void 0 : _c.length) !== null && _d !== void 0 ? _d : 0);
            },
            render: (goodShapeAdvance, record) => {
                if (goodShapeAdvance !== undefined) {
                    return (<>
              <Tiles_1.default tiles={goodShapeAdvance} size={tileSize} sorted/>
              <span>（共{record.goodShapeAdvanceNum}张）</span>
            </>);
                }
                else {
                    return null;
                }
            }
        },
        {
            title: '好型率',
            key: 'goodShapeRate',
            dataIndex: 'goodShapeRate',
            sorter: (a, b) => {
                var _a, _b;
                return ((_a = a.goodShapeRate) !== null && _a !== void 0 ? _a : 0) - ((_b = b.goodShapeRate) !== null && _b !== void 0 ? _b : 0);
            },
            render: (value) => value !== undefined ? (value * 100).toFixed(2) + '%' : ''
        }
    ];
    if (props.showGoodShapeInfo !== true) {
        columns.pop();
        columns.pop();
    }
    return (<antd_1.Table columns={columns} pagination={false} size={tableSize} {...props}/>);
};
exports.default = ActionToDiscardTable;
