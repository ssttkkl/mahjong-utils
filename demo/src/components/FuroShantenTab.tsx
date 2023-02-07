import {Form, Input, Button, FormProps, Radio} from "antd";
import React, {useState} from "react";
import {
    Tile,
    ShantenWithoutGot,
    ShantenWithGot,
    FuroChanceShantenResult, furoChanceShanten, Tatsu
} from "mahjong-utils";

const initialValues = {
    tiles: "3456778m123457p",
    chanceTile: "7m",
    allowChi: true
}

const FuroChanceShantenForm: React.FC<FormProps> = (props) => {
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
                label="机会牌"
                name="chanceTile"
                rules={[{required: true, message: '请填入机会牌！'}]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label="允许吃"
                name="allowChi"
                initialValue={true}
            >
                <Radio.Group>
                    <Radio.Button value={true}>是</Radio.Button>
                    <Radio.Button value={false}>否</Radio.Button>
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

const FuroChanceShantenResultView: React.FC<{ result?: FuroChanceShantenResult }> = (props) => {
    if (props.result === undefined) {
        return null
    }
    const {result} = props
    const {shantenInfo} = result

    let content: React.ReactNode[] = []
    const {
        shantenNum,
        pass,
        chi,
        pon,
        minkan
    } = shantenInfo

    const grouped = new Map<number, Map<['pass',] | ['minkan',] | ['pon', Tile] | ['chi', Tatsu, Tile], ShantenWithoutGot>>()

    if (pass !== undefined) {
        if (!grouped.has(pass.shantenNum)) {
            grouped.set(pass.shantenNum, new Map())
        }
        grouped.get(pass.shantenNum)?.set(["pass",], pass)
    }

    if (minkan !== undefined) {
        if (!grouped.has(minkan.shantenNum)) {
            grouped.set(minkan.shantenNum, new Map())
        }
        grouped.get(minkan.shantenNum)?.set(["minkan",], minkan)
    }

    if (pon !== undefined) {
        pon.discardToAdvance.forEach((shantenAfterPonDiscard, discard) => {
            if (!grouped.has(shantenAfterPonDiscard.shantenNum)) {
                grouped.set(shantenAfterPonDiscard.shantenNum, new Map())
            }
            grouped.get(shantenAfterPonDiscard.shantenNum)?.set(["pon", discard], shantenAfterPonDiscard)
        })
    }

    chi.forEach(([tatsu, shantenAfterChi]) => {
        shantenAfterChi.discardToAdvance.forEach((shantenAfterChiDiscard, discard) => {
            if (!grouped.has(shantenAfterChiDiscard.shantenNum)) {
                grouped.set(shantenAfterChiDiscard.shantenNum, new Map())
            }
            grouped.get(shantenAfterChiDiscard.shantenNum)?.set(["chi", tatsu, discard], shantenAfterChiDiscard)
        })
    })

    grouped.forEach((groupedShanten, shantenNum) => {
        if (shantenNum === 0) {
            content.push(<div>
                <b>
                    听牌
                    {shantenNum !== shantenInfo.shantenNum ? "（退向）" : ""}
                </b>
            </div>)
        } else {
            content.push(<div>
                <b>
                    {shantenNum.toString()}向听
                    {shantenNum !== shantenInfo.shantenNum ? "（退向）" : ""}
                    ：
                </b>
            </div>)
        }

        groupedShanten.forEach((shantenAfterAction, action) => {
            const advanceText = shantenNum !== 1
                ? `进张：${shantenAfterAction.advance.join(", ")} (共${shantenAfterAction.advanceNum}张)`
                : `进张：${shantenAfterAction.advance.join(", ")} (共${shantenAfterAction.advanceNum}张，好型${shantenAfterAction.goodShapeAdvanceNum}张)`

            switch (action[0]) {
                case "pass":
                    content.push(<div>{`[PASS] ${advanceText}`}</div>)
                    break
                case "chi":
                    content.push(<div>{`[${action[1]}吃打${action[2]}] ${advanceText}`}</div>)
                    break
                case "pon":
                    content.push(<div>{`[碰打${action[1]}] ${advanceText}`}</div>)
                    break
                case "minkan":
                    content.push(<div>{`[杠] ${advanceText}`}</div>)
                    break
            }
        })
    })

    return (
        <React.Fragment>
            {content}
        </React.Fragment>
    )
}


const FuroChanceShantenTab: React.FC = () => {
    const [result, setResult] = useState<FuroChanceShantenResult>()

    const onFinish = (values: any) => {
        const tiles = Tile.parseTiles(values.tiles)
        const chanceTile = Tile.byText(values.chanceTile)
        if (tiles !== undefined && chanceTile !== undefined) {
            const r = furoChanceShanten(tiles, chanceTile, {
                allowChi: values.allowChi
            })
            console.log(r)
            setResult(r)
        }
    };

    return (
        <React.Fragment>
            <FuroChanceShantenForm
                name="shanten"
                initialValues={initialValues}
                onFinish={onFinish}/>
            <FuroChanceShantenResultView result={result}/>
        </React.Fragment>
    )
}

export default FuroChanceShantenTab
