import { Alert, Button, Descriptions, Form, type FormProps, Input, InputNumber, Radio, Select, Space } from 'antd'
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons'
import React, { useState } from 'react'
import { buildHora, Furo, getChildPointByHanHu, getParentPointByHanHu, type Hora, Tile, Wind } from 'mahjong-utils'
import { tileRule, tilesRule } from '../utils/form-rules'
import { doubleTimesYakuman, yakuName } from '../utils/yakuName'
import { PointByHanHuResult } from './PointByHanHuTab'
import { type ExtraYaku } from 'mahjong-utils/dist/hora/yaku'

interface HoraFormValues {
  tsumo: boolean
  tiles: string
  roundWind: Wind
  agari: string
  dora: number
  selfWind: Wind
  furo: string[]
  extraYaku: ExtraYaku[]
}

const exampleValues: HoraFormValues = {
  tiles: '12233466m111z',
  furo: ['789p'],
  agari: '1z',
  tsumo: true,
  dora: 2,
  selfWind: Wind.East,
  roundWind: Wind.East,
  extraYaku: ['Richi', 'Ippatsu']
}

const HoraForm: React.FC<FormProps<HoraFormValues>> = (props) => {
  const [form] = Form.useForm<HoraFormValues>()
  return (
    <Form<HoraFormValues>
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
            minLength: 2,
            maxLength: 14,
            allowRestModThree: [2]
          }),
          ({ getFieldValue }) => ({
            validateTrigger: 'onSubmit',
            async validator (_, value) {
              const tiles = Tile.parseTiles(value)
              const furo = getFieldValue('furo')
              if ((tiles?.length ?? 0) + furo.length * 3 !== 14) {
                throw new Error('手牌数量不合法！')
              }
            }
          })
        ]}
        validateFirst
      >
        <Input/>
      </Form.Item>

      <Form.Item
        label="副露"
      >
        <Form.List
          name="furo">
          {(fields, {
            add,
            remove
          }) => (
            <>
              {fields.map(({
                key,
                name,
                ...restField
              }) => (
                <Space key={key} style={{ display: 'flex' }} align="baseline">
                  <Form.Item
                    {...restField}
                    name={name}
                    rules={[
                      {
                        required: true,
                        message: '请填入副露！'
                      },
                      tilesRule({
                        minLength: 3,
                        maxLength: 3
                      })
                    ]}
                    validateFirst
                  >
                    <Input/>
                  </Form.Item>
                  <MinusCircleOutlined onClick={() => {
                    remove(name)
                  }} onPointerEnterCapture={null} onPointerLeaveCapture={null}/>
                </Space>
              ))}
              {fields.length < 4
                ? <Form.Item>
                  <Button type="dashed" onClick={() => {
                    add()
                  }} block icon={<PlusOutlined onPointerEnterCapture={null} onPointerLeaveCapture={null}/>}>
                    添加
                  </Button>
                </Form.Item>
                : null}
            </>
          )}
        </Form.List>

      </Form.Item>

      <Form.Item
        label="和牌"
        name="agari"
        rules={[
          {
            required: true,
            message: '请填入和牌！'
          },
          tileRule
        ]}
        validateFirst
      >
        <Input/>
      </Form.Item>

      <Form.Item
        label="自摸/荣和"
        name="tsumo"
        initialValue={true}
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
        rules={[{
          required: true,
          message: '请选择自风！'
        }]}
      >
        <Radio.Group>
          <Radio.Button value={Wind.East}>东</Radio.Button>
          <Radio.Button value={Wind.South}>南</Radio.Button>
          <Radio.Button value={Wind.West}>西</Radio.Button>
          <Radio.Button value={Wind.North}>北</Radio.Button>
        </Radio.Group>
      </Form.Item>

      <Form.Item
        label="场风"
        name="roundWind"
        rules={[{
          required: true,
          message: '请选择场风！'
        }]}
      >
        <Radio.Group>
          <Radio.Button value={Wind.East}>东</Radio.Button>
          <Radio.Button value={Wind.South}>南</Radio.Button>
          <Radio.Button value={Wind.West}>西</Radio.Button>
          <Radio.Button value={Wind.North}>北</Radio.Button>
        </Radio.Group>
      </Form.Item>

      <Form.Item
        name="extraYaku"
        label="额外役"
      >
        <Select
          mode="multiple"
          allowClear
          style={{ width: '100%' }}
          options={[
            {
              value: 'Richi',
              label: yakuName.Richi
            },
            {
              value: 'Ippatsu',
              label: yakuName.Ippatsu
            },
            {
              value: 'Rinshan',
              label: yakuName.Rinshan
            },
            {
              value: 'Chankan',
              label: yakuName.Chankan
            },
            {
              value: 'Haitei',
              label: yakuName.Haitei
            },
            {
              value: 'Houtei',
              label: yakuName.Houtei
            },
            {
              value: 'WRichi',
              label: yakuName.WRichi
            },
            {
              value: 'Tenhou',
              label: yakuName.Tenhou
            },
            {
              value: 'Chihou',
              label: yakuName.Chihou
            }
          ]}/>
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

const HoraView: React.FC<{
  hora: Hora
}> = ({ hora }) => {
  let timesOfYakuman = 0
  if (hora.hasYakuman) {
    hora.yaku.forEach(yk => {
      if (doubleTimesYakuman.find(x => x === yk) !== undefined) {
        timesOfYakuman += 2
      } else {
        timesOfYakuman += 1
      }
    })
  }

  const isParent = hora.pattern.selfWind === hora.pattern.roundWind
  let point: { ron: number, tsumoParent: number, tsumoChild: number } = {
    ron: 0,
    tsumoParent: 0,
    tsumoChild: 0
  }

  if (isParent) {
    let r: { ron: number, tsumo: number } = {
      ron: 0,
      tsumo: 0
    }
    if (timesOfYakuman > 0) {
      r = getParentPointByHanHu(13, 20)
      r.ron *= timesOfYakuman
      r.tsumo *= timesOfYakuman
    } else if (hora.han > 0) {
      r = getParentPointByHanHu(hora.han, hora.hu)
    }
    point = {
      ron: r.ron,
      tsumoChild: r.tsumo,
      tsumoParent: 0
    }
  } else {
    if (timesOfYakuman > 0) {
      point = getChildPointByHanHu(13, 20)
      point.ron *= timesOfYakuman
      point.tsumoParent *= timesOfYakuman
      point.tsumoChild *= timesOfYakuman
    } else if (hora.han > 0) {
      point = getChildPointByHanHu(hora.han, hora.hu)
    }
  }

  return (
    <React.Fragment>
      <Descriptions title="和牌分析" column={1}>
        <Descriptions.Item label="番数">
          {hora.hasYakuman
            ? `${timesOfYakuman}倍役满`
            : (hora.han >= 13 ? `累计役满（${hora.han}番）` : `${hora.han}番`)}
        </Descriptions.Item>
        <Descriptions.Item label="符数">
          {hora.hu}
        </Descriptions.Item>
        <Descriptions.Item label="役种">
          <Space wrap>
            {hora.yaku.map(x => (
              <span key={x}>{yakuName[x]}</span>
            ))}
          </Space>
        </Descriptions.Item>
      </Descriptions>
      <PointByHanHuResult isParent={isParent} {...point}/>
    </React.Fragment>
  )
}

const HoraTab: React.FC = () => {
  const [result, setResult] = useState<Hora>()
  const [error, setError] = useState<unknown>()

  const onFinish = (values: HoraFormValues): void => {
    try {
      const tiles = Tile.parseTiles(values.tiles)
      const furo = values.furo.map(value => {
        const x = Furo.parse(value)
        if (x === undefined) {
          throw new Error('解析牌代码时发生错误')
        }
        return x
      })
      const agari = Tile.byText(values.agari)

      if (tiles !== undefined && agari !== undefined) {
        const r = buildHora({
          ...values,
          tiles,
          furo,
          agari
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
      <HoraForm
        name="hora"
        onFinish={onFinish}/>
      <Alert
        message="发生错误"
        description={error?.toString()}
        type="error"
        style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}
      />
      {(result != null)
        ? <HoraView hora={result}/>
        : null}

    </React.Fragment>
  )
}

export default HoraTab
