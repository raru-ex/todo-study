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
    LOAD: 'api/v1/todo'
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
  static getSelected(state: TodoStateModel): Todo | undefined {
    console.log("called get selected")
    return state.todos.find(todo => todo.id === state.selectedId)
  }

  @Action(TodoAction.Load)
  load(ctx: StateContext<TodoStateModel>) {
    //TODO: loadの結果取得前によばれてる
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

  @Action(TodoAction.Select)
  select(ctx: StateContext<TodoStateModel>, action: TodoAction.Select) {
    ctx.patchState({
      selectedId: action.payload.id
    })
  }

  @Action(TodoAction.Create)
  create(ctx: StateContext<TodoStateModel>, action: TodoAction.Create) {
    const currentState = ctx.getState()
    const unstoredTodo = action.unstoredTodo
    console.log(action)
    const id = currentState.todos.length + 1
    currentState.todos.push({
      id: id,
      name: unstoredTodo.name,
      content: unstoredTodo.content
    })
    ctx.setState(currentState)
  }
}
