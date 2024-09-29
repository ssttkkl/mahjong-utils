"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const react_1 = __importDefault(require("react"));
require("./Tiles.css");
const Tiles = (props) => {
    let tiles_ = props.tiles;
    if (props.sorted === true) {
        tiles_ = [...tiles_];
        tiles_.sort((a, b) => a.compareTo(b));
    }
    return (<>
      {tiles_.map(x => {
            var _a;
            const text = x.toString();
            return <img src={`${process.env.PUBLIC_URL}/assets/images/tiles/${text.toLowerCase()}.png`} alt={text} key={text} className={`tile-${(_a = props.size) !== null && _a !== void 0 ? _a : 'normal'}`}/>;
        })}
    </>);
};
exports.default = Tiles;
