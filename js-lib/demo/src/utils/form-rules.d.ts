import { type FormRule } from 'antd';
export declare function tilesRule(opts?: {
    minLength?: number;
    maxLength?: number;
    allowRestModThree?: number[];
    validateTrigger?: string | string[];
}): FormRule;
export declare const tileRule: FormRule;
