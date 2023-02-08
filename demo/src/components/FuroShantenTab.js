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
const { Title } = antd_1.Typography;
const exampleValues = {
    tiles: '3456778m123457p',
    chanceTile: '7m',
    allowChi: true
};
const FuroChanceShantenForm = (props) => {
    const [form] = antd_1.Form.useForm();
    return (<antd_1.Form form={form} labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} style={{ maxWidth: 600 }} {...props}>

      <antd_1.Form.Item label="手牌" name="tiles" rules={[
            {
                required: true,
                message: '请填入手牌！'
            },
            (0, form_rules_1.tilesRule)({
                minLength: 4,
                maxLength: 13,
                allowRestModThree: [1]
            })
        ]} validateFirst>
        <antd_1.Input />
      </antd_1.Form.Item>

      <antd_1.Form.Item label="机会牌" name="chanceTile" rules={[
            {
                required: true,
                message: '请填入机会牌！'
            },
            form_rules_1.tileRule
        ]} validateFirst>
        <antd_1.Input />
      </antd_1.Form.Item>

      <antd_1.Form.Item label="允许吃" name="allowChi" initialValue={true}>
        <antd_1.Radio.Group>
          <antd_1.Radio.Button value={true}>是</antd_1.Radio.Button>
          <antd_1.Radio.Button value={false}>否</antd_1.Radio.Button>
        </antd_1.Radio.Group>
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
const ShantenWithFuroChanceView = ({ shantenInfo }) => {
    var _a, _b;
    const { pass, chi, pon, minkan } = shantenInfo;
    const grouped = new Map();
    if (pass !== undefined) {
        if (!grouped.has(pass.shantenNum)) {
            grouped.set(pass.shantenNum, new Map());
        }
        (_a = grouped.get(pass.shantenNum)) === null || _a === void 0 ? void 0 : _a.set(['pass'], pass);
    }
    if (minkan !== undefined) {
        if (!grouped.has(minkan.shantenNum)) {
            grouped.set(minkan.shantenNum, new Map());
        }
        (_b = grouped.get(minkan.shantenNum)) === null || _b === void 0 ? void 0 : _b.set(['minkan'], minkan);
    }
    if (pon !== undefined) {
        pon.discardToAdvance.forEach((shantenAfterPonDiscard, discard) => {
            var _a;
            if (!grouped.has(shantenAfterPonDiscard.shantenNum)) {
                grouped.set(shantenAfterPonDiscard.shantenNum, new Map());
            }
            (_a = grouped.get(shantenAfterPonDiscard.shantenNum)) === null || _a === void 0 ? void 0 : _a.set(['pon', discard], shantenAfterPonDiscard);
        });
    }
    chi.forEach(([tatsu, shantenAfterChi]) => {
        shantenAfterChi.discardToAdvance.forEach((shantenAfterChiDiscard, discard) => {
            var _a;
            if (!grouped.has(shantenAfterChiDiscard.shantenNum)) {
                grouped.set(shantenAfterChiDiscard.shantenNum, new Map());
            }
            (_a = grouped.get(shantenAfterChiDiscard.shantenNum)) === null || _a === void 0 ? void 0 : _a.set(['chi', tatsu, discard], shantenAfterChiDiscard);
        });
    });
    const ordered = [...grouped.entries()];
    ordered.sort((a, b) => a[0] - b[0]);
    return (<react_1.default.Fragment>
      <antd_1.Descriptions title="副露判断">
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
                let actionText = [];
                switch (action[0]) {
                    case 'pass':
                        actionText = ['PASS'];
                        break;
                    case 'chi':
                        actionText = [action[1].first, action[1].second, '吃打', action[2]];
                        break;
                    case 'pon':
                        actionText = ['碰打', action[1]];
                        break;
                    case 'minkan':
                        actionText = ['大明杠'];
                        break;
                }
                return {
                    action: actionText,
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
const FuroChanceShantenTab = () => {
    const [result, setResult] = (0, react_1.useState)();
    const [error, setError] = (0, react_1.useState)();
    const onFinish = (values) => {
        try {
            const tiles = mahjong_utils_1.Tile.parseTiles(values.tiles);
            const chanceTile = mahjong_utils_1.Tile.byText(values.chanceTile);
            if (tiles !== undefined && chanceTile !== undefined) {
                const r = (0, mahjong_utils_1.furoChanceShanten)(tiles, chanceTile, {
                    allowChi: values.allowChi
                });
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
      <FuroChanceShantenForm name="shanten" onFinish={onFinish}/>
      <antd_1.Alert message="发生错误" description={error === null || error === void 0 ? void 0 : error.toString()} type="error" style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}/>
      {(result != null)
            ? <ShantenWithFuroChanceView shantenInfo={result.shantenInfo}/>
            : null}
    </react_1.default.Fragment>);
};
exports.default = FuroChanceShantenTab;
