import { Alert, Button, Descriptions, Form, type FormProps, Input, Space, Typography } from 'antd'
import React, { useState } from 'react'
import { shanten, type ShantenWithGot, type ShantenWithoutGot, Tile, type UnionShantenResult } from 'mahjong-utils'
import ActionToDiscardTable, { type ActionToDiscard } from './ActionToDiscardTable'
import { tilesRule } from '../utils/form-rules'
import Tiles from './Tiles/Tiles'
import { useWindowWidth } from '@react-hook/window-size'

const { Title } = Typography

interface ShantenFormValues {
  tiles: string
}

const exampleValues: ShantenFormValues = {
  tiles: '34568m235p688s'
}

const ShantenForm: React.FC<
FormProps<ShantenFormValues>
> = (props) => {
  const [form] = Form.useForm<ShantenFormValues>()
  return (
    <Form<ShantenFormValues>
      form={form}
      labelCol={{ span: 8 }}
      wrapperCol={{ span: 16 }}
      style={{ maxWidth: 600 }}
      {...props}>

      <Form.Item
        label="手牌"
        name="tiles"
        rules={[
          {
            required: true,
            message: '请填入手牌！'
          },
          tilesRule({
            maxLength: 14,
            allowRestModThree: [1, 2]
          })
        ]}
        validateFirst
      >
        <Input/>
      </Form.Item>

      <Form.Item wrapperCol={{
        offset: 8,
        span: 16
      }}>
        <Space wrap>
          <Button type="primary" htmlType="submit">
            计算
          </Button>
          <Button onClick={() => {
            form.resetFields()
          }}>
            清空
          </Button>
          <Button onClick={() => {
            form.setFieldsValue(exampleValues)
          }}>
            填入示例
          </Button>
        </Space>
      </Form.Item>
    </Form>
  )
}

const ShantenWithoutGotView: React.FC<{
  shantenInfo: ShantenWithoutGot
}> = ({ shantenInfo }) => {
  const windowWidth = useWindowWidth()
  const tileSize = windowWidth < 540 ? 'small' : 'normal'

  return (
    <Descriptions title="向听计算（未摸牌）" column={1}>
      <Descriptions.Item label="向听数">
        {shantenInfo.shantenNum
        }</Descriptions.Item>
      <Descriptions.Item label="进张">
        <div style={{ overflowWrap: 'break-word' }}>
          <Tiles tiles={shantenInfo.advance} size={tileSize} sorted/>
          <span>（{shantenInfo.advanceNum}张）</span>
        </div>
      </Descriptions.Item>
      {shantenInfo.goodShapeAdvance !== null && shantenInfo.goodShapeAdvanceNum !== null
        ? <>
          <Descriptions.Item label="好型进张">
            <Tiles
              tiles={shantenInfo.goodShapeAdvance} sorted/>
            <span>（{shantenInfo.goodShapeAdvanceNum}张）</span>
          </Descriptions.Item>
          <Descriptions.Item label="好型率">
            {((shantenInfo.goodShapeAdvanceNum ?? 0) / shantenInfo.advanceNum * 100).toFixed(2)}%
          </Descriptions.Item>
        </>
        : null}
    </Descriptions>
  )
}

const ShantenWithGotView: React.FC<{
  shantenInfo: ShantenWithGot
}> = ({ shantenInfo }) => {
  const {
    discardToAdvance,
    ankanToAdvance
  } = shantenInfo
  const grouped = new Map<number, Map<['discard' | 'ankan', Tile], ShantenWithoutGot>>()
  discardToAdvance.forEach((shantenAfterDiscard, discard) => {
    if (!grouped.has(shantenAfterDiscard.shantenNum)) {
      grouped.set(shantenAfterDiscard.shantenNum, new Map())
    }
    grouped.get(shantenAfterDiscard.shantenNum)?.set(['discard', discard], shantenAfterDiscard)

    shantenAfterDiscard.advance.sort((a, b) => a.compareTo(b))
    if (shantenAfterDiscard.shantenNum === 1) {
      shantenAfterDiscard.goodShapeAdvance?.sort((a, b) => a.compareTo(b))
    }
  })

  ankanToAdvance.forEach((shantenAfterAnkan, ankan) => {
    if (!grouped.has(shantenAfterAnkan.shantenNum)) {
      grouped.set(shantenAfterAnkan.shantenNum, new Map())
    }
    grouped.get(shantenAfterAnkan.shantenNum)?.set(['ankan', ankan], shantenAfterAnkan)

    shantenAfterAnkan.advance.sort((a, b) => a.compareTo(b))
    if (shantenAfterAnkan.shantenNum === 1) {
      shantenAfterAnkan.goodShapeAdvance?.sort((a, b) => a.compareTo(b))
    }
  })

  const ordered = [...grouped.entries()]
  ordered.sort((a, b) => a[0] - b[0])

  return (
    <React.Fragment>
      <Descriptions title="向听计算（已摸牌）">
        <Descriptions.Item label="向听数">
          {shantenInfo.shantenNum}
          {
            shantenInfo.shantenNum === -1
              ? '（和牌）'
              : ''
          }
        </Descriptions.Item>
      </Descriptions>
      {ordered.map(([shantenNum, infos]) => {
        let title = shantenNum === 0 ? '听牌' : `${shantenNum}向听`
        if (shantenNum !== shantenInfo.shantenNum) {
          title += '（退向）'
        }

        const curGroup = [...infos.entries()]
        curGroup.sort((a, b) => {
          if (a[1].advanceNum !== b[1].advanceNum) {
            return a[1].advanceNum - b[1].advanceNum
          } else if (a[1].goodShapeAdvanceNum !== null && b[1].goodShapeAdvanceNum !== null) {
            return a[1].goodShapeAdvanceNum - b[1].goodShapeAdvanceNum
          } else {
            return 0
          }
        }).reverse()

        const data: ActionToDiscard[] = curGroup.map(([action, shantenAfterAction]) => {
          return {
            action: [action[0] === 'discard' ? '打' : '暗杠', action[1]],
            advance: shantenAfterAction.advance,
            advanceNum: shantenAfterAction.advanceNum,
            goodShapeAdvance: shantenAfterAction.goodShapeAdvance,
            goodShapeAdvanceNum: shantenAfterAction.goodShapeAdvanceNum,
            goodShapeRate: shantenAfterAction.goodShapeAdvanceNum !== null
              ? shantenAfterAction.goodShapeAdvanceNum / shantenAfterAction.advanceNum
              : null
          }
        })

        return (
          <>
            <Title level={5}>{title} </Title>
            <ActionToDiscardTable
              dataSource={data}
              showGoodShapeInfo={shantenNum === 1}
            />
          </>
        )
      })}
    </React.Fragment>
  )
}

const ShantenTab: React.FC = () => {
  const [result, setResult] = useState<UnionShantenResult>()
  const [error, setError] = useState<unknown>()

  const onFinish = (values: ShantenFormValues): void => {
    try {
      const tiles = Tile.parseTiles(values.tiles)
      if (tiles !== undefined) {
        const r = shanten(tiles)
        setResult(r)
        setError(undefined)
      } else {
        throw new Error('解析牌代码时发生错误')
      }
    } catch (e) {
      setResult(undefined)
      setError(e)
    }
  }

  return (
    <React.Fragment>
      <ShantenForm
        name="shanten"
        onFinish={onFinish}/>
      <Alert
        message="发生错误"
        description={error?.toString()}
        type="error"
        style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}
      />
      {
        (result != null)
          ? (
              result?.shantenInfo.type === 'ShantenWithoutGot'
                ? <ShantenWithoutGotView shantenInfo={result.shantenInfo}/>
                : <ShantenWithGotView shantenInfo={result.shantenInfo}/>
            )
          : null
      }
    </React.Fragment>
  )
}

export default ShantenTab
