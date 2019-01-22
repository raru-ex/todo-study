import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs'
import { MatDialog } from '@angular/material';
import { TodoAddModalComponemt } from '@app/components/modal';
import { Store, Select } from '@ngxs/store'
import { TodoAction, TodoState, TodoStateModel } from '@shared/state'
import {Todo} from "@shared/model";

@Component({
  selector: 'page-main',
  templateUrl: './page-main.html',
  styleUrls: ['./page-main.scss']
})
export class PageMainComponent implements OnInit {
  @Select(TodoState.getState)    state$: Observable<TodoStateModel>
  @Select(TodoState.getSelected) selected$: Observable<Todo>

  constructor(
    public dialog: MatDialog,
    private store: Store,
  ) {}

  ngOnInit() {
    this.store.dispatch(new TodoAction.Load())
  }

  /**
   * TODOの新規追加ダイアログ表示
   */
  openAddModal() {
    const dialogRef = this.dialog.open(TodoAddModalComponemt, {
      width: '400px',
      data: { title: "TODOの新規追加" }
    })

    dialogRef.afterClosed().subscribe(_ => {
      console.log('todo add modal is closed');
    })
  }
}
