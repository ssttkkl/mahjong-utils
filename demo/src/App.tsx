import React from 'react';
import {Tabs, TabsProps} from "antd";
import ShantenTab from "./components/ShantenTab";
import PointByHanHuTab from "./components/PointByHanHuTab";
import FuroChanceShantenTab from "./components/FuroShantenTab";
import HoraTab from "./components/HoraTab";

function App() {
    const items: TabsProps['items'] = [
        {
            key: 'shanten',
            label: `向听计算`,
            children: <ShantenTab/>,
        },
        {
            key: 'furoChanceShanten',
            label: `副露判断`,
            children: <FuroChanceShantenTab/>,
        },
        {
            key: 'hora',
            label: `和牌分析`,
            children: <HoraTab/>,
        },
        {
            key: 'hanhu',
            label: `番符算点`,
            children: <PointByHanHuTab/>,
        },
    ];
    return (
        <div style={{padding: "24px 16px"}}>
            <Tabs items={items}/>
        </div>
    );
}

export default App;