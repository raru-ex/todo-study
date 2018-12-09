import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormControl, Validators } from '@angular/forms';

export interface TodoAddData {
  name: string
  content: string
}

@Component({
  templateUrl: './todo-add-modal.html',
  styleUrls:  ['./todo-add-modal.scss']
})
export class TodoAddModalComponemt implements OnInit {
  todoForm = new FormGroup({
    name: new FormControl('', Validators.required),
    content: new FormControl('', Validators.required),
  })

  constructor(
    public dialogRef: MatDialogRef<TodoAddModalComponemt>,
    @Inject(MAT_DIALOG_DATA) public data: TodoAddData
  ) { }

  ngOnInit() {
  }

  /**
   * 登録ボタン押下時の処理
   */
  onClickSubmit() {
    console.log('data is submitted')
    if(this.todoForm.dirty) {
      console.log(this.todoForm.get('name'))
      console.log(this.todoForm.get('content'))
    }
  }
}

