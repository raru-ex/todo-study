import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { MatDialog, MatSnackBar } from '@angular/material';
import { TodoAddModalComponent } from '@app/components/modal';
import { Store, Select, Actions, ofActionSuccessful } from '@ngxs/store';
import { TodoAction, TodoState } from '@app/state';
import { Todo } from '@app/model';
import { SessionAction } from '@app/state/session.actions';

@Component({
  selector: 'page-main',
  templateUrl: './page-main.html',
  styleUrls: ['./page-main.scss']
})
export class PageMainComponent implements OnInit, OnDestroy {
  @Select(TodoState.getRows)    rows$: Observable<Todo[]>;
  @Select(TodoState.getSelected) selected$: Observable<Todo>;

  subscription: Subscription;

  constructor(
    public  dialog: MatDialog,
    private actions: Actions,
    private store: Store,
    private snackbar: MatSnackBar,
  ) {}

  ngOnInit() {
    this.store.dispatch(new TodoAction.Load());

    // 登録処理が成功した場合
    this.subscription = this.actions.pipe(
      ofActionSuccessful(TodoAction.Create)
    ).subscribe(_ => {
      this.snackbar.open(
        '新しいTodoを追加しました。',
        '',
        { duration: 1000 }
      );
    });
  }

  /**
   * コンポーネントは記事に実行
   */
  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  /**
   * TODOの新規追加ダイアログ表示
   */
  openAddModal() {
    this.dialog.open(TodoAddModalComponent, {
      width: '400px',
      data: { title: 'TODOの新規追加'},
      disableClose: true,
      autoFocus: true
    });
  }

  logout() {
    this.store.dispatch(new SessionAction.Logout())
  }
}
