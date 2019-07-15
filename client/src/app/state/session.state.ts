import { State, StateContext, Action } from '@ngxs/store';
import { HttpClient } from '@angular/common/http';
import {SessionAction} from '@app/state/session.actions';
import {tap} from 'rxjs/operators';

export interface SessionStateModel {
  userId?: number;
}

export module SessionStateConfig {
  export const UNIQUE_NAME = 'SessionStateModel';

  export const DEFAULT_STATE = {
    name: SessionStateConfig.UNIQUE_NAME,
    defaults: {
      userId: null
    }
  }

  export const API = {
    LOGOUT: 'api/v1/logout'
  }
}

@State<SessionStateModel>(SessionStateConfig.DEFAULT_STATE)
export class SessionState {
  constructor(private http: HttpClient) {}

  @Action(SessionAction.Logout)
  logout(ctx: StateContext<SessionStateModel>) {
    console.log("logout")
    return this.http.get(SessionStateConfig.API.LOGOUT).pipe(
      tap(_ => {
        ctx.patchState(SessionStateConfig.DEFAULT_STATE.defaults)
        window.location.href = '/login'
      })
    )
  }
}
