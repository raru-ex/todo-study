import { UnstoredTodo } from '@shared/model'

export module TodoAction {
  export const PREFIX = '[TODO] '

  export const LOAD   = PREFIX + 'Load'
  export const SELECT = PREFIX + 'Select'
  export const CREATE = PREFIX + 'Create'

  export class Load {
    static readonly type = LOAD
  }

  export class Select {
    static readonly type = SELECT
    constructor(public payload: { id: number }) {}
  }

  export class Create {
    static readonly type = CREATE
    constructor(public unstoredTodo: UnstoredTodo) {}
  }
}
