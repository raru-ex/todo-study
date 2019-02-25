import {Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Todo } from '@shared/model'
import {MatDialog, MatSnackBar} from "@angular/material";
import {TodoEditModalComponent, TodoDeleteModalComponent} from "@app/components/modal";
import {Subscription} from "rxjs";
import { Actions, ofActionSuccessful} from '@ngxs/store';
import {TodoAction} from '@shared/state';
import {map} from "rxjs/operators";

@Component({
  selector: 'ui-content',
  templateUrl: './ui-content.html',
  styleUrls: ['./ui-content.scss']
})
export class UiContentComponent implements OnInit, OnDestroy {
  @Input() todo: Todo
  subscriptions: Subscription[] = []

  constructor(
    private dialog: MatDialog,
    private actions: Actions,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    //- 更新成功時
    this.subscriptions.push(
      this.actions.pipe(
        ofActionSuccessful(TodoAction.Update),
        map(action => action.payload)
      ).subscribe(payload => {
        this.snackbar.open(
          `「${this.todo.name}」を「${payload.name}」に更新しました。`,
          "",
          { duration: 1000 }
        )
      })
    )
    //- 削除処理成功時
    this.subscriptions.push(
      this.actions.pipe(
        ofActionSuccessful(TodoAction.Delete)
      ).subscribe(_ => {
        this.snackbar.open(
          this.todo.name + "を削除しました",
          "",
          { duration: 1000 }
        )
      })
    )
  }

  ngOnDestroy(): void {
    if(this.subscriptions.length > 0) {
      this.subscriptions.forEach(subscription => {
        subscription.unsubscribe()
      })
    }
  }

  openEditModal(): void {
    const option = {
      width: '400px',
      disableClose: true,
      autoFocus: true,
      data: this.todo
    }
    this.dialog.open(TodoEditModalComponent, option)
  }

  openDeleteModal(): void {
    const option = {
      width: '400px',
      disableClose: true,
      autoFocus: true,
      data: this.todo
    }
    this.dialog.open(TodoDeleteModalComponent, option)
  }
}
