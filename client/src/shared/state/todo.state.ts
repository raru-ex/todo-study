import { State, StateContext, Action, Selector } from '@ngxs/store'
import { TodoAction } from './todo.actions'
import { HttpClient } from "@angular/common/http";
import { tap } from "rxjs/operators";
import { Todo } from '../model'

export interface TodoStateModel {
  todos: Todo[]
  selectedId: number
}

export module CompanionTodoState {
  export const UNIQUE_NAME = 'TodoStateModel'

  export const DEFAULT_STATE = {
    name: CompanionTodoState.UNIQUE_NAME,
    defaults: {
      todos: [],
      selectedId: -1
    }
  }

  export const API = {
    LOAD: 'api/v1/todo',
    CREATE: 'api/v1/todo'
  }
}

@State<TodoStateModel>(CompanionTodoState.DEFAULT_STATE)
export class TodoState {
  constructor(private http: HttpClient) {}

  @Selector()
  static getState(state: TodoStateModel): TodoStateModel {
    console.log("called get state ")
    return state
  }

  @Selector()
  static getRows(state: TodoStateModel): Todo[] {
    return state.todos
  }

  @Selector()
  static getSelected(state: TodoStateModel): Todo {
    console.log("called get selected")
    return state.todos.find(todo => todo.id === state.selectedId)
  }

  @Action(TodoAction.Load)
  load(ctx: StateContext<TodoStateModel>) {
    console.log("============ called load ================")
    return this.http.get(CompanionTodoState.API.LOAD).pipe(
      tap((data: {rows: Todo[]}) => {
        console.log(data)
        ctx.setState({
            "todos": data.rows,
            "selectedId": 1
          }
        )
      })
    )
  }

  @Action(TodoAction.Reload)
  reload(ctx: StateContext<TodoStateModel>) {
    console.log("============ called Reload ================")
    return this.http.get(CompanionTodoState.API.LOAD).pipe(
      tap((data: {rows: Todo[]}) => {
        console.log(data)
        ctx.patchState({
            "todos": data.rows
          }
        )
      })
    )
  }

  @Action(TodoAction.Select)
  select(ctx: StateContext<TodoStateModel>, action: TodoAction.Select) {
    ctx.patchState({
      selectedId: action.payload.id
    })
  }

  @Action(TodoAction.Create)
  create(ctx: StateContext<TodoStateModel>, action: TodoAction.Create) {
    const body = { name: action.payload.name, content: action.payload.content}
    return this.http.post(
      CompanionTodoState.API.CREATE,
      body
    ).pipe(
      tap(_ => {
        ctx.dispatch(new TodoAction.Reload())
      })
    )
  }
}
