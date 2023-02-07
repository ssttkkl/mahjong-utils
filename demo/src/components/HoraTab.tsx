import {Form, Input, Space, Button, FormProps, Radio, InputNumber, Select} from "antd";
import {MinusCircleOutlined, PlusOutlined} from '@ant-design/icons';
import React, {useState} from "react";
import {Furo, Hora, buildHora, Tile} from "mahjong-utils";

const initialValues = {
    tiles: "12233466m111z",
    furo: ["789p"],
    agari: "1z",
    tsumo: true,
    dora: 4,
    selfWind: "East",
    roundWind: "East"
}

const HoraForm: React.FC<FormProps> = (props) => {
    return (
        <Form
            labelCol={{span: 8}}
            wrapperCol={{span: 16}}
            style={{maxWidth: 600}}
            {...props}>

            <Form.Item
                label="手牌"
                name="tiles"
                rules={[{required: true, message: '请填入手牌！'}]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label="副露"
            >
                <Form.List
                    name="furo">
                    {(fields, {add, remove}) => (
                        <>
                            {fields.map(({key, name, ...restField}) => (
                                <Space key={key} style={{display: 'flex'}} align="baseline">
                                    <Form.Item
                                        {...restField}
                                        name={name}
                                        rules={[{required: true, message: '请填入副露！'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <MinusCircleOutlined onClick={() => remove(name)}/>
                                </Space>
                            ))}
                            <Form.Item>
                                <Button type="dashed" onClick={() => add()} block icon={<PlusOutlined/>}>
                                    添加
                                </Button>
                            </Form.Item>
                        </>
                    )}
                </Form.List>

            </Form.Item>

            <Form.Item
                label="和牌"
                name="agari"
                rules={[{required: true, message: '请填入和牌！'}]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label="自摸/荣和"
                name="tsumo"
            >
                <Radio.Group>
                    <Radio.Button value={true}>自摸</Radio.Button>
                    <Radio.Button value={false}>荣和</Radio.Button>
                </Radio.Group>
            </Form.Item>

            <Form.Item
                label="宝牌"
                name="dora"
                initialValue={0}
            >
                <InputNumber min={0} max={64}/>
            </Form.Item>

            <Form.Item
                label="自风"
                name="selfWind"
                rules={[{required: true, message: '请选择自风！'}]}
            >
                <Radio.Group>
                    <Radio.Button value="East">东</Radio.Button>
                    <Radio.Button value="South">南</Radio.Button>
                    <Radio.Button value="West">西</Radio.Button>
                    <Radio.Button value="North">北</Radio.Button>
                </Radio.Group>
            </Form.Item>

            <Form.Item
                label="场风"
                name="roundWind"
                rules={[{required: true, message: '请选择场风！'}]}
            >
                <Radio.Group>
                    <Radio.Button value="East">东</Radio.Button>
                    <Radio.Button value="South">南</Radio.Button>
                    <Radio.Button value="West">西</Radio.Button>
                    <Radio.Button value="North">北</Radio.Button>
                </Radio.Group>
            </Form.Item>

            <Form.Item
                name="extraYaku"
                label="额外役"
            >
                <Select
                    mode="multiple"
                    allowClear
                    style={{width: '100%'}}
                    options={[
                        {value: "Richi", label: "立直"},
                        {value: "Ippatsu", label: "一发"},
                        {value: "Rinshan", label: "岭上自摸"},
                        {value: "Chankan", label: "枪杠"},
                        {value: "Haitei", label: "海底捞月"},
                        {value: "Houtei", label: "河底捞鱼"},
                        {value: "WRichi", label: "两立直"},
                        {value: "Tenhou", label: "天和"},
                        {value: "Chihou", label: "地和"},
                    ]}/>
            </Form.Item>

            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                <Button type="primary" htmlType="submit">
                    计算
                </Button>
            </Form.Item>
        </Form>
    )
}

const HoraTab: React.FC = () => {
    const [result, setResult] = useState<Hora>()

    const onFinish = (values: any) => {
        const r = buildHora({
            ...values,
            tiles: Tile.parseTiles(values.tiles)!,
            furo: values.furo.map(Furo.parse),
            agari: Tile.byText(values.agari)!
        })
        setResult(r)
    };

    return (
        <React.Fragment>
            <HoraForm
                name="hora"
                initialValues={initialValues}
                onFinish={onFinish}/>
        </React.Fragment>
    )
}

export default HoraTab
