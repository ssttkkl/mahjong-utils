"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const antd_1 = require("antd");
const react_1 = __importStar(require("react"));
const mahjong_utils_1 = require("mahjong-utils");
const ActionToDiscardTable_1 = __importDefault(require("./ActionToDiscardTable"));
const form_rules_1 = require("../utils/form-rules");
const Tiles_1 = __importDefault(require("./Tiles/Tiles"));
const window_size_1 = require("@react-hook/window-size");
const { Title } = antd_1.Typography;
const exampleValues = {
    tiles: '34568m235p688s'
};
const ShantenForm = (props) => {
    const [form] = antd_1.Form.useForm();
    return (<antd_1.Form form={form} labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} style={{ maxWidth: 600 }} {...props}>

      <antd_1.Form.Item label="手牌" name="tiles" rules={[
            {
                required: true,
                message: '请填入手牌！'
            },
            (0, form_rules_1.tilesRule)({
                maxLength: 14,
                allowRestModThree: [1, 2]
            })
        ]} validateFirst>
        <antd_1.Input />
      </antd_1.Form.Item>

      <antd_1.Form.Item wrapperCol={{
            offset: 8,
            span: 16
        }}>
        <antd_1.Space wrap>
          <antd_1.Button type="primary" htmlType="submit">
            计算
          </antd_1.Button>
          <antd_1.Button onClick={() => {
            form.resetFields();
        }}>
            清空
          </antd_1.Button>
          <antd_1.Button onClick={() => {
            form.setFieldsValue(exampleValues);
        }}>
            填入示例
          </antd_1.Button>
        </antd_1.Space>
      </antd_1.Form.Item>
    </antd_1.Form>);
};
const ShantenWithoutGotView = ({ shantenInfo }) => {
    const windowWidth = (0, window_size_1.useWindowWidth)();
    const tileSize = windowWidth < 540 ? 'small' : 'normal';
    return (<antd_1.Descriptions title="向听计算（未摸牌）" column={1}>
      <antd_1.Descriptions.Item label="向听数">
        {shantenInfo.shantenNum}</antd_1.Descriptions.Item>
      <antd_1.Descriptions.Item label="进张">
        <div style={{ overflowWrap: 'break-word' }}>
          <Tiles_1.default tiles={shantenInfo.advance} size={tileSize} sorted/>
          <span>（{shantenInfo.advanceNum}张）</span>
        </div>
      </antd_1.Descriptions.Item>
      {shantenInfo.goodShapeAdvance !== undefined && shantenInfo.goodShapeAdvanceNum !== undefined
            ? <>
          <antd_1.Descriptions.Item label="好型进张">
            <Tiles_1.default tiles={shantenInfo.goodShapeAdvance} sorted/>
            <span>（{shantenInfo.goodShapeAdvanceNum}张）</span>
          </antd_1.Descriptions.Item>
          <antd_1.Descriptions.Item label="好型率">
            {(shantenInfo.goodShapeAdvanceNum / shantenInfo.advanceNum * 100).toFixed(2)}%
          </antd_1.Descriptions.Item>
        </>
            : null}
    </antd_1.Descriptions>);
};
const ShantenWithGotView = ({ shantenInfo }) => {
    const { discardToAdvance, ankanToAdvance } = shantenInfo;
    const grouped = new Map();
    discardToAdvance.forEach((shantenAfterDiscard, discard) => {
        var _a, _b;
        if (!grouped.has(shantenAfterDiscard.shantenNum)) {
            grouped.set(shantenAfterDiscard.shantenNum, new Map());
        }
        (_a = grouped.get(shantenAfterDiscard.shantenNum)) === null || _a === void 0 ? void 0 : _a.set(['discard', discard], shantenAfterDiscard);
        shantenAfterDiscard.advance.sort((a, b) => a.compareTo(b));
        if (shantenAfterDiscard.shantenNum === 1) {
            (_b = shantenAfterDiscard.goodShapeAdvance) === null || _b === void 0 ? void 0 : _b.sort((a, b) => a.compareTo(b));
        }
    });
    ankanToAdvance.forEach((shantenAfterAnkan, ankan) => {
        var _a, _b;
        if (!grouped.has(shantenAfterAnkan.shantenNum)) {
            grouped.set(shantenAfterAnkan.shantenNum, new Map());
        }
        (_a = grouped.get(shantenAfterAnkan.shantenNum)) === null || _a === void 0 ? void 0 : _a.set(['ankan', ankan], shantenAfterAnkan);
        shantenAfterAnkan.advance.sort((a, b) => a.compareTo(b));
        if (shantenAfterAnkan.shantenNum === 1) {
            (_b = shantenAfterAnkan.goodShapeAdvance) === null || _b === void 0 ? void 0 : _b.sort((a, b) => a.compareTo(b));
        }
    });
    const ordered = [...grouped.entries()];
    ordered.sort((a, b) => a[0] - b[0]);
    return (<react_1.default.Fragment>
      <antd_1.Descriptions title="向听计算（已摸牌）">
        <antd_1.Descriptions.Item label="向听数">
          {shantenInfo.shantenNum}
          {shantenInfo.shantenNum === -1
            ? '（和牌）'
            : ''}
        </antd_1.Descriptions.Item>
      </antd_1.Descriptions>
      {ordered.map(([shantenNum, infos]) => {
            let title = shantenNum === 0 ? '听牌' : `${shantenNum}向听`;
            if (shantenNum !== shantenInfo.shantenNum) {
                title += '（退向）';
            }
            const curGroup = [...infos.entries()];
            curGroup.sort((a, b) => {
                if (a[1].advanceNum !== b[1].advanceNum) {
                    return a[1].advanceNum - b[1].advanceNum;
                }
                else if (a[1].goodShapeAdvanceNum !== undefined && b[1].goodShapeAdvanceNum !== undefined) {
                    return a[1].goodShapeAdvanceNum - b[1].goodShapeAdvanceNum;
                }
                else {
                    return 0;
                }
            }).reverse();
            const data = curGroup.map(([action, shantenAfterAction]) => {
                return {
                    action: [action[0] === 'discard' ? '打' : '暗杠', action[1]],
                    advance: shantenAfterAction.advance,
                    advanceNum: shantenAfterAction.advanceNum,
                    goodShapeAdvance: shantenAfterAction.goodShapeAdvance,
                    goodShapeAdvanceNum: shantenAfterAction.goodShapeAdvanceNum,
                    goodShapeRate: shantenAfterAction.goodShapeAdvanceNum !== undefined
                        ? shantenAfterAction.goodShapeAdvanceNum / shantenAfterAction.advanceNum
                        : undefined
                };
            });
            return (<>
            <Title level={5}>{title} </Title>
            <ActionToDiscardTable_1.default dataSource={data} showGoodShapeInfo={shantenNum === 1}/>
          </>);
        })}
    </react_1.default.Fragment>);
};
const ShantenTab = () => {
    const [result, setResult] = (0, react_1.useState)();
    const [error, setError] = (0, react_1.useState)();
    const onFinish = (values) => {
        try {
            const tiles = mahjong_utils_1.Tile.parseTiles(values.tiles);
            if (tiles !== undefined) {
                const r = (0, mahjong_utils_1.shanten)(tiles);
                setResult(r);
                setError(undefined);
            }
            else {
                throw new Error('解析牌代码时发生错误');
            }
        }
        catch (e) {
            setResult(undefined);
            setError(e);
        }
    };
    return (<react_1.default.Fragment>
      <ShantenForm name="shanten" onFinish={onFinish}/>
      <antd_1.Alert message="发生错误" description={error === null || error === void 0 ? void 0 : error.toString()} type="error" style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}/>
      {(result != null)
            ? ((result === null || result === void 0 ? void 0 : result.shantenInfo.type) === 'ShantenWithoutGot'
                ? <ShantenWithoutGotView shantenInfo={result.shantenInfo}/>
                : <ShantenWithGotView shantenInfo={result.shantenInfo}/>)
            : null}
    </react_1.default.Fragment>);
};
exports.default = ShantenTab;
