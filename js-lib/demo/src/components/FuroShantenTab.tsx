import { Alert, Button, Descriptions, Form, type FormProps, Input, Radio, Space, Typography } from 'antd'
import React, { useState } from 'react'
import {
  furoChanceShanten,
  type FuroChanceShantenResult,
  type ShantenWithFuroChance,
  type ShantenWithoutGot,
  type Tatsu,
  Tile
} from 'mahjong-utils'
import ActionToDiscardTable, { type ActionToDiscard } from './ActionToDiscardTable'
import { tileRule, tilesRule } from '../utils/form-rules'

const { Title } = Typography

interface FuroChanceShantenFormValues {
  tiles: string
  chanceTile: string
  allowChi: boolean
}

const exampleValues: FuroChanceShantenFormValues = {
  tiles: '3456778m123457p',
  chanceTile: '7m',
  allowChi: true
}

const FuroChanceShantenForm: React.FC<FormProps<FuroChanceShantenFormValues>> = (props) => {
  const [form] = Form.useForm<FuroChanceShantenFormValues>()
  return (
    <Form<FuroChanceShantenFormValues>
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
            minLength: 4,
            maxLength: 13,
            allowRestModThree: [1]
          })
        ]}
        validateFirst
      >
        <Input/>
      </Form.Item>

      <Form.Item
        label="机会牌"
        name="chanceTile"
        rules={[
          {
            required: true,
            message: '请填入机会牌！'
          },
          tileRule
        ]}
        validateFirst
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

const ShantenWithFuroChanceView: React.FC<{
  shantenInfo: ShantenWithFuroChance
}> = ({ shantenInfo }) => {
  const {
    pass,
    chi,
    pon,
    minkan
  } = shantenInfo
  const grouped = new Map<number, Map<['pass'] | ['minkan'] | ['pon', Tile] | ['chi', Tatsu, Tile], ShantenWithoutGot>>()

  if (pass !== undefined) {
    if (!grouped.has(pass.shantenNum)) {
      grouped.set(pass.shantenNum, new Map())
    }
    grouped.get(pass.shantenNum)?.set(['pass'], pass)
  }

  if (minkan !== undefined) {
    if (!grouped.has(minkan.shantenNum)) {
      grouped.set(minkan.shantenNum, new Map())
    }
    grouped.get(minkan.shantenNum)?.set(['minkan'], minkan)
  }

  if (pon !== undefined) {
    pon.discardToAdvance.forEach((shantenAfterPonDiscard, discard) => {
      if (!grouped.has(shantenAfterPonDiscard.shantenNum)) {
        grouped.set(shantenAfterPonDiscard.shantenNum, new Map())
      }
      grouped.get(shantenAfterPonDiscard.shantenNum)?.set(['pon', discard], shantenAfterPonDiscard)
    })
  }

  chi.forEach(([tatsu, shantenAfterChi]) => {
    shantenAfterChi.discardToAdvance.forEach((shantenAfterChiDiscard, discard) => {
      if (!grouped.has(shantenAfterChiDiscard.shantenNum)) {
        grouped.set(shantenAfterChiDiscard.shantenNum, new Map())
      }
      grouped.get(shantenAfterChiDiscard.shantenNum)?.set(['chi', tatsu, discard], shantenAfterChiDiscard)
    })
  })

  const ordered = [...grouped.entries()]
  ordered.sort((a, b) => a[0] - b[0])

  return (
    <React.Fragment>
      <Descriptions title="副露判断">
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
          } else if (a[1].goodShapeAdvanceNum !== undefined && b[1].goodShapeAdvanceNum !== undefined) {
            return a[1].goodShapeAdvanceNum - b[1].goodShapeAdvanceNum
          } else {
            return 0
          }
        }).reverse()

        const data: ActionToDiscard[] = curGroup.map(([action, shantenAfterAction]) => {
          let actionText: Array<string | Tile> = []
          switch (action[0]) {
            case 'pass':
              actionText = ['PASS']
              break
            case 'chi':
              actionText = [action[1].first, action[1].second, '吃打', action[2]]
              break
            case 'pon':
              actionText = ['碰打', action[1]]
              break
            case 'minkan':
              actionText = ['大明杠']
              break
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

const FuroChanceShantenTab: React.FC = () => {
  const [result, setResult] = useState<FuroChanceShantenResult>()
  const [error, setError] = useState<unknown>()

  const onFinish = (values: FuroChanceShantenFormValues): void => {
    try {
      const tiles = Tile.parseTiles(values.tiles)
      const chanceTile = Tile.byText(values.chanceTile)
      if (tiles !== undefined && chanceTile !== undefined) {
        const r = furoChanceShanten(tiles, chanceTile, {
          allowChi: values.allowChi
        })
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
      <FuroChanceShantenForm
        name="shanten"
        onFinish={onFinish}/>
      <Alert
        message="发生错误"
        description={error?.toString()}
        type="error"
        style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}
      />
      {(result != null)
        ? <ShantenWithFuroChanceView shantenInfo={result.shantenInfo}/>
        : null}
    </React.Fragment>
  )
}

export default FuroChanceShantenTab
