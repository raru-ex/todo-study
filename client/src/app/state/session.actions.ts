export module SessionAction {
  export const PREFIX = '[SESSION]';

  export const LOGOUT = PREFIX + 'LOGOUT';

  export class Logout {
    static readonly type = LOGOUT
  }
}
