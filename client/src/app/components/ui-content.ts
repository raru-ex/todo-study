import {Component, OnInit, Input } from '@angular/core';
import { Todo } from '@shared/model'
import {MatDialog} from "@angular/material";
import {TodoEditModalComponent, TodoDeleteModalComponent} from "@app/components/modal";

@Component({
  selector: 'ui-content',
  templateUrl: './ui-content.html',
  styleUrls: ['./ui-content.scss']
})
export class UiContentComponent implements OnInit {
  @Input() todo: Todo

  constructor(
    private dialog: MatDialog
  ) { }

  ngOnInit() {
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
