import { TodoStateModel } from '@shared/state'

export module TodoAction {
  export const PREFIX = '[TODO] '
  export const LOAD = PREFIX + 'Load'
  export const SELECT = PREFIX + 'Select'

  export class Load {
    static readonly type = LOAD
  }

  export class Select {
    static readonly type = SELECT
    constructor(public todo: TodoStateModel) {}
  }
}
