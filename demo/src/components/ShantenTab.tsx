import {Form, Input, Button, FormProps} from "antd";
import React, {useState} from "react";
import {shanten, Tile, UnionShantenResult, ShantenWithoutGot, ShantenWithGot} from "mahjong-utils";

const initialValues = {
    tiles: "34568m235p68s"
}

const ShantenForm: React.FC<FormProps> = (props) => {
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

            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                <Button type="primary" htmlType="submit">
                    计算
                </Button>
            </Form.Item>
        </Form>
    )
}

const ShantenResult: React.FC<{ result?: UnionShantenResult }> = (props) => {
    if (props.result === undefined) {
        return null
    }
    const {result} = props
    const {shantenInfo} = result

    let content: React.ReactNode[] = []
    if (shantenInfo.type === 'ShantenWithoutGot') {
        const {
            shantenNum,
            advance,
            advanceNum,
            goodShapeAdvance,
            goodShapeAdvanceNum
        } = shantenInfo as ShantenWithoutGot

        if (shantenNum === 0) {
            content.push(<div>
                <b>听牌</b>
            </div>)
        } else {
            content.push(<div>
                <b>{shantenNum.toString() + "向听："}</b>
            </div>)
        }
        advance.sort((a, b) => a.compareTo(b))
        content.push(<div>
            {`进张：${advance.map(it => it.toString()).join(", ")} (共${advanceNum}张)`}
        </div>)

        if (shantenNum === 1) {
            goodShapeAdvance?.sort((a, b) => a.compareTo(b))
            content.push(<div>
                {`好型进张：${goodShapeAdvance?.map(it => it.toString())?.join(", ")} (共${goodShapeAdvanceNum}张)`}
            </div>)
        }
    } else {
        const {
            shantenNum,
            discardToAdvance,
            ankanToAdvance
        } = shantenInfo as ShantenWithGot

        if (shantenNum === -1) {
            content.push(<div>
                <b>已和牌</b>
            </div>)
        }

        const grouped = new Map<number, Map<['discard' | 'ankan', Tile], ShantenWithoutGot>>()
        // @ts-ignore
        Object.entries(discardToAdvance).forEach(([discard, shantenAfterDiscard]: [Tile, ShantenWithoutGot]) => {
            if (!grouped.has(shantenAfterDiscard.shantenNum)) {
                grouped.set(shantenAfterDiscard.shantenNum, new Map())
            }
            grouped.get(shantenAfterDiscard.shantenNum)?.set(["discard", discard], shantenAfterDiscard)

            shantenAfterDiscard.advance.sort((a, b) => a.compareTo(b))
            if (shantenAfterDiscard.shantenNum === 1) {
                shantenAfterDiscard.goodShapeAdvance?.sort((a, b) => a.compareTo(b))
            }
        })

        // @ts-ignore
        Object.entries(ankanToAdvance).forEach(([ankan, shantenAfterAnkan]: [Tile, ShantenWithoutGot]) => {
            if (!grouped.has(shantenAfterAnkan.shantenNum)) {
                grouped.set(shantenAfterAnkan.shantenNum, new Map())
            }
            grouped.get(shantenAfterAnkan.shantenNum)?.set(["ankan", ankan], shantenAfterAnkan)

            shantenAfterAnkan.advance.sort((a, b) => a.compareTo(b))
            if (shantenAfterAnkan.shantenNum === 1) {
                shantenAfterAnkan.goodShapeAdvance?.sort((a, b) => a.compareTo(b))
            }
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

            groupedShanten.forEach((shantenInfoAfterAction, [action, tile]) => {
                let text =`[${action === 'discard' ? '打' : '暗杠'}${tile}] `
                text += `进张：${shantenInfoAfterAction.advance.join(", ")} (共${shantenInfoAfterAction.advanceNum}张`
                if (shantenNum === 1) {
                    text += `，好型${shantenInfoAfterAction.goodShapeAdvanceNum}张)`
                }
                text += ')'
                content.push(<div>{text}</div>)
            })
        })
    }

    return (
        <React.Fragment>
            {content}
        </React.Fragment>
    )
}


const ShantenTab: React.FC = () => {
    const [result, setResult] = useState<UnionShantenResult>()

    const onFinish = (values: any) => {
        const tiles = Tile.parseTiles(values.tiles)
        if (tiles !== undefined) {
            const r = shanten(tiles)
            setResult(r)
        }
    };

    return (
        <React.Fragment>
            <ShantenForm
                name="shanten"
                initialValues={initialValues}
                onFinish={onFinish}/>
            <ShantenResult result={result}/>
        </React.Fragment>
    )
}

export default ShantenTab
