import { Todo } from '../model'

export module TodoAction {
  export const PREFIX = '[TODO] '

  export const LOAD   = PREFIX + 'Load'
  export const RELOAD = PREFIX + 'Reload'
  export const SELECT = PREFIX + 'Select'
  export const CREATE = PREFIX + 'Create'
  export const UPDATE = PREFIX + 'Update'
  export const DELETE = PREFIX + 'Delete'
  export const ERROR_TEST = PREFIX + 'ERROR_TEST'
  export const ERROR_TEST_JSON = PREFIX + 'ERROR_TEST_JSON'

  export class Load {
    static readonly type = LOAD
  }

  export class Reload {
    static readonly type = RELOAD
  }

  export class Select {
    static readonly type = SELECT
    constructor(public payload: { id: number }) {}
  }

  export class Create {
    static readonly type = CREATE
    constructor(public payload: Todo.NoId) {}
  }

  export class Update {
    static readonly type = UPDATE
    constructor(public payload: Todo) {}
  }

  export class Delete {
    static readonly type = DELETE
    constructor(public payload: { id: number }) {}
  }

  export class ErrorTest {
    static readonly type = ERROR_TEST
  }

  export class ErrorTestJson {
    static readonly type = ERROR_TEST_JSON
  }
}
