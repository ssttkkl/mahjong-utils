export class InvalidTypeException extends Error {
  constructor (type: string) {
    super('invalid type: ' + type)
  }
}
