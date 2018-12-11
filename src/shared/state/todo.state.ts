import { State, StateContext, Action, Selector } from '@ngxs/store'
import { TodoAction } from './todo.actions'
import { Todo } from '@shared/model'

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

  export const TEST_STATE: TodoStateModel = {
    todos: [
      {id: 1, name: 'test1', content: 'content1'},
      {id: 2, name: 'test2', content: 'content2'},
      {id: 3, name: 'test3', content: 'content3'}
    ],
    selectedIndex: 0
  }
}

@State<TodoStateModel>(CompanionTodoState.DEFAULT_STATE)
export class TodoState {

  @Selector()
  static getTodos(state: TodoStateModel): Todo[] {
    return state.todos
  }

  @Selector()
  static getSelected(state: TodoStateModel): Todo {
    return state.todos[state.selectedIndex]
  }

  @Action(TodoAction.Load)
  load(ctx: StateContext<TodoStateModel>) {
    const state = ctx.getState()
    ctx.setState({
      ...state,
      ...(CompanionTodoState.TEST_STATE)
    })
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
}
