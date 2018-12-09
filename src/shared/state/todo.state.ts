import { State, StateContext, Action, Selector } from '@ngxs/store'
import { TodoAction } from './todo.actions'
import { Todo } from '@shared/model'

export interface TodoStateModel {
  todos: Todo[]
  selectedIndex: number
}

class CompanionTodoState {
  static readonly UNIQUE_NAME = 'TodoStateModel'

  public static DEFAULT_STATE = {
    name: CompanionTodoState.UNIQUE_NAME,
    defaults: {
      todos: [],
      selectedIndex: -1
    }
  }

  public static TEST_STATE: TodoStateModel = {
    todos: [
      {name: 'test1', content: 'content1'},
      {name: 'test2', content: 'content2'},
      {name: 'test3', content: 'content3'}
    ],
    selectedIndex: 0
  }
}

@State<TodoStateModel>(CompanionTodoState.DEFAULT_STATE)
export class TodoState {


  @Action(TodoAction.SelectAll)
  selectAll(ctx: StateContext<TodoStateModel>) {
    const state = ctx.getState()
    ctx.setState({
      ...state,
      ...(CompanionTodoState.TEST_STATE)
    })
  }

  @Selector()
  static getState(state: TodoStateModel) {
    return state
  }
}
