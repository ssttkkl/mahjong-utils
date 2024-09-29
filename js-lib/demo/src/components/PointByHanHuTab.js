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
Object.defineProperty(exports, "__esModule", { value: true });
exports.PointByHanHuResult = void 0;
const react_1 = __importStar(require("react"));
const antd_1 = require("antd");
const mahjong_utils_1 = require("mahjong-utils");
const exampleValues = {
    han: 3,
    hu: 40,
    isParent: true
};
const PointByHanHuForm = (props) => {
    const [form] = antd_1.Form.useForm();
    return (<antd_1.Form form={form} labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} style={{ maxWidth: 600 }} {...props}>

      <antd_1.Form.Item label="番数" name="han" rules={[{
                required: true,
                message: '请填入番数！'
            }]}>
        <antd_1.InputNumber min={1} max={13} step={1}/>
      </antd_1.Form.Item>

      <antd_1.Form.Item label="符数" name="hu" rules={[{
                required: true,
                message: '请填入符数！'
            }]}>
        <antd_1.InputNumber min={20} max={140} step={10}/>
      </antd_1.Form.Item>

      <antd_1.Form.Item label=" " colon={false} name="parent" initialValue={true}>
        <antd_1.Radio.Group>
          <antd_1.Radio.Button value={true}>亲家</antd_1.Radio.Button>
          <antd_1.Radio.Button value={false}>子家</antd_1.Radio.Button>
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
const PointByHanHuResult = ({ isParent, ron, tsumoChild, tsumoParent }) => {
    return (<antd_1.Descriptions title="番符算点" column={1}>
      {ron !== 0
            ? <antd_1.Descriptions.Item label="荣和">
          {ron}点
        </antd_1.Descriptions.Item>
            : null}
      {(tsumoChild !== 0 || tsumoParent !== 0)
            ? <antd_1.Descriptions.Item label="自摸">
          {tsumoChild !== 0 ? `子家${tsumoChild}点` : ''}
          {(tsumoChild !== 0 && tsumoParent !== 0) ? '，' : ''}
          {tsumoParent !== 0 ? `亲家${tsumoParent}点` : ''}
          （共{isParent ? tsumoChild * 3 : tsumoChild * 2 + tsumoParent}点）
        </antd_1.Descriptions.Item>
            : null}
    </antd_1.Descriptions>);
};
exports.PointByHanHuResult = PointByHanHuResult;
const PointByHanHuTab = () => {
    var _a, _b, _c;
    const [isParent, setIsParent] = (0, react_1.useState)(true);
    const [result, setResult] = (0, react_1.useState)();
    const [error, setError] = (0, react_1.useState)();
    const onFinish = (values) => {
        try {
            setIsParent(values.isParent);
            if (values.isParent) {
                const r = (0, mahjong_utils_1.getParentPointByHanHu)(values.han, values.hu);
                setResult({
                    ron: r.ron,
                    tsumoChild: r.tsumo,
                    tsumoParent: 0
                });
            }
            else {
                setResult((0, mahjong_utils_1.getChildPointByHanHu)(values.han, values.hu));
            }
            setError(undefined);
        }
        catch (e) {
            setResult(undefined);
            setError(e);
        }
    };
    return (<react_1.default.Fragment>
      <PointByHanHuForm name="shanten" onFinish={onFinish}/>
      <antd_1.Alert message="发生错误" description={error === null || error === void 0 ? void 0 : error.toString()} type="error" style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}/>
      <exports.PointByHanHuResult isParent={isParent} ron={(_a = result === null || result === void 0 ? void 0 : result.ron) !== null && _a !== void 0 ? _a : 0} tsumoChild={(_b = result === null || result === void 0 ? void 0 : result.tsumoChild) !== null && _b !== void 0 ? _b : 0} tsumoParent={(_c = result === null || result === void 0 ? void 0 : result.tsumoParent) !== null && _c !== void 0 ? _c : 0}/>
    </react_1.default.Fragment>);
};
exports.default = PointByHanHuTab;
