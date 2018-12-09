export class TodoAction {
  static readonly prefix = '[TODO] '
  static readonly SELECT_ALL = TodoAction.prefix + 'selectAll'

  public static SelectAll = class {
    static readonly type = TodoAction.SELECT_ALL
    constructor() {}
  }
}
