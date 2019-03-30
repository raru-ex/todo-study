import { Component, Inject } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Store } from '@ngxs/store'
import { Todo } from '@app/model'
import { TodoAction } from '@app/state'

@Component({
  templateUrl: './todo-edit-modal.html',
  styleUrls:  ['./todo-edit-modal.scss']
})
export class TodoEditModalComponent {
  todoForm = new FormGroup({
    name: new FormControl(this.data.name, Validators.required),
    content: new FormControl(this.data.content, Validators.required),
  })

  constructor(
    public dialogRef: MatDialogRef<TodoEditModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Todo,
    private store: Store
  ) { }

  /**
   * 登録ボタン押下時の処理
   */
  onClickSubmit() {
    const name = this.todoForm.get('name')
    const content = this.todoForm.get('content')

    //TODO !!name等をしないとコンパイル時にnull可能性有りで落ちる。lintをゆるくしたい
    if(this.todoForm.valid && !!name && !!content) {
      this.store.dispatch(new TodoAction.Update({
          ...this.data,
          name: name.value,
          content: content.value
        })
      )
      this.dialogRef.close()
    }
  }
}

