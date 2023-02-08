"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const react_1 = __importDefault(require("react"));
const antd_1 = require("antd");
const ShantenTab_1 = __importDefault(require("./components/ShantenTab"));
const PointByHanHuTab_1 = __importDefault(require("./components/PointByHanHuTab"));
const FuroShantenTab_1 = __importDefault(require("./components/FuroShantenTab"));
const HoraTab_1 = __importDefault(require("./components/HoraTab"));
function App() {
    const items = [
        {
            key: 'shanten',
            label: '向听计算&牌理',
            children: <ShantenTab_1.default />
        },
        {
            key: 'furoChanceShanten',
            label: '副露牌理',
            children: <FuroShantenTab_1.default />
        },
        {
            key: 'hora',
            label: '和牌分析',
            children: <HoraTab_1.default />
        },
        {
            key: 'hanhu',
            label: '番符算点',
            children: <PointByHanHuTab_1.default />
        }
    ];
    return (<div style={{ padding: '24px 16px' }}>
            <antd_1.Tabs items={items}/>
        </div>);
}
exports.default = App;
