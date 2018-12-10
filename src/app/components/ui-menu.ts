import { Component, OnInit, Input } from '@angular/core';
import { Store } from '@ngxs/store';
import { Todo } from '@shared/model'
import { TodoStateModel, TodoAction } from '@shared/state'

@Component({
  selector: 'ui-menu',
  templateUrl: './ui-menu.html',
  styleUrls: ['./ui-menu.scss']
})
export class UiMenuComponent implements OnInit {
  @Input('rows') todos: Todo[]

  constructor(
    private store: Store
  ) { }

  ngOnInit() {
  }

  onClickMenuItem(todo: TodoStateModel) {
    console.log(todo)
    this.store.dispatch(new TodoAction.Select(todo))
  }

}
