import React from 'react'
import { type ColumnsType } from 'antd/es/table'
import { Tile } from 'mahjong-utils'
import { Table } from 'antd'
import { type TableProps } from 'antd/lib'
import Tiles from './Tiles/Tiles'
import { useWindowWidth } from '@react-hook/window-size'

export interface ActionToDiscard {
  action: Array<string | Tile> | string
  advance: Tile[]
  advanceNum: number
  goodShapeAdvance?: Tile[]
  goodShapeAdvanceNum?: number
  goodShapeRate?: number
}

export type ActionToDiscardTableProps = TableProps<ActionToDiscard> & {
  showGoodShapeInfo?: boolean
}

const ActionToDiscardTable: React.FC<ActionToDiscardTableProps> = (props) => {
  const windowWidth = useWindowWidth()
  const tileSize = windowWidth < 540 ? 'small' : 'normal'
  const tableSize = windowWidth < 540 ? 'middle' : 'large'

  const columns: ColumnsType<ActionToDiscard> = [
    {
      title: '',
      key: 'action',
      dataIndex: 'action',
      render: (action) => {
        if (action instanceof Array) {
          const children: React.ReactNode[] = []
          let pending: Tile[] = []
          action.forEach(value => {
            if (typeof value === 'string') {
              if (pending.length > 0) {
                children.push(<Tiles key={Tile.tilesToString(pending)} tiles={pending} size={tileSize} sorted/>)
                pending = []
              }
              children.push(<span>{value}</span>)
            } else {
              pending.push(value as Tile)
              console.log(pending)
            }
          })
          if (pending.length > 0) {
            children.push(<Tiles key={Tile.tilesToString(pending)} tiles={pending} size={tileSize} sorted/>)
          }
          return <React.Fragment>{children}</React.Fragment>
        } else {
          return <span>{action}</span>
        }
      }
    },
    {
      title: '进张',
      key: 'advance',
      dataIndex: 'advance',
      sorter: (a, b) => {
        return a.advance.length - b.advance.length
      },
      render: (advance, record) => (
        <>
          <Tiles tiles={advance} size={tileSize} sorted/>
          <span>（共{record.advanceNum}张）</span>
        </>
      )
    },
    {
      title: '好型进张',
      key: 'goodShapeAdvance',
      dataIndex: 'goodShapeAdvance',
      sorter: (a, b) => {
        return (a.goodShapeAdvance?.length ?? 0) - (b.goodShapeAdvance?.length ?? 0)
      },
      render: (goodShapeAdvance, record) => {
        if (goodShapeAdvance !== undefined) {
          return (
            <>
              <Tiles tiles={goodShapeAdvance} size={tileSize} sorted/>
              <span>（共{record.goodShapeAdvanceNum}张）</span>
            </>
          )
        } else {
          return null
        }
      }
    },
    {
      title: '好型率',
      key: 'goodShapeRate',
      dataIndex: 'goodShapeRate',
      sorter: (a, b) => {
        return (a.goodShapeRate ?? 0) - (b.goodShapeRate ?? 0)
      },
      render: (value: number | undefined) => value !== undefined ? (value * 100).toFixed(2) + '%' : ''
    }
  ]

  if (props.showGoodShapeInfo !== true) {
    columns.pop()
    columns.pop()
  }

  return (
    <Table
      columns={columns}
      pagination={false}
      size={tableSize}
      {...props}
    />
  )
}

export default ActionToDiscardTable
