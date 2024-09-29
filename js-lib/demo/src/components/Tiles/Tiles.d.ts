import React from 'react';
import { type Tile } from 'mahjong-utils';
import './Tiles.css';
declare const Tiles: React.FC<{
    tiles: Tile[];
    sorted?: boolean;
    size?: 'large' | 'normal' | 'small';
}>;
export default Tiles;
