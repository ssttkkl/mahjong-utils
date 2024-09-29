import { Tile } from 'mahjong-utils'
import { type FormRule } from 'antd'

export function tilesRule (opts: {
  minLength?: number
  maxLength?: number
  allowRestModThree?: number[]
  validateTrigger?: string | string[]
} = {}): FormRule {
  return {
    validateTrigger: opts.validateTrigger ?? 'onSubmit',
    validator: async (_rule, value) => {
      const tiles = Tile.parseTiles(value)
      if (tiles === undefined) {
        throw new Error('请填入合法的牌代码！')
      }
      if (opts.minLength !== undefined && tiles.length < opts.minLength) {
        throw new Error(`手牌不得少于${opts.minLength}张！`)
      }
      if (opts.maxLength !== undefined && tiles.length > opts.maxLength) {
        throw new Error(`手牌不得多于${opts.maxLength}张！`)
      }
      if ((opts.allowRestModThree?.every(v => v !== tiles.length % 3)) === true) {
        throw new Error('手牌数量不合法！')
      }
    }
  }
}

export const tileRule: FormRule = {
  validator: async (_rule, value) => {
    const tile = Tile.byText(value)
    if (tile === undefined) {
      throw new Error('请填入合法的牌代码！')
    }
  }
}
