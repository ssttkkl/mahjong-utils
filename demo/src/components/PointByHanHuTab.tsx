import React, {useState} from "react";
import {Button, Form, FormProps, Input, Radio} from "antd";
import {getChildPointByHanHu, getParentPointByHanHu} from "mahjong-utils";

const initialValues = {
    han: 3,
    hu: 40,
    parent: true
}

const PointByHanHuForm: React.FC<FormProps> = (props) => {
    return (
        <Form
            labelCol={{span: 8}}
            wrapperCol={{span: 16}}
            style={{maxWidth: 600}}
            {...props}>

            <Form.Item
                label="番数"
                name="han"
                rules={[{required: true, message: '请填入番数！'}]}
            >
                <Input suffix="番"/>
            </Form.Item>

            <Form.Item
                label="符数"
                name="hu"
                rules={[{required: true, message: '请填入符数！'}]}
            >
                <Input suffix="符"/>
            </Form.Item>

            <Form.Item
                label=" "
                colon={false}
                name="parent"
                initialValue={true}>
                <Radio.Group>
                    <Radio.Button value={true}>亲家</Radio.Button>
                    <Radio.Button value={false}>子家</Radio.Button>
                </Radio.Group>

            </Form.Item>

            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                <Button type="primary" htmlType="submit">
                    计算
                </Button>
            </Form.Item>
        </Form>
    )
}

const PointByHanHuResult: React.FC<{ parent: boolean, result?: any }> = (props) => {
    if (props.result === undefined) {
        return null
    }
    const {result} = props

    return (
        <React.Fragment>
            <div>荣和：{result.ron}</div>
            <div>自摸：{
                props.parent
                    ? `${result.tsumo} ALL`
                    : `${result.tsumoParent} ${result.tsumoChild}`
            }</div>
        </React.Fragment>
    )
}

const PointByHanHuTab: React.FC = () => {
    const [values, setValues] = useState<any>()
    const [result, setResult] = useState<any>()

    const onFinish = (values: any) => {
        setValues(values)
        if (values.parent) {
            setResult(getParentPointByHanHu(values.han, values.hu))
        } else {
            setResult(getChildPointByHanHu(values.han, values.hu))
        }
    };

    return (
        <React.Fragment>
            <PointByHanHuForm
                name="shanten"
                initialValues={initialValues}
                onFinish={onFinish}/>
            <PointByHanHuResult parent={values?.parent} result={result}/>
        </React.Fragment>
    )
}

export default PointByHanHuTab
