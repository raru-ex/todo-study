import { Component, OnInit, Input } from '@angular/core';
import { Store } from '@ngxs/store';
import { Todo } from '@shared/model'
import {TodoAction} from '@shared/state'

@Component({
  selector: 'ui-menu',
  templateUrl: './ui-menu.html',
  styleUrls: ['./ui-menu.scss']
})
export class UiMenuComponent implements OnInit {
  @Input() rows: Todo[]
  @Input() selectedId: number

  constructor(
    private store: Store
  ) { }

  ngOnInit() {
  }

  onClickMenuItem(todo: Todo) {
    console.log(todo)
    this.store.dispatch(new TodoAction.Select({ id: todo.id }))
  }

  isActive(todo: Todo) {
    return todo.id === this.selectedId
  }
}
