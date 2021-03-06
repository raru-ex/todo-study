import { State, StateContext, Action, Selector } from '@ngxs/store';
import { TodoAction } from './todo.actions';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Todo } from '../model';

export interface TodoStateModel {
  todos: Todo[];
  selectedId: number;
}

export module TodoStateConfig {
  export const UNIQUE_NAME = 'TodoStateModel';

  export const DEFAULT_STATE = {
    name: TodoStateConfig.UNIQUE_NAME,
    defaults: {
      todos: [],
      selectedId: -1
    }
  };

  export const API = {
    LOAD: 'api/v1/todo',
    CREATE: 'api/v1/todo',
    UPDATE: 'api/v1/todo/:id',
    DELETE: 'api/v1/todo/:id',
    ERROR_TEST: 'api/v1/todo/error1',
    ERROR_TEST_JSON: 'api/v1/todo/error2',
  };
}

@State<TodoStateModel>(TodoStateConfig.DEFAULT_STATE)
export class TodoState {
  constructor(private http: HttpClient) {}

  @Selector()
  static getState(state: TodoStateModel): TodoStateModel {
    return state;
  }

  @Selector()
  static getRows(state: TodoStateModel): Todo[] {
    return state.todos;
  }

  @Selector()
  static getSelected(state: TodoStateModel): Todo {
    return state.todos.find(todo => todo.id === state.selectedId);
  }

  @Action(TodoAction.Load)
  load(ctx: StateContext<TodoStateModel>) {
    return this.http.get(TodoStateConfig.API.LOAD).pipe(
      tap((data: {rows: Todo[]}) => {
        ctx.setState({
            'todos': data.rows,
            'selectedId': 1
          }
        );
      })
    );
  }

  @Action(TodoAction.Reload)
  reload(ctx: StateContext<TodoStateModel>) {
    return this.http.get(TodoStateConfig.API.LOAD).pipe(
      tap((data: {rows: Todo[]}) => {
        ctx.patchState({
            'todos': data.rows
          }
        );
      })
    );
  }

  @Action(TodoAction.Select)
  select(ctx: StateContext<TodoStateModel>, action: TodoAction.Select) {
    ctx.patchState({
      selectedId: action.payload.id
    });
  }

  @Action(TodoAction.Create)
  create(ctx: StateContext<TodoStateModel>, action: TodoAction.Create) {
    const body = { name: action.payload.name, content: action.payload.content};
    return this.http.post(
      TodoStateConfig.API.CREATE,
      body
    ).pipe(
      tap(_ => {
        ctx.dispatch(new TodoAction.Reload());
      })
    );
  }

  @Action(TodoAction.Update)
  update(ctx: StateContext<TodoStateModel>, action: TodoAction.Update) {
    const body = action.payload;
    const url = this.bindUrlParams(
      TodoStateConfig.API.UPDATE,
      { id: action.payload.id }
    );
    return this.http.put(
      url,
      body
    ).pipe(
      tap(_ => {
        ctx.dispatch(new TodoAction.Reload());
        }
      )
    );
  }

  @Action(TodoAction.Delete)
  delete(ctx: StateContext<TodoStateModel>, action: TodoAction.Delete) {
    const url = this.bindUrlParams(
      TodoStateConfig.API.DELETE,
      { id: action.payload.id }
    );
    return this.http.delete(url).pipe(
      tap(_ => {
        ctx.dispatch(new TodoAction.Reload());
      })
    );
  }

  // ======== エラー動作検証
  @Action(TodoAction.ErrorTest)
  errorTest() {
    console.log('========== error test');
    return this.http.get(TodoStateConfig.API.ERROR_TEST).pipe(
      tap(response => {
        console.log(response);
      })
    );
  }

  @Action(TodoAction.ErrorTestJson)
  errorJson() {
    console.log('========== json error test');
    return this.http.post(TodoStateConfig.API.ERROR_TEST_JSON, { name: null }).pipe(
      tap(response => {
        console.log(response);
      })
    );
  }

  private bindUrlParams(url: string, params): string {
    let resultUrl = url;
    Object.entries(params).forEach(keyValue => {
      resultUrl = url.replace(':' + keyValue[0], <string>keyValue[1]);
    });
    return resultUrl;
  }
}
