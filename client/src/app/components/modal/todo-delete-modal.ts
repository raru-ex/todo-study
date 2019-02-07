import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from '@angular/material';
import { Store } from '@ngxs/store'
import { Todo } from '@shared/model'
import { TodoAction } from '@shared/state'

@Component({
  templateUrl: './todo-delete-modal.html',
  styleUrls:  ['./todo-delete-modal.scss']
})
export class TodoDeleteModalComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TodoDeleteModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Todo,
    private store: Store
  ) { }

  ngOnInit() {
  }

  /**
   * 登録ボタン押下時の処理
   */
  onClickSubmit() {
      this.store.dispatch(new TodoAction.Delete({
        id: this.data.id
      }))
      this.dialogRef.close()
  }
}

