import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from '@angular/material';

export interface TodoAddData {
  title: string;
}

@Component({
  templateUrl: './todo-add-modal.html',
  styleUrls:  ['./todo-add-modal.scss']
})
export class TodoAddModalComponemt implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TodoAddModalComponemt>,
    @Inject(MAT_DIALOG_DATA) public data: TodoAddData
  ) { }

  ngOnInit() {
  }
}

