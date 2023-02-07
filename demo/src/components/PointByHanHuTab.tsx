import React, { useState } from 'react'
import { Alert, Button, Descriptions, Form, type FormProps, InputNumber, Radio, Space } from 'antd'
import { getChildPointByHanHu, getParentPointByHanHu } from 'mahjong-utils'

interface PointByHanHuFormValues {
  han: number
  hu: number
  isParent: boolean
}

const exampleValues: PointByHanHuFormValues = {
  han: 3,
  hu: 40,
  isParent: true
}

const PointByHanHuForm: React.FC<FormProps<PointByHanHuFormValues>> = (props) => {
  const [form] = Form.useForm<PointByHanHuFormValues>()
  return (
    <Form<PointByHanHuFormValues>
      form={form}
      labelCol={{ span: 8 }}
      wrapperCol={{ span: 16 }}
      style={{ maxWidth: 600 }}
      {...props}>

      <Form.Item
        label="番数"
        name="han"
        rules={[{
          required: true,
          message: '请填入番数！'
        }]}
      >
        <InputNumber min={1} max={13} step={1}/>
      </Form.Item>

      <Form.Item
        label="符数"
        name="hu"
        rules={[{
          required: true,
          message: '请填入符数！'
        }]}
      >
        <InputNumber min={20} max={140} step={10}/>
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

export const PointByHanHuResult: React.FC<{
  ron: number
  tsumoParent: number
  tsumoChild: number
  isParent: boolean
}> = ({
  isParent,
  ron,
  tsumoChild,
  tsumoParent
}) => {
  return (
    <Descriptions title="番符算点" column={1}>
      {ron !== 0
        ? <Descriptions.Item label="荣和">
          {ron}点
        </Descriptions.Item>
        : null}
      {(tsumoChild !== 0 || tsumoParent !== 0)
        ? <Descriptions.Item label="自摸">
          {tsumoChild !== 0 ? `子家${tsumoChild}点` : ''}
          {(tsumoChild !== 0 && tsumoParent !== 0) ? '，' : ''}
          {tsumoParent !== 0 ? `亲家${tsumoParent}点` : ''}
          （共{isParent ? tsumoChild * 3 : tsumoChild * 2 + tsumoParent}点）
        </Descriptions.Item>
        : null}
    </Descriptions>
  )
}

const PointByHanHuTab: React.FC = () => {
  const [isParent, setIsParent] = useState(true)
  const [result, setResult] = useState<{ ron: number, tsumoParent: number, tsumoChild: number }>()
  const [error, setError] = useState<unknown>()

  const onFinish = (values: PointByHanHuFormValues): void => {
    try {
      setIsParent(values.isParent)
      if (values.isParent) {
        const r = getParentPointByHanHu(values.han, values.hu)
        setResult({
          ron: r.ron,
          tsumoChild: r.tsumo,
          tsumoParent: 0
        })
      } else {
        setResult(getChildPointByHanHu(values.han, values.hu))
      }
      setError(undefined)
    } catch (e) {
      setResult(undefined)
      setError(e)
    }
  }

  return (
    <React.Fragment>
      <PointByHanHuForm
        name="shanten"
        onFinish={onFinish}/>
      <Alert
        message="发生错误"
        description={error?.toString()}
        type="error"
        style={{ visibility: error !== undefined ? 'visible' : 'hidden' }}
      />
      <PointByHanHuResult
        isParent={isParent}
        ron={result?.ron ?? 0}
        tsumoChild={result?.tsumoChild ?? 0}
        tsumoParent={result?.tsumoParent ?? 0}
      />
    </React.Fragment>
  )
}

export default PointByHanHuTab
