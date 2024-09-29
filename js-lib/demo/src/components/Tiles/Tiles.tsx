import React from 'react'
import { type Tile } from 'mahjong-utils'
import './Tiles.css'

const Tiles: React.FC<{
  tiles: Tile[]
  sorted?: boolean
  size?: 'large' | 'normal' | 'small'
}> = (props) => {
  let tiles_ = props.tiles
  if (props.sorted === true) {
    tiles_ = [...tiles_]
    tiles_.sort((a, b) => a.compareTo(b))
  }

  return (
    <>
      {tiles_.map(x => {
        const text = x.toString()
        return <img src={`/assets/images/tiles/${text.toLowerCase()}.png`}
                    alt={text} key={text} className={`tile-${props.size ?? 'normal'}`}/>
      })}
    </>
  )
}

export default Tiles
