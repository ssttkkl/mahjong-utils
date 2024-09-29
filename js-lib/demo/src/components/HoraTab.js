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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
Object.defineProperty(exports, "__esModule", { value: true });
const antd_1 = require("antd");
const icons_1 = require("@ant-design/icons");
const react_1 = __importStar(require("react"));
const mahjong_utils_1 = require("mahjong-utils");
const form_rules_1 = require("../utils/form-rules");
const yakuName_1 = require("../utils/yakuName");
const PointByHanHuTab_1 = require("./PointByHanHuTab");
const exampleValues = {
    tiles: '12233466m111z',
    furo: ['789p'],
    agari: '1z',
    tsumo: true,
    dora: 2,
    selfWind: mahjong_utils_1.Wind.East,
    roundWind: mahjong_utils_1.Wind.East,
    extraYaku: ['Richi', 'Ippatsu']
};
const HoraForm = (props) => {
    const [form] = antd_1.Form.useForm();
    return (<antd_1.Form form={form} labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} style={{ maxWidth: 600 }} {...props}>

      <antd_1.Form.Item label="手牌" name="tiles" rules={[
            {
                required: true,
                message: '请填入手牌！'
            },
            (0, form_rules_1.tilesRule)({
                minLength: 2,
                maxLength: 14,
                allowRestModThree: [2]
            }),
            ({ getFieldValue }) => ({
                validateTrigger: 'onSubmit',
                validator(_, value) {
                    var _a;
                    return __awaiter(this, void 0, void 0, function* () {
                        const tiles = mahjong_utils_1.Tile.parseTiles(value);
                        const furo = getFieldValue('furo');
                        if (((_a = tiles === null || tiles === void 0 ? void 0 : tiles.length) !== null && _a !== void 0 ? _a : 0) + furo.length * 3 !== 14) {
                            throw new Error('手牌数量不合法！');
                        }
                    });
                }
            })
        ]} validateFirst>
        <antd_1.Input />
      </antd_1.Form.Item>

      <antd_1.Form.Item label="副露">
        <antd_1.Form.List name="furo">
          {(fields, { add, remove }) => (<>
              {fields.map((_a) => {
                var { key, name } = _a, restField = __rest(_a, ["key", "name"]);
                return (<antd_1.Space key={key} style={{ display: 'flex' }} align="baseline">
                  <antd_1.Form.Item {...restField} name={name} rules={[
                        {
                            required: true,
                            message: '请填入副露！'
                        },
                        (0, form_rules_1.tilesRule)({
                            minLength: 3,
                            maxLength: 3
                        })
                    ]} validateFirst>
                    <antd_1.Input />
                  </antd_1.Form.Item>
                  <icons_1.MinusCircleOutlined onClick={() => {
                        remove(name);
                    }}/>
                </antd_1.Space>);
            })}
              {fields.length < 4
                ? <antd_1.Form.Item>
                  <antd_1.Button type="dashed" onClick={() => {
                        add();
                    }} block icon={<icons_1.PlusOutlined />}>
                    添加
                  </antd_1.Button>
                </antd_1.Form.Item>
                : null}
            </>)}
        </antd_1.Form.List>

      </antd_1.Form.Item>

      <antd_1.Form.Item label="和牌" name="agari" rules={[
            {
                required: true,
                message: '请填入和牌！'
            },
            form_rules_1.tileRule
        ]} validateFirst>
        <antd_1.Input />
      </antd_1.Form.Item>

      <antd_1.Form.Item label="自摸/荣和" name="tsumo" initialValue={true}>
        <antd_1.Radio.Group>
          <antd_1.Radio.Button value={true}>自摸</antd_1.Radio.Button>
          <antd_1.Radio.Button value={false}>荣和</antd_1.Radio.Button>
        </antd_1.Radio.Group>
      </antd_1.Form.Item>

      <antd_1.Form.Item label="宝牌" name="dora" initialValue={0}>
        <antd_1.InputNumber min={0} max={64}/>
      </antd_1.Form.Item>

      <antd_1.Form.Item label="自风" name="selfWind" rules={[{
                required: true,
                message: '请选择自风！'
            }]}>
        <antd_1.Radio.Group>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.East}>东</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.South}>南</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.West}>西</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.North}>北</antd_1.Radio.Button>
        </antd_1.Radio.Group>
      </antd_1.Form.Item>

      <antd_1.Form.Item label="场风" name="roundWind" rules={[{
                required: true,
                message: '请选择场风！'
            }]}>
        <antd_1.Radio.Group>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.East}>东</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.South}>南</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.West}>西</antd_1.Radio.Button>
          <antd_1.Radio.Button value={mahjong_utils_1.Wind.North}>北</antd_1.Radio.Button>
        </antd_1.Radio.Group>
      </antd_1.Form.Item>

      <antd_1.Form.Item name="extraYaku" label="额外役">
        <antd_1.Select mode="multiple" allowClear style={{ width: '100%' }} options={[
            {
                value: 'Richi',
                label: yakuName_1.yakuName.Richi
            },
            {
                value: 'Ippatsu',
                label: yakuName_1.yakuName.Ippatsu
            },
            {
                value: 'Rinshan',
                label: yakuName_1.yakuName.Rinshan
            },
            {
                value: 'Chankan',
                label: yakuName_1.yakuName.Chankan
            },
            {
                value: 'Haitei',
                label: yakuName_1.yakuName.Haitei
            },
            {
                value: 'Houtei',
                label: yakuName_1.yakuName.Houtei
            },
            {
                value: 'WRichi',
                label: yakuName_1.yakuName.WRichi
            },
            {
                value: 'Tenhou',
                label: yakuName_1.yakuName.Tenhou
            },
            {
                value: 'Chihou',
                label: yakuName_1.yakuName.Chihou
            }
        ]}/>
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
const HoraView = ({ hora }) => {
    let timesOfYakuman = 0;
    if (hora.hasYakuman) {
        hora.yaku.forEach(yk => {
            if (yakuName_1.doubleTimesYakuman.find(x => x === yk) !== undefined) {
                timesOfYakuman += 2;
            }
            else {
                timesOfYakuman += 1;
            }
        });
    }
    const isParent = hora.pattern.selfWind === hora.pattern.roundWind;
    let point = {
        ron: 0,
        tsumoParent: 0,
        tsumoChild: 0
    };
    if (isParent) {
        let r = {
            ron: 0,
            tsumo: 0
        };
        if (timesOfYakuman > 0) {
            r = (0, mahjong_utils_1.getParentPointByHanHu)(13, 20);
            r.ron *= timesOfYakuman;
            r.tsumo *= timesOfYakuman;
        }
        else if (hora.han > 0) {
            r = (0, mahjong_utils_1.getParentPointByHanHu)(hora.han, hora.pattern.hu);
        }
        point = {
            ron: r.ron,
            tsumoChild: r.tsumo,
            tsumoParent: 0
        };
    }
    else {
        if (timesOfYakuman > 0) {
            point = (0, mahjong_utils_1.getChildPointByHanHu)(13, 20);
            point.ron *= timesOfYakuman;
            point.tsumoParent *= timesOfYakuman;
            point.tsumoChild *= timesOfYakuman;
        }
        else if (hora.han > 0) {
            point = (0, mahjong_utils_1.getChildPointByHanHu)(hora.han, hora.pattern.hu);
        }
    }
    return (<react_1.default.Fragment>
      <antd_1.Descriptions title="和牌分析" column={1}>
        <antd_1.Descriptions.Item label="番数">
          {hora.hasYakuman
            ? `${timesOfYakuman}倍役满`
            : (hora.han >= 13 ? `累计役满（${hora.han}番）` : `${hora.han}番`)}
        </antd_1.Descriptions.Item>
        <antd_1.Descriptions.Item label="符数">
          {hora.pattern.hu}
        </antd_1.Descriptions.Item>
        <antd_1.Descriptions.Item label="役种">
          <antd_1.Space wrap>
            {hora.yaku.map(x => (<span key={x}>{yakuName_1.yakuName[x]}</span>))}
          </antd_1.Space>
        </antd_1.Descriptions.Item>
      </antd_1.Descriptions>
      <PointByHanHuTab_1.PointByHanHuResult isParent={isParent} {...point}/>
    </react_1.default.Fragment>);
};
const HoraTab = () => {
    const [result, setResult] = (0, react_1.useState)();
    const [error, setError] = (0, react_1.useState)();
    const onFinish = (values) => {
        try {
            const tiles = mahjong_utils_1.Tile.parseTiles(values.tiles);
            const furo = values.furo.map(value => {
                const x = mahjong_utils_1.Furo.parse(value);
                if (x === undefined) {
                    throw new Error('解析牌代码时发生错误');
                }
                return x;
            });
            const agari = mahjong_utils_1.Tile.byText(values.agari);
            if (tiles !== undefined && agari !== undefined) {
                const r = (0, mahjong_utils_1.buildHora)(Object.assign(Object.assign({}, values), { tiles,
                    furo,
                    agari }));
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
      <HoraForm name="hora" onFinish={onFinish}/>
      <antd_1.Alert message="发生错误" description={error === null || error === void 0 ? void 0 : error.toString()} type="error" style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}/>
      {(result != null)
            ? <HoraView hora={result}/>
            : null}

    </react_1.default.Fragment>);
};
exports.default = HoraTab;
