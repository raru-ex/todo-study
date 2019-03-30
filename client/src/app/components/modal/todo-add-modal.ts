import { Component, Inject }                  from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA }       from '@angular/material';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Store }                              from '@ngxs/store'
import { UnstoredTodo }                       from '@app/model'
import { TodoAction }                         from '@app/state'
import { CustomValidators }                   from "@app/common/validator/CustomValidator";

enum CONDITION {
  IS_REQUIRED,
  IS_NOT_REQUIRED
}

@Component({
  templateUrl: './todo-add-modal.html',
  styleUrls:  ['./todo-add-modal.scss']
})
export class TodoAddModalComponent {

  selectOptions = [
    { id: CONDITION.IS_REQUIRED,     label: '条件付き必須' },
    { id: CONDITION.IS_NOT_REQUIRED, label: '必須ではない' }
  ]

  todoForm: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    content: new FormControl('', Validators.required),
    condition: new FormControl(CONDITION.IS_REQUIRED, Validators.required),
    conditionContent: new FormControl(null)
  }, {
    validators: CustomValidators.validateIf(
      "conditionContent",
      "condition",
      (value) => value === CONDITION.IS_REQUIRED,
      [Validators.min(1), Validators.required])
  })

  constructor(
    public dialogRef: MatDialogRef<TodoAddModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UnstoredTodo,
    private store: Store
  ) { }

  /**
   * 登録ボタン押下時の処理
   */
  onClickSubmit() {
    const name = this.todoForm.get('name')
    const content = this.todoForm.get('content')

    //TODO !!name等をしないとコンパイル時にnull可能性有りで落ちる。lintをゆるくしたい
    if( this.todoForm.valid && !!name && !!content) {
      this.store.dispatch(new TodoAction.Create(<UnstoredTodo>{
        name: name.value,
        content: content.value
      }))
      this.dialogRef.close()
    }
  }
}

