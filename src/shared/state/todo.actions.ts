export module TodoAction {
  export const PREFIX = '[TODO] '
  export const SELECT_ALL = PREFIX + 'selectAll'

  export class SelectAll {
    static readonly type = SELECT_ALL
    constructor() {}
  }
}
