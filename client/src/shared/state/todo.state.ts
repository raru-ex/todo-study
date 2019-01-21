import { State, StateContext, Action, Selector } from '@ngxs/store'
import { TodoAction } from './todo.actions'
import { Todo } from '@shared/model'
import {HttpClient} from "@angular/common/http";
import {tap} from "rxjs/operators";

export interface TodoStateModel {
  todos: Todo[]
  selectedIndex: number
}

export module CompanionTodoState {
  export const UNIQUE_NAME = 'TodoStateModel'

  export const DEFAULT_STATE = {
    name: CompanionTodoState.UNIQUE_NAME,
    defaults: {
      todos: [],
      selectedIndex: -1
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
  static getTodos(state: TodoStateModel): Todo[] {
    return state.todos
  }

  @Selector()
  static getSelected(state: TodoStateModel): Todo {
    console.log("============ called get selected ================")
    console.log(state.todos[state.selectedIndex])
    return state.todos[state.selectedIndex]
  }

  @Action(TodoAction.Load)
  load(ctx: StateContext<TodoStateModel>) {
    //TODO: loadの結果取得前によばれてる
    console.log("============ called load ================")
    return this.http.get(CompanionTodoState.API.LOAD).pipe(
      tap(data => {
        console.log(data)
        ctx.patchState({
            "todos": data.rows,
            "selectedIndex": 1
          }
        )
      })
    )
  }

  @Action(TodoAction.Select)
  select(ctx: StateContext<TodoStateModel>, action: TodoAction.Select) {
    const currentState = ctx.getState()
    const todo = action.todo
    const selectedIndex = currentState.todos.findIndex(item => item.id === todo.id)
    console.log(selectedIndex)
    console.log(todo)
    ctx.patchState({
      selectedIndex: selectedIndex
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
