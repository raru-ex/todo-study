import {Component, OnDestroy, OnInit} from '@angular/core';
import { Observable, Subscription } from 'rxjs'
import {MatDialog, MatSnackBar} from '@angular/material';
import { TodoAddModalComponemt } from '@app/components/modal';
import { Store, Select } from '@ngxs/store'
import { TodoAction, TodoState } from '@shared/state'
import {Todo} from "@shared/model";

@Component({
  selector: 'page-main',
  templateUrl: './page-main.html',
  styleUrls: ['./page-main.scss']
})
export class PageMainComponent implements OnInit, OnDestroy {
  @Select(TodoState.getRows)    rows$: Observable<Todo[]>
  @Select(TodoState.getSelected) selected$: Observable<Todo>

  subscription: Subscription

  constructor(
    public dialog: MatDialog,
    private store: Store,
    private snackbar: MatSnackBar
  ) {}

  ngOnInit() {
    this.store.dispatch(new TodoAction.Load())
  }

  /**
   * コンポーネントは記事に実行
   */
  ngOnDestroy() {
    if(this.subscription) {
      this.subscription.unsubscribe()
    }
  }

  /**
   * TODOの新規追加ダイアログ表示
   */
  openAddModal() {
   this.subscription = this.dialog.open(TodoAddModalComponemt, {
      width: '400px',
      data: { title: "TODOの新規追加" }
    }).afterClosed().subscribe(_ => {
      console.log('todo add modal is closed');
      this.snackbar.open(
        "新しいTodoを追加しました。",
        "",
        { duration: 500 } )
    })
  }
}
